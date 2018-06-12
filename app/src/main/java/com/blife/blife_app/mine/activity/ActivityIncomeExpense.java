package com.blife.blife_app.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.adapter.AdapterMyadvFinished;
import com.blife.blife_app.adv.advmine.api.API_MyadvFinished;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.mine.adapter.AdapterIncomeExpense;
import com.blife.blife_app.mine.api.API_IncomeExpense;
import com.blife.blife_app.mine.bean.BeanIncomeExpense;
import com.blife.blife_app.mine.bean.BeanIncomeExpenseList;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullToRefreshLayout;
import com.blife.blife_app.tools.pulltorefresh.pulltorefreshview.PullToRefreshRecyclerView;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/9/18.
 */
public class ActivityIncomeExpense extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private PullToRefreshRecyclerView refresh_recycler;
    private ArrayList<BeanIncomeExpense> list;
    int offset = 0;
    int limit = 10;
    private AdapterIncomeExpense adapterIncomeExpense;
    private int loadSize = 0;
    private boolean isLoadMore;
    private TextView tv_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expense);
        showLoadingDialog();
        initView();
        initAdapter();
        initIncomeExpense();
    }

    private void initIncomeExpense() {
        if (list != null) {
            list.clear();
        }
        offset = 0;
        loadSize = 0;
        API_IncomeExpense api_incomeExpense = new API_IncomeExpense(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(api_incomeExpense);
    }

    private void initView() {
        initBackTopBar(R.string.income_expense);
        refresh_recycler = (PullToRefreshRecyclerView) findViewById(R.id.refresh_recycler);
        refresh_recycler.setOnRefreshListener(this);

        tv_null = (TextView) findViewById(R.id.tv_null);
    }

    private void initAdapter() {
        list = new ArrayList<>();
        adapterIncomeExpense = new AdapterIncomeExpense(this, list);
        refresh_recycler.setAdapter(adapterIncomeExpense);
        refresh_recycler.setCanLoadMore(true);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_IncomeExpense.TAG)) {
                    LogUtils.e("收支明细****" + json);
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(ActivityIncomeExpense.this);
                        return;
                    }
                    BeanIncomeExpenseList beanIncomeExpenseList = (BeanIncomeExpenseList) JsonObjUItils.fromJson(json, BeanIncomeExpenseList.class);
                    List<BeanIncomeExpense> incomeExpense = beanIncomeExpenseList.getList();
                    if (incomeExpense != null && incomeExpense.size() > 0) {
                        tv_null.setVisibility(View.GONE);
                        refresh_recycler.setVisibility(View.VISIBLE);
                        LogUtils.e("beanMyadv.size===" + incomeExpense.size());
                        loadSize = incomeExpense.size();
                        list.addAll(incomeExpense);
                        adapterIncomeExpense.notifyDataSetChanged();
                        refresh_recycler.setCanLoadMore(true);
                    } else {
                        LogUtils.e("offset*****"+offset);
                        if (offset == 0) {
                            tv_null.setVisibility(View.VISIBLE);
                            refresh_recycler.setVisibility(View.GONE);
                        }
                        refresh_recycler.setCanLoadMore(false);
                    }
                    refresh_recycler.Finish();
                }
            }

            @Override
            public void onError(Object tag, String message) {
//                if (message != null) {
//                    ToastUtils.showShort(ActivityIncomeExpense.this, message);
//                }
                if (tag.equals(API_IncomeExpense.TAG)) {
                    cancelLoadingDialog();
                    refresh_recycler.FinishFailed();
//                    if (offset == 0) {
//                        tv_null.setVisibility(View.VISIBLE);
//                        refresh_recycler.setVisibility(View.GONE);
//                    }
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
        API_IncomeExpense api_incomeExpense = new API_IncomeExpense(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(api_incomeExpense);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        isLoadMore = true;
        offset = offset + loadSize;
        API_IncomeExpense api_incomeExpense = new API_IncomeExpense(ACCESS_TOKEN, offset, limit);
        dataManager.getServiceData(api_incomeExpense);
    }
}
