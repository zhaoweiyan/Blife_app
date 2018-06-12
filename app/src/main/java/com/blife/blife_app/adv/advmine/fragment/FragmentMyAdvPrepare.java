package com.blife.blife_app.adv.advmine.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.adapter.AdapterMyadvPrepare;
import com.blife.blife_app.adv.advmine.api.API_MyadvPrepare;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv;
import com.blife.blife_app.adv.advmine.bean.BeanMyadvPrepareList;
import com.blife.blife_app.base.fragment.BaseFragment;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullToRefreshLayout;
import com.blife.blife_app.tools.pulltorefresh.pulltorefreshview.PullToRefreshRecyclerView;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/8/31.
 */
public class FragmentMyAdvPrepare extends BaseFragment implements PullToRefreshLayout.OnRefreshListener {
    private PullToRefreshRecyclerView refresh_recycler;
    private List<BeanMyadv> list;
    private AdapterMyadvPrepare adapterMyadvprepare;
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
        adapterMyadvprepare = new AdapterMyadvPrepare(getActivity(), list);
        refresh_recycler.setAdapter(adapterMyadvprepare);
        refresh_recycler.setCanLoadMore(true);
//        //请求网络
        getApiData();
    }

    public void getApiData() {
        L.e("TAG", "编辑中..加载");
        if (list != null) {
            list.clear();
        }
        offset = 0;
        loadSize = 0;
        API_MyadvPrepare api_myadvPrepare = new API_MyadvPrepare(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(api_myadvPrepare);
    }

    @Override
    protected UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_MyadvPrepare.TAG)) {
                    cancelLoadingDialog();
                    LogUtils.e("我的广告列表" + json);
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity(), refresh_recycler);
                        return;
                    }
                    BeanMyadvPrepareList beanMyadvPrepareList = (BeanMyadvPrepareList) JsonObjUItils.fromJson(json, BeanMyadvPrepareList.class);
                    List<BeanMyadv> beanMyadv = beanMyadvPrepareList.getList();
                    if (beanMyadv != null && beanMyadv.size() > 0) {
                        tv_null.setVisibility(View.INVISIBLE);
                        refresh_recycler.setVisibility(View.VISIBLE);
                        loadSize = beanMyadv.size();
                        list.addAll(beanMyadv);
                        adapterMyadvprepare.notifyDataSetChanged();
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
                if (tag.equals(API_MyadvPrepare.TAG)) {
                    refresh_recycler.FinishFailed();
                    if (isLoadMore) {
                        if (offset > loadSize)
                            offset = offset - loadSize;
                    }
                }
                cancelLoadingDialog();
            }

        };
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        list.clear();
        offset = 0;
        API_MyadvPrepare api_myadvPrepare = new API_MyadvPrepare(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(api_myadvPrepare);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        isLoadMore = true;
        offset = offset + loadSize;
        API_MyadvPrepare api_myadvPrepare = new API_MyadvPrepare(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(api_myadvPrepare);
    }

    @Override
    public void onDestroy() {
        cancelLoadingDialog();
        super.onDestroy();
    }
}
