package com.blife.blife_app.index.bean;

/**
 * Created by Somebody on 2016/9/7.
 */
public class BeanContact {
    private String phone;

    public BeanContact() {
    }

    public BeanContact(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "BeanContact{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
