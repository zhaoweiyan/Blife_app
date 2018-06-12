package com.blife.blife_app.tools.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.blife.blife_app.R;
import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.login.activity.ActivityPasswordLogin;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.activity.ActivityTask;
import com.blife.blife_app.utils.cache.SQLiteCacheManager;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.NetWorkUtil;
import com.blife.blife_app.utils.net.request.DownloadCallback;
import com.blife.blife_app.utils.net.request.HttpRequest;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.ShardPreferUtil;
import com.blife.blife_app.utils.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by w on 2016/8/25.
 */
public class DataManager {
    //网络层
    private UIResultCallback uiResultCallback;
    private HttpRequest httpRequest;
    private Map<String, String> param, head;
    //数据层
    private SQLiteCacheManager sqLiteCacheManager;
    private ShardPreferUtil shardPreferName;
    private ActivityTask activityTask;
    //默认缓存时间--24小时
    private long DEFAULT_CACHE_TIME = 24 * 60 * 60 * 1000;
    //请求接口集合
    private List<InterfaceAPIData> interfaceServiceDataList;
    //离线缓存key标识
    private String OFFLINE_CACHE_KEY = "api_offline_key";
    private int Flag = 0;

    private boolean stopRequest;

    public DataManager(UIResultCallback callback) {
        L.e("TAG", "BUG调试--DataManager--打开界面：" + callback.getClass().getName());
        if (uiResultCallback != null) {
            uiResultCallback = null;
        }
        this.uiResultCallback = callback;
        L.e("TAG", "BUG调试--DataManager--打开界面：" + uiResultCallback.getClass().getName());
        httpRequest = new HttpRequest(getUiResultCallback());
        param = httpRequest.getParams();
        head = httpRequest.getHeader();
        init();
    }

    public void download(final String url, final String saveFile, final DownloadCallback downloadCallback) {
        httpRequest.download(url, saveFile, downloadCallback);
    }

    private void init() {
        interfaceServiceDataList = new ArrayList<>();
        sqLiteCacheManager = SQLiteCacheManager.getInstance(BlifeApplication.AppContext);
        shardPreferName = ShardPreferUtil.getInstance(BlifeApplication.AppContext, Constants.BlifeName);
        activityTask = ActivityTask.newInstance();
        LogUtils.e("当前的activity dataManger****" + activityTask.getCurrentActivity());
    }

//    public void stopUiCallBack() {
//        LogUtils.e("uiResultCallback****1"+uiResultCallback);
//        if (uiResultCallback != null) {
//            uiResultCallback = null;
//        }
//        LogUtils.e("uiResultCallback****1"+uiResultCallback);
//    }

    /**
     * InterfaceAPIData.params和 Map<String, String> Params()区分开
     * 第一步：顺势流程：InterfaceAPIData.params的put方法将参数放到了Map<String, String> Params()的返回值
     * 第二步：当前一个接口没走完的时候，InterfaceAPIData.params.clear()，InterfaceAPIData.header.clear();清理了内部参数，但是没有清理Map<String, String> Params()，这个参数还可以使用
     * 第三步：当请求下一个接口的时候，Map<String, String> Params()重新获取InterfaceAPIData.params的值；
     */


    public void setStopRequest(boolean stopRequest) {
        this.stopRequest = stopRequest;
    }

