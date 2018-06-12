package com.blife.blife_app.adv.advmine.bean;

/**
 * Created by Somebody on 2016/8/29.
 */
public class BeanMyadv {

    private String adv_id;
    private String bonus_accepted_amount;
    private String bonus_accepted_num;
    private String bonus_created;
    private String bonus_payment_create_time;
    private String bonus_payment_order_id;
    private String bonus_payment_status;
    private String bonus_payment_time;
    private String bonus_payment_type;
    private String bonus_remain_num;
    private String bonus_total_amount;
    private String bonus_total_num;
    private double contact_lng;
    private double contact_lat;
    private String contact_address;
    private BeanContent content;
    private String create_time;
    private String description;
    private String lastmodified_time;
    private String lat;
    private String lng;
    private String pub_begin_time;
    private String pub_end_time;
    private String pub_ext_id;
    private String pub_ext_type;
    private String pub_name;
    private String pub_user_id;
    private String pub_user_logo;
    private String pub_user_nickname;
    private String pub_user_realname;
    private String refund_finish_time;
    private String refund_request_time;
    private String refund_status;
    private String refund_total_amount;
    private String status;
    private String title;


    public BeanMyadv() {
    }

    public BeanMyadv(String adv_id, String bonus_accepted_amount, String bonus_accepted_num, String bonus_created, String bonus_payment_create_time, String bonus_payment_order_id, String bonus_payment_status, String bonus_payment_time, String bonus_payment_type, String bonus_remain_num, String bonus_total_amount, String bonus_total_num, String contact_address, BeanContent content, String create_time, String description, String lastmodified_time, String lat, String lng, String pub_begin_time, String pub_end_time, String pub_ext_id, String pub_ext_type, String pub_name, String pub_user_id, String pub_user_logo, String pub_user_nickname, String pub_user_realname, String refund_finish_time, String refund_request_time, String refund_status, String refund_total_amount, String status, String title) {
        this.adv_id = adv_id;
        this.bonus_accepted_amount = bonus_accepted_amount;
        this.bonus_accepted_num = bonus_accepted_num;
        this.bonus_created = bonus_created;
        this.bonus_payment_create_time = bonus_payment_create_time;
        this.bonus_payment_order_id = bonus_payment_order_id;
        this.bonus_payment_status = bonus_payment_status;
        this.bonus_payment_time = bonus_payment_time;
        this.bonus_payment_type = bonus_payment_type;
        this.bonus_remain_num = bonus_remain_num;
        this.bonus_total_amount = bonus_total_amount;
        this.bonus_total_num = bonus_total_num;
        this.contact_address = contact_address;
        this.content = content;
        this.create_time = create_time;
        this.description = description;
        this.lastmodified_time = lastmodified_time;
        this.lat = lat;
        this.lng = lng;
        this.pub_begin_time = pub_begin_time;
        this.pub_end_time = pub_end_time;
        this.pub_ext_id = pub_ext_id;
        this.pub_ext_type = pub_ext_type;
        this.pub_name = pub_name;
        this.pub_user_id = pub_user_id;
        this.pub_user_logo = pub_user_logo;
        this.pub_user_nickname = pub_user_nickname;
        this.pub_user_realname = pub_user_realname;
        this.refund_finish_time = refund_finish_time;
        this.refund_request_time = refund_request_time;
        this.refund_status = refund_status;
        this.refund_total_amount = refund_total_amount;
        this.status = status;
        this.title = title;
    }

    public double getContact_lng() {
        return contact_lng;
    }

    public void setContact_lng(double contact_lng) {
        this.contact_lng = contact_lng;
    }

    public double getContact_lat() {
        return contact_lat;
    }

    public void setContact_lat(double contact_lat) {
        this.contact_lat = contact_lat;
    }

    public String getContact_address() {
        return contact_address;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address;
    }

    public String getAdv_id() {
        return adv_id;
    }

    public void setAdv_id(String adv_id) {
        this.adv_id = adv_id;
    }

    public String getBonus_accepted_num() {
        return bonus_accepted_num;
    }

    public void setBonus_accepted_num(String bonus_accepted_num) {
        this.bonus_accepted_num = bonus_accepted_num;
    }

    public String getBonus_created() {
        return bonus_created;
    }

    public void setBonus_created(String bonus_created) {
        this.bonus_created = bonus_created;
    }

    public String getBonus_payment_create_time() {
        return bonus_payment_create_time;
    }

    public void setBonus_payment_create_time(String bonus_payment_create_time) {
        this.bonus_payment_create_time = bonus_payment_create_time;
    }

    public String getBonus_payment_order_id() {
        return bonus_payment_order_id;
    }

    public void setBonus_payment_order_id(String bonus_payment_order_id) {
        this.bonus_payment_order_id = bonus_payment_order_id;
    }

    public String getBonus_payment_status() {
        return bonus_payment_status;
    }

    public void setBonus_payment_status(String bonus_payment_status) {
        this.bonus_payment_status = bonus_payment_status;
    }

    public String getBonus_payment_time() {
        return bonus_payment_time;
    }

    public void setBonus_payment_time(String bonus_payment_time) {
        this.bonus_payment_time = bonus_payment_time;
    }

    public String getBonus_payment_type() {
        return bonus_payment_type;
    }

