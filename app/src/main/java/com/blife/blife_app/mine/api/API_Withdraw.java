package com.blife.blife_app.mine.api;

import com.blife.blife_app.tools.data.InterfaceAPIData;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.HttpRequest;
import com.blife.blife_app.utils.util.LogUtils;

import java.util.Map;

/**
 * Created by Somebody on 2016/9/18.
 */
public class API_Withdraw implements InterfaceAPIData {

    private int paytype;
    private String account;
    private String name;
    private String token, balance;
    private String API = Constants.HTTP + "/finance/transfer";
    public static String TAG = "tag_post_finance_transfer";

    public API_Withdraw(String access_token, int paytype, String account, String name, String balance) {
        this.token = access_token;
        this.paytype = paytype;
        this.account = account;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public void HttpRequest(HttpRequest httpRequest) {
        httpRequest.postForm(TAG, API);
    }

    @Override
    public Map<String, String> Header() {
        header.put(Constants.TOKEN, token);
        return header;
    }

    @Override
    public Map<String, String> Params() {
        params.put("pay_type", paytype + "");
        params.put("action", "REQUEST_WITH_DRAW");
        params.put("ext_id", account);
        params.put("ext_name", name);
        LogUtils.e("amount****" + balance);
        LogUtils.e("amount****" + Double.parseDouble(balance));
        LogUtils.e("amount****" + Double.parseDouble(balance) * 100);
        params.put("amount", Double.parseDouble(balance) * 100 + "");
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
