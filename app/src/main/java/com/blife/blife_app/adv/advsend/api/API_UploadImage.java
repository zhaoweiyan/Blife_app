package com.blife.blife_app.adv.advsend.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.io.File;
import java.util.Map;

/**
 * Created by w on 2016/8/29.
 */
public class API_UploadImage implements InterfaceAPIData {

    private String API = Constants.HTTP + "/file/image";
    public static String TAG = "tag_put_file_image";
    private String accessToken;
    private File file;


    public void setParams(String accessToken, File file) {
        this.accessToken = accessToken;
        this.file = file;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.putFile(TAG, API, file);
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", accessToken);
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("type", "adv_img");
        String name = file.getName();
        String ext = name.substring(name.lastIndexOf(".") + 1);
        params.put("ext", ext);
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
