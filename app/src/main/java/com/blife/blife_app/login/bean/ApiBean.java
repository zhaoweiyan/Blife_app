package com.blife.blife_app.login.bean;

/**
 * Created by Somebody on 2016/8/23.
 */
public class ApiBean {

    private String host;
    private String port;

    public ApiBean() {
    }

    public ApiBean(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ApiBean{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
