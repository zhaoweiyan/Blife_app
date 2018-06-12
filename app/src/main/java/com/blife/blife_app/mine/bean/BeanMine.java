package com.blife.blife_app.mine.bean;

/**
 * Created by Somebody on 2016/8/29.
 */
public class BeanMine {

    private long birthday;
    private long create_time;
    private String email;
    private int gender;
    private String headimg;
    private long last_verify_time;
    private String nickname;
    private String realname;
    private String reg_lat;
    private String reg_lng;
    private String secretkey;
    private int status;
    private String telphone;
    private int type;
    private String union_ext_id;
    private String verify_reason;
    private int verify_status;

    public BeanMine() {
    }

    public BeanMine(long birthday, long create_time, String email, int gender, String headimg, long last_verify_time, String nickname, String realname, String reg_lat, String reg_lng, String secretkey, int status, String telphone, int type, String union_ext_id, String verify_reason, int verify_status) {
        this.birthday = birthday;
        this.create_time = create_time;
        this.email = email;
        this.gender = gender;
        this.headimg = headimg;
        this.last_verify_time = last_verify_time;
        this.nickname = nickname;
        this.realname = realname;
        this.reg_lat = reg_lat;
        this.reg_lng = reg_lng;
        this.secretkey = secretkey;
        this.status = status;
        this.telphone = telphone;
        this.type = type;
        this.union_ext_id = union_ext_id;
        this.verify_reason = verify_reason;
        this.verify_status = verify_status;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public long getLast_verify_time() {
        return last_verify_time;
    }

    public void setLast_verify_time(long last_verify_time) {
        this.last_verify_time = last_verify_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getReg_lat() {
        return reg_lat;
    }

    public void setReg_lat(String reg_lat) {
        this.reg_lat = reg_lat;
    }

    public String getReg_lng() {
        return reg_lng;
    }

    public void setReg_lng(String reg_lng) {
        this.reg_lng = reg_lng;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUnion_ext_id() {
        return union_ext_id;
    }

    public void setUnion_ext_id(String union_ext_id) {
        this.union_ext_id = union_ext_id;
    }

    public String getVerify_reason() {
        return verify_reason;
    }

    public void setVerify_reason(String verify_reason) {
        this.verify_reason = verify_reason;
    }

    public int getVerify_status() {
        return verify_status;
    }

    public void setVerify_status(int verify_status) {
        this.verify_status = verify_status;
    }

    @Override
    public String toString() {
        return "BeanMine{" +
                "birthday=" + birthday +
                ", create_time=" + create_time +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", headimg='" + headimg + '\'' +
                ", last_verify_time=" + last_verify_time +
                ", nickname='" + nickname + '\'' +
                ", realname='" + realname + '\'' +
                ", reg_lat='" + reg_lat + '\'' +
                ", reg_lng='" + reg_lng + '\'' +
                ", secretkey='" + secretkey + '\'' +
                ", status=" + status +
                ", telphone='" + telphone + '\'' +
                ", type=" + type +
                ", union_ext_id='" + union_ext_id + '\'' +
                ", verify_reason='" + verify_reason + '\'' +
                ", verify_status=" + verify_status +
                '}';
    }
}
