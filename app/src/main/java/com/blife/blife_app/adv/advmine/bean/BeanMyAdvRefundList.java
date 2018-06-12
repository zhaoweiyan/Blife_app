package com.blife.blife_app.adv.advmine.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/9/7.
 */
public class BeanMyAdvRefundList {

    private List<BeanMyAdvRefund> list;
    private String pub_name;


    public BeanMyAdvRefundList() {
    }

    public BeanMyAdvRefundList(List<BeanMyAdvRefund> list) {
        this.list = list;
    }

    public List<BeanMyAdvRefund> getList() {
        return list;
    }

    public void setList(List<BeanMyAdvRefund> list) {
        this.list = list;
    }

    public String getPub_name() {
        return pub_name;
    }

    public void setPub_name(String pub_name) {
        this.pub_name = pub_name;
    }

    @Override
    public String toString() {
        return "BeanMyAdvRefundList{" +
                "list=" + list +
                ", pub_name='" + pub_name + '\'' +
                '}';
    }
}
