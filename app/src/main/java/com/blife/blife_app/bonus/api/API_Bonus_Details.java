package com.blife.blife_app.bonus.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.cache.SQLiteCacheManager;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/9/14.
 */
public class API_Bonus_Details implements InterfaceAPIData {

    private String API = Constants.HTTP + "/advertisement/detail";
    public static String TAG = "tag_get_advertisement_detail";

    private String ACCESSTOKEN;
    private String ADV_ID;

    public API_Bonus_Details(String ACCESSTOKEN, String ADV_ID) {
        this.ACCESSTOKEN = ACCESSTOKEN;
        this.ADV_ID = ADV_ID;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", ACCESSTOKEN);
        header.put("IDENTITY", "CONSUMER");
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("adv_id", ADV_ID);
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
        return true;
    }

    @Override
    public long saveCacheTime() {
        return SQLiteCacheManager.FOREVER_CACHE_LOSE_TIME;
    }

    @Override
    public boolean offlineCache() {
        return false;
    }
}
