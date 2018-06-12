package com.blife.blife_app.index.bean;

/**
 * Created by Somebody on 2016/8/25.
 */
public class BeanHomeTurnImages {

    private String adv_image;
    private String adv_merchant_name;
    private String adv_target;
    private String begin_time;
    private String create_time;
    private String end_time;
    private String id;
    private String lastmodified_time;
    private String status;

    public BeanHomeTurnImages() {
    }

    public BeanHomeTurnImages(String adv_image, String adv_merchant_name, String adv_target, String begin_time, String create_time, String end_time, String id, String lastmodified_time, String status) {
        this.adv_image = adv_image;
        this.adv_merchant_name = adv_merchant_name;
        this.adv_target = adv_target;
        this.begin_time = begin_time;
        this.create_time = create_time;
        this.end_time = end_time;
        this.id = id;
        this.lastmodified_time = lastmodified_time;
        this.status = status;
    }

    public String getAdv_image() {
        return adv_image;
    }

    public void setAdv_image(String adv_image) {
        this.adv_image = adv_image;
    }

    public String getAdv_merchant_name() {
        return adv_merchant_name;
    }

    public void setAdv_merchant_name(String adv_merchant_name) {
        this.adv_merchant_name = adv_merchant_name;
    }

    public String getAdv_target() {
        return adv_target;
    }

    public void setAdv_target(String adv_target) {
        this.adv_target = adv_target;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastmodified_time() {
        return lastmodified_time;
    }

    public void setLastmodified_time(String lastmodified_time) {
        this.lastmodified_time = lastmodified_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BeanHomeTurnImages{" +
                "adv_image='" + adv_image + '\'' +
                ", adv_merchant_name='" + adv_merchant_name + '\'' +
                ", adv_target='" + adv_target + '\'' +
                ", begin_time='" + begin_time + '\'' +
                ", create_time='" + create_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", id='" + id + '\'' +
                ", lastmodified_time='" + lastmodified_time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
