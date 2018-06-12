package com.blife.blife_app.index.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/9/23.
 */
public class API_SuperBonusDetail implements InterfaceAPIData {

    private String API = Constants.HTTP + "/superbonus/detail";
    public static String TAG = "tag_get_superbonus_detail";

    private String ACCESSTOEN, event_id;

    public API_SuperBonusDetail(String ACCESSTOEN, String event_id) {
        this.event_id = event_id;
        this.ACCESSTOEN = ACCESSTOEN;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", ACCESSTOEN);
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("event_id", event_id);
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
        return true;
    }
}
