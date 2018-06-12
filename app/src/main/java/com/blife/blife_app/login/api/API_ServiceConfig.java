package com.blife.blife_app.login.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.http.HttpUtil;
import com.blife.blife_app.utils.net.request.HttpRequest;
import com.blife.blife_app.utils.util.NumberUtils;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/12.
 */
public class API_ServiceConfig implements InterfaceAPIData {

    private String token;
    private String API = Constants.HTTP + "/server/common";
    public static String TAG = "tag_get_servser_common";

    public API_ServiceConfig(String access_token) {
        this.token = access_token;
    }

    public API_ServiceConfig() {
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        return header;
    }

    @Override
    public Map<String, String> Params() {
        String nonce = NumberUtils.getRandom(6);
        long timeMillis = System.currentTimeMillis() / 1000;
        String time = timeMillis + "";
        params.put("app_id", Constants.APP_ID);
        params.put("nonce", nonce);
        params.put("time", time);
        params.put("sign", HttpUtil.sign(nonce, time));
        return params;
    }

    @Override
    public String getAPI() {
        return null;
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