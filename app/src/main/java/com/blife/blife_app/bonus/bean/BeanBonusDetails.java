package com.blife.blife_app.bonus.bean;

/**
 * Created by w on 2016/9/14.
 */
public class BeanBonusDetails {

    private BeanBonusDetails info;
    private String adv_id;
    private String title;
    private String pub_name;
    private String description;
    private long pub_begin_time;
    private long pub_end_time;
    private BeanBonusContent content;
    private String contact_address;
    private float contact_lng;
    private float contact_lat;

    public BeanBonusDetails getInfo() {
        return info;
    }

    public void setInfo(BeanBonusDetails info) {
        this.info = info;
    }

    public String getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(String adv_id) {
        this.adv_id = adv_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPub_name() {
        return pub_name;
    }

    public void setPub_name(String pub_name) {
        this.pub_name = pub_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPub_begin_time() {
        return pub_begin_time;
    }

    public void setPub_begin_time(long pub_begin_time) {
        this.pub_begin_time = pub_begin_time;
    }

    public long getPub_end_time() {
        return pub_end_time;
    }

    public void setPub_end_time(long pub_end_time) {
        this.pub_end_time = pub_end_time;
    }

    public BeanBonusContent getContent() {
        return content;
    }

    public void setContent(BeanBonusContent content) {
        this.content = content;
    }

    public String getContact_address() {
        return contact_address;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address;
    }

    public float getContact_lng() {
        return contact_lng;
    }

    public void setContact_lng(float contact_lng) {
        this.contact_lng = contact_lng;
    }

    public float getContact_lat() {
        return contact_lat;
    }

    public void setContact_lat(float contact_lat) {
        this.contact_lat = contact_lat;
    }
}
