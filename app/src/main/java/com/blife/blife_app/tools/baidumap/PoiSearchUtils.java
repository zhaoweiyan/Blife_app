package com.blife.blife_app.tools.baidumap;

import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/8/31.
 */
public class PoiSearchUtils implements OnGetPoiSearchResultListener {

    private static PoiSearchUtils INSTANCE;
    private PoiSearch poiSearch;
    private String City = "";
    private String Address = "";

    private int num;
    private boolean poiNoResult;
    private int suggest = 1;
    private List<PoiInfo> poiList;
    private List<CityInfo> cityInfoList;

    private InterfacePoiResultCallback interfacePoiResultCallback;

    private boolean isSearching;
    private boolean STOP;

    private PoiSearchUtils() {
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
        poiList = new ArrayList<>();
        cityInfoList = new ArrayList<>();
    }

    public static PoiSearchUtils getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new PoiSearchUtils();
//        }
        return new PoiSearchUtils();
    }

    public void search(String address, InterfacePoiResultCallback interfacePoiResultCallback) {
        this.interfacePoiResultCallback = interfacePoiResultCallback;
        if (TextUtils.isEmpty(address)) {
            interfacePoiResultCallback.onNoResult();
            STOP = true;
            return;
        } else {
            STOP = false;
        }
        if (TextUtils.isEmpty(Address)) {
            Log.e("TAG_POI", "第一次搜索");
            Address = address;
            poi();
        } else {
            if (isSearching) {
                Log.e("TAG_POI", "停止搜索-----------------------------------------------------------------------");
                stopSearch(address);
            } else {
                Log.e("TAG_POI", "继续搜索*************************************************************************");
                continueSearch(address);
            }
        }
    }

    private void stopSearch(String address) {
        STOP = true;
        Address = address;
        poiList.clear();
        cityInfoList.clear();
        City = "";
        STOP = false;
        isSearching = false;
        poi();
    }

    private void continueSearch(String address) {
        poiList.clear();
        cityInfoList.clear();
        City = "";
        Address = address;
        poi();
    }

    private void poi() {
        Log.e("TAG_POI", "开始搜索");
        if (STOP)
            return;
        poiNoResult = false;
        PoiCitySearchOption option = new PoiCitySearchOption();
        option.city(City);
        option.keyword(Address);
        option.pageCapacity(15);
        option.pageNum(0);
        poiSearch.searchInCity(option);
        isSearching = true;
    }


    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (STOP)
            return;
        Log.e("TAG_POI", "结果");
        SearchResult.ERRORNO error = poiResult.error;
        if (error == SearchResult.ERRORNO.NO_ERROR) {
            if (poiNoResult) {//建议搜索
                Log.e("TAG_POI", "建议循环搜索");
                if (num < suggest - 1) {
                    poiList.addAll(poiResult.getAllPoi());
                    num = num + 1;
                    City = cityInfoList.get(num).city;
                    poi();
                } else {
                    poiList.addAll(poiResult.getAllPoi());
                    isSearching = false;
                    if (interfacePoiResultCallback != null) {
                        interfacePoiResultCallback.onSuccess(poiList);
                    }
                }
            } else {
                Log.e("TAG_POI", "第一次结果");
                poiList.addAll(poiResult.getAllPoi());
                isSearching = false;
                if (interfacePoiResultCallback != null) {
                    interfacePoiResultCallback.onSuccess(poiList);
                }
            }
        }

        if (error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            Log.e("TAG_POI", "建议搜索结果");
            if (poiNoResult) {
                if (num < suggest - 1) {
                    num = num + 1;
                    City = cityInfoList.get(num).city;
                    poi();
                } else {
                    poiNoResult = false;
                    suggest = 0;
                    num = 0;
                }
            } else {
                cityInfoList = new ArrayList<>();
                cityInfoList.addAll(poiResult.getSuggestCityList());
                suggest = cityInfoList.size();
                if (suggest > 0) {
                    num = 0;
                    City = cityInfoList.get(num).city;
                    poi();
                    poiNoResult = true;
                }
            }
        }
        if (error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Log.e("TAG_POI", "没有找到检索结果");
        }
        if (error == SearchResult.ERRORNO.NETWORK_ERROR) {
            Log.e("TAG_POI", "网络错误");
        }
        if (error == SearchResult.ERRORNO.NETWORK_TIME_OUT) {
            Log.e("TAG_POI", "网络超时");
        }
        if (error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Log.e("TAG_POI", "检索地址有岐义");
        }
        if (error == SearchResult.ERRORNO.PERMISSION_UNFINISHED) {
            Log.e("TAG_POI", "授权未完成");
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }
}
