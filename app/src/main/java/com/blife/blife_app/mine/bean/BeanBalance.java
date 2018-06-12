package com.blife.blife_app.mine.bean;

/**
 * Created by Somebody on 2016/9/13.
 */
public class BeanBalance {

    private long create_time;
    private long lastmodified_time;
    private String user_id;
    private String wallet_balance;

    public BeanBalance() {
    }

    public BeanBalance(long create_time, long lastmodified_time, String user_id, String wallet_balance) {
        this.create_time = create_time;
        this.lastmodified_time = lastmodified_time;
        this.user_id = user_id;
        this.wallet_balance = wallet_balance;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getLastmodified_time() {
        return lastmodified_time;
    }

    public void setLastmodified_time(long lastmodified_time) {
        this.lastmodified_time = lastmodified_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(String wallet_balance) {
        this.wallet_balance = wallet_balance;
    }

    @Override
    public String toString() {
        return "BeanBalance{" +
                "create_time=" + create_time +
                ", lastmodified_time=" + lastmodified_time +
                ", user_id='" + user_id + '\'' +
                ", wallet_balance=" + wallet_balance +
                '}';
    }
}
