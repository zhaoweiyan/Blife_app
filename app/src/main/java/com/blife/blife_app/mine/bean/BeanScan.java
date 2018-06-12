package com.blife.blife_app.mine.bean;

/**
 * Created by Somebody on 2016/9/19.
 */
public class BeanScan {
    private String promotion_link;

    public BeanScan() {
    }

    public BeanScan(String promotion_link) {
        this.promotion_link = promotion_link;
    }

    public String getPromotion_link() {
        return promotion_link;
    }

    public void setPromotion_link(String promotion_link) {
        this.promotion_link = promotion_link;
    }

    @Override
    public String toString() {
        return "BeanScan{" +
                "promotion_link='" + promotion_link + '\'' +
                '}';
    }
}
