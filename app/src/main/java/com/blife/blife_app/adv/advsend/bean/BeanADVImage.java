package com.blife.blife_app.adv.advsend.bean;

import android.widget.ImageView;

/**
 * Created by w on 2016/10/19.
 */
public class BeanADVImage {

    public BeanADVImage(int index) {
        this.index = index;
    }

    public BeanADVImage(int index, ImageView imageView) {
        this.index = index;
        this.imageView = imageView;
    }

    private int index;
    private ImageView imageView;
    private String diskPath;
    private String netPath;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getDiskPath() {
        return diskPath;
    }

    public void setDiskPath(String diskPath) {
        this.diskPath = diskPath;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }
}
