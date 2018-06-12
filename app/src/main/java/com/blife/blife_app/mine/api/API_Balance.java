package com.blife.blife_app.mine.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;
import com.blife.blife_app.utils.util.LogUtils;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/13.
 */
public class API_Balance implements InterfaceAPIData {

    String API = Constants.HTTP + "/user/balance";
    public static String TAG = "tag_get_user_balance";
    private String token;

    public API_Balance(String access_token) {
        this.token = access_token;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        LogUtils.e("fragment apibalance****" + API);
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put(Constants.TOKEN, token);
        return header;
    }

    @Override
    public Map<String, String> Params() {
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
