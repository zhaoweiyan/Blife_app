package com.blife.blife_app.index.bean;

/**
 * Created by Somebody on 2016/9/7.
 */
public class BeanAliPay {
    private String partner;
    private String seller_id;


    public BeanAliPay() {
    }

    public BeanAliPay(String partner, String seller_id) {
        this.partner = partner;
        this.seller_id = seller_id;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    @Override
    public String toString() {
        return "BeanAliPay{" +
                "partner='" + partner + '\'' +
                ", seller_id='" + seller_id + '\'' +
                '}';
    }
}
