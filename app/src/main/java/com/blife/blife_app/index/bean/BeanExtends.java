package com.blife.blife_app.index.bean;

/**
 * Created by Somebody on 2016/10/26.
 */
public class BeanExtends {

    private String content;
    private String image;
    private String title;

    public BeanExtends() {
    }

    public BeanExtends(String content, String image, String title) {
        this.content = content;
        this.image = image;
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "BeanExtends{" +
                "content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
