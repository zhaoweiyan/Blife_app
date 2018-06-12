package com.blife.blife_app.tools.wx;

import android.content.Context;
import android.content.Intent;

import com.blife.blife_app.tools.http.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Random;

/**
 * Created by Somebody on 2016/8/30.
 */
public class WXStart {

    private static WXStart wxStart;
    private Context context;
    private IWXAPI api;

    private WXStart(Context context) {
        this.context = context;
        api = WXAPIFactory.createWXAPI(context, Constants.WEIXIN_APPID, true);
    }


    public synchronized static WXStart getInstance(Context context) {
        if (wxStart == null) {
            wxStart = new WXStart(context);
        }
        return wxStart;
    }

    public void startWx() {
        api.registerApp(Constants.WEIXIN_APPID);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        Random random = new Random();
        req.state = "wechat_sdk_blife" + random.nextInt(1000);
        api.sendReq(req);
    }

    public void stopWx() {
        api.unregisterApp();
    }

}
