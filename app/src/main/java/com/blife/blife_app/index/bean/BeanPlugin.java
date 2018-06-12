package com.blife.blife_app.index.bean;

/**
 * Created by Somebody on 2016/8/29.
 */
public class BeanPlugin {

    private String app_id;
    private String create_time;
    private String description;
    private String lastmodified_time;
    private String logo;
    private String name;
    private String target;
    private String link;

    public BeanPlugin() {
    }

    public BeanPlugin(String app_id, String create_time, String description, String lastmodified_time, String link, String logo, String name, String target) {
        this.app_id = app_id;
        this.create_time = create_time;
        this.description = description;
        this.lastmodified_time = lastmodified_time;
        this.link = link;
        this.logo = logo;
        this.name = name;
        this.target = target;
    }

    public String getApp_id() {
        return app_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getDescription() {
        return description;
    }

    public String getLastmodified_time() {
        return lastmodified_time;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public String getTarget() {
        return target;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastmodified_time(String lastmodified_time) {
        this.lastmodified_time = lastmodified_time;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "BeanPlugin{" +
                "app_id='" + app_id + '\'' +
                ", create_time='" + create_time + '\'' +
                ", description='" + description + '\'' +
                ", lastmodified_time='" + lastmodified_time + '\'' +
                ", link='" + link + '\'' +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
