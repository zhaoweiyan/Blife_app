package com.blife.blife_app.adv.advmine.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.adapter.AdapterMyadvGetCount;
import com.blife.blife_app.adv.advmine.api.API_GetCount;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv2;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.adv.advmine.bean.BeanGetCount;
import com.blife.blife_app.adv.advmine.bean.BeanGetCountList;
import com.blife.blife_app.tools.BitmapHelp;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullToRefreshLayout;
import com.blife.blife_app.tools.pulltorefresh.pulltorefreshview.PullToRefreshRecyclerView;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.NumberUtils;
import com.blife.blife_app.utils.util.ToastUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/9/8.
 */
public class ActivityGetCount extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private String adv_id = "-1";
    private ArrayList<BeanGetCount> list;
    private BeanGetCountList beanGetCountList;
    private AdapterMyadvGetCount adapterMyadvGetCount;
    private PullToRefreshRecyclerView refresh_recycler;
    private int offset = 0;
    private int limit = 10;
    private boolean isLoadMore;
    private ImageView iv_logo;
    private TextView tv_begintime, tv_end_time, tv_description, tv_get_num, tv_total_num, tv_get_amount, tv_total_amount;
    private ImageView iv_stroke;
    private int loadSize = 0;
    private String bouns_total_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_count);
        initPre();
        initView();
        initAdapter();
        initGetCount();
    }

    private void initPre() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.ADV_ID)) {
            adv_id = bundle.getString(Constants.ADV_ID);
        }
        if (bundle != null && bundle.containsKey(Constants.BOUNS_AMOUNT)) {
            bouns_total_amount = bundle.getString(Constants.BOUNS_AMOUNT);
        }
    }

    private void initGetCount() {
        showLoadingDialog();
        offset = 0;
        loadSize = 0;
        API_GetCount api_getCount = new API_GetCount(ACCESS_TOKEN, adv_id);
        dataManager.getServiceData(api_getCount);
    }

    private void initAdapter() {
        list = new ArrayList<>();
        adapterMyadvGetCount = new AdapterMyadvGetCount(this, list);
        refresh_recycler.setAdapter(adapterMyadvGetCount);
        refresh_recycler.setCanLoadMore(false);
        refresh_recycler.setCanRefresh(false);
    }

    private void initView() {
        initBackTopBar(getResources().getString(R.string.getcount));
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        iv_stroke = (ImageView) findViewById(R.id.iv_stroke);
        //0-255透明值
        iv_stroke.setAlpha(200);
        tv_begintime = (TextView) findViewById(R.id.tv_begintime);
        tv_end_time = (TextView) findViewById(R.id.tv_endtime);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_get_num = (TextView) findViewById(R.id.tv_get_num);
        tv_total_num = (TextView) findViewById(R.id.tv_total_num);
        tv_get_amount = (TextView) findViewById(R.id.tv_get_amount);
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);
        refresh_recycler = (PullToRefreshRecyclerView) findViewById(R.id.refresh_recycler);
        refresh_recycler.setOnRefreshListener(this);
    }


    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_GetCount.TAG)) {
                    cancelLoadingDialog();
                    LogUtils.e("统计详情****" + json);
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(ActivityGetCount.this, refresh_recycler);
                        return;
                    }
                    BeanGetCountList beanGetCountList = (BeanGetCountList) JsonObjUItils.fromJson(json, BeanGetCountList.class);
                    List<BeanGetCount> acccepted_list = beanGetCountList.getAcccepted_list();
                    if (acccepted_list != null && acccepted_list.size() > 0) {
                        LogUtils.e("list.size===" + list.size());
                        loadSize = acccepted_list.size();
                        list.addAll(acccepted_list);
                        adapterMyadvGetCount.notifyDataSetChanged();
                        refresh_recycler.setCanLoadMore(false);
                    } else {
                        ToastUtils.showShort(ActivityGetCount.this, "已加载全部");
                        refresh_recycler.setCanLoadMore(false);
                    }
                    refresh_recycler.Finish();

                    if (beanGetCountList.getInfo() != null) {
                        BeanMyadv info = beanGetCountList.getInfo();
                        if (info.getPub_begin_time() != null) {
                            tv_begintime.setText(DateFormatUtils.getTimeHStr(Long.parseLong(info.getPub_begin_time().trim()) * 1000, DateFormatUtils.format));
                        }
                        if (info.getPub_end_time() != null) {
                            tv_end_time.setText(DateFormatUtils.getTimeHStr(Long.parseLong(info.getPub_end_time().trim()) * 1000, DateFormatUtils.format));
                        }
                        if (info.getTitle() != null) {
                            tv_description.setText(info.getTitle().trim());
                        }

                        if (info.getBonus_accepted_num() != null) {
                            tv_get_num.setText(info.getBonus_accepted_num());
                        }

                        if (info.getBonus_total_num() != null) {
                            tv_total_num.setText("/" + info.getBonus_total_num());
                        }

                        if (info.getBonus_accepted_amount() != null) {
                            tv_get_amount.setText(NumberUtils.getTwoPoint(info.getBonus_accepted_amount().trim()));
                        }

                        if (bouns_total_amount != null) {
                            tv_total_amount.setText("/" + NumberUtils.getTwoPoint(bouns_total_amount));
                        }
//                        if (info.getBonus_total_amount() != null) {
//                            tv_total_amount.setText("/" + NumberUtils.getTwoPoint(info.getBonus_total_amount()));
//                        }

                        if (info.getContent() != null) {
                            if (info.getContent().getImages() != null && info.getContent().getImages().size() > 0) {
                                BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(ActivityGetCount.this);
                                bitmapUtils.display(iv_logo, info.getContent().getImages().get(0));
                            }
                        }
                    }
                    refresh_recycler.Finish();
                }
            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
                if (tag.equals(API_GetCount.TAG)) {
                    refresh_recycler.FinishFailed();
                    if (isLoadMore) {
                        if (offset > loadSize) {
                            offset = offset - loadSize;
                        }
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
        API_GetCount api_getCount = new API_GetCount(ACCESS_TOKEN, adv_id);
        dataManager.getServiceData(api_getCount);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        isLoadMore = false;
    }
}
