package com.blife.blife_app.bonus.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.tools.http.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/9/19.
 */
public class ActivityBusinessLocation extends BaseActivity {

    private MapView mapView;
    private TextView tv_bonus_location_address;
    private BaiduMap baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_location);
        initBackTopBar(R.string.bonus_location);
        initView();
        initData();
    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.bonus_mapview);
        tv_bonus_location_address = (TextView) findViewById(R.id.tv_bonus_location_address);
        baiduMap = mapView.getMap();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tv_bonus_location_address.setText(bundle.getString(Constants.BONUS_LOCATION_ADDRESS));
            LatLng latLng = bundle.getParcelable(Constants.BONUS_LOCATION_LAT_LNG);
            if (latLng != null) {
                moveViewMap(latLng);
            }
        }
    }


    /**
     * 地图中心位置
     *
     * @param latLng
     */
    private void moveViewMap(LatLng latLng) {
        baiduMap.clear();
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.location_pointbottom);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions pointOption = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        //设置地图状态
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(0)//设置地图俯仰角
                .rotate(0)//设置地图旋转角度，逆时针旋转。
                .target(latLng)//设置地图中心点
                .zoom(16)//设置地图缩放级别
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.animateMapStatus(mapStatusUpdate);
        baiduMap.addOverlay(pointOption);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
