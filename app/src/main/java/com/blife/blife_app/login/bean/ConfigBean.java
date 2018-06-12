package com.blife.blife_app.login.bean;

/**
 * Created by w on 2016/8/22.
 */
public class ConfigBean {

    private ApiBean api;
    private String password_secret_key;

    public ConfigBean() {
    }

    public ConfigBean(ApiBean api, String password_secret_key) {
        this.api = api;
        this.password_secret_key = password_secret_key;
    }

    public ApiBean getApi() {
        return api;
    }

    public void setApi(ApiBean api) {
        this.api = api;
    }

    public String getPassword_secret_key() {
        return password_secret_key;
    }

    public void setPassword_secret_key(String password_secret_key) {
        this.password_secret_key = password_secret_key;
    }

    @Override
    public String toString() {
        return "ConfigBean{" +
                "api=" + api +
                ", password_secret_key='" + password_secret_key + '\'' +
                '}';
    }
}
