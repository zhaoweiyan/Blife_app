package com.blife.blife_app.adv.advsend.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by w on 2016/9/5.
 */
public class API_Payment implements InterfaceAPIData {

    private String API = Constants.HTTP + "/payment/order";
    public static String TAG = "tag_post_payment_order";

    private String ACCESSTOKEN;
    private String adv_id;
    private int pay_type;

    public API_Payment(String ACCESSTOKEN, String adv_id, int pay_type) {
        this.ACCESSTOKEN = ACCESSTOKEN;
        this.adv_id = adv_id;
        this.pay_type = pay_type;
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
        params.put("pay_type", "" + pay_type);
        params.put("adv_id", adv_id);
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
