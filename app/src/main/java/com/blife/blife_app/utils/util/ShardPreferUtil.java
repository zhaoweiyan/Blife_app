package com.blife.blife_app.utils.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.blife.blife_app.tools.JsonObjUItils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoxx on 2016/1/13.
 */
public class ShardPreferUtil {
    private static ShardPreferUtil shardPreferUtil;
    private SharedPreferences sharedPreferences;
    private Editor editor;

    private ShardPreferUtil(Context context, String spName) {
        sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized ShardPreferUtil getInstance(Context context, String spName) {
        if (shardPreferUtil == null) {
            shardPreferUtil = new ShardPreferUtil(context, spName);
        }
        return shardPreferUtil;
    }

    public void setStringData(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringData(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public String getStringData(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void setBooleanData(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBooleanData(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void setLongData(String key, Long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public Long getLongData(String key, Long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public void setIntData(String key, Integer value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getIntData(String key, Integer defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void setFloatData(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloatData(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public void setStringList(String key, List<String> list) {
        if (list == null) return;
        editor.putBoolean(key + "_B", true);
        editor.putInt(key + "_I", list.size());
        for (int i = 0; i < list.size(); i++) {
            editor.putString(key + i, list.get(i));
        }
        editor.commit();
    }

    public List<String> getStringList(String key) {
        List<String> list = new ArrayList<>();
        if (sharedPreferences.getBoolean(key + "_B", false)) {
            int size = sharedPreferences.getInt(key + "_I", 0);
            for (int i = 0; i < size; i++) {
                String values = sharedPreferences.getString(key + i, "");
                list.add(values);
            }
        }
        return list;
    }

}
