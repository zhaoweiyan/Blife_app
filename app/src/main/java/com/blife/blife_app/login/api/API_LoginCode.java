package com.blife.blife_app.login.api;

import com.baidu.platform.comapi.map.C;
import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/12.
 */
public class API_LoginCode implements InterfaceAPIData {

    private String token, account, type;
    private String API = Constants.HTTP + "/user/identifyingcode";
    public static String TAG = "tag_get_user_identifyingcode";

    public API_LoginCode(String access_token, String account, String type) {
        this.token = access_token;
        this.account = account;
        this.type = type;
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
        params.put("type", type);
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
