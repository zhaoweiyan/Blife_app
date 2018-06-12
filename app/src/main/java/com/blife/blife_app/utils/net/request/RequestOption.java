package com.blife.blife_app.utils.net.request;

import okhttp3.Request;

/**
 * Created by w on 2016/8/8.
 */
public class RequestOption {

    private Request request;//请求Request
    private String filePath;//下载文件保存路径
    private boolean putFile;
    private boolean location;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public boolean isPutFile() {
        return putFile;
    }

    public boolean isLocation() {
        return location;
    }

    public void setPutFile(boolean putFile) {
        this.putFile = putFile;
    }

    public void setLocation(boolean location) {
        this.location = location;
    }
}
