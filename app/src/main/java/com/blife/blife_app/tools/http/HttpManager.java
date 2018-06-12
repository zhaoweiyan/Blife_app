package com.blife.blife_app.tools.http;


import com.blife.blife_app.utils.net.request.HttpRequest;
import com.blife.blife_app.utils.net.request.ResultCallback;
import com.blife.blife_app.utils.util.NumberUtils;

import java.util.Map;

/**
 * Created by w on 2016/8/15.
 */
public class HttpManager {

    public Map<String, String> param, head;
    public HttpRequest httpRequest;

    public HttpManager(ResultCallback callback) {
        httpRequest = new HttpRequest(callback);
        param = httpRequest.getParams();
        head = httpRequest.getHeader();
    }


    public void getServiceConfig() {
        String nonce = NumberUtils.getRandom(6);
        long timeMillis = System.currentTimeMillis() / 1000;
        String time = timeMillis + "";
        param.put("app_id", Constants.APP_ID);
        param.put("nonce", nonce);
        param.put("time", time);
        param.put("sign", HttpUtil.sign(nonce, time));
        httpRequest.get(Constants.TAG_SERVICE_COMMON, Constants.HTTP_SERVICE_COMMON);
    }


    public void getLoginCode(String phone, String type) {
        param.put("telphone", phone);
        param.put("type", type);
        httpRequest.get(Constants.TAG_LOGINCODE, Constants.HTTP_LOGINCODE);
    }

    public void getRegistCommit(String phone, String code, String pwd, String lng, String lat) {
        param.put("telphone", phone);
        param.put("verifycode", code);
        param.put("set_password", pwd);
        param.put("lng", lng);
        param.put("lat", lat);
//        param.put("iloveblife", "1");
        httpRequest.get(Constants.TAG_REGITS_COMMIT, Constants.HTTP_REGITS_COMMIT);
    }

    public void codeLogin(String account, String code) {
        param.put("telphone", account);
        param.put("verifycode", code);
        param.put("lng", "1");
        param.put("lat", "1");
        httpRequest.get(Constants.TAG_LOGIN, Constants.HTTP_LOGIN);
    }
    public void PwdLogin(String account, String pwd) {
        param.put("telphone", account);
        param.put("password", pwd);
        param.put("lng", "1");
        param.put("lat", "1");
        httpRequest.get(Constants.TAG_LOGIN, Constants.HTTP_LOGIN);
    }
    public void getRsetPwd(String phone, String code, String pwd) {
        param.put("telphone", phone);
        param.put("verifycode", code);
        param.put("password", pwd);
//        param.put("iloveblife", "1");
        httpRequest.postForm(Constants.TAG_RESET_COMMIT, Constants.HTTP__RESET_COMMIT);
    }
}
