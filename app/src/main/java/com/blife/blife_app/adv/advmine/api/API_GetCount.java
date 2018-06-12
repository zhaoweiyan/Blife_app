package com.blife.blife_app.adv.advmine.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/8.
 */
public class API_GetCount implements InterfaceAPIData {

    private String token;
    private String adv_id;
    private String API = Constants.HTTP + "/bonus/statistics";
    public static String TAG = "tag_get_bonus_statistics";

    public API_GetCount(String access_token, String adv_id) {
        this.token = access_token;
        this.adv_id = adv_id;
    }


    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put(Constants.TOKEN, token);
        header.put(Constants.IDENTITY, "PRODUCER");
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
        return Constants.COMMONTIME;
    }

    @Override
    public boolean offlineCache() {
        return true;
    }

}
