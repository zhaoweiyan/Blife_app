package com.blife.blife_app.adv.advsend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.blife.blife_app.R;
import com.blife.blife_app.adv.advsend.adapter.AdapterPoiResult;
import com.blife.blife_app.adv.advsend.adapter.InterfacePoiResult;
import com.blife.blife_app.adv.advsend.bean.BeanPoiResult;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.tools.baidumap.InterfacePoiResultCallback;
import com.blife.blife_app.tools.baidumap.PoiSearchUtils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.logcat.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/8/31.
 */
public class ActivityPoiSearch extends BaseActivity implements View.OnClickListener, TextWatcher
        , InterfacePoiResultCallback, InterfacePoiResult {

    private FrameLayout framelayout_topleft;
    private EditText et_searchPoi;
    private TextView tv_confirm;
    private RecyclerView recycler_poiresult;
    private AdapterPoiResult adapterPoiResult;
    private List<BeanPoiResult> list;
    private String address;
    private BeanPoiResult currentBeanPoiResult;
    private LatLng selectLatLng;
    private String selectAddress;
    private boolean isSearch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poisearch);
        initView();
        initData();
    }

    private void initView() {
        framelayout_topleft = (FrameLayout) findViewById(R.id.framelayout_topleft);
        et_searchPoi = (EditText) findViewById(R.id.et_poisearch);
        tv_confirm = (TextView) findViewById(R.id.tv_poiconfirm);
        recycler_poiresult = (RecyclerView) findViewById(R.id.recycle_poiresult);
        framelayout_topleft.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        et_searchPoi.addTextChangedListener(this);
        tv_confirm.setText(R.string.uploadadv_search);
    }

    private void initData() {
        list = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String address = bundle.getString(Constants.LOCATION_CURRENT_ADDRESS);
            if (!TextUtils.isEmpty(address)) {
                double lat = bundle.getDouble(Constants.LOCATION_CURRENT_LAT);
                double lng = bundle.getDouble(Constants.LOCATION_CURRENT_LNG);
                L.e("TAG", "POI接收位置：" + address + "--" + lat + "--" + lng);
                currentBeanPoiResult = new BeanPoiResult();
                currentBeanPoiResult.setTYPE(AdapterPoiResult.TYPE_CURRENT);
                currentBeanPoiResult.setAddress(address);
                currentBeanPoiResult.setLatLng(new LatLng(lat, lng));
                list.add(currentBeanPoiResult);
            }
        }
        adapterPoiResult = new AdapterPoiResult(instance, list);
        adapterPoiResult.setInterfacePoiResult(this);
        recycler_poiresult.setLayoutManager(new LinearLayoutManager(instance, LinearLayoutManager.VERTICAL, false));
        recycler_poiresult.setAdapter(adapterPoiResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.framelayout_topleft:
                finish();
                break;
            case R.id.tv_poiconfirm:
                if (isSearch) {
                    startPOI();
                } else {
                    setAddressResult();
                }
                break;
        }
    }

    /**
     * 开始POI搜索
     */
    private void startPOI() {
        isSearch = true;
        tv_confirm.setText(R.string.uploadadv_search);
        PoiSearchUtils.getInstance().search(address, this);
    }

    @Override
    public void onSuccess(List<PoiInfo> list) {
        dealResult(list);
    }

    @Override
    public void onNoResult() {
        list.clear();
        if (currentBeanPoiResult != null && !TextUtils.isEmpty(currentBeanPoiResult.getAddress()))
            list.add(currentBeanPoiResult);
        adapterPoiResult.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        address = s.toString().trim();
        list.clear();
        if (currentBeanPoiResult != null && !TextUtils.isEmpty(currentBeanPoiResult.getAddress()))
            list.add(currentBeanPoiResult);
        adapterPoiResult.notifyDataSetChanged();
        startPOI();
    }

    private void dealResult(List<PoiInfo> poiList) {
        list.clear();
        if (currentBeanPoiResult != null && !TextUtils.isEmpty(currentBeanPoiResult.getAddress()))
            list.add(currentBeanPoiResult);
        for (PoiInfo info : poiList) {
            BeanPoiResult beanPoiResult = new BeanPoiResult();
            beanPoiResult.setAddress(info.name);
            beanPoiResult.setAddressdetail(info.address);
            beanPoiResult.setLatLng(info.location);
            beanPoiResult.setTYPE(AdapterPoiResult.TYPE_SEARCH);
            list.add(beanPoiResult);
        }
        adapterPoiResult.notifyDataSetChanged();
    }


    /**
     * 设置地址搜索回传
     */
    private void setAddressResult() {
        L.e("TAG", "POI发送位置：" + selectAddress + "--" + selectLatLng.latitude + "--" + selectLatLng.longitude);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.LOCATION_RESULT_LATLNG, selectLatLng);
        bundle.putString(Constants.LOCATION_RESULT_ADDRESS, selectAddress);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemClick(int position) {
        BeanPoiResult bean = list.get(position);
        selectLatLng = bean.getLatLng();
        selectAddress = bean.getAddress();
        setAddressResult();
    }

    @Override
    public void onItemAdd(int position) {
        BeanPoiResult bean = list.get(position);
        selectLatLng = bean.getLatLng();
        selectAddress = bean.getAddress();
        et_searchPoi.setText(bean.getAddress());
        et_searchPoi.setSelection(bean.getAddress().length());
        tv_confirm.setText(R.string.uploadadv_confirm);
        isSearch = false;
    }
}
