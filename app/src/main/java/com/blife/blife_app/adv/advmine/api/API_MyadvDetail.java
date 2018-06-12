package com.blife.blife_app.adv.advmine.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.cache.SQLiteCacheManager;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by Somebody on 2016/8/29.
 */
public class API_MyadvDetail implements InterfaceAPIData {

    private boolean isCache = false;
    private String adv_id;
    String API = Constants.HTTP + "/advertisement/detail";
    public static String TAG = "tag_get_advertisement_detail";
    private String token;

    public API_MyadvDetail(String accessToken, String adv_id, boolean isCache) {
        this.token = accessToken;
        this.adv_id = adv_id;
        this.isCache = isCache;
    }

    public API_MyadvDetail(String accessToken, String adv_id) {
        this.token = accessToken;
        this.adv_id = adv_id;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", token);
        header.put("IDENTITY", "PRODUCER");
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("adv_id", adv_id);
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
        if (isCache) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public long saveCacheTime() {
        if (isCache) {
            return SQLiteCacheManager.FOREVER_CACHE_LOSE_TIME;
        } else {
            return 0;
        }
    }

    @Override
    public boolean offlineCache() {
        return true;
    }
}
