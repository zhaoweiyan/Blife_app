package com.blife.blife_app.adv.advmine.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.adapter.AdapterMyadvPerforming;
import com.blife.blife_app.adv.advmine.adapter.AdapterMyadvPrepare;
import com.blife.blife_app.adv.advmine.api.API_MyadvFinished;
import com.blife.blife_app.adv.advmine.api.API_MyadvPerforming;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv;
import com.blife.blife_app.adv.advmine.bean.BeanMyadvPerformingList;
import com.blife.blife_app.adv.advmine.bean.BeanMyadvPrepareList;
import com.blife.blife_app.base.fragment.BaseFragment;
import com.blife.blife_app.tools.JsonObjUItils;
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
public class FragmentMyAdvPerforming extends BaseFragment implements PullToRefreshLayout.OnRefreshListener {
    private PullToRefreshRecyclerView refresh_recycler;
    private List<BeanMyadv> list;
    private AdapterMyadvPerforming adapterMyadvPerforming;
    private int offset = 0;
    private int limit = 10;
    private boolean isLoadMore;
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

        tv_null=(TextView)rootView.findViewById(R.id.tv_null);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_prepare;
    }

    private void initMyadv() {
        list = new ArrayList<>();
        adapterMyadvPerforming = new AdapterMyadvPerforming(getActivity(), list);
        refresh_recycler.setAdapter(adapterMyadvPerforming);
        refresh_recycler.setCanLoadMore(true);
        if (list != null) {
            list.clear();
        }
        offset=0;
        loadSize=0;
        //请求网络
        API_MyadvPerforming aPI_MyadvPerforming = new API_MyadvPerforming(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(aPI_MyadvPerforming);
    }


    @Override
    protected UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_MyadvPerforming.TAG)) {
                    cancelLoadingDialog();
                    LogUtils.e("我的广告发送中列表" + json);
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity(), refresh_recycler);
                        return;
                    }
                    BeanMyadvPerformingList beanMyadvPerformingList = (BeanMyadvPerformingList) JsonObjUItils.fromJson(json, BeanMyadvPerformingList.class);
                    List<BeanMyadv> beanMyadv = beanMyadvPerformingList.getList();
                    if (beanMyadv != null && beanMyadv.size() > 0) {
                        LogUtils.e("list.size===" + list.size());
                        tv_null.setVisibility(View.INVISIBLE);
                        refresh_recycler.setVisibility(View.VISIBLE);
                        loadSize = beanMyadv.size();
                        list.addAll(beanMyadv);
                        adapterMyadvPerforming.notifyDataSetChanged();
                        refresh_recycler.setCanLoadMore(true);
                    } else {
                        if(offset==0){
                            tv_null.setVisibility(View.VISIBLE);
                            refresh_recycler.setVisibility(View.INVISIBLE);
                        }
                        refresh_recycler.setCanLoadMore(false);
                    }
                    refresh_recycler.Finish();
                }
            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
                if (tag.equals(API_MyadvPerforming.TAG)) {
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
        if ( list != null) {
            list.clear();
        }
        offset = 0;
        API_MyadvPerforming aPI_MyadvPerforming = new API_MyadvPerforming(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(aPI_MyadvPerforming);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        isLoadMore = true;
        offset = offset + loadSize;
        API_MyadvPerforming aPI_MyadvPerforming = new API_MyadvPerforming(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(aPI_MyadvPerforming);
    }
    @Override
    public void onDestroy() {
        cancelLoadingDialog();
        super.onDestroy();
    }
}