    /**
     * 获取数据，策略
     *
     * @param interfaceAPIData
     */
    public void getServiceData(InterfaceAPIData interfaceAPIData) {
        if (stopRequest) return;
        L.e("TAG", "BUG调试--DataManager00：" + uiResultCallback.getClass().getName() + "-------" + interfaceAPIData.getTag());
        if (!NetWorkUtil.isNetwork(BlifeApplication.AppContext)) {//无网络
            String data = readCache(getCacheKey(interfaceAPIData));
            if (interfaceAPIData.saveCache() && !TextUtils.isEmpty(data)) {//接口保存有时效缓存
                L.e("TAG", "BUG调试--DataManager11：" + uiResultCallback.getClass().getName() + "-------" + interfaceAPIData.getTag());
                uiResultCallback.onSuccess(interfaceAPIData.getTag(), data);
            } else {
                String offlineData = readCache(getOfflineCacheKey(interfaceAPIData));
                L.e("TAG", "无网络离线缓存：" + offlineData);
                if (interfaceAPIData.offlineCache() && !TextUtils.isEmpty(offlineData)) {//接口需要保存离线缓存[离线缓存：无网络时，展示的数据，数据为有网络时，最后一次请求到的数据]
                    L.e("TAG", "BUG调试--DataManager22：" + uiResultCallback.getClass().getName() + "-------" + interfaceAPIData.getTag());
                    uiResultCallback.onSuccess(interfaceAPIData.getTag(), offlineData);
                } else {//接口不需要保存离线缓存
                    L.e("TAG", "BUG调试--DataManager33：" + uiResultCallback.getClass().getName() + "-------" + interfaceAPIData.getTag());
                    uiResultCallback.onError(interfaceAPIData.getTag(), Constants.ERROR_MESSAGE);
                }
            }
        } else {//有网络
            interfaceServiceDataList.add(interfaceAPIData);//添加到接口集合
            if (interfaceAPIData.saveCache()) {//接口需要保存有时效的缓存
                //获取时效缓存
                String data = readCache(getCacheKey(interfaceAPIData));
                if (TextUtils.isEmpty(data)) {//缓存为空，请求接口
                    L.e("TAG", "缓存为空走接口 params" + interfaceAPIData.Params());
                    L.e("TAG", "缓存为空走接口 header" + interfaceAPIData.Header());
//                    if (interfaceAPIData.offlineCache()) {
//                        L.e("TAG", "缓存为空走接口--先返回离线数据");
//                        String offlineData = readCache(getOfflineCacheKey(interfaceAPIData));
//                        uiResultCallback.onSuccess(interfaceAPIData.getTag(), offlineData);
//                    }
                    param.putAll(interfaceAPIData.Params());
                    head.putAll(interfaceAPIData.Header());
                    InterfaceAPIData.params.clear();
                    InterfaceAPIData.header.clear();
                    interfaceAPIData.HttpRequest(httpRequest);
                } else {//缓存不为空返回结果
                    L.e("TAG", "缓存不为空返回结果");
                    L.e("TAG", "BUG调试--DataManager44：" + uiResultCallback.getClass().getName() + "-------" + interfaceAPIData.getTag());
                    uiResultCallback.onSuccess(interfaceAPIData.getTag(), data);
                }
            } else {//接口不保存有时效的缓存
                L.e("TAG", "不保存缓存走接口");
                LogUtils.e("server****走接口" + interfaceAPIData.getTag());
//                if (interfaceAPIData.offlineCache()) {
//                    L.e("TAG", "不保存缓存走接口--先返回离线数据");
//                    String offlineData = readCache(getOfflineCacheKey(interfaceAPIData));
//                    uiResultCallback.onSuccess(interfaceAPIData.getTag(), offlineData);
//                }
                param.putAll(interfaceAPIData.Params());
                head.putAll(interfaceAPIData.Header());
                InterfaceAPIData.params.clear();
                InterfaceAPIData.header.clear();
                interfaceAPIData.HttpRequest(httpRequest);
            }
        }
    }

//    /**
//     * 获取数据，策略
//     *
//     * @param interfaceAPIData
//     */
//    public synchronized void getServiceData(Context context, InterfaceAPIData interfaceAPIData) {
//        if (context == null) {
//            return;
//        }
//        if (!NetWorkUtil.isNetwork(BlifeApplication.AppContext)) {//无网络
//            String data = readCache(getCacheKey(interfaceAPIData));
//            if (interfaceAPIData.saveCache() && !TextUtils.isEmpty(data)) {//接口保存有时效缓存
//                uiResultCallback.onSuccess(interfaceAPIData.getTag(), data);
//            } else {
//                String offlineData = readCache(getOfflineCacheKey(interfaceAPIData));
//                L.e("TAG", "无网络离线缓存：" + offlineData);
//                if (interfaceAPIData.offlineCache() && !TextUtils.isEmpty(offlineData)) {//接口需要保存离线缓存[离线缓存：无网络时，展示的数据，数据为有网络时，最后一次请求到的数据]
//                    uiResultCallback.onSuccess(interfaceAPIData.getTag(), offlineData);
//                } else {//接口不需要保存离线缓存
//                    uiResultCallback.onError(interfaceAPIData.getTag(), Constants.ERROR_MESSAGE);
//                }
//            }
//        } else {//有网络
//            interfaceServiceDataList.add(interfaceAPIData);//添加到接口集合
//            if (interfaceAPIData.saveCache()) {//接口需要保存有时效的缓存
//                //获取时效缓存
//                String data = readCache(getCacheKey(interfaceAPIData));
//                if (TextUtils.isEmpty(data)) {//缓存为空，请求接口
//                    L.e("TAG", "缓存为空走接口 params" + interfaceAPIData.Params());
//                    L.e("TAG", "缓存为空走接口 header" + interfaceAPIData.Header());
////                    if (interfaceAPIData.offlineCache()) {
////                        L.e("TAG", "缓存为空走接口--先返回离线数据");
////                        String offlineData = readCache(getOfflineCacheKey(interfaceAPIData));
////                        uiResultCallback.onSuccess(interfaceAPIData.getTag(), offlineData);
////                    }
//                    param.putAll(interfaceAPIData.Params());
//                    head.putAll(interfaceAPIData.Header());
//                    InterfaceAPIData.params.clear();
//                    InterfaceAPIData.header.clear();
//                    interfaceAPIData.HttpRequest(httpRequest);
//                } else {//缓存不为空返回结果
//                    L.e("TAG", "缓存不为空返回结果");
//                    uiResultCallback.onSuccess(interfaceAPIData.getTag(), data);
//                }
//            } else {//接口不保存有时效的缓存
//                L.e("TAG", "不保存缓存走接口");
//                LogUtils.e("server****走接口" + interfaceAPIData.getTag());
////                if (interfaceAPIData.offlineCache()) {
////                    L.e("TAG", "不保存缓存走接口--先返回离线数据");
////                    String offlineData = readCache(getOfflineCacheKey(interfaceAPIData));
////                    uiResultCallback.onSuccess(interfaceAPIData.getTag(), offlineData);
////                }
//                param.putAll(interfaceAPIData.Params());
//                head.putAll(interfaceAPIData.Header());
//                InterfaceAPIData.params.clear();
//                InterfaceAPIData.header.clear();
//                interfaceAPIData.HttpRequest(httpRequest);
//            }
//        }
//    }

