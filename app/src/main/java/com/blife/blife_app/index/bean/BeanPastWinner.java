package com.blife.blife_app.index.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by w on 2016/9/23.
 */
public class BeanPastWinner {

    private List<BeanPastWinner> list;
    private String event_name;
    private String merchant_name;
    private String bonus_num;
    private String bonus_money;
    private String max_join_user_num;
    private long begin_time;
    private long end_time;
    private String begin_time_str;
    private String end_time_str;
    private List<String> images;
    private String keyword;
    private String description;
    private long create_time;
    private String create_time_str;
    private long lastmodified_time;
    private String lastmodified_time_str;
    private List<String> reward_users;
    private String reward_page;
    private long reward_time;
    private String event_id;

    public List<BeanPastWinner> getList() {
        return list;
    }

    public void setList(List<BeanPastWinner> list) {
        this.list = list;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getBonus_num() {
        return bonus_num;
    }

    public void setBonus_num(String bonus_num) {
        this.bonus_num = bonus_num;
    }

    public String getBonus_money() {
        return bonus_money;
    }

    public void setBonus_money(String bonus_money) {
        this.bonus_money = bonus_money;
    }

    public String getMax_join_user_num() {
        return max_join_user_num;
    }

    public void setMax_join_user_num(String max_join_user_num) {
        this.max_join_user_num = max_join_user_num;
    }


    public String getBegin_time_str() {
        return begin_time_str;
    }

    public void setBegin_time_str(String begin_time_str) {
        this.begin_time_str = begin_time_str;
    }

    public String getEnd_time_str() {
        return end_time_str;
    }

    public void setEnd_time_str(String end_time_str) {
        this.end_time_str = end_time_str;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCreate_time_str() {
        return create_time_str;
    }

    public void setCreate_time_str(String create_time_str) {
        this.create_time_str = create_time_str;
    }


    public String getLastmodified_time_str() {
        return lastmodified_time_str;
    }

    public void setLastmodified_time_str(String lastmodified_time_str) {
        this.lastmodified_time_str = lastmodified_time_str;
    }

    public List<String> getReward_users() {
        return reward_users;
    }

    public void setReward_users(List<String> reward_users) {
        this.reward_users = reward_users;
    }


    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public long getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(long begin_time) {
        this.begin_time = begin_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getLastmodified_time() {
        return lastmodified_time;
    }

    public void setLastmodified_time(long lastmodified_time) {
        this.lastmodified_time = lastmodified_time;
    }

    public String getReward_page() {
        return reward_page;
    }

    public void setReward_page(String reward_page) {
        this.reward_page = reward_page;
    }

    public long getReward_time() {
        return reward_time;
    }

    public void setReward_time(long reward_time) {
        this.reward_time = reward_time;
    }
}
