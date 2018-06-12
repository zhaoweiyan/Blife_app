package com.blife.blife_app.adv.advmine.bean;

import java.util.ArrayList;

/**
 * Created by Somebody on 2016/9/6.
 */
public class BeanMyAdvPublishAddressList {

    private ArrayList<BeanMyAdvPublishAddress> list;

    public BeanMyAdvPublishAddressList() {
    }

    public BeanMyAdvPublishAddressList(ArrayList<BeanMyAdvPublishAddress> list) {
        this.list = list;
    }

    public ArrayList<BeanMyAdvPublishAddress> getList() {
        return list;
    }

    public void setList(ArrayList<BeanMyAdvPublishAddress> list) {
        this.list = list;
    }


    @Override
    public String toString() {
        return "BeanMyAdvPublishAddressList{" +
                "list=" + list +
                '}';
    }
}
