package com.blife.blife_app.mine.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/19.
 */
public class API_IncomeExpense implements InterfaceAPIData {

    private String API = Constants.HTTP + "/user/transactionrecords";
    public static String TAG = "get_user_transaction_records";
    private String token;
    private int offset;
    private int limit;

    public API_IncomeExpense(String access_token, int offset, int limit) {
        this.token = access_token;
        this.offset = offset;
        this.limit = limit;
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
        params.put("offset", offset + "");
        params.put("limit", limit + "");
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
