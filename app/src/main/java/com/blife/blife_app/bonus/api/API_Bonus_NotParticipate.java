package com.blife.blife_app.bonus.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/9/13.
 */
public class API_Bonus_NotParticipate implements InterfaceAPIData {

    private String API = Constants.HTTP + "/advertisement/list";
    public static String TAG = "tag_get_advertisement_list";

    private String ACCESSTOKEN;
    private String lng;
    private String lat;
    private int offset;
    private int limit;

    public API_Bonus_NotParticipate(String ACCESSTOKEN) {
        this.ACCESSTOKEN = ACCESSTOKEN;
    }

    public void setLocation(String lat, String lng) {
        this.lng = lng;
        this.lat = lat;
    }

    public void setOffsetLimit(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
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
        params.put("lng", lng);
        params.put("lat", lat);
        params.put("offset", offset + "");
        params.put("limit", limit + "");
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
