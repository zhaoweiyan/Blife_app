package com.blife.blife_app.adv.advmine.bean;

/**
 * Created by Somebody on 2016/9/7.
 */
public class BeanMyAdvRefund {


    private String adv_id;
    private String create_time;
    private String log_id;
    private String refund_status;
    private String refund_status_str;
    private String refund_total_amount;
    private String user_id;

    public BeanMyAdvRefund() {
    }

    public BeanMyAdvRefund(String adv_id, String create_time, String log_id, String refund_status, String refund_status_str, String refund_total_amount, String user_id) {
        this.adv_id = adv_id;
        this.create_time = create_time;
        this.log_id = log_id;
        this.refund_status = refund_status;
        this.refund_status_str = refund_status_str;
        this.refund_total_amount = refund_total_amount;
        this.user_id = user_id;
    }

    public String getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(String adv_id) {
        this.adv_id = adv_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getRefund_status_str() {
        return refund_status_str;
    }

    public void setRefund_status_str(String refund_status_str) {
        this.refund_status_str = refund_status_str;
    }

    public String getRefund_total_amount() {
        return refund_total_amount;
    }

    public void setRefund_total_amount(String refund_total_amount) {
        this.refund_total_amount = refund_total_amount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "BeanMyAdvRefund{" +
                "adv_id='" + adv_id + '\'' +
                ", create_time='" + create_time + '\'' +
                ", log_id='" + log_id + '\'' +
                ", refund_status='" + refund_status + '\'' +
                ", refund_status_str='" + refund_status_str + '\'' +
                ", refund_total_amount='" + refund_total_amount + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
