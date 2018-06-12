package com.blife.blife_app.adv.advmine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.adapter.AdapterMyadvPrepare;
import com.blife.blife_app.adv.advmine.adapter.AdapterMyadvRefund;
import com.blife.blife_app.adv.advmine.api.API_MyadvDetail;
import com.blife.blife_app.adv.advmine.api.API_MyadvRefundSchduel;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvRefund;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvRefundList;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullToRefreshLayout;
import com.blife.blife_app.tools.pulltorefresh.pulltorefreshview.PullToRefreshRecyclerView;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.NumberUtils;
import com.blife.blife_app.utils.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/9/6.
 */
public class ActivityRefundSchedule extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private String adv_id = "-1";
    private PullToRefreshRecyclerView refresh_recycler;
    private ArrayList<BeanMyAdvRefund> list;
    private AdapterMyadvRefund adapterMyadvRefund;
    private TextView tv_refund_explain, tv_refund_punname, tv_refund_total_amount;
    private String phone;
    private TextView tv_refund_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_schdule);
        initPre();
        initView();
        iniClick();
        initAdapter();
        initRefundDetail();
    }


    private void initAdapter() {
        list = new ArrayList<>();
        adapterMyadvRefund = new AdapterMyadvRefund(ActivityRefundSchedule.this, list);
        refresh_recycler.setAdapter(adapterMyadvRefund);
        refresh_recycler.setCanLoadMore(false);
        refresh_recycler.setCanRefresh(false);
    }

    private void initRefundDetail() {
        //广告详情
        API_MyadvRefundSchduel api_myadvRefundSchduel = new API_MyadvRefundSchduel(ACCESS_TOKEN, adv_id);
        dataManager.getServiceData(api_myadvRefundSchduel);
    }

    private void initPre() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ADV_ID)) {
                adv_id = bundle.getString(Constants.ADV_ID);
            }
        }
        phone = shardPreferName.getStringData(Constants.CONTACT_PHONE, "");
    }

    private void initView() {
        initBackTopBarColor(R.string.refund_schedule, R.color.colorWhite, R.color.color_reset, R.mipmap.findpwd_back);
        tv_refund_explain = (TextView) findViewById(R.id.tv_refund_explain);
        tv_refund_punname = (TextView) findViewById(R.id.tv_refund_punname);
        tv_refund_total_amount = (TextView) findViewById(R.id.tv_refund_total_amount);
        tv_refund_phone = (TextView) findViewById(R.id.tv_refund_phone);
        tv_refund_phone.setText("拨打客服电话" + phone);

        refresh_recycler = (PullToRefreshRecyclerView) findViewById(R.id.refresh_recycler);
        refresh_recycler.setOnRefreshListener(this);
    }


    private void iniClick() {
        tv_refund_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringUtils.getNumber(phone)));
                startActivity(intent);
            }
        });
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_MyadvRefundSchduel.TAG)) {
                    LogUtils.e("退款详情****" + json);
                    BeanMyAdvRefundList beanMyAdvRefundList = (BeanMyAdvRefundList) JsonObjUItils.fromJson(json, BeanMyAdvRefundList.class);
                    List<BeanMyAdvRefund> beanMyAdvRefund = beanMyAdvRefundList.getList();
                    if (beanMyAdvRefund != null && beanMyAdvRefund.size() > 0) {
                        list.addAll(beanMyAdvRefund);
                        adapterMyadvRefund.notifyDataSetChanged();
                        if (list.get(0).getRefund_status_str().trim() != null) {
                            tv_refund_explain.setText(list.get(0).getRefund_status_str().trim());
                        } else {
                            tv_refund_explain.setText("未设置");
                        }

                        if (list.get(0).getRefund_total_amount() != null) {
                            tv_refund_total_amount.setText("￥" + NumberUtils.getTwoPoint(list.get(0).getRefund_total_amount()));
                        } else {
                            tv_refund_total_amount.setText("未设置");
                        }
                        if (beanMyAdvRefundList.getPub_name() != null) {
                            tv_refund_punname.setText(beanMyAdvRefundList.getPub_name());
                        } else {
                            tv_refund_punname.setText("未设置");
                        }
                    }
                }

            }

            @Override
            public void onError(Object tag, String message) {

            }
        };
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