    public void setBonus_payment_type(String bonus_payment_type) {
        this.bonus_payment_type = bonus_payment_type;
    }

    public String getBonus_remain_num() {
        return bonus_remain_num;
    }

    public void setBonus_remain_num(String bonus_remain_num) {
        this.bonus_remain_num = bonus_remain_num;
    }

    public String getBonus_total_amount() {
        return bonus_total_amount;
    }

    public void setBonus_total_amount(String bonus_total_amount) {
        this.bonus_total_amount = bonus_total_amount;
    }

    public String getBonus_total_num() {
        return bonus_total_num;
    }

    public void setBonus_total_num(String bonus_total_num) {
        this.bonus_total_num = bonus_total_num;
    }

    public BeanContent getContent() {
        return content;
    }

    public void setContent(BeanContent content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastmodified_time() {
        return lastmodified_time;
    }

    public void setLastmodified_time(String lastmodified_time) {
        this.lastmodified_time = lastmodified_time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPub_begin_time() {
        return pub_begin_time;
    }

    public void setPub_begin_time(String pub_begin_time) {
        this.pub_begin_time = pub_begin_time;
    }

    public String getPub_end_time() {
        return pub_end_time;
    }

    public void setPub_end_time(String pub_end_time) {
        this.pub_end_time = pub_end_time;
    }

    public String getPub_ext_id() {
        return pub_ext_id;
    }

    public void setPub_ext_id(String pub_ext_id) {
        this.pub_ext_id = pub_ext_id;
    }

    public String getPub_ext_type() {
        return pub_ext_type;
    }

    public void setPub_ext_type(String pub_ext_type) {
        this.pub_ext_type = pub_ext_type;
    }

    public String getPub_name() {
        return pub_name;
    }

    public void setPub_name(String pub_name) {
        this.pub_name = pub_name;
    }

    public String getPub_user_id() {
        return pub_user_id;
    }

    public void setPub_user_id(String pub_user_id) {
        this.pub_user_id = pub_user_id;
    }

    public String getPub_user_logo() {
        return pub_user_logo;
    }

    public void setPub_user_logo(String pub_user_logo) {
        this.pub_user_logo = pub_user_logo;
    }

    public String getPub_user_nickname() {
        return pub_user_nickname;
    }

    public void setPub_user_nickname(String pub_user_nickname) {
        this.pub_user_nickname = pub_user_nickname;
    }

    public String getPub_user_realname() {
        return pub_user_realname;
    }

    public void setPub_user_realname(String pub_user_realname) {
        this.pub_user_realname = pub_user_realname;
    }

    public String getRefund_finish_time() {
        return refund_finish_time;
    }

    public void setRefund_finish_time(String refund_finish_time) {
        this.refund_finish_time = refund_finish_time;
    }

    public String getRefund_request_time() {
        return refund_request_time;
    }

    public void setRefund_request_time(String refund_request_time) {
        this.refund_request_time = refund_request_time;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getRefund_total_amount() {
        return refund_total_amount;
    }

    public void setRefund_total_amount(String refund_total_amount) {
        this.refund_total_amount = refund_total_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBonus_accepted_amount() {
        return bonus_accepted_amount;
    }

    public void setBonus_accepted_amount(String bonus_accepted_amount) {
        this.bonus_accepted_amount = bonus_accepted_amount;
    }


    @Override
    public String toString() {
        return "BeanMyadv{" +
                "adv_id='" + adv_id + '\'' +
                ", bonus_accepted_amount='" + bonus_accepted_amount + '\'' +
                ", bonus_accepted_num='" + bonus_accepted_num + '\'' +
                ", bonus_created='" + bonus_created + '\'' +
                ", bonus_payment_create_time='" + bonus_payment_create_time + '\'' +
                ", bonus_payment_order_id='" + bonus_payment_order_id + '\'' +
                ", bonus_payment_status='" + bonus_payment_status + '\'' +
                ", bonus_payment_time='" + bonus_payment_time + '\'' +
                ", bonus_payment_type='" + bonus_payment_type + '\'' +
                ", bonus_remain_num='" + bonus_remain_num + '\'' +
                ", bonus_total_amount='" + bonus_total_amount + '\'' +
                ", bonus_total_num='" + bonus_total_num + '\'' +
                ", contact_address='" + contact_address + '\'' +
                ", content=" + content +
                ", create_time='" + create_time + '\'' +
                ", description='" + description + '\'' +
                ", lastmodified_time='" + lastmodified_time + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", pub_begin_time='" + pub_begin_time + '\'' +
                ", pub_end_time='" + pub_end_time + '\'' +
                ", pub_ext_id='" + pub_ext_id + '\'' +
                ", pub_ext_type='" + pub_ext_type + '\'' +
                ", pub_name='" + pub_name + '\'' +
                ", pub_user_id='" + pub_user_id + '\'' +
                ", pub_user_logo='" + pub_user_logo + '\'' +
                ", pub_user_nickname='" + pub_user_nickname + '\'' +
                ", pub_user_realname='" + pub_user_realname + '\'' +
                ", refund_finish_time='" + refund_finish_time + '\'' +
                ", refund_request_time='" + refund_request_time + '\'' +
                ", refund_status='" + refund_status + '\'' +
                ", refund_total_amount='" + refund_total_amount + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
