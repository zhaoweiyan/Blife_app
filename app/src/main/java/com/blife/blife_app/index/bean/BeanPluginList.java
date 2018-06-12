package com.blife.blife_app.index.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/8/29.
 */
public class BeanPluginList {

   private List<BeanPlugin> list;

    public BeanPluginList() {
    }

    public BeanPluginList(List<BeanPlugin> list) {
        this.list = list;
    }

    public List<BeanPlugin> getList() {
        return list;
    }

    public void setList(List<BeanPlugin> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "BeanPluginList{" +
                "list=" + list +
                '}';
    }
}
