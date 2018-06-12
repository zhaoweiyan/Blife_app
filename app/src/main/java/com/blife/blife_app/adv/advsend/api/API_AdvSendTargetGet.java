package com.blife.blife_app.adv.advsend.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/9/1.
 */
public class API_AdvSendTargetGet implements InterfaceAPIData {

    private String API = Constants.HTTP + "/advertisement/publishdetail";
    public static String TAG = "tag_get_advertisement_publishdetail";

    private String ACCESSTOKEN, ADV_ID;

    public API_AdvSendTargetGet(String ACCESSTOKEN, String ADV_ID) {
        this.ACCESSTOKEN = ACCESSTOKEN;
        this.ADV_ID = ADV_ID;
    }


    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", ACCESSTOKEN);
        header.put("IDENTITY", "PRODUCER");
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
