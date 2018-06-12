package com.blife.blife_app.mine.api;


import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/14.
 */
public class API_MeInfoCommit implements InterfaceAPIData {

    private String API = Constants.HTTP + "/user/info";
    public static String TAG = "tag_put_user_info";

    private String ACCESSTOKEN, Json;

    public API_MeInfoCommit(String ACCESSTOKEN, String json) {
        Json = json;
        this.ACCESSTOKEN = ACCESSTOKEN;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.putPayload(TAG, API, Json);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", ACCESSTOKEN);
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

