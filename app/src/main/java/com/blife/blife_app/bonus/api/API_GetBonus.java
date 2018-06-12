package com.blife.blife_app.bonus.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/9/14.
 */
public class API_GetBonus implements InterfaceAPIData {

    private String API = Constants.HTTP + "/advertisement/bonus";
    public static String TAG = "tag_get_advertisement_bonus";

    private String ACCESSTOKEN;
    private String ADV_ID, PUB_ID;

    public API_GetBonus(String ACCESSTOKEN, String ADV_ID, String PUB_ID) {
        this.ADV_ID = ADV_ID;
        this.ACCESSTOKEN = ACCESSTOKEN;
        this.PUB_ID = PUB_ID;
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
        params.put("pub_id", PUB_ID);
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
