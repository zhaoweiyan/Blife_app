package com.blife.blife_app.mine.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.interfacer.DialogListener;
import com.blife.blife_app.base.fragment.BaseFragment;
import com.blife.blife_app.h5.ActivityWebView;
import com.blife.blife_app.mine.activity.ActivityExtends;
import com.blife.blife_app.mine.activity.ActivityMine;
import com.blife.blife_app.mine.activity.ActivityWithdraw;
import com.blife.blife_app.mine.api.API_Balance;
import com.blife.blife_app.mine.api.API_Mine;
import com.blife.blife_app.mine.api.API_Version;
import com.blife.blife_app.mine.bean.BeanAmount;
import com.blife.blife_app.mine.bean.BeanMine;
import com.blife.blife_app.mine.bean.BeanVersion;
import com.blife.blife_app.tools.BitmapHelp;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.view.CircleImageView;
import com.blife.blife_app.tools.view.ConfirmDialog;
import com.blife.blife_app.tools.view.DialogProgress;
import com.blife.blife_app.utils.appvercode.VerCodeManager;
import com.blife.blife_app.utils.cache.SQLiteCacheManager;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.logcat.LogcatManager;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.Contant;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.NumberUtils;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;

/**
 * Created by Somebody on 2016/8/25.
 */
public class FragmentMine extends BaseFragment implements View.OnClickListener {
    private CircleImageView me_iv_header;
    private LinearLayout me_lin_edit_info, me_lin_withdraw, me_lin_extends, me_lin_cleancache, me_lin_upgrad_version, me_lin_about;
    private TextView me_tv_extends, me_tv_nickname, me_tv_amount, me_tv_cache_size, me_tv_version;
    private ImageView me_iv_sex;
    private TextView me_tv_identify;
    private String header = "";
    private String nickname = "";
    private int isIdentify = 0;
    private int type = 0;
    private String appVerName;
    private boolean mandatory, upgrade;
    private String downLoadLink = "";
    private String FilePath = Contant.AppDirPath + File.separator + "download_app";
    private DialogProgress dialogProgress;
    private ImageView iv;
    private API_Mine api_mine;
    private ConfirmDialog confirmDialog;

    @Override
    public int getLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void init() {
        showLoadingDialog();
        initView();
        initClick();
        initMine();
    }

    private void initClick() {
        me_lin_edit_info.setOnClickListener(this);
        me_lin_withdraw.setOnClickListener(this);
        me_lin_about.setOnClickListener(this);
        me_lin_extends.setOnClickListener(this);
        me_lin_cleancache.setOnClickListener(this);
        me_lin_upgrad_version.setOnClickListener(this);
    }

    private void initMine() {
        //获取新版本
        LogUtils.e("fragment version****" + VerCodeManager.getAppVerName(getActivity()));
        API_Version api_version = new API_Version(ACCESS_TOKEN, VerCodeManager.getAppVerName(getActivity()));
        dataManager.getServiceData(api_version);
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取个人信息
        api_mine = new API_Mine(ACCESS_TOKEN, shardPreferName.getBooleanData(Constants.IDENTITY_PASS, false));
        dataManager.getServiceData(api_mine);
        //获取余额
        API_Balance api_balance = new API_Balance(ACCESS_TOKEN);
        dataManager.getServiceData(api_balance);
    }

