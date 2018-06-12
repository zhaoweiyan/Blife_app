package com.blife.blife_app.tools.location;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/8/26.
 */
public class API_UploadLocation implements InterfaceAPIData {

    private String API = Constants.HTTP + "/user/position";
    private String TAG = "tag_post_user_position";

    private UploadLocationParams uploadLocationParams;

    public void setParams(UploadLocationParams params) {
        this.uploadLocationParams = params;
    }


    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        if (uploadLocationParams != null) {
            String json = "{\"lat\": " + uploadLocationParams.getLat() + ", \"lng\": " + uploadLocationParams.getLng() + "}";
            httpRequest.postPayload(TAG, API, json,true);//上传位置为了不回调，单独拿出来
        }
    }

    @Override
    public Map<String, String> Header() {
        header.put("ACCESS-TOKEN", uploadLocationParams.getACCESS_TOKEN());
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
