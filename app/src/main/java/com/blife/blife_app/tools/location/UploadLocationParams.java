package com.blife.blife_app.tools.location;

/**
 * Created by w on 2016/8/26.
 */
public class UploadLocationParams {

    private String ACCESS_TOKEN;
    private String lat;
    private String lng;

    public String getACCESS_TOKEN() {
        return ACCESS_TOKEN;
    }

    public void setACCESS_TOKEN(String ACCESS_TOKEN) {
        this.ACCESS_TOKEN = ACCESS_TOKEN;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
