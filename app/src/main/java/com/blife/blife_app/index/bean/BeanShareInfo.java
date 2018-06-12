package com.blife.blife_app.index.bean;

/**
 * Created by Somebody on 2016/9/20.
 */
public class BeanShareInfo {

    private String description;
    private String description_on_accepted;
    private String description_on_pay_success;
    private String link;
    private String title;
    private String title_on_pay_success;
//    private String super_bonus_title;
    private String super_bonus_description;

    public BeanShareInfo() {
    }

    public BeanShareInfo(String description, String description_on_accepted, String description_on_pay_success, String link, String title, String title_on_pay_success) {
        this.description = description;
        this.description_on_accepted = description_on_accepted;
        this.description_on_pay_success = description_on_pay_success;
        this.link = link;
        this.title = title;
        this.title_on_pay_success = title_on_pay_success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription_on_accepted() {
        return description_on_accepted;
    }

    public void setDescription_on_accepted(String description_on_accepted) {
        this.description_on_accepted = description_on_accepted;
    }

    public String getDescription_on_pay_success() {
        return description_on_pay_success;
    }

    public void setDescription_on_pay_success(String description_on_pay_success) {
        this.description_on_pay_success = description_on_pay_success;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_on_pay_success() {
        return title_on_pay_success;
    }

    public void setTitle_on_pay_success(String title_on_pay_success) {
        this.title_on_pay_success = title_on_pay_success;
    }

//    public String getSuper_bonus_title() {
//        return super_bonus_title;
//    }
//
//    public void setSuper_bonus_title(String super_bonus_title) {
//        this.super_bonus_title = super_bonus_title;
//    }

    public String getSuper_bonus_description() {
        return super_bonus_description;
    }

    public void setSuper_bonus_description(String super_bonus_description) {
        this.super_bonus_description = super_bonus_description;
    }

    @Override
    public String toString() {
        return "BeanShareInfo{" +
                "description='" + description + '\'' +
                ", description_on_accepted='" + description_on_accepted + '\'' +
                ", description_on_pay_success='" + description_on_pay_success + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", title_on_pay_success='" + title_on_pay_success + '\'' +
                '}';
    }
}
