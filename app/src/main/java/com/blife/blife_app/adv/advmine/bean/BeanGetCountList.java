package com.blife.blife_app.adv.advmine.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/9/8.
 */
public class BeanGetCountList {

    private List<BeanGetCount> acccepted_list;
    private BeanMyadv info;

    public BeanGetCountList() {
    }

    public BeanGetCountList(BeanMyadv info, List<BeanGetCount> acccepted_list) {
        this.info = info;
        this.acccepted_list = acccepted_list;
    }

    public List<BeanGetCount> getAcccepted_list() {
        return acccepted_list;
    }

    public void setAcccepted_list(List<BeanGetCount> acccepted_list) {
        this.acccepted_list = acccepted_list;
    }

    public BeanMyadv getInfo() {
        return info;
    }

    public void setInfo(BeanMyadv info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "BeanGetCountList{" +
                "acccepted_list=" + acccepted_list +
                ", info=" + info +
                '}';
    }
}
