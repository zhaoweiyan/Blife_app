package com.blife.blife_app.mine.bean;

/**
 * Created by Somebody on 2016/9/13.
 */
public class BeanAmount {

    private BeanBalance balance;

    public BeanAmount(BeanBalance balance) {
        this.balance = balance;
    }

    public BeanAmount() {
    }

    public BeanBalance getBalance() {
        return balance;
    }

    public void setBalance(BeanBalance balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BeanAmount{" +
                "balance=" + balance +
                '}';
    }
}
