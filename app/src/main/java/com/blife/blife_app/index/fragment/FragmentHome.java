package com.blife.blife_app.index.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.blife.blife_app.R;
import com.blife.blife_app.base.fragment.BaseFragment;
import com.blife.blife_app.bonus.activity.ActivityBonusDetails;
import com.blife.blife_app.h5.ActivityWebView;
import com.blife.blife_app.index.activity.ActivitySuperBonusDetail;
import com.blife.blife_app.index.adapter.AdapterPlugin;
import com.blife.blife_app.index.api.API_BounsList;
import com.blife.blife_app.index.api.API_PluginList;
import com.blife.blife_app.index.api.API_SuperBonusList;
import com.blife.blife_app.index.api.API_TurnImages;
import com.blife.blife_app.index.api.API_TurnMessage;
import com.blife.blife_app.index.api.API_config;
import com.blife.blife_app.index.bean.BeanBonus;
import com.blife.blife_app.index.bean.BeanBonusList;
import com.blife.blife_app.index.bean.BeanExtends;
import com.blife.blife_app.index.bean.BeanHomeTurnImages;
import com.blife.blife_app.index.bean.BeanHomeTurnImagesList;
import com.blife.blife_app.index.bean.BeanJPushRecevier;
import com.blife.blife_app.index.bean.BeanMessageList;
import com.blife.blife_app.index.bean.BeanPlugin;
import com.blife.blife_app.index.bean.BeanPluginList;
import com.blife.blife_app.index.bean.BeanServerConfig;
import com.blife.blife_app.index.bean.BeanShareInfo;
import com.blife.blife_app.index.bean.BeanSuperBonus;
import com.blife.blife_app.index.view.BoundsLayout;
import com.blife.blife_app.index.view.InterfaceBonusClick;
import com.blife.blife_app.tools.JPushUtils;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.ScrollBanner;
import com.blife.blife_app.tools.TimeCountDownUtils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.interfaceUtil.InterfaceTimeCountDown;
import com.blife.blife_app.tools.location.InterfaceLocationCallback;
import com.blife.blife_app.tools.location.LocationUtil;
import com.blife.blife_app.tools.rollviewpager.InterfaceTurnImageItemClick;
import com.blife.blife_app.tools.rollviewpager.TurnImagePager;
import com.blife.blife_app.tools.view.JPushReceiverDialog;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.logcat.LogcatManager;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Somebody on 2016/8/25.
 */
public class FragmentHome extends BaseFragment implements View.OnClickListener, InterfaceTimeCountDown, InterfaceTurnImageItemClick {

    private List<BeanHomeTurnImages> beanHomeTurnImages;
    private ScrollBanner scroll;
    private LinearLayout lin_super_bonus;
    private TextView tv_superbonus_amount;
    private TextView tv_superbonus_name;
    private TextView tv_superbonus_time;
    private LinearLayout lin_gotosuperbonus;
    private TextView day_1, day_2, hours_1, hours_2, minute_1, minute_2, second_1, second_2;
    private LinearLayout lin_refresh;
    private List<BeanPlugin> Pluginlist;
    private List<BeanBonus> BoundsList;
    private ArrayList<String> list = new ArrayList<>();
    private FrameLayout lin;
    private AdapterPlugin adapterPlugin;
    private GridView gd_apk;
    private ImageView iv_refresh;
    private boolean isRefresh;
    private RotateAnimation refreshAnimation;
    private BoundsLayout bounds_layout;
    private ImageView iv_home_bonus;
    private TimeCountDownUtils timeCountDown;
    //超级大红包
    private boolean isReward;
    private ImageView iv_loading_superbonus_adv_img;
    private String Event_ID;
    private SpannableStringBuilder spannableStringBuilder;
    private String superBonusADVDialogImageUrl;

