package com.blife.blife_app.mine.bean;

/**
 * Created by Somebody on 2016/9/19.
 */
public class BeanIncomeExpense {

    private String amount;
    private long create_time;
    private String log_id;
    private String source;
    private String source_id;
    private String source_str;
    private String type;
    private String user_id;

    public BeanIncomeExpense() {
    }

    public BeanIncomeExpense(String amount, long create_time, String log_id, String source, String source_id, String source_str, String type, String user_id) {
        this.amount = amount;
        this.create_time = create_time;
        this.log_id = log_id;
        this.source = source;
        this.source_id = source_id;
        this.source_str = source_str;
        this.type = type;
        this.user_id = user_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSource_str() {
        return source_str;
    }

    public void setSource_str(String source_str) {
        this.source_str = source_str;
    }

    @Override
    public String toString() {
        return "BeanIncomeExpense{" +
                "amount='" + amount + '\'' +
                ", create_time=" + create_time +
                ", log_id='" + log_id + '\'' +
                ", source='" + source + '\'' +
                ", source_id='" + source_id + '\'' +
                ", source_str='" + source_str + '\'' +
                ", type='" + type + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
