package com.blife.blife_app.tools.baidumap;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * Created by w on 2016/9/1.
 */
public class MapGetGeoCodeUtils implements OnGetGeoCoderResultListener {

    public static MapGetGeoCodeUtils INSTANCE;
    //反地理编码
    private GeoCoder geoCoder;
    //结果回调
    private InterfaceGeoCodeResultCallback interfaceGeoCodeResultCallback;


    private MapGetGeoCodeUtils() {
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    public static MapGetGeoCodeUtils getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new MapGetGeoCodeUtils();
//        }
        return new MapGetGeoCodeUtils();
    }

    /**
     * 搜索
     *
     * @param latLng
     * @param interfaceGeoCodeResultCallback
     */
    public void search(LatLng latLng, InterfaceGeoCodeResultCallback interfaceGeoCodeResultCallback) {
        this.interfaceGeoCodeResultCallback = interfaceGeoCodeResultCallback;
        ReverseGeoCodeOption option = new ReverseGeoCodeOption();
        option.location(latLng);
        geoCoder.reverseGeoCode(option);
    }


    //地理编码查询结果回调函数
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    //反地理编码查询结果回调函数
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult != null || reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
            if (interfaceGeoCodeResultCallback != null) {
                interfaceGeoCodeResultCallback.onGeoCodeSuccess(reverseGeoCodeResult);
            }
        } else {
            if (interfaceGeoCodeResultCallback != null) {
                interfaceGeoCodeResultCallback.onGeoCodeError();
            }
        }
    }
}
