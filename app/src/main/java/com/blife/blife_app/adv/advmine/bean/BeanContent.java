package com.blife.blife_app.adv.advmine.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/9/1.
 */
public class BeanContent {

    private String contact_phone;
    private List<String> images;
    private String link;
    private String link_label;
    private List<String> videos;

    public BeanContent() {
    }

    public BeanContent(String contact_phone, List<String> images, String link, String link_label, List<String> videos) {
        this.contact_phone = contact_phone;
        this.images = images;
        this.link = link;
        this.link_label = link_label;
        this.videos = videos;
    }



    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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

    @Override
    public String toString() {
        return "BeanContent{" +
                "contact_phone='" + contact_phone + '\'' +
                ", images=" + images +
                ", link='" + link + '\'' +
                ", link_label='" + link_label + '\'' +
                ", videos=" + videos +
                '}';
    }
}
