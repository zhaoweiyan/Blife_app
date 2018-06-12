package com.blife.blife_app.adv.advmine.activity;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.api.API_MyAdvPublish_Address;
import com.blife.blife_app.adv.advmine.api.API_MyadvDetail;
import com.blife.blife_app.adv.advmine.api.API_Myadv_BonusSet;
import com.blife.blife_app.adv.advmine.bean.BeanBonusSetInfo;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvDetail;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvPublishAddress;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvPublishAddressList;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv;
import com.blife.blife_app.adv.advmine.bean.BeanMyadvDetailBonus;
import com.blife.blife_app.adv.advsend.activity.ActivityPayment;
import com.blife.blife_app.adv.advsend.api.API_SubmitAdv;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.index.bean.BeanHomeTurnImages;
import com.blife.blife_app.index.bean.BeanHomeTurnImagesList;
import com.blife.blife_app.tools.DateUtils;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.rollviewpager.TurnImagePager;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/8/29.
 */
public class ActivityMyadvPrepareDetail extends BaseDetailActivity implements View.OnClickListener {

    private TextView tv_send_myadv;
    private String amount;
    private LinearLayout lin_get_count;
    private LinearLayout lin_remain_money;
    private View lin_v;
    private LinearLayout lin_button;
    private String endTime, startTime;
    private String adv_title, adv_first_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myadv_prepare_detail);
        activityTask.addFinishActivity(this);
        initPre();
        initView();
        init();
        initClick();
    }

    private boolean isPreView = false;

    private void initPre() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.ADV_CURRENT_TYPE)) {
            if (bundle.getInt(Constants.ADV_CURRENT_TYPE) == Constants.ADV_CURRENT_TYPE_CREATE) {
                isPreView = true;
            }
        }
    }


    private void initView() {
        initBackTopBar(R.string.adv_detail);

        //到达情况
        lin_get_count = (LinearLayout) findViewById(R.id.lin_get_count);
        lin_get_count.setVisibility(View.GONE);

        //退回金额
        lin_remain_money = (LinearLayout) findViewById(R.id.lin_remain_money);
        lin_remain_money.setVisibility(View.GONE);
        //立即发布按钮
        tv_send_myadv = (TextView) findViewById(R.id.tv_send_myadv);
        lin_v = (View) findViewById(R.id.lin_v);
        lin_button = (LinearLayout) findViewById(R.id.lin_button);
        tv_send_myadv.setText("立即付款");
        if (isPreView == true) {
            lin_button.setVisibility(View.GONE);
            lin_v.setVisibility(View.GONE);
        } else {
            lin_button.setVisibility(View.VISIBLE);
            lin_v.setVisibility(View.VISIBLE);
        }
    }

    private void initClick() {
        tv_send_myadv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_myadv:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CREATE_ADV_ID, adv_id);
                bundle.putString(Constants.ADV_CURRENT_TITLE, adv_title);
                bundle.putString(Constants.ADV_CURRENT_FIRST_IMAGE, adv_first_image);
                bundle.putLong(Constants.CREATE_ADV_TOTAL_AMOUNT, Long.parseLong(totalAmount));
                bundle.putLong(Constants.ADV_CURRENT_START_TIME, Long.parseLong(startTime));
                bundle.putLong(Constants.ADV_CURRENT_END_TIME, Long.parseLong(endTime));
                startActivity(ActivityPayment.class, bundle);
                break;
        }
    }

    @Override
    protected void initGetData(BeanMyadv beanMyadv) {
        super.initGetData(beanMyadv);
        adv_title = beanMyadv.getTitle();
        adv_first_image = beanMyadv.getContent().getImages().get(0);
    }

    @Override
    protected void getAmount(String amount) {
        super.getAmount(amount);
        this.amount = amount;
    }

    @Override
    protected void getTime(String startTime, String endTime) {
        super.getTime(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }
}