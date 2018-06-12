package com.blife.blife_app.bonus.bean;

import java.util.List;

/**
 * Created by w on 2016/9/13.
 */
public class BeanAlreadyParticipate {
    private List<BeanAlreadyParticipate> list;
    private String log_id;
    private String adv_id;
    private String pub_id;
    private String user_id;
    private int prize_level;
    private long money;
    private String adv_title;
    private String adv_description;
    private BeanBonusContent adv_content;
    private String pub_name;
    private String user_telphone;
    private String user_nickname;
    private String user_headimg;
    private long create_time;
    private int count;


    public List<BeanAlreadyParticipate> getList() {
        return list;
    }

    public void setList(List<BeanAlreadyParticipate> list) {
        this.list = list;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(String adv_id) {
        this.adv_id = adv_id;
    }

    public String getPub_id() {
        return pub_id;
    }

    public void setPub_id(String pub_id) {
        this.pub_id = pub_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getPrize_level() {
        return prize_level;
    }

    public void setPrize_level(int prize_level) {
        this.prize_level = prize_level;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getAdv_title() {
        return adv_title;
    }

    public void setAdv_title(String adv_title) {
        this.adv_title = adv_title;
    }

    public String getAdv_description() {
        return adv_description;
    }

    public void setAdv_description(String adv_description) {
        this.adv_description = adv_description;
    }

    public BeanBonusContent getAdv_content() {
        return adv_content;
    }

    public void setAdv_content(BeanBonusContent adv_content) {
        this.adv_content = adv_content;
    }

    public String getPub_name() {
        return pub_name;
    }

    public void setPub_name(String pub_name) {
        this.pub_name = pub_name;
    }

    public String getUser_telphone() {
        return user_telphone;
    }

    public void setUser_telphone(String user_telphone) {
        this.user_telphone = user_telphone;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_headimg() {
        return user_headimg;
    }

    public void setUser_headimg(String user_headimg) {
        this.user_headimg = user_headimg;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
