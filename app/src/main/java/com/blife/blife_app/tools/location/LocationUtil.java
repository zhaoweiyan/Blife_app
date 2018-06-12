package com.blife.blife_app.tools.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blife.blife_app.R;
import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.tools.permissionutil.InterfacePermissionResult;
import com.blife.blife_app.tools.permissionutil.PermissionUtils;
import com.blife.blife_app.utils.logcat.L;

/**
 * Created by w on 2016/10/18.
 */
public class LocationUtil implements BDLocationListener, InterfacePermissionResult {

    private Context context;
    private LocationClient locationClient;
    private InterfaceLocationCallback interfaceLocationCallback;
    //权限验证
    private PermissionUtils permissionUtils;
    private boolean showDialog = true;

    private LocationUtil(Activity activity) {
        context = BlifeApplication.AppContext;
        if (activity != null) {
            permissionUtils = new PermissionUtils(context, activity);
        }
        locationClient = new LocationClient(context);
        locationClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);//设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        option.setIsNeedAddress(true);//设置是否需要地址信息，默认为无地址
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        locationClient.setLocOption(option);
    }

    public static LocationUtil getInstance(Activity activity) {
        return new LocationUtil(activity);
    }

    private void checkPermission() {
        if (permissionUtils != null) {
            permissionUtils.setInterfacePermissionResult(this);
            permissionUtils.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 0x777);
        } else {
            locationClient.start();
        }
    }

    public void startLocation(InterfaceLocationCallback interfaceLocationCallback) {
        if (locationClient != null) {
            L.e("TAG", "启动定位");
            this.interfaceLocationCallback = interfaceLocationCallback;
            if (showDialog)
                checkPermission();
            else
                locationClient.start();
        }
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onGranted() {//有定位权限
        locationClient.start();
    }

    @Override
    public void onDenied() {//没有定位权限-调用系统权限弹窗
        if (showDialog) {
            permissionUtils.showDeniedDialog(context.getString(R.string.permission_denied_title),
                    context.getString(R.string.permission_denied_location_message));
        } else {
            interfaceLocationCallback.onLocationError();
        }
    }

    @Override
    public void onNotShowRationale() {//没有定位权限-不再提醒--无法调用系统权限弹窗--进入设置
        if (showDialog)
            permissionUtils.showApplyDialog(context.getString(R.string.permission_location_title),
                    context.getString(R.string.permission_location_message));
    }

    @Override
    public void onCancelShowRationale() {//没有定位权限-取消调用系统弹窗
        if (interfaceLocationCallback != null)
            interfaceLocationCallback.onCancelShowRationale();
    }

    @Override
    public void onDeniedDialogPositive() {//没有定位权限-不再提醒--无法调用系统权限弹窗--确定进入设置
        if (interfaceLocationCallback != null)
            interfaceLocationCallback.onDeniedDialogPositive();
    }

    @Override
    public void onDeniedDialogNegative() {//没有定位权限-不再提醒--无法调用系统权限弹窗--取消进入设置
        if (interfaceLocationCallback != null)
            interfaceLocationCallback.onDeniedDialogNegative();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        locationClient.stop();
        if (bdLocation == null) {
            L.e("定位失败");
            locationError();
            return;
        }
        if (bdLocation.getLocType() == bdLocation.TypeNetWorkLocation
                || bdLocation.getLocType() == bdLocation.TypeOffLineLocation
                || bdLocation.getLocType() == bdLocation.TypeGpsLocation) {
            locationSuccess(bdLocation);
            L.e("定位成功" + bdLocation.getLongitude() + "--**--" + bdLocation.getLatitude());
        } else if (bdLocation.getLocType() == bdLocation.TypeCriteriaException) {
            L.e("飞行模式，无法定位结果");
            locationError();
        } else if (bdLocation.getLocType() == bdLocation.TypeNetWorkException) {
            L.e("网络连接失败");
            locationError();
        } else if (bdLocation.getLocType() == bdLocation.TypeNone) {
            L.e("无效定位结果，一般由于定位SDK内部逻辑异常时出现");
            locationError();
        } else if (bdLocation.getLocType() == bdLocation.TypeOffLineLocationFail) {
            L.e("离线定位失败结果");
            locationError();
        } else if (bdLocation.getLocType() == bdLocation.TypeServerError) {
            L.e("server定位失败，没有对应的位置信息");
            locationError();
        } else {
            L.e("定位失败，bdLocation.getLocType()==" + bdLocation.getLocType());
            locationError();
        }
    }


    private void locationSuccess(BDLocation bdLocation) {
        if (interfaceLocationCallback != null) {
            interfaceLocationCallback.onLocationSuccess(bdLocation);
        }
    }

    private void locationError() {
        if (interfaceLocationCallback != null) {
            interfaceLocationCallback.onLocationError();
        }
    }
}