    private void initView() {
        //个人信息
        me_iv_header = (CircleImageView) rootView.findViewById(R.id.me_iv_header);
        me_lin_edit_info = (LinearLayout) rootView.findViewById(R.id.me_lin_edit_info);
        me_tv_extends = (TextView) rootView.findViewById(R.id.me_tv_extends);
        dialogProgress = new DialogProgress(getActivity(), me_tv_extends, dataManager);
        me_tv_nickname = (TextView) rootView.findViewById(R.id.me_tv_nickname);
        me_tv_identify = (TextView) rootView.findViewById(R.id.me_tv_identify);
        me_iv_sex = (ImageView) rootView.findViewById(R.id.me_iv_sex);

        //立即提现
        me_lin_withdraw = (LinearLayout) rootView.findViewById(R.id.me_lin_withdraw);
        me_tv_amount = (TextView) rootView.findViewById(R.id.me_tv_amount);
        //申请推广
        me_lin_extends = (LinearLayout) rootView.findViewById(R.id.me_lin_extends);
        //清理内存
        me_lin_cleancache = (LinearLayout) rootView.findViewById(R.id.me_lin_cleancache);
        me_tv_cache_size = (TextView) rootView.findViewById(R.id.me_tv_cache_size);
        me_tv_cache_size.setText(sqLiteCacheManager.getCacheSize(getActivity()));
        confirmDialog = new ConfirmDialog(getActivity(), R.layout.dialog_confirm, "清理缓存\n" + sqLiteCacheManager.getCacheSize(getActivity()));
        confirmDialog.setViewDismiss(true);
        //升级版本l
        me_lin_upgrad_version = (LinearLayout) rootView.findViewById(R.id.me_lin_upgrad_version);
        me_tv_version = (TextView) rootView.findViewById(R.id.me_tv_version);
        appVerName = VerCodeManager.getAppVerName(getActivity());
        me_tv_version.setText(appVerName);
        //关于便联
        me_lin_about = (LinearLayout) rootView.findViewById(R.id.me_lin_about);
        iv = (ImageView) rootView.findViewById(R.id.iv);
    }

