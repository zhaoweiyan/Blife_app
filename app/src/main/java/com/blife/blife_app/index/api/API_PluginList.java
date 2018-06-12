package com.blife.blife_app.index.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/8/24.
 */
public class API_PluginList implements InterfaceAPIData {

    public static String API = Constants.HTTP + "/plugin/list";
    public static String TAG = "tag_get_plugin_list";
    private String token;

    public API_PluginList(String accesstoken) {
        this.token = accesstoken;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.get(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", token);
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
        return true;
    }

    @Override
    public long saveCacheTime() {
        return Constants.COMMONTIME_THREE_DAY;
    }

    @Override
    public boolean offlineCache() {
        return true;
    }


}
