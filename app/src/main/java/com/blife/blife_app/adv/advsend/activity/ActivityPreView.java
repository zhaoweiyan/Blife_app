package com.blife.blife_app.adv.advsend.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.api.API_MyadvDetail;
import com.blife.blife_app.adv.advmine.bean.BeanContent;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvDetail;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.bonus.activity.ActivityBusinessLocation;
import com.blife.blife_app.h5.ActivityWebView;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.permissionutil.InterfacePermissionResult;
import com.blife.blife_app.tools.permissionutil.PermissionUtils;
import com.blife.blife_app.tools.rollviewpager.TurnImagePager;
import com.blife.blife_app.tools.view.DialogPopWindows;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.ToastUtils;

/**
 * Created by w on 2016/9/22.
 */
public class ActivityPreView extends BaseActivity implements View.OnClickListener, InterfacePermissionResult {

    private FrameLayout framelayout_bonus_turnimage;
    private TextView tv_bonus_title, tv_bonus_businessname, tv_bonus_content, tv_bonus_phone, tv_bonus_link, tv_bonus_location;
    private LinearLayout lin_bonus_pulldown;
    private ImageView iv_pulldown;
    //广告信息
    private String ADV_ID;
    private String ADV_Phone;
    private String ADV_Link;
    private String ADV_Link_LABLE;
    private String ADV_BUSINESS_ADDRESS;
    private LatLng ADV_BUSINESS_LAT_LNG;
    private DialogPopWindows dialogPopWindows;

    private boolean pullDown;

    //检查权限
    private PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_preview);
        initBackTopBar(R.string.uploadadv_bonus_preview);
        initView();
        initTurnImageLayout();
        initData();
        initPopupWindows();
        checkPermission();
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(instance, ActivityPreView.this);
        permissionUtils.setInterfacePermissionResult(this);
        permissionUtils.checkPermission(Manifest.permission.CALL_PHONE, 102);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied() {

    }

    @Override
    public void onNotShowRationale() {

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
        lin_bonus_pulldown = (LinearLayout) findViewById(R.id.lin_bonus_pulldown);
        tv_bonus_content = (TextView) findViewById(R.id.tv_bonus_content);
        tv_bonus_phone = (TextView) findViewById(R.id.tv_bonus_phone);
        tv_bonus_link = (TextView) findViewById(R.id.tv_bonus_link);
        tv_bonus_location = (TextView) findViewById(R.id.tv_bonus_location);
        iv_pulldown = (ImageView) findViewById(R.id.iv_pulldown);
        //监听
        lin_bonus_pulldown.setOnClickListener(this);
        tv_bonus_phone.setOnClickListener(this);
        tv_bonus_link.setOnClickListener(this);
        tv_bonus_location.setOnClickListener(this);
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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ADV_ID = bundle.getString(Constants.ADV_ID, "");
        }
        API_MyadvDetail api_myadvDetail = new API_MyadvDetail(ACCESS_TOKEN, ADV_ID);
        dataManager.getServiceData(api_myadvDetail);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_MyadvDetail.TAG)) {
                    BeanMyAdvDetail beanMyAdvDetail = (BeanMyAdvDetail) JsonObjUItils.fromJson(json, BeanMyAdvDetail.class);
                    setBonusInfo(beanMyAdvDetail.getInfo());
                }
            }

            @Override
            public void onError(Object tag, String message) {

            }
        };
    }


    private void setBonusInfo(BeanMyadv beanMyadv) {
        ADV_BUSINESS_ADDRESS = beanMyadv.getContact_address();
        ADV_BUSINESS_LAT_LNG = new LatLng(beanMyadv.getContact_lat(), beanMyadv.getContact_lng());
        tv_bonus_title.setText(beanMyadv.getTitle());
        tv_bonus_businessname.setText(beanMyadv.getPub_name());
        tv_bonus_content.setText(beanMyadv.getDescription());
        BeanContent content = beanMyadv.getContent();
        if (!TextUtils.isEmpty(content.getLink_label())) {
            tv_bonus_link.setText(content.getLink_label());
        }
        ADV_Link = content.getLink();
        ADV_Link_LABLE = content.getLink_label();
        ADV_Phone = content.getContact_phone();
        if (content.getImages().size() > 0) {
            TurnImagePager turnImagePager = new TurnImagePager(instance, content.getImages());
            framelayout_bonus_turnimage.removeAllViews();
            framelayout_bonus_turnimage.addView(turnImagePager.initView());
            turnImagePager.initData();
        }
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
                openWebView(ADV_Link, ADV_Link_LABLE);
                break;
            case R.id.tv_bonus_location:
                if (ADV_BUSINESS_LAT_LNG != null && !TextUtils.isEmpty(ADV_BUSINESS_ADDRESS)) {
                    Bundle bun = new Bundle();
                    bun.putString(Constants.BONUS_LOCATION_ADDRESS, ADV_BUSINESS_ADDRESS);
                    bun.putParcelable(Constants.BONUS_LOCATION_LAT_LNG, ADV_BUSINESS_LAT_LNG);
                    startActivity(ActivityBusinessLocation.class, bun);
                } else {
                    showFailedDialog(R.string.bonus_details_business_location_empty, tv_bonus_location);
                }
                break;
        }
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
}
