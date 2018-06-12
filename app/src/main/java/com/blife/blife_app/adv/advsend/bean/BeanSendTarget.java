package com.blife.blife_app.adv.advsend.bean;

import java.util.List;

/**
 * Created by w on 2016/8/31.
 */
public class BeanSendTarget {

    private List<BeanSendTarget> list;
    private BeanSendTarget info;
    private String adv_id;
    private String pub_user_id;
    private String pub_address;
    private double pub_lat;
    private double pub_lng;
    private double pub_range;
    private String pub_id;
    private int create_time;
    private int lastmodified_time;
    private int pub_begin_time;
    private int pub_end_time;

    public List<BeanSendTarget> getList() {
        return list;
    }

    public void setList(List<BeanSendTarget> list) {
        this.list = list;
    }

    public BeanSendTarget getInfo() {
        return info;
    }

    public void setInfo(BeanSendTarget info) {
        this.info = info;
    }

    public String getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(String adv_id) {
        this.adv_id = adv_id;
    }

    public String getPub_user_id() {
        return pub_user_id;
    }

    public void setPub_user_id(String pub_user_id) {
        this.pub_user_id = pub_user_id;
    }

    public double getPub_lat() {
        return pub_lat;
    }

    public void setPub_lat(double pub_lat) {
        this.pub_lat = pub_lat;
    }

    public double getPub_lng() {
        return pub_lng;
    }

    public void setPub_lng(double pub_lng) {
        this.pub_lng = pub_lng;
    }

    public double getPub_range() {
        return pub_range;
    }

    public void setPub_range(double pub_range) {
        this.pub_range = pub_range;
    }

    public String getPub_id() {
        return pub_id;
    }

    public void setPub_id(String pub_id) {
        this.pub_id = pub_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getLastmodified_time() {
        return lastmodified_time;
    }

    public void setLastmodified_time(int lastmodified_time) {
        this.lastmodified_time = lastmodified_time;
    }

    public int getPub_begin_time() {
        return pub_begin_time;
    }

    public void setPub_begin_time(int pub_begin_time) {
        this.pub_begin_time = pub_begin_time;
    }

    public int getPub_end_time() {
        return pub_end_time;
    }

    public void setPub_end_time(int pub_end_time) {
        this.pub_end_time = pub_end_time;
    }

    public String getPub_address() {
        return pub_address;
    }

    public void setPub_address(String pub_address) {
        this.pub_address = pub_address;
    }
}
