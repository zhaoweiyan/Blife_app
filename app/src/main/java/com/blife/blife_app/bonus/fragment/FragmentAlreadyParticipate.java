package com.blife.blife_app.bonus.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.fragment.BaseFragment;
import com.blife.blife_app.bonus.activity.ActivityBonusDetails;
import com.blife.blife_app.bonus.adapter.Adapter_Already_Participate;
import com.blife.blife_app.bonus.adapterintreface.InterfaceBonusAdapterOnItemClick;
import com.blife.blife_app.bonus.api.API_Bonus_AlreadyParticipate;
import com.blife.blife_app.bonus.bean.BeanAlreadyParticipate;
import com.blife.blife_app.bonus.bean.BeanMessageEvent;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullToRefreshLayout;
import com.blife.blife_app.tools.pulltorefresh.pulltorefreshview.PullToRefreshRecyclerView;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/9/13.
 */
public class FragmentAlreadyParticipate extends BaseFragment implements PullToRefreshLayout.OnRefreshListener, InterfaceBonusAdapterOnItemClick {

    private TextView tv_empty_list;
    private PullToRefreshRecyclerView pull_recyclerview_already_participate;

    private List<BeanAlreadyParticipate> list;
    private Adapter_Already_Participate adapterAlreadyParticipate;

    @Override
    public void init() {
        initView();
        initDate();
    }

    private void initDate() {
        LogUtils.e("position****FragmentAlreadyParticipate");
        list = new ArrayList<>();
        adapterAlreadyParticipate = new Adapter_Already_Participate(instance, list);
        adapterAlreadyParticipate.setOnItemClick(this);
        pull_recyclerview_already_participate.setAdapter(adapterAlreadyParticipate);
        getAlreadyParticipate();
    }

    public void getAlreadyParticipate() {
        showLoadingDialog();
        list.clear();
        API_Bonus_AlreadyParticipate api_bonus_alreadyParticipate = new API_Bonus_AlreadyParticipate(ACCESS_TOKEN);
        dataManager.getServiceData(api_bonus_alreadyParticipate);
    }

    @Override
    protected UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_Bonus_AlreadyParticipate.TAG)) {
                    cancelLoadingDialog();
                    BeanAlreadyParticipate beanAlreadyParticipate = (BeanAlreadyParticipate) JsonObjUItils.fromJson(json, BeanAlreadyParticipate.class);
                    if (beanAlreadyParticipate != null) {
                        list.clear();
                        list.addAll(beanAlreadyParticipate.getList());
                        adapterAlreadyParticipate.notifyDataSetChanged();
                        setAlreadyParticipateNum(beanAlreadyParticipate.getCount());
                    }
                    pull_recyclerview_already_participate.Finish();
                    if (list.size() <= 0) {
                        tv_empty_list.setVisibility(View.VISIBLE);
                    } else {
                        tv_empty_list.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onError(Object tag, String message) {
                if (tag.equals(API_Bonus_AlreadyParticipate.TAG)) {
                    cancelLoadingDialog();
                    pull_recyclerview_already_participate.FinishFailed();
                }
            }
        };
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        getAlreadyParticipate();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }


    private void initView() {
        tv_empty_list = (TextView) rootView.findViewById(R.id.tv_empty_list);
        pull_recyclerview_already_participate = (PullToRefreshRecyclerView) rootView.findViewById(R.id.pull_recyclerview_already_participate);
        pull_recyclerview_already_participate.getListView().setLayoutManager(new LinearLayoutManager(instance, LinearLayoutManager.VERTICAL, false));
        pull_recyclerview_already_participate.setOnRefreshListener(this);
        pull_recyclerview_already_participate.setCanLoadMore(false);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_already_participate;
    }

    @Override
    public void onClick(int position) {
        String ADV_ID = list.get(position).getAdv_id();
        long time = list.get(position).getCreate_time();
//        String money = StringUtils.dealMoney(list.get(position).getMoney() + "", 100);
        String money = list.get(position).getMoney() + "";
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ADV_ID, ADV_ID);
        bundle.putString(Constants.BONUS_GRAB_MONEY, money);
        bundle.putLong(Constants.BONUS_GRAB_TIME, time);
        startActivity(ActivityBonusDetails.class, bundle);
    }

    private void setAlreadyParticipateNum(int num) {
        BeanMessageEvent messageEvent = new BeanMessageEvent(num + "", 2);
        EventBus.getDefault().post(messageEvent);
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
}