    @Override
    protected UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                L.e("TAG", "BUG调试--我的--成功：" + tag);
                if (tag.equals(API_Mine.TAG)) {
                    LogUtils.e("获取我的信息**" + json);
//                    LogcatManager.i(getActivity(),"fragmentmine",json,true);
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity());
                        return;
                    }
                    if (getActivity() != null)
                        LogcatManager.w(getActivity(), "TAG", json, true);
                    BeanMine beanMine = (BeanMine) JsonObjUItils.fromJson(json, BeanMine.class);
                    if (beanMine.getVerify_status() == 0) {
                        me_tv_identify.setText("(未认证)");
                        isIdentify = 0;
                        dataManager.clearCache(api_mine);
                        shardPreferName.setBooleanData(Constants.IDENTITY_PASS, false);
                    } else if (beanMine.getVerify_status() == 1) {
                        me_tv_identify.setText("(审核中)");
                        isIdentify = 1;
                        dataManager.clearCache(api_mine);
                        shardPreferName.setBooleanData(Constants.IDENTITY_PASS, false);
                    } else if (beanMine.getVerify_status() == 2) {
                        me_tv_identify.setText("(已认证)");
                        isIdentify = 2;
                        shardPreferName.setBooleanData(Constants.IDENTITY_PASS, true);
                    } else if (beanMine.getVerify_status() == 3) {
                        me_tv_identify.setText("(认证失败)");
                        isIdentify = 3;
                        dataManager.clearCache(api_mine);
                        shardPreferName.setBooleanData(Constants.IDENTITY_PASS, false);
                    } else {
                        isIdentify = 4;
                        dataManager.clearCache(api_mine);
                        shardPreferName.setBooleanData(Constants.IDENTITY_PASS, false);
                    }
                    type = beanMine.getType();
                    if (beanMine.getHeadimg() != null) {
                        BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(getActivity());
                        bitmapUtils.display(me_iv_header, beanMine.getHeadimg());
                        header = beanMine.getHeadimg();
                    }
                    if (beanMine.getNickname() != null) {
                        me_tv_nickname.setText(beanMine.getNickname().trim());
                        nickname = beanMine.getNickname().trim();
                        shardPreferName.setStringData(Constants.MINE_NICKNAME, nickname);
                    }
                    if (beanMine.getGender() == 2) {
                        me_iv_sex.setImageResource(R.mipmap.me_sex_woman);
                    } else {
                        me_iv_sex.setImageResource(R.mipmap.me_sex_man);
                    }
                }
                if (tag.equals(API_Balance.TAG)) {
                    LogUtils.e("获取我的余额**" + json);
//                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity());
                        return;
                    }
                    BeanAmount beanAmount = (BeanAmount) JsonObjUItils.fromJson(json, BeanAmount.class);
                    if (beanAmount.getBalance() != null && beanAmount.getBalance().getWallet_balance() != null)
                        me_tv_amount.setText("￥" + NumberUtils.getTwoPoint(beanAmount.getBalance().getWallet_balance()));
                }
                if (tag.equals(API_Version.TAG)) {
//                    cancelLoadingDialog();
                    LogUtils.e("获取新版本****" + json);
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity());
                        return;
                    }
                    BeanVersion beanVersion = (BeanVersion) JsonObjUItils.fromJson(json, BeanVersion.class);
                    mandatory = beanVersion.isMandatory();
                    upgrade = beanVersion.isUpgrade();
                    if (beanVersion.getPackages() != null && beanVersion.getPackages().size() > 0) {
                        downLoadLink = beanVersion.getPackages().get(0);
                    }
                }
            }

            @Override
            public void onError(Object tag, String message) {
                L.e("TAG", "BUG调试--我的--失败：" + tag);
                cancelLoadingDialog();
//                if (message != null) {
//                    ToastUtils.showShort(getActivity(), JsonObjUItils.getERRORJsonDetail(message));
//                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_lin_edit_info:
                startActivity(ActivityMine.class);
                break;
            case R.id.me_lin_withdraw:
//                Bundle bundl = new Bundle();
//                bundl.putInt(Constants.IS_IDENTIFY, isIdentify);
//                startActivity(ActivityWithdraw.class, bundl);
                if (isIdentify == 2) {
                    startActivity(ActivityWithdraw.class);
                } else {
                    showFailedDialog("未认证", R.string.identify_mine, me_lin_withdraw);
                }
                break;
            case R.id.me_lin_extends:
                extendsCompany();
                break;
            case R.id.me_lin_cleancache:
                cleanCache(v);
                break;
            case R.id.me_lin_upgrad_version:
                downLoadApk();
                break;
            case R.id.me_lin_about:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.H5_TITLE_TAG, "关于便联生活");
                bundle.putString(Constants.H5_URL_TAG, Constants.ABOUT_COMPANY);
                startActivity(ActivityWebView.class, bundle);
                break;
        }
    }

    private void cleanCache(View v) {
        confirmDialog.show(v);
        confirmDialog.setDialogListener(new DialogListener() {
            @Override
            public void dialogConfirmListener() {
                deleteCache();
                confirmDialog.dismiss();
            }

            @Override
            public void dialogCacleListener() {
                confirmDialog.dismiss();
            }
        });

    }

    private void extendsCompany() {
//        if (type == 0) {
//            showFailedDialog("未认证", "企业认证之后才可以推广", me_lin_extends);
//            return;
//        } else if (type == 1) {
//            showFailedDialog("未认证", "暂不支持个人推广", me_lin_extends);
//            return;
//        } else if (type == 2) {
//            if (isIdentify == 2) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MINEHEADER, header);
        bundle.putString(Constants.MINE_NICKNAME, nickname);
        startActivity(ActivityExtends.class, bundle);
//            } else {
//                showFailedDialog("未认证", R.string.identify_mine, me_lin_extends);
//            }
//        }
    }

    private void downLoadApk() {
        dialogProgress.setProgress(mandatory, upgrade, downLoadLink, FilePath);
    }

    private void deleteCache() {
        sqLiteCacheManager.deleteAllNetCache(new SQLiteCacheManager.DeleteIsSucess() {
            @Override
            public void deleteSucess() {
                LogUtils.e("deleteSucess size****" + sqLiteCacheManager.getCacheSize(getActivity()));
                me_tv_cache_size.setText("0.00KB");
                confirmDialog.setTitle("清理缓存\n" + "0.00KB");
            }

            @Override
            public void deletFiled() {
                LogUtils.e("deletFiled size****" + sqLiteCacheManager.getCacheSize(getActivity()));
                me_tv_cache_size.setText("清理失败");
            }

            @Override
            public void deletError() {
                LogUtils.e("deletError size****" + sqLiteCacheManager.getCacheSize(getActivity()));
                me_tv_cache_size.setText("清理失败");
            }
        });
    }


    @Override
    public void onDestroy() {
        cancelLoadingDialog();
        super.onDestroy();
    }
}
