package com.blife.blife_app.index.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/10/18.
 */
public class API_SuperBonusReward implements InterfaceAPIData {

    private String API = Constants.HTTP + "/superbonus/reward";
    public static String TAG = "tag_get_superbonus_reward";

    private String ACCESSTOKEN;
    private String EVENTID;

    public API_SuperBonusReward(String ACCESSTOKEN, String EVENTID) {
        this.ACCESSTOKEN = ACCESSTOKEN;
        this.EVENTID = EVENTID;
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
        params.put("event_id", EVENTID);
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
