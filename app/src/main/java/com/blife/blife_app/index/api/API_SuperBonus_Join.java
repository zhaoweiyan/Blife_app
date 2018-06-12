package com.blife.blife_app.index.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/9/23.
 */
public class API_SuperBonus_Join implements InterfaceAPIData {

    private String API = Constants.HTTP + "/superbonus/detail";
    public static String TAG = "tag_post_superbonus_detail";

    private String ACCESSTOEN, event_id, keyword;

    public API_SuperBonus_Join(String ACCESSTOEN, String event_id, String keyword) {
        this.ACCESSTOEN = ACCESSTOEN;
        this.event_id = event_id;
        this.keyword = keyword;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.postForm(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", ACCESSTOEN);
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("event_id", event_id);
        params.put("action", "JOIN");
        params.put("keyword", keyword);
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
