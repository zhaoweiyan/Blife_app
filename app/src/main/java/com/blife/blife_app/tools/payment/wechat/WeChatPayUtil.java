package com.blife.blife_app.tools.payment.wechat;

import android.content.Context;

import com.blife.blife_app.utils.logcat.L;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by w on 2016/6/17.
 */
public class WeChatPayUtil {

    private Context CONTEXT;
    private String APPID;
    private IWXAPI api;
    private PayReq payReq;

    public WeChatPayUtil(Context context, String appID) {
        CONTEXT = context;
        APPID = appID;
        //将APP注册到微信
        api = WXAPIFactory.createWXAPI(CONTEXT, APPID);
        api.registerApp(APPID);
        L.e("TAG", "微信支付--registerApp");
    }

    /**
     * @param partnerid
     * @param prepayId
     * @param packageValue
     * @param nonceStr
     * @param timeStamp
     * @param sign
     */
    public void createPayReq(String partnerid, String prepayId, String packageValue,
                             String nonceStr, String timeStamp, String sign) {
        if (!isPaySupported()) {
            return;
        }
        payReq = new PayReq();
        payReq.appId = APPID;
        payReq.partnerId = partnerid;
        payReq.prepayId = prepayId;
        payReq.packageValue = packageValue;
        payReq.nonceStr = nonceStr;
        payReq.timeStamp = timeStamp;
        payReq.sign = sign;
        L.e("TAG", "微信创建订单");
    }

    /**
     * 支付
     */
    public void pay() {
        if (!isPaySupported()) {
            return;
        }
        if (api != null && payReq != null) {
            L.e("TAG", "微信开始支付");
            api.sendReq(payReq);
        }
    }

    /**
     * 是否支持支付
     *
     * @return
     */
    public boolean isPaySupported() {
        boolean isSupport = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        return isSupport;
    }


}
