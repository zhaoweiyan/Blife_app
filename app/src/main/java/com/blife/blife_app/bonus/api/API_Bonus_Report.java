package com.blife.blife_app.bonus.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/9/19.
 */
public class API_Bonus_Report implements InterfaceAPIData {

    private String API = Constants.HTTP + "/advertisement/accusation";
    public static String TAG = "tag_post_advertisement_accusation";

    private String ACCESSTOKEN;
    private String ADV_ID;
    private String reason;

    public API_Bonus_Report(String ACCESSTOKEN, String ADV_ID, String reason) {
        this.ACCESSTOKEN = ACCESSTOKEN;
        this.ADV_ID = ADV_ID;
        this.reason = reason;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.postForm(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", ACCESSTOKEN);
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("adv_id", ADV_ID);
        params.put("reason", reason);
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
