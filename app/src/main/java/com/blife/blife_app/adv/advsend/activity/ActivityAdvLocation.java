package com.blife.blife_app.adv.advsend.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
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
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.tools.baidumap.InterfaceGeoCodeResultCallback;
import com.blife.blife_app.tools.baidumap.MapGetGeoCodeUtils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.location.InterfaceLocationCallback;
import com.blife.blife_app.tools.location.LocationUtil;
import com.blife.blife_app.utils.logcat.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/8/31.
 */
public class ActivityAdvLocation extends BaseActivity implements View.OnClickListener, OnMapStatusChangeListener,
        InterfaceGeoCodeResultCallback, InterfaceLocationCallback, BaiduMap.OnMapLoadedCallback {
    //视图
    private MapView mapView;
    private ImageButton ib_location;
    private TextView tv_locationaddress;
    private Button button_confirm;
    //定位信息
    private LocationUtil locationUtil;
    private double LocationLat, LocationLng;
    private String LocationAddress = "";
    private LatLng currentLatLng;
    //地图操作
    private BaiduMap baiduMap;
    //地图默认缩放
    private float defaultZoom = 16;
    //地址搜索
    private int REQUEST_CODE = 101;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advlocation);
        initBackTopBar(R.string.uploadadv_location_businessaddress);
        initView();
        initData();
    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.mapview);
        ib_location = (ImageButton) findViewById(R.id.ib_location);
        tv_locationaddress = (TextView) findViewById(R.id.tv_locationaddress);
        button_confirm = (Button) findViewById(R.id.button_confirm);
        ib_location.setOnClickListener(this);
        tv_locationaddress.setOnClickListener(this);
        button_confirm.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        screenWidth = ScreenUtils.getScreenWidth(instance);
        baiduMap = mapView.getMap();
        baiduMap.setOnMapStatusChangeListener(this);
        baiduMap.setOnMapLoadedCallback(this);
        locationUtil = LocationUtil.getInstance(ActivityAdvLocation.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            LocationLat = Double.valueOf(bundle.getString(Constants.LOCATION_CURRENT_LAT));
            LocationLng = Double.valueOf(bundle.getString(Constants.LOCATION_CURRENT_LNG));
            LocationAddress = bundle.getString(Constants.LOCATION_CURRENT_ADDRESS);
            L.e("TAG", "LocationLat:" + LocationLat);
            L.e("TAG", "LocationLng:" + LocationLng);
            L.e("TAG", "LocationAddress:" + LocationAddress);
            tv_locationaddress.setText(LocationAddress);
            currentLatLng = new LatLng(LocationLat, LocationLng);
            updateMapState(currentLatLng);
            moveViewMap(currentLatLng);
        } else {
            startLocation();
        }
    }

    //点击监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_location:
                startLocation();
                break;
            case R.id.tv_locationaddress:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.LOCATION_CURRENT_ADDRESS, LocationAddress);
                bundle.putDouble(Constants.LOCATION_CURRENT_LAT, LocationLat);
                bundle.putDouble(Constants.LOCATION_CURRENT_LNG, LocationLng);
                startActivityForResult(ActivityPoiSearch.class, REQUEST_CODE, bundle);
                break;
            case R.id.button_confirm:
                setLocationResult();
                break;
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        locationUtil.startLocation(this);
    }

    //定位成功
    @Override
    public void onLocationSuccess(BDLocation bdLocation) {
        LocationLat = bdLocation.getLatitude();
        LocationLng = bdLocation.getLongitude();
        LocationAddress = bdLocation.getAddrStr();
        tv_locationaddress.setText(LocationAddress);
        currentLatLng = new LatLng(LocationLat, LocationLng);
        updateMapState(currentLatLng);
    }

    //定位失败
    @Override
    public void onLocationError() {

    }

    @Override
    public void onCancelShowRationale() {

    }

    @Override
    public void onDeniedDialogPositive() {

    }

    @Override
    public void onDeniedDialogNegative() {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        moveViewMap(mapStatus.target);
        MapGetGeoCodeUtils.getInstance().search(mapStatus.target, this);
    }

    @Override
    public void onMapLoaded() {
        updateMapState(baiduMap.getMapStatus().target);
        MapGetGeoCodeUtils.getInstance().search(baiduMap.getMapStatus().target, this);
    }

    private void updateMapState(LatLng latLng) {
        //设置地图状态
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(0)//设置地图俯仰角
                .rotate(0)//设置地图旋转角度，逆时针旋转。
                .target(latLng)//设置地图中心点
                .zoom(defaultZoom)//设置地图缩放级别
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.animateMapStatus(mapStatusUpdate);
    }

    /**
     * 地图中心位置
     *
     * @param latLng
     */
    private void moveViewMap(LatLng latLng) {
        baiduMap.clear();
        int radius = (int) getRadius();
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.location_pointbottom);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions pointOption = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        //创建范围
        OverlayOptions circleOption = new CircleOptions()
                .center(latLng)
                .radius(radius)
                .stroke(new Stroke(2, 0xff809aff))
                .fillColor(0x66809aff);
        List<OverlayOptions> list = new ArrayList<>();
        list.add(circleOption);
        list.add(pointOption);
        baiduMap.addOverlays(list);
    }

    private double getRadius() {
        try {
            LatLng latLng1 = baiduMap.getProjection().fromScreenLocation(new Point(0, 0));
            LatLng latLng2 = baiduMap.getProjection().fromScreenLocation(new Point(screenWidth / 2, 0));
            double dis = DistanceUtil.getDistance(latLng1, latLng2);
            return dis * 3 / 5;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWidth * 3 / 5;
    }


    /**
     * 回传定位结果
     */
    private void setLocationResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LOCATION_RESULT_LAT, "" + LocationLat);
        bundle.putString(Constants.LOCATION_RESULT_LNG, "" + LocationLng);
        bundle.putString(Constants.LOCATION_RESULT_ADDRESS, LocationAddress);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                LocationAddress = bundle.getString(Constants.LOCATION_RESULT_ADDRESS);
                tv_locationaddress.setText(LocationAddress);
                LatLng latLng = bundle.getParcelable(Constants.LOCATION_RESULT_LATLNG);
                LocationLat = latLng.latitude;
                LocationLng = latLng.longitude;
                updateMapState(latLng);
            }
        }
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

    @Override
    public void onGeoCodeSuccess(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult != null) {
            if (reverseGeoCodeResult.getLocation() != null) {
                LocationLat = reverseGeoCodeResult.getLocation().latitude;
                LocationLng = reverseGeoCodeResult.getLocation().longitude;
            }
            LocationAddress = reverseGeoCodeResult.getAddress();
            tv_locationaddress.setText(LocationAddress);
        }
    }

    @Override
    public void onGeoCodeError() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
