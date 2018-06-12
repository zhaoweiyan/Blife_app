package com.blife.blife_app.index.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/9/7.
 */
public class BeanPayment {
    private BeanAliPay alipay;

    private List<String> wxpay;

    public BeanPayment() {
    }

    public BeanPayment(BeanAliPay alipay, List<String> wxpay) {
        this.alipay = alipay;
        this.wxpay = wxpay;
    }

    public BeanAliPay getAlipay() {
        return alipay;
    }

    public void setAlipay(BeanAliPay alipay) {
        this.alipay = alipay;
    }

    public List<String> getWxpay() {
        return wxpay;
    }

    public void setWxpay(List<String> wxpay) {
        this.wxpay = wxpay;
    }


    @Override
    public String toString() {
        return "BeanPayment{" +
                "alipay=" + alipay +
                ", wxpay=" + wxpay +
                '}';
    }
}
