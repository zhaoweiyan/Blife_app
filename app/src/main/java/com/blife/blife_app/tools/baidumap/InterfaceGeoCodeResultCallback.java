package com.blife.blife_app.tools.baidumap;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * Created by w on 2016/9/1.
 */
public interface InterfaceGeoCodeResultCallback {

    void onGeoCodeSuccess(ReverseGeoCodeResult reverseGeoCodeResult);

    void onGeoCodeError();

}
