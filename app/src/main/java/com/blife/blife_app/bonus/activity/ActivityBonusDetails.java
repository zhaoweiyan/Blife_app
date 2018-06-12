package com.blife.blife_app.bonus.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.bonus.api.API_Bonus_Details;
import com.blife.blife_app.bonus.api.API_GetBonus;
import com.blife.blife_app.bonus.bean.BeanBonus;
import com.blife.blife_app.bonus.bean.BeanBonusContent;
import com.blife.blife_app.bonus.bean.BeanBonusDetails;
import com.blife.blife_app.bonus.view.anim.AnimOpenBonus;
import com.blife.blife_app.h5.ActivityWebView;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.permissionutil.InterfacePermissionResult;
import com.blife.blife_app.tools.permissionutil.PermissionUtils;
import com.blife.blife_app.tools.rollviewpager.TurnImagePager;
import com.blife.blife_app.tools.share.ShareUtils;
import com.blife.blife_app.tools.view.DialogPopWindows;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;

/**
 * Created by w on 2016/9/14.
 */
public class ActivityBonusDetails extends BaseActivity implements View.OnClickListener, InterfacePermissionResult {

    private FrameLayout framelayout_bonus_turnimage;
    private TextView tv_bonus_title, tv_bonus_businessname, tv_bonus_report, tv_bonus_share, tv_bonus_grabmoney,
            tv_bonus_content, tv_bonus_phone, tv_bonus_link, tv_bonus_location;
    private Button button_rankinglist, button_grab;
    private LinearLayout lin_bonus_ranklist;
    private LinearLayout lin_bonus_pulldown;
    private ImageView iv_pulldown;
    //广告信息
    private String ADV_ID, PUB_ID, BONUS_MONEY;
    private long BONUS_TIME;
    private String ADV_Phone;
    private String ADV_PubName;
    private String ADV_Link;
    private String ADV_Link_Lable;
    private String ADV_BUSINESS_ADDRESS;
    private LatLng ADV_BUSINESS_LAT_LNG;

