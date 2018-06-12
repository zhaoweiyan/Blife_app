package com.blife.blife_app.mine.bean;

/**
 * Created by Somebody on 2016/9/14.
 */
public class BeanIdentify {

    private BeanIdentifyInfo info;
    private BeanVerfyResult verify_result;

    public BeanIdentify() {
    }

    public BeanIdentify(BeanIdentifyInfo info, BeanVerfyResult verify_result) {
        this.info = info;
        this.verify_result = verify_result;
    }

    public BeanIdentifyInfo getInfo() {
        return info;
    }

    public void setInfo(BeanIdentifyInfo info) {
        this.info = info;
    }

    public BeanVerfyResult getVerify_result() {
        return verify_result;
    }

    public void setVerify_result(BeanVerfyResult verify_result) {
        this.verify_result = verify_result;
    }

    @Override
    public String toString() {
        return "BeanIdentify{" +
                "info=" + info +
                ", verify_result=" + verify_result +
                '}';
    }
}