    private JPushReceiverDialog jPushReceiverDialog;
    private JPushReceiverDialog jpushDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                jpushDialog.show(tv_superbonus_amount);
                shardPreferName.setStringData(Constants.JPUSH_IMAGE, "");
            } catch (Exception e) {
                e.printStackTrace();
//                LogcatManager.w(getActivity(), "Fragment", "这是一条popuwindow的异常", e.getCause(), true);
            }
        }
    };
    private boolean isFragemntVisiable = false;
    private int SuperBonusTextLength = 16;

    private LocationUtil locationUtil;

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }


    @Override
    public void init() {
        initView();
        initData();   //获取token
        initConfig();//获取通用配置
        getSuperBonus();//超级大红包
        getTurnImages();//获取轮播图
        getPluginList();//获取插件
        getBonusList();//获取附近的红包
        initClick();
        jpushDialog = new JPushReceiverDialog(getActivity(), R.layout.dialog_jpush_receiver);
        showJupshDialog();
    }

    private void showJupshDialog() {
        JPushUtils.getInstance(getActivity()).initJPush(getActivity(), new JPushUtils.JpushJson() {
            @Override
            public void getJpushJson(String json) {
                Gson gson = new Gson();
                BeanJPushRecevier beanJPushRecevier = gson.fromJson(json, BeanJPushRecevier.class);
                if (beanJPushRecevier.getType().equals(Constants.JPUSH_BROADCAST)) {
                    LogUtils.e("activity****" + getActivity());
                    shardPreferName.setStringData(Constants.JPUSH_IMAGE, beanJPushRecevier.getNotify_image());
                    if (isFragemntVisiable == true) {
                        jpushDialog.setImage(getActivity(), beanJPushRecevier.getNotify_image());
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                }
            }
        });
    }

    private void initConfig() {
        API_config api_config = new API_config(ACCESS_TOKEN);
        dataManager.getServiceData(api_config);
    }

    private void initView() {
        initTopBar(R.string.index_tab_index);
        lin_refresh = (LinearLayout) rootView.findViewById(R.id.lin_refresh);
        gd_apk = (GridView) rootView.findViewById(R.id.gd_apk);
        scroll = (ScrollBanner) rootView.findViewById(R.id.scroll);
        lin = (FrameLayout) rootView.findViewById(R.id.lin);
        lin_super_bonus = (LinearLayout) rootView.findViewById(R.id.lin_super_bonus);
        tv_superbonus_amount = (TextView) rootView.findViewById(R.id.tv_superbonus_amount);
        tv_superbonus_name = (TextView) rootView.findViewById(R.id.tv_superbonus_name);
        tv_superbonus_time = (TextView) rootView.findViewById(R.id.tv_superbonus_time);
        lin_gotosuperbonus = (LinearLayout) rootView.findViewById(R.id.lin_gotosuperbonus);
        day_1 = (TextView) rootView.findViewById(R.id.day_1);
        day_2 = (TextView) rootView.findViewById(R.id.day_2);
        hours_1 = (TextView) rootView.findViewById(R.id.hours_1);
        hours_2 = (TextView) rootView.findViewById(R.id.hours_2);
        minute_1 = (TextView) rootView.findViewById(R.id.minute_1);
        minute_2 = (TextView) rootView.findViewById(R.id.minute_2);
        second_1 = (TextView) rootView.findViewById(R.id.second_1);
        second_2 = (TextView) rootView.findViewById(R.id.second_2);
        iv_refresh = (ImageView) rootView.findViewById(R.id.iv_refresh);
        bounds_layout = (BoundsLayout) rootView.findViewById(R.id.bounds_layout);
        iv_home_bonus = (ImageView) rootView.findViewById(R.id.iv_home_bonus);
        iv_loading_superbonus_adv_img = (ImageView) rootView.findViewById(R.id.iv_loading_superbonus_adv_img);
    }

    private void initClick() {
        lin_refresh.setOnClickListener(this);
        lin_gotosuperbonus.setOnClickListener(this);
        gd_apk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPlugin(position);
            }
        });
        bounds_layout.setBonusClick(new InterfaceBonusClick() {
            @Override
            public void onClick(int position, int state) {
                if (state == BoundsLayout.STATE_PROGRESS) {
                    String ADV_ID = BoundsList.get(position).getAdv_id();
                    String PUB_ID = BoundsList.get(position).getPub_id();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.ADV_ID, ADV_ID);
                    bundle.putString(Constants.PUB_ID, PUB_ID);
                    startActivity(ActivityBonusDetails.class, bundle);
                } else if (state == BoundsLayout.STATE_READY) {
                    messageDialog.setTitle(R.string.index_readying);
                    messageDialog.setMessage(R.string.index_readying_hint);
                    messageDialog.show(bounds_layout);
                } else {
                    messageDialog.setTitle(R.string.index_ending);
                    messageDialog.setMessage(R.string.index_ending_hint);
                    messageDialog.show(bounds_layout);
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 打开插件
     *
     * @param position
     */
    private void openPlugin(int position) {
        String target = Pluginlist.get(position).getTarget();
        String title = Pluginlist.get(position).getName();
        if (!TextUtils.isEmpty(target)) {
            if (StringUtils.isHttp(target)) {
                openWebView(target, title);
            } else {
                openAPP(target);
            }
        } else {
            String link = Pluginlist.get(position).getLink();
            if (!TextUtils.isEmpty(link)) {
                openWebView(link, title);
            } else
                ToastUtils.showShort(instance, R.string.index_link_empty_open_error);
        }
    }

    /**
     * 打开App
     *
     * @param target
     */
    private void openAPP(String target) {
        Uri uri;
        try {
            uri = Uri.parse(target);
            Intent intent = new Intent();
            intent.setData(uri);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开网页
     *
     * @param link
     * @param title
     */
    private void openWebView(String link, String title) {
        if (!TextUtils.isEmpty(link)) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.H5_URL_TAG, link);
            bundle.putString(Constants.H5_TITLE_TAG, title);
            startActivity(ActivityWebView.class, bundle);
        } else {
            ToastUtils.showShort(instance, R.string.index_link_empty_open_error);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        locationUtil = LocationUtil.getInstance(getActivity());
        timeCountDown = TimeCountDownUtils.getInstance();
        refreshAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        refreshAnimation.setDuration(1000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_refresh:
                isRefresh = true;
                iv_refresh.startAnimation(refreshAnimation);
                getBonusList();
                break;
            case R.id.lin_gotosuperbonus:
                if (!TextUtils.isEmpty(Event_ID)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.SUPER_BONUS_EVENT_ID, Event_ID);
                    bundle.putString(Constants.SUPER_BONUS_RANDOM_IMAGE, superBonusADVDialogImageUrl);
                    startActivity(ActivitySuperBonusDetail.class, bundle);
                }
                break;
        }
    }

    @Override
    protected UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                L.e("TAG", "BUG调试--首页--成功：" + tag);
                if (tag.equals(API_SuperBonusList.TAG)) {
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity());
                        return;
                    }
                    BeanSuperBonus superBonus = (BeanSuperBonus) JsonObjUItils.fromJson(json, BeanSuperBonus.class);
                    if (isAdded()) {
                        setSuperBonusData(superBonus);
                    }
                }
                if (tag.equals(API_BounsList.TAG)) {
                    cancelLoadingDialog();
                    if (isRefresh) {
                        isRefresh = false;
                    }
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity());
                        return;
                    }
                    LogUtils.e("附近的红包****" + json);
