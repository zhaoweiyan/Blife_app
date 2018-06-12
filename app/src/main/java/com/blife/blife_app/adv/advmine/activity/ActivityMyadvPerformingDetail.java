package com.blife.blife_app.adv.advmine.activity;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.api.API_MyadvDetail;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvDetail;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv;
import com.blife.blife_app.adv.advsend.activity.ActivityPayment;
import com.blife.blife_app.adv.advsend.activity.ActivitySendAdv;
import com.blife.blife_app.adv.advsend.api.API_SubmitAdv;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.tools.DateUtils;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.rollviewpager.TurnImagePager;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.NumberUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/8/29.
 */
public class ActivityMyadvPerformingDetail extends BaseDetailActivity implements View.OnClickListener {


    private TextView tv_send_myadv;
    private String amount;
    private LinearLayout lin_get_count;
    //    private TextView tv_bouns_total_get;
//    private TextView tv_bouns_total_acount;
    private LinearLayout lin_remain_money;
    private TextView tv_bouns_quit_money;
//    private TextView tv_get_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myadv_prepare_detail);
        activityTask.addFinishActivity(this);
        initView();
        init();
        initClick();
    }

    private void initView() {
        initBackTopBar(R.string.adv_detail);

        //到达情况
        lin_get_count = (LinearLayout) findViewById(R.id.lin_get_count);
        lin_get_count.setVisibility(View.GONE);
//        tv_get_count = (TextView) findViewById(R.id.tv_get_count);
//        tv_bouns_total_get = (TextView) findViewById(R.id.tv_bouns_total_get);
//        tv_bouns_total_acount = (TextView) findViewById(R.id.tv_bouns_total_acount);

        //退回金额
        lin_remain_money = (LinearLayout) findViewById(R.id.lin_remain_money);
        lin_remain_money.setVisibility(View.GONE);
        tv_bouns_quit_money = (TextView) findViewById(R.id.tv_bouns_quit_money);
        //立即发布按钮
        tv_send_myadv = (TextView) findViewById(R.id.tv_send_myadv);
        tv_send_myadv.setText("再次发布");
    }

    private void initClick() {
        tv_send_myadv.setOnClickListener(this);
//        tv_get_count.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_myadv:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CREATE_ADV_ID, adv_id);
                bundle.putInt(Constants.ADV_CURRENT_TYPE, Constants.ADV_CURRENT_TYPE_AGAIN);
                startActivity(ActivitySendAdv.class, bundle);
                break;
            case R.id.tv_get_count:
                Bundle bund = new Bundle();
                bund.putString(Constants.ADV_ID, adv_id);
                LogUtils.e("adv_id****" + adv_id);
                startActivity(ActivityGetCount.class, bund);
                break;
        }
    }

    @Override
    protected void getAmount(String amount) {
        super.getAmount(amount);
        this.amount = amount;
    }

    @Override
    protected void initGetData(BeanMyadv beanMyadv) {
        super.initGetData(beanMyadv);
        LogUtils.e("beanMyadv****" + beanMyadv);
//        if (beanMyadv != null) {
//            if (beanMyadv.getBonus_accepted_num() != null && beanMyadv.getBonus_total_num() != null) {
//                tv_bouns_total_get.setText(beanMyadv.getBonus_accepted_num() + "/" + beanMyadv.getBonus_total_num());
//            } else if (beanMyadv.getBonus_total_num() != null) {
//                tv_bouns_total_get.setText(0 + "/" + beanMyadv.getBonus_total_num());
//            }
//            if (beanMyadv.getBonus_total_amount() != null) {
//                if (beanMyadv.getBonus_accepted_amount() != null) {
//                    tv_bouns_total_acount.setText("￥" + NumberUtils.getTwoPoint(beanMyadv.getBonus_accepted_amount()) + "/" + "￥" + NumberUtils.getTwoPoint(beanMyadv.getBonus_total_amount()));
//                } else {
//                    tv_bouns_total_acount.setText("￥" + 0 + "/" + "￥" + NumberUtils.getTwoPoint(beanMyadv.getBonus_total_amount()));
//                }
//            } else {
//                tv_bouns_total_acount.setText("￥" + "0");
//            }
//        }
    }
}























