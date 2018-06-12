package com.blife.blife_app.login.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/12.
 */
public class API_LoginPwd implements InterfaceAPIData {


    private String account, pwd;
    private String API = Constants.HTTP + "/user/authentication";
    public static String TAG = "tag_get_user_authentication";
    private String lat, lng;

    public API_LoginPwd(String account, String pwd, String lat, String lng) {
        this.account = account;
        this.pwd = pwd;
        this.lat = lat;
        this.lng = lng;
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
        params.put("password", pwd);
        params.put("lat", lat);
        params.put("lng", lng);
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
        return 0;
    }

    @Override
    public boolean offlineCache() {
        return false;
    }
}
