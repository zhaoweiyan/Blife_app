package com.blife.blife_app.bonus.bean;

import java.util.List;

/**
 * Created by w on 2016/9/14.
 */
public class BeanBonusContent {

    private List<String> images;
    private List<String> videos;
    private String link;
    private String link_label;
    private String contact_phone;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink_label() {
        return link_label;
    }

    public void setLink_label(String link_label) {
        this.link_label = link_label;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }
}
