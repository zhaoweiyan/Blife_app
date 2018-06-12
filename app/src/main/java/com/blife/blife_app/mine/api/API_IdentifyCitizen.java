package com.blife.blife_app.mine.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/14.
 */
public class API_IdentifyCitizen implements InterfaceAPIData {
    String API = Constants.HTTP + "/citizen/identification";
    public static String TAG = "tag_post_citizen_identification";
    private String token;

    public API_IdentifyCitizen(String accessToken) {
        this.token = accessToken;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.postForm(TAG, API);
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

