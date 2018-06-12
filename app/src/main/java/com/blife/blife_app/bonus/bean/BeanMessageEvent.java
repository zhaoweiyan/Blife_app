package com.blife.blife_app.bonus.bean;

/**
 * Created by w on 2016/9/21.
 */
public class BeanMessageEvent {

    private String msg;
    private int Type;

    public BeanMessageEvent(String msg, int type) {
        this.msg = msg;
        Type = type;
    }

    public BeanMessageEvent(int type) {
        Type = type;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
