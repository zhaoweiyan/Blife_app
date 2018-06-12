package com.blife.blife_app.adv.advsend.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.blife.blife_app.R;
import com.blife.blife_app.adv.advsend.api.API_AdvSendTarget;
import com.blife.blife_app.adv.advsend.api.API_AdvSendTargetDelete;
import com.blife.blife_app.adv.advsend.api.API_AdvSendTargetGet;
import com.blife.blife_app.adv.advsend.bean.BeanSendTarget;
import com.blife.blife_app.adv.advsend.view.InterfaceOnItemDelete;
import com.blife.blife_app.adv.advsend.view.MapArcView;
import com.blife.blife_app.adv.advsend.view.VerticalListLayout;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.baidumap.InterfaceGeoCodeResultCallback;
import com.blife.blife_app.tools.baidumap.MapGetGeoCodeUtils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.location.InterfaceLocationCallback;
import com.blife.blife_app.tools.location.LocationUtil;
import com.blife.blife_app.utils.activity.ActivityManager;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/8/30.
 */
public class ActivitySendTarget extends BaseActivity implements View.OnClickListener, BaiduMap.OnMapStatusChangeListener,
        InterfaceGeoCodeResultCallback, InterfaceOnItemDelete, InterfaceLocationCallback, BaiduMap.OnMapLoadedCallback {

    //视图
    private MapView mapView;
    private ImageButton ib_location;
    private TextView tv_locationaddress, tv_locationdistance;
    private VerticalListLayout verticalListLayout;
    private FrameLayout framelayout_confirm;
    //    private Button button_sendtargetconfirm;
    //地图距离标识圆
    private MapArcView maparcview;
    //定位信息
    private double LocationLat, LocationLng;
    //    private String LocationAddress;
    private LatLng currentLatLng;
    //地图操作
    private boolean openLocation = true;
    private BaiduMap baiduMap;
    private int mapArcCenterPointX = 0, mapArcCenterPointY = 0;
    private int PointX = 0, PointY = 0;
    private double Distance = 0;
    private String currentAddress = "";
    //屏幕点对应的地图坐标
    private LatLng pointLatLng, centerLatLng;
    //地址搜索
    private int REQUEST_CODE = 101;
    private String ADV_ID;

    //地理位置列表
    private List<BeanSendTarget> list;

    private LocationUtil locationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendtarget);
        initBackTopBar(R.string.uploadadv_sendtarget, R.string.uploadadv_confirm);
        initView();
        initData();
    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.mapview);
        ib_location = (ImageButton) findViewById(R.id.ib_location);
        tv_locationaddress = (TextView) findViewById(R.id.tv_locationaddress);
        tv_locationdistance = (TextView) findViewById(R.id.tv_locationdistance);
        verticalListLayout = (VerticalListLayout) findViewById(R.id.verticellayout);
        framelayout_confirm = (FrameLayout) findViewById(R.id.framelayout_confirm);
//        button_sendtargetconfirm = (Button) findViewById(R.id.button_sendtargetconfirm);
        maparcview = (MapArcView) findViewById(R.id.maparcview);
        ib_location.setOnClickListener(this);
//        button_sendtargetconfirm.setOnClickListener(this);
        tv_locationaddress.setOnClickListener(this);
        framelayout_confirm.setOnClickListener(this);
        verticalListLayout.setInterfaceOnItemDelete(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        locationUtil = LocationUtil.getInstance(ActivitySendTarget.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ADV_ID = bundle.getString(Constants.CREATE_ADV_ID);
            L.e("TAG", "广告ID:" + ADV_ID);
        }
        list = new ArrayList<>();
        baiduMap = mapView.getMap();
        baiduMap.setOnMapStatusChangeListener(this);
        baiduMap.setOnMapLoadedCallback(this);
        API_AdvSendTargetGet api = new API_AdvSendTargetGet(ACCESS_TOKEN, ADV_ID);
        dataManager.getServiceData(api);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_location:
                openLocation = true;
                startLocation();
                break;
            case R.id.tv_locationaddress:
                L.e("TAG", "发送位置：" + currentAddress + "--" + LocationLat + "--" + LocationLng);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.LOCATION_CURRENT_ADDRESS, currentAddress);
                bundle.putDouble(Constants.LOCATION_CURRENT_LAT, LocationLat);
                bundle.putDouble(Constants.LOCATION_CURRENT_LNG, LocationLng);
                startActivityForResult(ActivityPoiSearch.class, REQUEST_CODE, bundle);
                break;
            case R.id.framelayout_confirm:
                postJsonData();
                break;
