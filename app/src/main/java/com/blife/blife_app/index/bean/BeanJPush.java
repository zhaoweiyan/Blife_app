package com.blife.blife_app.index.bean;

/**
 * Created by Somebody on 2016/9/20.
 */
public class BeanJPush {
    private String type;
    private String content;
    private String title;
    private String name;


    public BeanJPush() {
    }

    public BeanJPush(String type, String content, String title, String name) {
        this.type = type;
        this.content = content;
        this.title = title;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BeanJPush{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
