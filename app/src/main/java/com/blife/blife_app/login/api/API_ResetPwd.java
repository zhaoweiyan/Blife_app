package com.blife.blife_app.login.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/12.
 */
public class API_ResetPwd implements InterfaceAPIData {

    private String token, account, code, pwd;
    private String API = Constants.HTTP + "/user/password";
    public static String TAG = "tag_get_user_password";

    public API_ResetPwd(String access_token, String phone, String code, String pwd) {
        this.token = access_token;
        this.account = phone;
        this.code = code;
        this.pwd = pwd;
    }


    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.postForm(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("telphone", account);
        params.put("verifycode", code);
        params.put("password", pwd);
        return params;
    }

    @Override
    public String getAPI() {
        return null;
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
