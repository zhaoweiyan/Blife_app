package com.blife.blife_app.adv.advmine.bean;

/**
 * Created by Somebody on 2016/9/6.
 */
public class BeanMyadvDetailBonus {


    private BeanBonusSetInfo info;

    public BeanMyadvDetailBonus() {
    }


    public BeanMyadvDetailBonus(BeanBonusSetInfo info) {
        this.info = info;
    }

    public BeanBonusSetInfo getInfo() {
        return info;
    }

    public void setInfo(BeanBonusSetInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "BeanMyadvDetailBonus{" +
                "info=" + info +
                '}';
    }
}
