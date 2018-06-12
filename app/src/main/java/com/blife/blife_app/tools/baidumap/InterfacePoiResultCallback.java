package com.blife.blife_app.tools.baidumap;

import com.baidu.mapapi.search.core.PoiInfo;

import java.util.List;

/**
 * Created by w on 2016/8/31.
 */
public interface InterfacePoiResultCallback {

    void onSuccess(List<PoiInfo> list);

    void onNoResult();

}
