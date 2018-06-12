package com.blife.blife_app.adv.advmine.bean;

/**
 * Created by Somebody on 2016/9/8.
 */
public class BeanGetCount {
    private String adv_descriptionl;
    private String adv_id;
    private String adv_title;
    private long create_time;
    private String log_id;
    private String money;
    private String prize_level;
    private String pub_id;
    private String user_headimg;
    private String user_id;
    private String user_nickname;
    private String user_telphone;


    public BeanGetCount() {
    }

    public BeanGetCount(String adv_descriptionl, String adv_id, String adv_title, long create_time, String log_id, String money, String prize_level, String pub_id, String user_headimg, String user_id, String user_nickname, String user_telphone) {
        this.adv_descriptionl = adv_descriptionl;
        this.adv_id = adv_id;
        this.adv_title = adv_title;
        this.create_time = create_time;
        this.log_id = log_id;
        this.money = money;
        this.prize_level = prize_level;
        this.pub_id = pub_id;
        this.user_headimg = user_headimg;
        this.user_id = user_id;
        this.user_nickname = user_nickname;
        this.user_telphone = user_telphone;
    }

    public String getAdv_descriptionl() {
        return adv_descriptionl;
    }

    public void setAdv_descriptionl(String adv_descriptionl) {
        this.adv_descriptionl = adv_descriptionl;
    }

    public String getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(String adv_id) {
        this.adv_id = adv_id;
    }

    public String getAdv_title() {
        return adv_title;
    }

    public void setAdv_title(String adv_title) {
        this.adv_title = adv_title;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPrize_level() {
        return prize_level;
    }

    public void setPrize_level(String prize_level) {
        this.prize_level = prize_level;
    }

    public String getPub_id() {
        return pub_id;
    }

    public void setPub_id(String pub_id) {
        this.pub_id = pub_id;
    }

    public String getUser_headimg() {
        return user_headimg;
    }

    public void setUser_headimg(String user_headimg) {
        this.user_headimg = user_headimg;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_telphone() {
        return user_telphone;
    }

    public void setUser_telphone(String user_telphone) {
        this.user_telphone = user_telphone;
    }

    @Override
    public String toString() {
        return "BeanGetCount{" +
                "adv_descriptionl='" + adv_descriptionl + '\'' +
                ", adv_id='" + adv_id + '\'' +
                ", adv_title='" + adv_title + '\'' +
                ", create_time=" + create_time +
                ", log_id='" + log_id + '\'' +
                ", money='" + money + '\'' +
                ", prize_level='" + prize_level + '\'' +
                ", pub_id='" + pub_id + '\'' +
                ", user_headimg='" + user_headimg + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", user_telphone='" + user_telphone + '\'' +
                '}';
    }
}
