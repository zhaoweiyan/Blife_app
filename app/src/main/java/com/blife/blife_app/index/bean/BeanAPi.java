package com.blife.blife_app.index.bean;

/**
 * Created by Somebody on 2016/8/26.
 */
public class BeanAPi {
    private String host;
    private String port;

    public BeanAPi() {
    }

    public BeanAPi(String host, String port) {
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
        return "BeanAPi{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
