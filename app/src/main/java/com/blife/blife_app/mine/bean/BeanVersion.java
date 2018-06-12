package com.blife.blife_app.mine.bean;

import com.blife.blife_app.tools.BitmapHelp;

import java.util.List;

/**
 * Created by Somebody on 2016/9/20.
 */
public class BeanVersion {

    private boolean mandatory;
    private List<String> packages;
    private String platform;
    private boolean upgrade;
    private String version;

    public BeanVersion() {
    }

    public BeanVersion(boolean mandatory, List<String> packages, String platform, boolean upgrade, String version) {
        this.mandatory = mandatory;
        this.packages = packages;
        this.platform = platform;
        this.upgrade = upgrade;
        this.version = version;
    }


    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isUpgrade() {
        return upgrade;
    }

    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "BeanVersion{" +
                "mandatory=" + mandatory +
                ", packages=" + packages +
                ", platform='" + platform + '\'' +
                ", upgrade=" + upgrade +
                ", version='" + version + '\'' +
                '}';
    }
}
