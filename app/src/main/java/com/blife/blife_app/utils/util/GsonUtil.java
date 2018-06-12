package com.blife.blife_app.utils.util;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhao on 2016/5/30.
 */
public class GsonUtil {

    private static GsonUtil gsonutils;
    private Gson gson;

    private GsonUtil() {
        this.gson = new Gson();
    }

    public static synchronized GsonUtil getInstance() {
        if (gsonutils == null) {
            gsonutils = new GsonUtil();
        }
        return gsonutils;
    }

    //json转换bean
    public Object FromJson(String json, Class<?> cls) {
        return gson.fromJson(json, cls);
    }

    public String toJson(Class<?> cls) {
        return gson.toJson(cls);
    }

    //某个字段itemHead下辖的json转换bean
    public Object FromJson(String json, String itemHead, Class<?> cls) {
        String data = getData(json, itemHead);
        return gson.fromJson(data, cls);
    }

    public String getData(String json, String itemHead) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(itemHead)) {
                return jsonObject.getString(itemHead);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

}
