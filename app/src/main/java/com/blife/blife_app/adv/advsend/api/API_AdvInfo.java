package com.blife.blife_app.adv.advsend.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/8/30.
 */
public class API_AdvInfo implements InterfaceAPIData {

    private String API = Constants.HTTP + "/advertisement/detail";
    public static String TAG = "tag_post_advertisement_detail";


    private String Accesstoken, json;

    public void setAccesstoken(String accesstoken, String json) {
        Accesstoken = accesstoken;

        this.json = json;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.postPayload(TAG, API, json);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", Accesstoken);
        header.put("IDENTITY", "PRODUCER");
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
        return 0;
    }

    @Override
    public boolean offlineCache() {
        return false;
    }
}
