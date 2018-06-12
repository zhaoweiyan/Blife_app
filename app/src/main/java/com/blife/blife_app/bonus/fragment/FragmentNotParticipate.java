package com.blife.blife_app.bonus.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.blife.blife_app.R;
import com.blife.blife_app.base.fragment.BaseFragment;
import com.blife.blife_app.bonus.activity.ActivityBonusDetails;
import com.blife.blife_app.bonus.adapter.Adapter_Not_Participate;
import com.blife.blife_app.bonus.adapterintreface.InterfaceBonusAdapterOnItemClick;
import com.blife.blife_app.bonus.api.API_Bonus_NotParticipate;
import com.blife.blife_app.bonus.bean.BeanMessageEvent;
import com.blife.blife_app.bonus.bean.BeanNotParticipate;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.location.InterfaceLocationCallback;
import com.blife.blife_app.tools.location.LocationUtil;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullToRefreshLayout;
import com.blife.blife_app.tools.pulltorefresh.pulltorefreshview.PullToRefreshRecyclerView;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/9/13.
 */
public class FragmentNotParticipate extends BaseFragment implements PullToRefreshLayout.OnRefreshListener,
        InterfaceBonusAdapterOnItemClick, InterfaceLocationCallback {

    private TextView tv_empty_list;
    private PullToRefreshRecyclerView pull_recyclerview_not_participate;

    private List<BeanNotParticipate> list;
    private Adapter_Not_Participate adapter_not_participate;

    private API_Bonus_NotParticipate api_bonus_notParticipate;
    private int limit = 10, offset = 0;
    private String lat, lng;

    private LocationUtil locationUtil;

    @Override
    public void init() {
        initView();
        initData();
    }

    private void initView() {
        tv_empty_list = (TextView) rootView.findViewById(R.id.tv_empty_list);
        pull_recyclerview_not_participate = (PullToRefreshRecyclerView) rootView.findViewById(R.id.pull_recyclerview_not_participate);
        pull_recyclerview_not_participate.getListView().setLayoutManager(new LinearLayoutManager(instance, LinearLayoutManager.VERTICAL, false));
        pull_recyclerview_not_participate.setOnRefreshListener(this);
    }

    private void initData() {
        locationUtil = LocationUtil.getInstance(getActivity());
        list = new ArrayList<>();
        adapter_not_participate = new Adapter_Not_Participate(instance, list);
        adapter_not_participate.setOnItemClick(this);
        pull_recyclerview_not_participate.setAdapter(adapter_not_participate);
        api_bonus_notParticipate = new API_Bonus_NotParticipate(ACCESS_TOKEN);
        offset = 0;
        showLoadingDialog();
        startLocation();
    }

    @Override
    public void onDestroy() {
        cancelLoadingDialog();
        super.onDestroy();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onClick(int position) {
        BeanNotParticipate bean = list.get(position);
        if (bean.getBonus_remain_num() <= 0) {
            messageDialog.setTitle(R.string.uploadadv_bonus_ending);
            messageDialog.setMessage(R.string.uploadadv_bonus_ending_message);
            messageDialog.show(pull_recyclerview_not_participate);
        } else {
            String ADV_ID = list.get(position).getAdv_id();
            String PUB_ID = list.get(position).getPub_id();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.ADV_ID, ADV_ID);
            bundle.putString(Constants.PUB_ID, PUB_ID);
            startActivity(ActivityBonusDetails.class, bundle);
        }
    }

    /**
     * 请求数据
     */
    private void getAPIData() {
        api_bonus_notParticipate.setLocation(lat, lng);
        api_bonus_notParticipate.setOffsetLimit(offset, limit);
        dataManager.getServiceData(api_bonus_notParticipate);
    }

    @Override
    protected UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_Bonus_NotParticipate.TAG)) {
                    LogUtils.e("BUG调试--未领取红包" + json);
                    cancelLoadingDialog();
                    BeanNotParticipate beanNotParticipate = (BeanNotParticipate) JsonObjUItils.fromJson(json, BeanNotParticipate.class);
                    List<BeanNotParticipate> notParticipateList = beanNotParticipate.getList();
                    if (notParticipateList.size() < limit) {
                        pull_recyclerview_not_participate.setCanLoadMore(false);
                    } else {
                        pull_recyclerview_not_participate.setCanLoadMore(true);
                    }
                    if (offset == 0)
                        list.clear();
                    list.addAll(notParticipateList);
                    adapter_not_participate.notifyDataSetChanged();
                    pull_recyclerview_not_participate.Finish();
                    setNotParticipateNum(beanNotParticipate.getCount());
                    if (list.size() <= 0) {
                        tv_empty_list.setVisibility(View.VISIBLE);
                    } else {
                        tv_empty_list.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onError(Object tag, String message) {
                if (tag.equals(API_Bonus_NotParticipate.TAG)) {
                    cancelLoadingDialog();
                    pull_recyclerview_not_participate.FinishFailed();
                }
            }
        };
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        list.clear();
        offset = 0;
        startLocation();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        offset = list.size();
        startLocation();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_not_participate;
    }

    private void startLocation() {
        locationUtil.startLocation(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationSuccess(BDLocation bdLocation) {
        lat = bdLocation.getLatitude() + "";
        lng = bdLocation.getLongitude() + "";
        getAPIData();
    }

    @Override
    public void onLocationError() {
        pull_recyclerview_not_participate.FinishFailed();
        cancelLoadingDialog();
    }

    @Override
    public void onCancelShowRationale() {
        pull_recyclerview_not_participate.FinishFailed();
        cancelLoadingDialog();
    }

    @Override
    public void onDeniedDialogPositive() {
        pull_recyclerview_not_participate.FinishFailed();
        cancelLoadingDialog();
    }

    @Override
    public void onDeniedDialogNegative() {
        pull_recyclerview_not_participate.FinishFailed();
        cancelLoadingDialog();
    }

    private void setNotParticipateNum(int num) {
        BeanMessageEvent messageEvent = new BeanMessageEvent(num + "", 1);
        EventBus.getDefault().post(messageEvent);
    }

}
