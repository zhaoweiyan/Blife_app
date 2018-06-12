package com.blife.blife_app.index.bean;

/**
 * Created by Somebody on 2016/9/21.
 */
public class BeanJPushRecevier {

    private String content;
    private String notify_image;
    private String pub_name;
    private String title;
    private String type;

    public BeanJPushRecevier() {
    }

    public BeanJPushRecevier(String content, String notify_image, String pub_name, String title, String type) {
        this.content = content;
        this.notify_image = notify_image;
        this.pub_name = pub_name;
        this.title = title;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotify_image() {
        return notify_image;
    }

    public void setNotify_image(String notify_image) {
        this.notify_image = notify_image;
    }

    public String getPub_name() {
        return pub_name;
    }

    public void setPub_name(String pub_name) {
        this.pub_name = pub_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BeanJPushRecevier{" +
                "content='" + content + '\'' +
                ", notify_image='" + notify_image + '\'' +
                ", pub_name='" + pub_name + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
