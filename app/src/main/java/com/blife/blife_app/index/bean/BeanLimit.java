package com.blife.blife_app.index.bean;

/**
 * Created by Somebody on 2016/10/17.
 */
public class BeanLimit {
    private String transfer_min_amount;
    private int trasnfer_request_limit_per_day;

    public BeanLimit() {
    }

    public BeanLimit(String transfer_min_amount, int trasnfer_request_limit_per_day) {
        this.transfer_min_amount = transfer_min_amount;
        this.trasnfer_request_limit_per_day = trasnfer_request_limit_per_day;
    }

    public String getTransfer_min_amount() {
        return transfer_min_amount;
    }

    public void setTransfer_min_amount(String transfer_min_amount) {
        this.transfer_min_amount = transfer_min_amount;
    }

    public int getTrasnfer_request_limit_per_day() {
        return trasnfer_request_limit_per_day;
    }

    public void setTrasnfer_request_limit_per_day(int trasnfer_request_limit_per_day) {
        this.trasnfer_request_limit_per_day = trasnfer_request_limit_per_day;
    }

    @Override
    public String toString() {
        return "BeanLimit{" +
                "transfer_min_amount='" + transfer_min_amount + '\'' +
                ", trasnfer_request_limit_per_day=" + trasnfer_request_limit_per_day +
                '}';
    }
}