    /**
     * 获取离线缓存的key
     *
     * @param interfaceAPIData
     * @return
     */
    private String getOfflineCacheKey(InterfaceAPIData interfaceAPIData) {
        L.e("TAG", "离线缓存KEY:**********" + getCacheKey(interfaceAPIData) + "*" + OFFLINE_CACHE_KEY);
        return getCacheKey(interfaceAPIData) + "*" + OFFLINE_CACHE_KEY;
    }

    /**
     * 获取缓存的key
     *
     * @return
     */
    private String getCacheKey(InterfaceAPIData interfaceAPIData) {
        String Url;
        if (!interfaceAPIData.Params().isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : interfaceAPIData.Params().entrySet()) {
                if (!entry.getKey().equals("lng") && !entry.getKey().equals("lat"))
                    stringBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
            String pam = stringBuilder.toString();
            Url = interfaceAPIData.getAPI() + "?" + pam.substring(0, pam.length() - 1);
            InterfaceAPIData.params.clear();
            InterfaceAPIData.header.clear();
        } else {
            Url = interfaceAPIData.getAPI();
            InterfaceAPIData.header.clear();
        }
        String key = shardPreferName.getStringData(Constants.CACHE_USER_ACCOUNT_KEY) + Url + interfaceAPIData.getTag();
        L.e("TAG", "最新缓存KEY:**********" + key);
        return shardPreferName.getStringData(Constants.CACHE_USER_ACCOUNT_KEY) + Url + interfaceAPIData.getTag();
    }

