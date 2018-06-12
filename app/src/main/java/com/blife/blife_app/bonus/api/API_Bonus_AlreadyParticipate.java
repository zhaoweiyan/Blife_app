package com.blife.blife_app.bonus.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/9/13.
 */
public class API_Bonus_AlreadyParticipate implements InterfaceAPIData {

    private String API = Constants.HTTP + "/bonus/list";
    public static String TAG = "tag_get_bonus_list";

    private String ACCESSTOKEN;

    public API_Bonus_AlreadyParticipate(String ACCESSTOKEN) {
        this.ACCESSTOKEN = ACCESSTOKEN;
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
