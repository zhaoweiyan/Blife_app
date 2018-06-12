package com.blife.blife_app.login.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/12.
 */
public class API_LoginCodeCommit implements InterfaceAPIData {


    private String LOGIN_LNG;
    private String LOGIN_LAT;
    private String token, account, code;
    private String API = Constants.HTTP + "/user/authentication";
    public static String TAG = "tag_get_user_authentication";

    public API_LoginCodeCommit(String access_token, String account, String code, String LOGIN_LAT, String LOGIN_LNG) {
        this.token = access_token;
        this.account = account;
        this.code = code;
        this.LOGIN_LAT = LOGIN_LAT;
        this.LOGIN_LNG = LOGIN_LNG;
    }


    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("telphone", account);
        params.put("verifycode", code);
        params.put("lng", LOGIN_LNG);
        params.put("lat", LOGIN_LAT);
        return params;
    }

    @Override
    public String getAPI() {
        return API;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public boolean saveCache() {
        return false;
    }

    @Override
    public long saveCacheTime() {
        return Constants.COMMONTIME;
    }

    @Override
    public boolean offlineCache() {
        return false;
    }
}
