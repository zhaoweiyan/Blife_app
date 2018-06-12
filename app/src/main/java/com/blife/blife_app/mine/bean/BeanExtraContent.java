package com.blife.blife_app.mine.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/9/14.
 */
public class BeanExtraContent {

private List<String>  images;

    public BeanExtraContent() {
    }

    public BeanExtraContent(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "BeanExtraContent{" +
                "images=" + images +
                '}';
    }
}
