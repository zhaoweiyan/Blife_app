package com.blife.blife_app.tools.data;

import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by w on 2016/9/2.
 */
public interface InterfaceAPIData {

    Map<String, String> params = new HashMap<>();
    Map<String, String> header = new HashMap<>();

    void HttpRequest(HttpRequest httpRequest);

    Map<String, String> Header();

    Map<String, String> Params();

    String getAPI();

    String getTag();

    boolean saveCache();

    long saveCacheTime();

    boolean offlineCache();

}