//            case R.id.button_sendtargetconfirm:
//                TopBack();
//                finish();
//                break;
        }
    }

    @Override
    protected void TopRightClick() {
        TopBack();
        finish();
    }

    @Override
    protected void TopBack() {
        Intent intent = new Intent();
        intent.putExtra(Constants.SEND_TARGET_NUM, list.size());
        setResult(RESULT_OK, intent);
    }

    /**
     * 上传数据
     */
    private void postJsonData() {
        if (TextUtils.isEmpty(currentAddress) || centerLatLng == null || Distance <= 0) {
            ToastUtils.showShort(instance, R.string.uploadadv_locationing);
            return;
        }
        JSONObject object = new JSONObject();
        try {
            object.put("adv_id", ADV_ID);
            object.put("lat", centerLatLng.latitude);
            object.put("lng", centerLatLng.longitude);
            object.put("range", Distance);
            object.put("address", currentAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        API_AdvSendTarget api_advSendTarget = new API_AdvSendTarget(ACCESS_TOKEN, object.toString());
        dataManager.getServiceData(api_advSendTarget);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_AdvSendTargetGet.TAG)) {
                    BeanSendTarget beanSendTarget = (BeanSendTarget) JsonObjUItils.fromJson(json, BeanSendTarget.class);
                    List<BeanSendTarget> beanList = beanSendTarget.getList();
                    if (beanList.size() <= 3) {
                        list.addAll(beanList);
                        verticalListLayout.setData(list);
                    }
                }
                if (tag.equals(API_AdvSendTarget.TAG)) {
                    L.e("TAG", "投放位置上传成功" + json);
                    BeanSendTarget beanSendTarget = (BeanSendTarget) JsonObjUItils.fromJson(json, BeanSendTarget.class);
                    if (list.size() < 3) {
                        list.add(beanSendTarget.getInfo());
                        verticalListLayout.setData(list);
                    }
                }
                if (tag.equals(API_AdvSendTargetDelete.TAG)) {
                    L.e("TAG", "位置删除成功" + json);
                    list.remove(DELETE_POSITION);
                    verticalListLayout.setData(list);
                }
            }

            @Override
            public void onError(Object tag, String message) {
                if (tag.equals(API_AdvSendTarget.TAG)) {
                    L.e("TAG", "投放位置上传失败" + message);
                }
                if (tag.equals(API_AdvSendTargetDelete.TAG)) {
                    L.e("TAG", "位置删除失败" + message);
                }
            }
        };
    }

    private int DELETE_POSITION;

    @Override
    public void onDelete(int position) {
        DELETE_POSITION = position;
        BeanSendTarget bean = list.get(position);
        if (bean != null)
            deleteAdvJson(bean.getPub_id());
    }

    private void deleteAdvJson(String pubId) {
        JSONObject object = new JSONObject();
        try {
            object.put("pub_id", pubId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        API_AdvSendTargetDelete api_advSendTargetDelete = new API_AdvSendTargetDelete(ACCESS_TOKEN, object.toString());
        dataManager.getServiceData(api_advSendTargetDelete);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int screenWidth = ActivityManager.getScreenWidth(instance);
        //计算标识圆在屏幕的位置--以屏幕左上角为原点
        int[] locations = new int[2];
        maparcview.getLocationInWindow(locations);
        //计算地图在屏幕的位置--以屏幕左上角为原点
        int[] mapLocation = new int[2];
        mapView.getLocationOnScreen(mapLocation);
        //计算标识圆的半径
        int radius = screenWidth / 2 - locations[0];
        //计算标识圆边上最左侧一点在地图上的位置--以地图左上角为原点
        PointX = locations[0];
        PointY = radius + locations[1] - mapLocation[1];
        //计算标识圆中心点在地图上的位置--以地图左上角为原点
        mapArcCenterPointX = screenWidth / 2;
        mapArcCenterPointY = PointY;
        L.e("TAG", "屏幕宽度：" + screenWidth);
        L.e("TAG", "位置-X：" + PointX);
        L.e("TAG", "位置-Y：" + PointY);
        L.e("TAG", "中心位置-X：" + mapArcCenterPointX);
        L.e("TAG", "中心位置-Y：" + mapArcCenterPointY);
        startLocation();
        openLocation = false;
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        if (openLocation) {
            locationUtil.startLocation(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //定位成功
    @Override
    public void onLocationSuccess(BDLocation bdLocation) {
        openLocation = false;
        LocationLat = bdLocation.getLatitude();
        LocationLng = bdLocation.getLongitude();
        currentAddress = bdLocation.getAddrStr();
        tv_locationaddress.setText(currentAddress);
        currentLatLng = new LatLng(LocationLat, LocationLng);
        moveViewMap(currentLatLng);
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
                .targetScreen(new Point(mapArcCenterPointX, mapArcCenterPointY))
                .zoom(16)//设置地图缩放级别
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.animateMapStatus(mapStatusUpdate);
        baiduMap.addOverlay(pointOption);
    }

    /**
     * 计算地图两点的距离
     */
    private void dealMapDistance() {
        if (baiduMap != null) {
            pointLatLng = baiduMap.getProjection().fromScreenLocation(new Point(PointX, PointY));
            L.e("TAG", "位置点对应坐标：" + pointLatLng.latitude + "-----" + pointLatLng.longitude);
            centerLatLng = baiduMap.getProjection().fromScreenLocation(new Point(mapArcCenterPointX, mapArcCenterPointY));
            L.e("TAG", "位置中心点对应坐标：" + centerLatLng.latitude + "-----" + centerLatLng.longitude);
            Distance = DistanceUtil.getDistance(pointLatLng, centerLatLng);
            L.e("TAG", "位置点距离：" + Distance);
            tv_locationdistance.setText(((int) Distance) + getString(R.string.uploadadv_m));
        }
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        dealMapDistance();
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        L.e("TAG", "位置onMapStatusChangeFinish");
        MapGetGeoCodeUtils.getInstance().search(centerLatLng, this);
    }

    @Override
    public void onMapLoaded() {
        dealMapDistance();
        MapGetGeoCodeUtils.getInstance().search(centerLatLng, this);
    }

    @Override
    public void onGeoCodeSuccess(ReverseGeoCodeResult reverseGeoCodeResult) {
        LatLng latLng = reverseGeoCodeResult.getLocation();
        if (latLng != null) {//修改缩放地图时，反编地理编码地址位置经纬度null
            LocationLat = latLng.latitude;
            LocationLng = latLng.longitude;
        }
        currentAddress = reverseGeoCodeResult.getAddress();
        tv_locationaddress.setText(currentAddress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                currentAddress = bundle.getString(Constants.LOCATION_RESULT_ADDRESS);
                LatLng latLng = bundle.getParcelable(Constants.LOCATION_RESULT_LATLNG);
                tv_locationaddress.setText(currentAddress);
                if (latLng != null) {
                    LocationLat = latLng.latitude;
                    LocationLng = latLng.longitude;
                    moveViewMap(latLng);
                }
                L.e("TAG", "接收位置：" + currentAddress + "--" + LocationLat + "--" + LocationLng);
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


    //定位失败
    @Override
    public void onLocationError() {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onGeoCodeError() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            TopBack();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
