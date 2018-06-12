package com.blife.blife_app.adv.advsend.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by w on 2016/9/6.
 */
public class BeanWeChatPay {
//{"pay_info":{"appid":"wx36e34c361a0b2b3f","partnerid":"1385215002","prepayid":"wx20160906154020816fbb39db0584320539",
// "package":"Sign=WXPay","noncestr":"1144k4gnjc8j706dq329e3ntz94j4b1w","timestamp":1473147620,
// "sign":"68AFAA1C8431273C498063DE637F7BE0"}}]

    private BeanWeChatPay pay_info;
    private String appid;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String pack;
    private String noncestr;
    private String timestamp;
    private String sign;

    public BeanWeChatPay getPay_info() {
        return pay_info;
    }

    public void setPay_info(BeanWeChatPay pay_info) {
        this.pay_info = pay_info;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
