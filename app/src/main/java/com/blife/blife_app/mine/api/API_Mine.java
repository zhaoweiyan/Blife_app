package com.blife.blife_app.mine.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.cache.SQLiteCacheManager;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by Somebody on 2016/8/29.
 */
public class API_Mine implements InterfaceAPIData {
    private boolean pass;
    String API = Constants.HTTP + "/user/info";
    public static String TAG = "tag_get_user_info";
    private String token;

    public API_Mine(String accessToken, boolean pass) {
        this.token = accessToken;
        this.pass = pass;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put(Constants.TOKEN, token);
        return header;
    }

    @Override
    public Map<String, String> Params() {
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
        if (pass == true) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public long saveCacheTime() {
        if (pass == true) {
//            return SQLiteCacheManager.FOREVER_CACHE_LOSE_TIME;
            return Constants.COMMONTIME_ONE_DAY;
        } else {
            return 0;
        }
    }

    @Override
    public boolean offlineCache() {
        return true;
    }


}
