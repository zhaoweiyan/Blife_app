package com.blife.blife_app.adv.advmine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.activity.ActivityRefundSchedule;
import com.blife.blife_app.adv.advmine.adapter.AdapterMyadvFinished;
import com.blife.blife_app.adv.advmine.adapter.AdapterMyadvPrepare;
import com.blife.blife_app.adv.advmine.api.API_MyadvFinished;
import com.blife.blife_app.adv.advmine.api.API_Refund;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv;
import com.blife.blife_app.adv.advmine.bean.BeanMyadvPrepareList;
import com.blife.blife_app.base.fragment.BaseFragment;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullToRefreshLayout;
import com.blife.blife_app.tools.pulltorefresh.pulltorefreshview.PullToRefreshRecyclerView;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/8/31.
 */
public class FragmentMyAdvFinished extends BaseFragment implements PullToRefreshLayout.OnRefreshListener {
    private PullToRefreshRecyclerView refresh_recycler;
    private List<BeanMyadv> list;
    private AdapterMyadvFinished adapterMyadvFinished;
    private int offset = 0;
    private int limit = 10;
    private boolean isLoadMore;
    private int positio;
    private int loadSize = 0;
    private TextView tv_null;

    @Override
    public void init() {
        showLoadingDialog();
        initView();
        initMyadv();
    }

    private void initView() {
        refresh_recycler = (PullToRefreshRecyclerView) rootView.findViewById(R.id.refresh_recycler);
        refresh_recycler.setOnRefreshListener(this);

        tv_null = (TextView) rootView.findViewById(R.id.tv_null);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_prepare;
    }

    private void initMyadv() {
        list = new ArrayList<>();
        adapterMyadvFinished = new AdapterMyadvFinished(getActivity(), list, ACCESS_TOKEN);
        refresh_recycler.setAdapter(adapterMyadvFinished);
        refresh_recycler.setCanLoadMore(true);
        //请求网络
        if (list != null) {
            list.clear();
        }
        offset = 0;
        loadSize = 0;
        API_MyadvFinished aPI_MyadvFinished = new API_MyadvFinished(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(aPI_MyadvFinished);


        adapterMyadvFinished.setNotifyDataSetChanged(new AdapterMyadvFinished.Notify() {
            @Override
            public void setNotify(String adv_id, int position) {
                LogUtils.e("adv_id****" + adv_id);
                showLoadingDialog();
                API_Refund api_refund = new API_Refund(ACCESS_TOKEN, adv_id);
                dataManager.getServiceData(api_refund);
                positio = position;
            }
        });


    }

    @Override
    protected UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_MyadvFinished.TAG)) {
                    LogUtils.e("我的广告完成列表" + json);
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity(), refresh_recycler);
                        return;
                    }
                    BeanMyadvPrepareList beanMyadvPrepareList = (BeanMyadvPrepareList) JsonObjUItils.fromJson(json, BeanMyadvPrepareList.class);
                    List<BeanMyadv> beanMyadv = beanMyadvPrepareList.getList();
                    if (beanMyadv != null && beanMyadv.size() > 0) {
                        LogUtils.e("beanMyadv.size===" + beanMyadv.size());
                        tv_null.setVisibility(View.INVISIBLE);
                        refresh_recycler.setVisibility(View.VISIBLE);
                        loadSize = beanMyadv.size();
                        list.addAll(beanMyadv);
                        adapterMyadvFinished.notifyDataSetChanged();
                        refresh_recycler.setCanLoadMore(true);
                    } else {
                        if (offset == 0) {
                            tv_null.setVisibility(View.VISIBLE);
                            refresh_recycler.setVisibility(View.INVISIBLE);
                        }
                        refresh_recycler.setCanLoadMore(false);
                    }
                    refresh_recycler.Finish();
                }

                if (tag.equals(API_Refund.TAG)) {
                    list.get(positio).setRefund_status("2");
                    adapterMyadvFinished.notifyDataSetChanged();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.ADV_ID, list.get(positio).getAdv_id());
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), ActivityRefundSchedule.class);
                    getActivity().startActivity(intent);
                    cancelLoadingDialog();
                }
            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
                if (tag.equals(API_MyadvFinished.TAG)) {
                    refresh_recycler.FinishFailed();
                    if (isLoadMore) {
                        if (offset > loadSize)
                            offset = offset - loadSize;
                    }
                }
            }

        };
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if (list != null) {
            list.clear();
        }
        offset = 0;
        API_MyadvFinished aPI_MyadvFinished = new API_MyadvFinished(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(aPI_MyadvFinished);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        isLoadMore = true;
        offset = offset + loadSize;
        API_MyadvFinished aPI_MyadvFinished = new API_MyadvFinished(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(aPI_MyadvFinished);
    }

    @Override
    public void onDestroy() {
        cancelLoadingDialog();
        super.onDestroy();
    }
}
