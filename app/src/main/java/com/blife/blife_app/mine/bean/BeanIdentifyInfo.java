package com.blife.blife_app.mine.bean;

/**
 * Created by Somebody on 2016/9/14.
 */
public class BeanIdentifyInfo {

    private String address;
    private String category;
    private long create_time;
    private BeanExtraContent extra_content;
    private String iden_id;
    private long lastmodified_time;
    private String name;
    private int type;


    public BeanIdentifyInfo() {
    }

    public BeanIdentifyInfo(String address, String category, long create_time, BeanExtraContent extra_content, String iden_id, long lastmodified_time, String name, int type) {
        this.address = address;
        this.category = category;
        this.create_time = create_time;
        this.extra_content = extra_content;
        this.iden_id = iden_id;
        this.lastmodified_time = lastmodified_time;
        this.name = name;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public BeanExtraContent getExtra_content() {
        return extra_content;
    }

    public void setExtra_content(BeanExtraContent extra_content) {
        this.extra_content = extra_content;
    }

    public String getIden_id() {
        return iden_id;
    }

    public void setIden_id(String iden_id) {
        this.iden_id = iden_id;
    }

    public long getLastmodified_time() {
        return lastmodified_time;
    }

    public void setLastmodified_time(long lastmodified_time) {
        this.lastmodified_time = lastmodified_time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BeanIdentifyInfo{" +
                "address='" + address + '\'' +
                ", category='" + category + '\'' +
                ", create_time=" + create_time +
                ", extra_content=" + extra_content +
                ", iden_id='" + iden_id + '\'' +
                ", lastmodified_time=" + lastmodified_time +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
