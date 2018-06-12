package com.blife.blife_app.adv.advsend.bean;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by w on 2016/8/31.
 */
public class BeanPoiResult {

    private String address;
    private String addressdetail;
    private int TYPE;
    private LatLng latLng;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressdetail() {
        return addressdetail;
    }

    public void setAddressdetail(String addressdetail) {
        this.addressdetail = addressdetail;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
