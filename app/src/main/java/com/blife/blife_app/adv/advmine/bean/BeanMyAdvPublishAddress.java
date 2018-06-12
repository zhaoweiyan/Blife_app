package com.blife.blife_app.adv.advmine.bean;

import java.util.ArrayList;

/**
 * Created by Somebody on 2016/9/6.
 */
public class BeanMyAdvPublishAddress {

    private String adv_id;
    private String create_time;
    private String lastmodified_time;
    private String pub_address;
    private String pub_begin_time;
    private String pub_end_time;
    private String pub_id;
    private String pub_lat;
    private String pub_lng;
    private String pub_range;
    private String pub_user_id;

    public BeanMyAdvPublishAddress() {
    }

    public BeanMyAdvPublishAddress(String adv_id, String create_time, String lastmodified_time, String pub_address, String pub_begin_time, String pub_end_time, String pub_id, String pub_lat, String pub_lng, String pub_range, String pub_user_id) {
        this.adv_id = adv_id;
        this.create_time = create_time;
        this.lastmodified_time = lastmodified_time;
        this.pub_address = pub_address;
        this.pub_begin_time = pub_begin_time;
        this.pub_end_time = pub_end_time;
        this.pub_id = pub_id;
        this.pub_lat = pub_lat;
        this.pub_lng = pub_lng;
        this.pub_range = pub_range;
        this.pub_user_id = pub_user_id;
    }

    public String getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(String adv_id) {
        this.adv_id = adv_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLastmodified_time() {
        return lastmodified_time;
    }

    public void setLastmodified_time(String lastmodified_time) {
        this.lastmodified_time = lastmodified_time;
    }

    public String getPub_address() {
        return pub_address;
    }

    public void setPub_address(String pub_address) {
        this.pub_address = pub_address;
    }

    public String getPub_begin_time() {
        return pub_begin_time;
    }

    public void setPub_begin_time(String pub_begin_time) {
        this.pub_begin_time = pub_begin_time;
    }

    public String getPub_end_time() {
        return pub_end_time;
    }

    public void setPub_end_time(String pub_end_time) {
        this.pub_end_time = pub_end_time;
    }

    public String getPub_id() {
        return pub_id;
    }

    public void setPub_id(String pub_id) {
        this.pub_id = pub_id;
    }

    public String getPub_lat() {
        return pub_lat;
    }

    public void setPub_lat(String pub_lat) {
        this.pub_lat = pub_lat;
    }

    public String getPub_lng() {
        return pub_lng;
    }

    public void setPub_lng(String pub_lng) {
        this.pub_lng = pub_lng;
    }

    public String getPub_range() {
        return pub_range;
    }

    public void setPub_range(String pub_range) {
        this.pub_range = pub_range;
    }

    public String getPub_user_id() {
        return pub_user_id;
    }

    public void setPub_user_id(String pub_user_id) {
        this.pub_user_id = pub_user_id;
    }

    @Override
    public String toString() {
        return "BeanMyAdvPublishAddress{" +
                "adv_id='" + adv_id + '\'' +
                ", create_time='" + create_time + '\'' +
                ", lastmodified_time='" + lastmodified_time + '\'' +
                ", pub_address='" + pub_address + '\'' +
                ", pub_begin_time='" + pub_begin_time + '\'' +
                ", pub_end_time='" + pub_end_time + '\'' +
                ", pub_id='" + pub_id + '\'' +
                ", pub_lat='" + pub_lat + '\'' +
                ", pub_lng='" + pub_lng + '\'' +
                ", pub_range='" + pub_range + '\'' +
                ", pub_user_id='" + pub_user_id + '\'' +
                '}';
    }
}
