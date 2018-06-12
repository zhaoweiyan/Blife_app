package com.blife.blife_app.mine.bean;

/**
 * Created by Somebody on 2016/9/14.
 */
public class BeanVerfyResult {
    private long last_verify_time;
    private int verify_status;
    private String verify_reason;

    public BeanVerfyResult() {
    }

    public BeanVerfyResult(long last_verify_time, int verify_status, String verify_reason) {
        this.last_verify_time = last_verify_time;
        this.verify_status = verify_status;
        this.verify_reason = verify_reason;
    }

    public BeanVerfyResult(long last_verify_time, int verify_status) {
        this.last_verify_time = last_verify_time;
        this.verify_status = verify_status;
    }

    public String getVerify_reason() {
        return verify_reason;
    }

    public void setVerify_reason(String verify_reason) {
        this.verify_reason = verify_reason;
    }

    public long getLast_verify_time() {
        return last_verify_time;
    }

    public void setLast_verify_time(long last_verify_time) {
        this.last_verify_time = last_verify_time;
    }

    public int getVerify_status() {
        return verify_status;
    }

    public void setVerify_status(int verify_status) {
        this.verify_status = verify_status;
    }

    @Override
    public String toString() {
        return "BeanVerfyResult{" +
                "last_verify_time=" + last_verify_time +
                ", verify_status=" + verify_status +
                ", verify_reason='" + verify_reason + '\'' +
                '}';
    }
}
