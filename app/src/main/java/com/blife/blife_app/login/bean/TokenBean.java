package com.blife.blife_app.login.bean;

import android.text.TextUtils;

/**
 * Created by w on 2016/8/24.
 */
public class TokenBean {
//{"userid":"100014719395147982919707","accesstoken":"dc318286a469f8e7f15d645e0d6d35d6979f37001472011457",
// "sourceip":"220.231.48.130","time":1472011457,"expire_time":2592000}}

    private String userid;
    private String accesstoken;
    private String sourceip;
    private long time;
    private long expire_time;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getSourceip() {
        return sourceip;
    }

    public void setSourceip(String sourceip) {
        this.sourceip = sourceip;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(long expire_time) {
        this.expire_time = expire_time;
    }

}
