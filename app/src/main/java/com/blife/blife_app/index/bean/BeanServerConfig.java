package com.blife.blife_app.index.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/8/26.
 */
public class BeanServerConfig {

    private List<String> accusation_options;
    private BeanAPi api;
    private BeanContact contact;
    private BeanLimit global_params;
    private String password_secret_key;
    private BeanPayment payment;
    private BeanExtends promotion_share_info;
    private String push_alias_id;
    private BeanShareInfo share_info;
    private BeanWords suggest_words;
    private String timestamp;

    public BeanServerConfig() {


    }


    public BeanServerConfig(List<String> accusation_options, BeanAPi api, BeanContact contact, BeanLimit global_params, String password_secret_key, BeanPayment payment, BeanExtends promotion_share_info, String push_alias_id, BeanShareInfo share_info, BeanWords suggest_words, String timestamp) {
        this.accusation_options = accusation_options;
        this.api = api;
        this.contact = contact;
        this.global_params = global_params;
        this.password_secret_key = password_secret_key;
        this.payment = payment;
        this.promotion_share_info = promotion_share_info;
        this.push_alias_id = push_alias_id;
        this.share_info = share_info;
        this.suggest_words = suggest_words;
        this.timestamp = timestamp;
    }

    public List<String> getAccusation_options() {
        return accusation_options;
    }

    public void setAccusation_options(List<String> accusation_options) {
        this.accusation_options = accusation_options;
    }

    public BeanAPi getApi() {
        return api;
    }

    public void setApi(BeanAPi api) {
        this.api = api;
    }

    public BeanContact getContact() {
        return contact;
    }

    public void setContact(BeanContact contact) {
        this.contact = contact;
    }

    public BeanLimit getGlobal_params() {
        return global_params;
    }

    public void setGlobal_params(BeanLimit global_params) {
        this.global_params = global_params;
    }

    public String getPassword_secret_key() {
        return password_secret_key;
    }

    public void setPassword_secret_key(String password_secret_key) {
        this.password_secret_key = password_secret_key;
    }

    public BeanPayment getPayment() {
        return payment;
    }

    public void setPayment(BeanPayment payment) {
        this.payment = payment;
    }

    public BeanExtends getPromotion_share_info() {
        return promotion_share_info;
    }

    public void setPromotion_share_info(BeanExtends promotion_share_info) {
        this.promotion_share_info = promotion_share_info;
    }

    public String getPush_alias_id() {
        return push_alias_id;
    }

    public void setPush_alias_id(String push_alias_id) {
        this.push_alias_id = push_alias_id;
    }

    public BeanShareInfo getShare_info() {
        return share_info;
    }

    public void setShare_info(BeanShareInfo share_info) {
        this.share_info = share_info;
    }

    public BeanWords getSuggest_words() {
        return suggest_words;
    }

    public void setSuggest_words(BeanWords suggest_words) {
        this.suggest_words = suggest_words;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BeanServerConfig{" +
                "accusation_options=" + accusation_options +
                ", api=" + api +
                ", contact=" + contact +
                ", global_params=" + global_params +
                ", password_secret_key='" + password_secret_key + '\'' +
                ", payment=" + payment +
                ", promotion_share_info=" + promotion_share_info +
                ", push_alias_id='" + push_alias_id + '\'' +
                ", share_info=" + share_info +
                ", suggest_words=" + suggest_words +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
