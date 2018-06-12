package com.blife.blife_app.adv.advsend.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/10/10.
 */
public class API_AdvGetBonus implements InterfaceAPIData {
    private String token;
    private String adv_id;
    private String API = Constants.HTTP + "/advertisement/bonus";
    public static String TAG = "tag_get_advertisement_bonus";

    public API_AdvGetBonus(String access_token, String adv_id) {
        this.token = access_token;
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
