
package com.blife.blife_app.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.blife.blife_app.R;
import com.blife.blife_app.mine.activity.ActivityWxPay;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.util.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class WXEntryActivity extends WXCallbackActivity {
    //    private IWXAPI api;
//
//    //获取openid一直不成功，回调两次，都是因为extends了分享的WXCallbackActivity，导致WXCallbackActivity和IWXAPIEventHandler都是回调的，总共就毁掉了两次
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wxresult);
//        api = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APPID);
//        api.handleIntent(getIntent(), this);
//    }
//
////    @Override
////    protected void onNewIntent(Intent intent) {
////        super.onNewIntent(intent);
////        LogcatManager.i(WXEntryActivity.this, "WX测试", "intenttttttttttttttttttttttttt", true);
////        setIntent(intent);
////        api.handleIntent(intent, this);
////    }
//
    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == 2) {
            super.onResp(resp);
        } else {
            if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                requestUrl(((SendAuth.Resp) resp).code);
            }
        }
    }

    private void requestUrl(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("appid", Constants.WEIXIN_APPID);
        params.addQueryStringParameter("secret", Constants.WEIXIN_APPSRCERT);
        params.addQueryStringParameter("code", code);
        params.addQueryStringParameter("grant_type", "authorization_code");
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> info) {
                try {
                    JSONObject jsonObject = new JSONObject(info.result);
                    if (jsonObject.has("openid")) {
                        Intent intent = new Intent();
                        Bundle bundlewx = new Bundle();
                        bundlewx.putString(Constants.OPENID, jsonObject.getString("openid"));
                        intent.putExtras(bundlewx);
                        intent.setClass(WXEntryActivity.this, ActivityWxPay.class);
                        startActivity(intent);
                        finish();
                    } else {
//                        ToastUtils.showShort(WXEntryActivity.this, "微信授权过于频繁，请稍后再试");
                        finish();
                    }
                } catch (JSONException e) {
                    finish();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {
                ToastUtils.showShort(WXEntryActivity.this, "获取微信授权失败");
                finish();
            }
        });
    }

}
