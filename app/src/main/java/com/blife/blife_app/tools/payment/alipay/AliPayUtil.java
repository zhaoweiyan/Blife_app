package com.blife.blife_app.tools.payment.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

/**
 * Created by w on 2016/6/17.
 */
public class AliPayUtil {

    private Activity ACTIVITY;
    private Handler HANDLER;
    private String pay_info;
    private final int SDK_PAY_FLAG = 1;
    private InterfaceAliPayCallBack aliPayCallBack;

    //支付状态
    private final int PAY_SUCCESS = 9000;
    private final int PAY_CANCEL = 6001;
    private final int PAY_LOADING = 8000;

    public AliPayUtil(Activity ACTIVITY, InterfaceAliPayCallBack callBack) {
        this.ACTIVITY = ACTIVITY;
        this.aliPayCallBack = callBack;
        HANDLER = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e("TAG", "支付宝支付结果：" + msg.obj.toString());
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        PayResult payResult = new PayResult((String) msg.obj);
                        /**
                         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                         * docType=1) 建议商户依赖异步通知
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        Log.e("TAG", "支付宝支付结果：" + resultInfo);
                        String resultStatus = payResult.getResultStatus();
                        Log.e("TAG", "支付宝支付结果Code：" + resultStatus);
                        int status = Integer.valueOf(resultStatus);
                        switch (status) {
                            case PAY_SUCCESS:// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                                if (aliPayCallBack != null)
                                    aliPayCallBack.aliPaySuccess();
                                break;
                            case PAY_LOADING:// 判断resultStatus 为非"9000"则代表可能支付失败,"8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                if (aliPayCallBack != null)
                                    aliPayCallBack.aliPaySuccess();
                                break;
                            case PAY_CANCEL: // 用户主动取消支付
                                if (aliPayCallBack != null)
                                    aliPayCallBack.aliPayCancel();
                                break;
                            default:// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                if (aliPayCallBack != null)
                                    aliPayCallBack.aliPayFailed();
                                break;
                        }
                        break;
                    }
                }
            }
        };
    }

    public void pay(String PAY_INFO) {
        pay_info = PAY_INFO;
        if (TextUtils.isEmpty(pay_info)) {
            return;
        }
        Runnable PAY_RUNNABLE = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(ACTIVITY);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(pay_info, true);
                Message MSG = new Message();
                MSG.what = SDK_PAY_FLAG;
                MSG.obj = result;
                HANDLER.sendMessage(MSG);
            }
        };

        // 必须异步调用
        Thread PAY_THREAD = new Thread(PAY_RUNNABLE);
        PAY_THREAD.start();
    }
}
