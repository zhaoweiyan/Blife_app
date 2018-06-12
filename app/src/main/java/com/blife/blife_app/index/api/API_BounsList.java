package com.blife.blife_app.index.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/8/24.
 */
public class API_BounsList implements InterfaceAPIData {

    public static String API = Constants.HTTP + "/advertisement/list";
    public static String TAG = "TAG_bounsList";
    private String lng;
    private String lat;
    private String token;

    private int offset = 0, limit = 3;


    public API_BounsList(String accesstoken, String lat, String lng) {
        this.token = accesstoken;
        this.lat = lat;
        this.lng = lng;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.get(TAG, API);
//        params.clear();
//        header.clear();
//        L.e("TAG", "传参数params长度：" + params.size());
//        L.e("TAG", "传参数header长度：" + header.size());
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", token);
        header.put("IDENTITY", "CONSUMER");
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("offset", "" + offset);
        params.put("limit", "" + limit);
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
