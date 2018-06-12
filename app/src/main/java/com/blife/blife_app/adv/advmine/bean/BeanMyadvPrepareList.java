package com.blife.blife_app.adv.advmine.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/8/29.
 */
public class BeanMyadvPrepareList {

    private List<BeanMyadv> list;

    public BeanMyadvPrepareList() {
    }

    public BeanMyadvPrepareList(List<BeanMyadv> list) {
        this.list = list;
    }

    public List<BeanMyadv> getList() {
        return list;
    }

    public void setList(List<BeanMyadv> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "BeanMyadvList{" +
                "list=" + list +
                '}';
    }
}
