package com.blife.blife_app.tools.permissionutil;

/**
 * Created by w on 2016/10/8.
 */
public interface InterfacePermissionResult {

    void onGranted();//允许

    void onDenied();

    void onNotShowRationale();

    void onCancelShowRationale();

    void onDeniedDialogPositive();

    void onDeniedDialogNegative();

}
