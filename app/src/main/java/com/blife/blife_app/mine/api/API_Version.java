package com.blife.blife_app.mine.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;
import com.blife.blife_app.utils.util.LogUtils;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/20.
 */
public class API_Version implements InterfaceAPIData {

    private String API = Constants.HTTP + "/platform/upgrade";
    public static String TAG = "get_platform_upgrade";

    private String token;
    private String appVerName;

    public API_Version(String access_token, String appVerName) {
        this.token = access_token;
        this.appVerName = appVerName;
    }
    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        LogUtils.e("fragment apiversion****" + API);
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put(Constants.TOKEN, token);
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("platform", "ANDROID");
        params.put("version", appVerName);
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
        return false;
    }
}