    /**
     * 结果回调
     *
     * @return
     */
    private UIResultCallback getUiResultCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (checkJsonStatus(json)) {
                    LogUtils.e("server****json状态成功" + tag);
                    L.e("TAG", "BUG调试--DataManager55：" + uiResultCallback.getClass().getName() + "-------" + tag);
                    uiResultCallback.onSuccess(tag, json);
                    detailCache(tag, json);
                } else {
                    LogUtils.e("server****json状态不成功" + tag);
                    if (!JsonObjUItils.getERRORJsonCode(json).equals(Constants.ACCESS_TOKEN_EXPIRED_CODE)) {
                        getOfflineCache(tag, json);
                    }
                    onDestroy();
                }
            }

            @Override
            public void onError(Object tag, String message) {
                LogUtils.e("server****回调失败");
                L.e("TAG", "DataManager结果回调：" + tag + "****" + message);
                if (!JsonObjUItils.getERRORJsonCode(message).equals(Constants.ACCESS_TOKEN_EXPIRED_CODE)) {
                    getOfflineCache(tag, message);
                }
                checkJsonStatus(message);
                onDestroy();
            }
        };
    }

    //当超时，获取离线缓存
    private void getOfflineCache(Object tag, String message) {
        for (InterfaceAPIData serviceData : interfaceServiceDataList) {
            if (serviceData.getTag().equals(tag)) {     //是否为当前请求
                if (serviceData.offlineCache()) {       //接口需要保存离线缓存
                    String offlineData = readCache(getOfflineCacheKey(serviceData));
                    LogUtils.e("offlineData****" + offlineData);
                    L.e("TAG", "BUG调试--DataManager66：" + uiResultCallback.getClass().getName() + "-------" + tag);
                    uiResultCallback.onSuccess(tag, offlineData);
                    LogUtils.e("server****超时 ，有离线缓存");
                } else {
                    L.e("TAG", "BUG调试--DataManager77：" + uiResultCallback.getClass().getName() + "-------" + tag);
                    uiResultCallback.onError(tag, message);
                    LogUtils.e("server****超时，无离线缓存");
                }
            }
        }
    }

    /**
     * 处理缓存
     *
     * @param tag
     * @param data
     */
    private void detailCache(Object tag, String data) {
        for (InterfaceAPIData serviceData : interfaceServiceDataList) {
            if (serviceData.getTag().equals(tag)) {//是否为当前请求
                //接口需要保存时效缓存
                if (serviceData.saveCache()) {
                    String cacheKey = getCacheKey(serviceData);//获取时效缓存key
                    saveCache(data, cacheKey, serviceData.saveCacheTime() > 0 ? serviceData.saveCacheTime() : DEFAULT_CACHE_TIME);
                    if (serviceData.offlineCache()) {//接口需要保存离线缓存
                        String offlineCacheKey = getOfflineCacheKey(serviceData);
                        saveCache(data, offlineCacheKey, SQLiteCacheManager.OFFLINE_CACHE_LOSE_TIME);
                    }
                } else {//接口不需要保存时效缓存
                    if (serviceData.offlineCache()) {//接口需要保存离线缓存
                        L.e("TAG", "server****保存离线缓存");
                        String offlineCacheKey = getOfflineCacheKey(serviceData);//获取接口离线缓存key
                        L.e("TAG", "保存离线缓存key" + offlineCacheKey);
                        L.e("TAG", "保存离线缓存Data" + data);
                        saveCache(data, offlineCacheKey, SQLiteCacheManager.OFFLINE_CACHE_LOSE_TIME);
                    }
                }
            }
        }
        onDestroy();
    }

    /**
     * 保存缓存
     *
     * @param data
     * @param cacheKey
     * @param time
     */
    private void saveCache(String data, String cacheKey, long time) {
        try {
            sqLiteCacheManager.insertNetCache(cacheKey, data, time, 1);
            LogUtils.e("cache url save network****" + cacheKey);
        } catch (DBCacheException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取缓存
     *
     * @param cacheKey 缓存key
     * @return 缓存数据
     */
    private String readCache(String cacheKey) {
        String data;
        try {
            data = sqLiteCacheManager.getNetCache(cacheKey);
            LogUtils.e("cache url network****" + cacheKey);
        } catch (DBCacheException e) {
            e.printStackTrace();
            data = "";
        }
        LogUtils.e("cache data network****" + data);
        return data;

    }

    /**
     * 清理缓存
     */
    public boolean clearCache(InterfaceAPIData interfaceAPIData) {
        String cacheKey = getCacheKey(interfaceAPIData);
        try {
            return sqLiteCacheManager.deleteNetCache(cacheKey);
        } catch (DBCacheException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 检查Json结果是否正确
     *
     * @param json
     * @return
     */
    protected Boolean checkJsonStatus(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("code")) {
                String code = jsonObject.getString("code");
                if (code.equals(Constants.API_SUCCESS_CODE)) {
                    return true;
                } else if (code.equals(Constants.ACCESS_TOKEN_EXPIRED_CODE)) {//ACCESS TOKEN 已过期
                    LogUtils.e("当前******code****" + Constants.ACCESS_TOKEN_EXPIRED_CODE);
                    TokenInvalid();
                    return false;
                } else {
                    String detail = jsonObject.getString("detail");
//                    ToastUtils.showLong(BlifeApplication.AppContext, detail);
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 处理Token过期
     */
    //{"code":"10000005","message":"ACCESSTOKEN_EXPIRED","detail":"ACCESS TOKEN 已过期"}
    private void TokenInvalid() {
        try {
            String token = sqLiteCacheManager.getNetCache(Constants.CACHE_ACCESS_TOKEN_KEY);
            if (!TextUtils.isEmpty(token)) {
                sqLiteCacheManager.deleteNetCache(Constants.CACHE_ACCESS_TOKEN_KEY);
            }
            Activity currentActivity = activityTask.getCurrentActivity();
            shardPreferName.setBooleanData(Constants.TOKEN_STATE_INVALID, true);
            activityTask.finishAllActivity();
            currentActivity.startActivity(new Intent(currentActivity, ActivityPasswordLogin.class));
        } catch (DBCacheException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除请求集合
     */
    private void onDestroy() {
        if (interfaceServiceDataList != null) {
            Flag++;
            if (Flag >= interfaceServiceDataList.size()) {
                interfaceServiceDataList.clear();
                Flag = 0;
            }
        }
    }
}
