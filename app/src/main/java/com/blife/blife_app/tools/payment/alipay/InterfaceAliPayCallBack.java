package com.blife.blife_app.tools.payment.alipay;

/**
 * Created by w on 2016/9/5.
 */
public interface InterfaceAliPayCallBack {
    /**
     * 支付成功
     */
    void aliPaySuccess();

    /**
     * 支付失败
     */
    void aliPayFailed();

    /**
     * 取消支付
     */
    void aliPayCancel();

}
