package com.blife.blife_app.adv.advmine.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by Somebody on 2016/8/29.
 */
public class API_MyadvPerforming implements InterfaceAPIData {

    private int limit;
    private int offset;
    String API = Constants.HTTP + "/advertisement/list";
    public static String TAG = "tag_get_performing_advertisement_list";
    private String token;

    public API_MyadvPerforming(String accessToken, int offset, int limit) {
        this.token = accessToken;
        this.offset = offset;
        this.limit = limit;
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
        params.put("offset", offset + "");
        params.put("limit", limit + "");
        params.put("status", "performing");
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
