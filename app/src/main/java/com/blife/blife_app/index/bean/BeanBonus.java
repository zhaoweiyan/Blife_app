package com.blife.blife_app.index.bean;

/**
 * Created by w on 2016/8/29.
 */
public class BeanBonus {

    private String adv_id;
    private String pub_id;
    private String title;
    private String description;
    private long pub_begin_time;
    private long pub_end_time;
    private String prize_top_money;
    private int bonus_remain_num;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPrize_top_money() {
        return prize_top_money;
    }

    public void setPrize_top_money(String prize_top_money) {
        this.prize_top_money = prize_top_money;
    }
//

    public int getBonus_remain_num() {
        return bonus_remain_num;
    }

    public void setBonus_remain_num(int bonus_remain_num) {
        this.bonus_remain_num = bonus_remain_num;
    }
}
