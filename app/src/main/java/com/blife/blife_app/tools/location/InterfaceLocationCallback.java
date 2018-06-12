package com.blife.blife_app.tools.location;

import com.baidu.location.BDLocation;

/**
 * Created by w on 2016/10/18.
 */
public interface InterfaceLocationCallback {

    void onLocationSuccess(BDLocation bdLocation);

    void onLocationError();

    void onCancelShowRationale();

    void onDeniedDialogPositive();

    void onDeniedDialogNegative();


}