//                    LogcatManager.i(getActivity(), "附近的红包", json);
                    cancelLoadingDialog();
                    BeanBonusList beanBonusList = (BeanBonusList) JsonObjUItils.fromJson(json, BeanBonusList.class);
                    BoundsList = beanBonusList.getList();
                    if (BoundsList != null && BoundsList.size() > 0) {
                        bounds_layout.setData(BoundsList);
                        iv_home_bonus.setVisibility(View.INVISIBLE);
                    } else {
                        iv_home_bonus.setVisibility(View.VISIBLE);
                    }
                }
                if (tag.equals(API_config.TAG)) {
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity());
                        return;
                    }
                    LogUtils.e("全局配置****" + json);
                    BeanServerConfig beanServerConfig = (BeanServerConfig) JsonObjUItils.fromJson(json, BeanServerConfig.class);

                    //推广信息
                    if (beanServerConfig.getPromotion_share_info() != null) {
                        saveExtendsShare(beanServerConfig.getPromotion_share_info());
                    }
                    //极光别名
                    if (beanServerConfig != null && beanServerConfig.getPush_alias_id() != null) {
                        shardPreferName.setStringData(Constants.JPUSH_ALIAS, beanServerConfig.getPush_alias_id());
                        shardPreferName.setStringList(Constants.ACCUSATION_OPTIONS, beanServerConfig.getAccusation_options());
                        JPushUtils.getInstance(getActivity()).setAlias();
                    }
                    //联系电话
                    if (beanServerConfig != null && beanServerConfig.getContact() != null && beanServerConfig.getContact().getPhone() != null) {
                        shardPreferName.setStringData(Constants.CONTACT_PHONE, beanServerConfig.getContact().getPhone().trim());
                    }
                    //提现次数和最小金额
                    if (beanServerConfig != null && beanServerConfig.getGlobal_params() != null) {
                        shardPreferName.setIntData(Constants.TRANSFER_LIMIT_TIME, beanServerConfig.getGlobal_params().getTrasnfer_request_limit_per_day());
                        if (beanServerConfig.getGlobal_params().getTransfer_min_amount() != null)
                            shardPreferName.setStringData(Constants.TRANSFER_MIN_MONEY, beanServerConfig.getGlobal_params().getTransfer_min_amount());
                    }
                    //提现说明
                    if (beanServerConfig != null && beanServerConfig.getSuggest_words() != null && beanServerConfig.getSuggest_words().getTransfer_request() != null) {
                        shardPreferName.setStringData(Constants.TRANSFER_REQUEST, beanServerConfig.getSuggest_words().getTransfer_request().trim());
                    }
                    //提现说明群组
                    if (beanServerConfig != null && beanServerConfig.getSuggest_words() != null && beanServerConfig.getSuggest_words().getTransfer_request_notice() != null && beanServerConfig.getSuggest_words().getTransfer_request_notice().size() > 0) {
                        shardPreferName.setStringList(Constants.TRANSFER_REQUEST_NOTICE, beanServerConfig.getSuggest_words().getTransfer_request_notice());
                    }
                    //分享信息
                    if (beanServerConfig.getShare_info() != null) {
                        saveShareInfo(beanServerConfig.getShare_info());
                    }

                }
                if (tag.equals(API_PluginList.TAG)) {
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity());
                        return;
                    }
                    BeanPluginList beanPluginList = (BeanPluginList) JsonObjUItils.fromJson(json, BeanPluginList.class);
                    Pluginlist = beanPluginList.getList();
                    if (Pluginlist != null && Pluginlist.size() > 0 && getActivity() != null) {
                        adapterPlugin = new AdapterPlugin(Pluginlist, getActivity());
                        gd_apk.setAdapter(adapterPlugin);
                    }
                }

                //滚动消息
                if (tag.equals(API_TurnMessage.TAG)) {
                    cancelLoadingDialog();
                    LogUtils.e("滚动消息****" + json);
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity());
                        return;
                    }
                    BeanMessageList benMessages = (BeanMessageList) JsonObjUItils.fromJson(json, BeanMessageList.class);
                    List<String> list = benMessages.getMessages();
                    if (list != null && list.size() > 0) {
                        LogUtils.e("滚动消息list****" + list);
                        scroll.setList(list);
                        scroll.startScroll();
                    }
                }
                if (tag.equals(API_TurnImages.TAG)) {
                    cancelLoadingDialog();
                    LogUtils.e("轮播图**" + json);
                    if (list != null && list.size() > 0) {
                        list.clear();
                    }
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity());
                        return;
                    }
                    //轮播图
                    BeanHomeTurnImagesList BeanHomeTurnImagesList = (BeanHomeTurnImagesList) JsonObjUItils.fromJson(json, BeanHomeTurnImagesList.class);
                    beanHomeTurnImages = BeanHomeTurnImagesList.getBeanHomeTurnImagesList();
                    if (beanHomeTurnImages != null && beanHomeTurnImages.size() > 0) {
                        for (int i = 0; i < beanHomeTurnImages.size(); i++) {
                            list.add(beanHomeTurnImages.get(i).getAdv_image());
                        }
                        if (getActivity() != null) {
                            TurnImagePager turnImagePager = new TurnImagePager(getActivity(), list);
                            for (int i = 1; i < lin.getChildCount(); i++) {
                                lin.removeViewAt(i);
                            }
                            lin.addView(turnImagePager.initView());
                            turnImagePager.initData();
                            turnImagePager.setItemClick(FragmentHome.this);
                        }
                    } else {
                        if (lin.getChildCount() >= 2) {
                            for (int i = 1; i < lin.getChildCount(); i++) {
                                lin.removeViewAt(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Object tag, String message) {
                L.e("TAG", "BUG调试--首页--失败：" + tag);
                cancelLoadingDialog();
                if (isRefresh) {
//                    ToastUtils.showShort(instance, R.string.index_refresh_failed);
                }
            }
        };
    }

    private void saveExtendsShare(BeanExtends beanExtends) {
        if (beanExtends.getTitle() != null) {
            shardPreferName.setStringData(Constants.EXTENDS_SHARE_TITLE, beanExtends.getTitle());
        }

        if (beanExtends.getContent() != null) {
            shardPreferName.setStringData(Constants.EXTENDS_SHARE_CONTENT, beanExtends.getContent());
        }

        if (beanExtends.getImage() != null) {
            shardPreferName.setStringData(Constants.EXTENDS_SHARE_IAMGEURL, beanExtends.getImage());
        }
    }

    private void saveShareInfo(BeanShareInfo beanShareInfo) {
        shardPreferName.setStringData(Constants.SHARE_INFO_TITLE, beanShareInfo.getTitle());
        shardPreferName.setStringData(Constants.SHARE_INFO_LINK, beanShareInfo.getLink());
        shardPreferName.setStringData(Constants.SHARE_INFO_DESCRIPTION, beanShareInfo.getDescription());
        shardPreferName.setStringData(Constants.SHARE_INFO_TITLE_ON_PAY_SUCCESS, beanShareInfo.getTitle_on_pay_success());
        shardPreferName.setStringData(Constants.SHARE_INFO_DESCRIPTION_ON_ACCEPTED, beanShareInfo.getDescription_on_accepted());
        shardPreferName.setStringData(Constants.SHARE_INFO_DESCRIPTION_ON_PAY_SUCCESS, beanShareInfo.getDescription_on_pay_success());
//        shardPreferName.setStringData(Constants.SHARE_INFO_SUPER_BONUS_TITLE, beanShareInfo.getDescription_on_accepted());
        shardPreferName.setStringData(Constants.SHARE_INFO_SUPER_BONUS_DESCRIPTION, beanShareInfo.getDescription_on_pay_success());
    }

    /**
     * 设置超级红包数据
     *
     * @param bonusData
     */
    private void setSuperBonusData(BeanSuperBonus bonusData) {
        if (bonusData == null) {
            Event_ID = "";
            lin_super_bonus.setVisibility(View.GONE);
            return;
        }
        if (bonusData.getList().size() <= 0) {
            Event_ID = "";
            lin_super_bonus.setVisibility(View.GONE);
            return;
        }
        lin_super_bonus.setVisibility(View.VISIBLE);
        BeanSuperBonus beanSuperBonus = bonusData.getList().get(0);
//        int index = (int) (Math.random() * beanSuperBonus.getImages().size());
        if (beanSuperBonus.getImages() != null && beanSuperBonus.getImages().size() > 0) {
            int size = beanSuperBonus.getImages().size();
            int index = new Random().nextInt(size);
            superBonusADVDialogImageUrl = beanSuperBonus.getImages().get(index < size ? index : 0);
            ImageLoader.getInstance().loadImage(superBonusADVDialogImageUrl, iv_loading_superbonus_adv_img, true);
        }
        Event_ID = beanSuperBonus.getEvent_id();
        if (beanSuperBonus.getReward_users() != null) {
            if (beanSuperBonus.getReward_users().size() <= 0) {
                isReward = false;
            } else {
                isReward = true;
            }
        } else {
            isReward = false;
        }
        tv_superbonus_amount.setText("￥" + StringUtils.dealMoney(beanSuperBonus.getBonus_money(), 100));
        String name = beanSuperBonus.getEvent_name().length() <= SuperBonusTextLength ? beanSuperBonus.getEvent_name() :
                beanSuperBonus.getEvent_name().substring(0, SuperBonusTextLength) + "...";
        tv_superbonus_name.setText(name);
        String text = getString(R.string.index_countdown);
        spannableStringBuilder = StringUtils.setDiffColorText(text,
                new int[][]{{0, 1}, {1, 3}, {3, text.length()}},
                new int[]{getResources().getColor(R.color.colorBlack),
                        getResources().getColor(R.color.colorRedLoginNormal),
                        getResources().getColor(R.color.colorBlack)});
        LogcatManager.e(instance, "SUPERBONUS", "开始时间：" + beanSuperBonus.getBegin_time() + "结束时间：" + beanSuperBonus.getEnd_time()
                + "当前时间：" + bonusData.getCurrent_time());
        timeCountDown.setTime(beanSuperBonus.getBegin_time(), beanSuperBonus.getEnd_time(), bonusData.getCurrent_time());
        timeCountDown.start(this);
    }

    @Override
    public void time(long day, long hour, long minute, long second) {
        setTimeData(day, day_1, day_2);
        setTimeData(hour, hours_1, hours_2);
        setTimeData(minute, minute_1, minute_2);
        setTimeData(second, second_1, second_2);
        if (minute == 0 && second <= 0) {
            if (!isReward)
                tv_superbonus_time.setText(R.string.index_countdown_ending);
            else
                tv_superbonus_time.setText(R.string.index_countdown_rewarding);
        } else {
            tv_superbonus_time.setText(spannableStringBuilder);
        }
    }

    private void setTimeData(long time, TextView TenView, TextView AView) {
        String T = time + "";
        int len = T.length();
        String ten = "";
        String a = "";
        if (len == 2) {
            ten = T.substring(0, 1);
            a = T.substring(1);
        } else if (len == 1) {
            ten = "0";
            a = T;
        } else {
            ten = "0";
            a = "0";
        }
        TenView.setText(ten);
        AView.setText(a);
    }

    @Override
    public void onPause() {
        scroll.stopScroll();
        isFragemntVisiable = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        getTurnMessage();//获取时事快讯
        if (!TextUtils.isEmpty(shardPreferName.getStringData(Constants.JPUSH_IMAGE, ""))) {
            jpushDialog.setImage(getActivity(), shardPreferName.getStringData(Constants.JPUSH_IMAGE, ""));
            mHandler.sendEmptyMessageDelayed(0, 1000);
        }
        isFragemntVisiable = true;
        super.onResume();
    }

    /**
     * 获取轮播图
     */
    public void getTurnImages() {
        API_TurnImages turnImages = new API_TurnImages(ACCESS_TOKEN);
        dataManager.getServiceData(turnImages);
    }

    /**
     * 获取轮播消息
     */
    private void getTurnMessage() {
        API_TurnMessage turnMessage = new API_TurnMessage(ACCESS_TOKEN);
        dataManager.getServiceData(turnMessage);
    }

    /**
     * 获取插件
     */
    public void getPluginList() {
        API_PluginList api_pluginList = new API_PluginList(ACCESS_TOKEN);
        dataManager.getServiceData(api_pluginList);
    }

    /**
     * 获取附近红包
     */
    public void getBonusList() {
        locationUtil.startLocation(new InterfaceLocationCallback() {
            @Override
            public void onLocationSuccess(BDLocation bdLocation) {
                getBonusList(bdLocation.getLatitude() + "", bdLocation.getLongitude() + "");
            }

            @Override
            public void onLocationError() {

            }

            @Override
            public void onCancelShowRationale() {

            }

            @Override
            public void onDeniedDialogPositive() {

            }

            @Override
            public void onDeniedDialogNegative() {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 获取附近红包
     *
     * @param lat
     * @param lng
     */
    public void getBonusList(String lat, String lng) {
        API_BounsList api_bounsList = new API_BounsList(ACCESS_TOKEN, lat, lng);
        api_bounsList.setOffset(0);
        api_bounsList.setLimit(3);
        dataManager.getServiceData(api_bounsList);
    }

    /**
     * 获取超级大红包
     */
    public void getSuperBonus() {
        API_SuperBonusList api_superBonusList = new API_SuperBonusList(ACCESS_TOKEN);
        dataManager.getServiceData(api_superBonusList);
    }

    @Override
    public void onItemClick(int position) {
        String targetUrl = "";
        if (position < beanHomeTurnImages.size())
            targetUrl = beanHomeTurnImages.get(position).getAdv_target();
        if (!TextUtils.isEmpty(targetUrl)) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.H5_URL_TAG, targetUrl);
            bundle.putString(Constants.H5_TITLE_TAG, beanHomeTurnImages.get(position).getAdv_merchant_name());
            startActivity(ActivityWebView.class, bundle);
        }
    }

    @Override
    public void onDestroy() {
        cancelLoadingDialog();
        LogUtils.e("destroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        L.e("TAG", "首页--onDetach");
        super.onDetach();
    }
}
