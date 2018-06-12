package com.blife.blife_app.index.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/8/29.
 */
public class BeanList<T> {

    private List<T> list;

    public BeanList() {
    }

    public BeanList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "BeanList{" +
                "list=" + list +
                '}';
    }
}