    private DialogPopWindows dialogPopWindows;
    //抢红包
    private AnimOpenBonus OpenBonusAnim;
    //分享
    private ShareUtils shareUtils;
    //查看更多
    private boolean pullDown;
    //活动是否结束
    private boolean isEnd;
    //检查权限
    private PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_details);
        initBackTopBar(R.string.bonus_details_title);
        initView();
        initTurnImageLayout();
        initPopupWindows();
        checkPermission();
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(instance, ActivityBonusDetails.this);
        permissionUtils.setInterfacePermissionResult(this);
        permissionUtils.checkPermission(Manifest.permission.CALL_PHONE, 102);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onGranted() {   //允许
        LogUtils.e("当前允许");
        initData();
    }

    @Override
    public void onDenied() {  //禁止
        LogUtils.e("当前禁止");
        initData();
    }

    @Override
    public void onNotShowRationale() {//不再提示
        LogUtils.e("当前不再提示");
        initData();
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

    private void initView() {
        framelayout_bonus_turnimage = (FrameLayout) findViewById(R.id.framelayout_bonus_turnimage);
        tv_bonus_title = (TextView) findViewById(R.id.tv_bonus_title);
        tv_bonus_businessname = (TextView) findViewById(R.id.tv_bonus_businessname);
        tv_bonus_report = (TextView) findViewById(R.id.tv_bonus_report);
        tv_bonus_share = (TextView) findViewById(R.id.tv_bonus_share);
        lin_bonus_pulldown = (LinearLayout) findViewById(R.id.lin_bonus_pulldown);
        tv_bonus_content = (TextView) findViewById(R.id.tv_bonus_content);
        tv_bonus_phone = (TextView) findViewById(R.id.tv_bonus_phone);
        tv_bonus_link = (TextView) findViewById(R.id.tv_bonus_link);
        tv_bonus_location = (TextView) findViewById(R.id.tv_bonus_location);
        tv_bonus_grabmoney = (TextView) findViewById(R.id.tv_bonus_grad_money);
        lin_bonus_ranklist = (LinearLayout) findViewById(R.id.lin_bonus_ranklist);
        button_rankinglist = (Button) findViewById(R.id.button_rankinglist);
        button_grab = (Button) findViewById(R.id.button_grab);
        iv_pulldown = (ImageView) findViewById(R.id.iv_pulldown);
        //监听
        tv_bonus_report.setOnClickListener(this);
        tv_bonus_share.setOnClickListener(this);
        lin_bonus_pulldown.setOnClickListener(this);
        tv_bonus_phone.setOnClickListener(this);
        tv_bonus_link.setOnClickListener(this);
        tv_bonus_location.setOnClickListener(this);
        button_rankinglist.setOnClickListener(this);
        button_grab.setOnClickListener(this);
    }

    private void initTurnImageLayout() {
        int screenWidth = ScreenUtils.getScreenWidth(instance);
        framelayout_bonus_turnimage.getLayoutParams().height = screenWidth * 4 / 3;
    }

    private void initPopupWindows() {
        dialogPopWindows = new DialogPopWindows(instance);
        dialogPopWindows.setText(DialogPopWindows.TYPE_TITLE, getString(R.string.bonus_details_business_phone));
        dialogPopWindows.setText(DialogPopWindows.TYPE_CONFIRM, getString(R.string.bonus_details_business_phone_call));
        dialogPopWindows.setConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone();
            }
        });
    }

    private void initData() {
        shareUtils = new ShareUtils(instance, baseActivity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ADV_ID = bundle.getString(Constants.ADV_ID, "");
            PUB_ID = bundle.getString(Constants.PUB_ID, "");
            BONUS_MONEY = bundle.getString(Constants.BONUS_GRAB_MONEY, "");
            BONUS_TIME = bundle.getLong(Constants.BONUS_GRAB_TIME, 0);
        }
        if (TextUtils.isEmpty(PUB_ID)) {//已参与
            button_grab.setVisibility(View.GONE);
            lin_bonus_ranklist.setVisibility(View.VISIBLE);
            tv_bonus_grabmoney.setText("￥" + StringUtils.dealMoney(BONUS_MONEY, 100));
            shareUtils.setShareText(shardPreferName.getStringData(Constants.SHARE_INFO_DESCRIPTION_ON_ACCEPTED).replace(Constants.SHARE_INFO_DESCRIPTION_REPLACE_MONEY, StringUtils.dealMoney(BONUS_MONEY, 100)));
        } else {//未参与
            button_grab.setVisibility(View.VISIBLE);
            lin_bonus_ranklist.setVisibility(View.GONE);
            OpenBonusAnim = new AnimOpenBonus(instance);
            shareUtils.setShareText(shardPreferName.getStringData(Constants.SHARE_INFO_DESCRIPTION));
        }
        shareUtils.setShareTitle(shardPreferName.getStringData(Constants.SHARE_INFO_TITLE));
        shareUtils.setShareLink(shardPreferName.getStringData(Constants.SHARE_INFO_LINK));
        showLoadingDialog();
        API_Bonus_Details api_bonus_details = new API_Bonus_Details(ACCESS_TOKEN, ADV_ID);
        dataManager.getServiceData(api_bonus_details);
        LogUtils.e("当前的界面****" + ActivityBonusDetails.this);
    }


    /**
     * 抢红包
     */
    private void getBonus() {
        showLoadingDialog();
        API_GetBonus api_getBonus = new API_GetBonus(ACCESS_TOKEN, ADV_ID, PUB_ID);
        dataManager.getServiceData(api_getBonus);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_Bonus_Details.TAG)) {
                    cancelLoadingDialog();
                    BeanBonusDetails beanBonusDetails = (BeanBonusDetails) JsonObjUItils.fromJson(json, BeanBonusDetails.class);
                    if (beanBonusDetails != null)
                        setBonusInfo(beanBonusDetails.getInfo());
                }
                if (tag.equals(API_GetBonus.TAG)) {
                    L.e("TAG", "结果：-------" + json);
                    cancelLoadingDialog();
                    BeanBonus beanBonus = (BeanBonus) JsonObjUItils.fromJson(json, BeanBonus.class);
                    button_grab.setVisibility(View.GONE);
                    lin_bonus_ranklist.setVisibility(View.VISIBLE);
                    BONUS_MONEY = beanBonus.getBonus();
                    BONUS_TIME = beanBonus.getAccept_time();
                    String money = StringUtils.dealMoney(BONUS_MONEY, 100);
                    tv_bonus_grabmoney.setText("￥" + money);
                    OpenBonusAnim.show(button_grab, money, ADV_PubName);
                }

            }

            @Override
            public void onError(Object tag, String message) {
                if (tag.equals(API_GetBonus.TAG)) {
                    cancelLoadingDialog();
//                    ToastUtils.showCenterShort(ActivityBonusDetails.this, JsonObjUItils.getERRORJsonDetail(message));
                    JsonObjUItils.getJsonCode(message, button_grab, ActivityBonusDetails.this, R.string.get_filer);
                }
                if (tag.equals(API_Bonus_Details.TAG)) {
                    L.e("TAG", "错误结果：" + message);
                    String code = JsonObjUItils.getERRORJsonCode(message);
                    if (code.equals("13000029")) {
                        button_grab.setVisibility(View.GONE);
                    }
                    cancelLoadingDialog();
                }
            }
        };
    }

    private void setBonusInfo(BeanBonusDetails beanBonusDetails) {
        ADV_BUSINESS_ADDRESS = beanBonusDetails.getContact_address();
        ADV_BUSINESS_LAT_LNG = new LatLng(beanBonusDetails.getContact_lat(), beanBonusDetails.getContact_lng());
        tv_bonus_title.setText(beanBonusDetails.getTitle());
        tv_bonus_businessname.setText(beanBonusDetails.getPub_name());
        tv_bonus_content.setText(beanBonusDetails.getDescription());
        BeanBonusContent content = beanBonusDetails.getContent();
        ADV_Link_Lable = content.getLink_label();
        if (!TextUtils.isEmpty(ADV_Link_Lable)) {
            tv_bonus_link.setText(ADV_Link_Lable);
        }
        ADV_Link = content.getLink();
        ADV_Phone = content.getContact_phone();
        ADV_PubName = beanBonusDetails.getPub_name();
        if (content.getImages().size() > 0) {
            String shareImageLink = content.getImages().get(0);
            TurnImagePager turnImagePager = new TurnImagePager(instance, content.getImages());
            framelayout_bonus_turnimage.removeAllViews();
            framelayout_bonus_turnimage.addView(turnImagePager.initView());
            turnImagePager.initData();
            shareUtils.setShareImagePath(shareImageLink);
        }
        if (beanBonusDetails.getPub_end_time() * 1000 < System.currentTimeMillis())
            isEnd = true;
        else
            isEnd = false;
    }

    /**
     * 拨打电话
     */

    private void callPhone() {
        dialogPopWindows.dismiss();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ADV_Phone));
        if (ActivityCompat.checkSelfPermission(instance, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            permissionUtils.showDeniedDialog(getString(R.string.permission_empty_call_phone));
            return;
        }
        startActivity(intent);
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
            showFailedDialog(R.string.bonus_details_business_link_empty, tv_bonus_link);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bonus_report:
                Bundle bundles = new Bundle();
                bundles.putString(Constants.ADV_ID, ADV_ID);
                startActivity(ActivityBonusReport.class, bundles);
                break;
            case R.id.tv_bonus_share:
                shareUtils.showSharePopWindow(tv_bonus_share);
                break;
            case R.id.lin_bonus_pulldown:
                pullDown();
                break;
            case R.id.tv_bonus_phone:
                if (!TextUtils.isEmpty(ADV_Phone)) {
                    dialogPopWindows.setTextColor(DialogPopWindows.TYPE_MESSAGE, getResources().getColor(R.color.colorWhite));
                    dialogPopWindows.setText(DialogPopWindows.TYPE_MESSAGE, ADV_Phone);
                    dialogPopWindows.show(tv_bonus_phone);
                } else {
                    showFailedDialog(R.string.bonus_details_business_phone_empty, tv_bonus_phone);
                }
                break;
            case R.id.tv_bonus_link:
                openWebView(ADV_Link, ADV_Link_Lable);
                break;
            case R.id.tv_bonus_location:
                if (ADV_BUSINESS_LAT_LNG != null && !TextUtils.isEmpty(ADV_BUSINESS_ADDRESS)) {
                    Bundle bun = new Bundle();
                    bun.putString(Constants.BONUS_LOCATION_ADDRESS, ADV_BUSINESS_ADDRESS);
                    bun.putParcelable(Constants.BONUS_LOCATION_LAT_LNG, ADV_BUSINESS_LAT_LNG);
                    startActivity(ActivityBusinessLocation.class, bun);
                } else {
                    showFailedDialog(R.string.bonus_details_business_location_empty, tv_bonus_link);
                }
                break;
            case R.id.button_rankinglist:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.ADV_ID, ADV_ID);
                bundle.putBoolean(Constants.ADV_TIME_END, isEnd);
                bundle.putString(Constants.BONUS_GRAB_MONEY, BONUS_MONEY);
                bundle.putLong(Constants.BONUS_GRAB_TIME, BONUS_TIME);
                startActivity(ActivityRankingList.class, bundle);
                break;
            case R.id.button_grab:
                getBonus();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void pullDown() {
        if (!pullDown) {
            tv_bonus_content.setMaxLines(50);
            iv_pulldown.setRotation(180);
        } else {
            tv_bonus_content.setMaxLines(4);
            iv_pulldown.setRotation(0);
        }
        pullDown = !pullDown;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
