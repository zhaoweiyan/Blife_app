package com.blife.blife_app.index.bean;

import java.util.List;

/**
 * Created by Somebody on 2016/8/26.
 */
public class BeanWords {
    private String transfer_request;
    private List<String> transfer_request_notice;

    public BeanWords() {
    }

    public BeanWords(String transfer_request, List<String> transfer_request_notice) {
        this.transfer_request = transfer_request;
        this.transfer_request_notice = transfer_request_notice;
    }

    public String getTransfer_request() {
        return transfer_request;
    }

    public void setTransfer_request(String transfer_request) {
        this.transfer_request = transfer_request;
    }

    public List<String> getTransfer_request_notice() {
        return transfer_request_notice;
    }

    public void setTransfer_request_notice(List<String> transfer_request_notice) {
        this.transfer_request_notice = transfer_request_notice;
    }

    @Override
    public String toString() {
        return "BeanWords{" +
                "transfer_request='" + transfer_request + '\'' +
                ", transfer_request_notice=" + transfer_request_notice +
                '}';
    }
}
