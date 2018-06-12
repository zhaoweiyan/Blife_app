package com.blife.blife_app.adv.advmine.bean;

/**
 * Created by Somebody on 2016/9/2.
 */
public class BeanMyAdvDetail {

    private BeanMyadv info;

    public BeanMyAdvDetail() {
    }

    public BeanMyAdvDetail(BeanMyadv info) {
        this.info = info;
    }

    public BeanMyadv getInfo() {
        return info;
    }

    public void setInfo(BeanMyadv info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "BeanMyAdvDetail{" +
                "info=" + info +
                '}';
    }
}
