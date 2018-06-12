package com.blife.blife_app.bonus.api;

import com.baidu.platform.comapi.map.A;
import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.cache.SQLiteCacheManager;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/9/18.
 */
public class API_BonusRankingList implements InterfaceAPIData {

    private String API = Constants.HTTP + "/bonus/rank";
    public static String TAG = "tag_get_bonus_rank";
    private String ACCESSTOKEN;
    private String ADV_ID;
    private boolean activityEnd;

    public API_BonusRankingList(String ACCESSTOKEN, String ADV_ID, boolean activityEnd) {
        this.ACCESSTOKEN = ACCESSTOKEN;
        this.ADV_ID = ADV_ID;
        this.activityEnd = activityEnd;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", ACCESSTOKEN);
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
        if (activityEnd) {
            return true;
        }
        return false;
    }

    @Override
    public long saveCacheTime() {
        if (activityEnd) {
            return SQLiteCacheManager.FOREVER_CACHE_LOSE_TIME;
        }
        return 0;
    }

    @Override
    public boolean offlineCache() {
        if (!activityEnd) {
            return true;
        }
        return false;
    }
}
