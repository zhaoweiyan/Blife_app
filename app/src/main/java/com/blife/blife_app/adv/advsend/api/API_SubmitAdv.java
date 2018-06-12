package com.blife.blife_app.adv.advsend.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/8/30.
 */
public class API_SubmitAdv implements InterfaceAPIData {

    private String API = Constants.HTTP + "/advertisement/status";
    public static String TAG = "tag_post_advertisement_status";

    private String ACCESSTOKEN, adv_id;
    private String lat, lng;

    public API_SubmitAdv(String ACCESSTOKEN, String adv_id, String lat, String lng) {
        this.ACCESSTOKEN = ACCESSTOKEN;
        this.adv_id = adv_id;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.postForm(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", ACCESSTOKEN);
        header.put("IDENTITY", "PRODUCER");
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("adv_id", adv_id);
        params.put("status", "submit");
        params.put("lat", lat );
        params.put("lng", lng );
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
