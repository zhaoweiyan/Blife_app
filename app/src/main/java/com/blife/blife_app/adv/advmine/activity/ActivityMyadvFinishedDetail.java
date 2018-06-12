package com.blife.blife_app.adv.advmine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv;
import com.blife.blife_app.adv.advsend.activity.ActivitySendAdv;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.NumberUtils;

/**
 * Created by Somebody on 2016/8/29.
 */
public class ActivityMyadvFinishedDetail extends BaseDetailActivity implements View.OnClickListener {
    private TextView tv_send_myadv;
    private LinearLayout lin_get_count;
    private TextView tv_bouns_total_get;
    private TextView tv_bouns_total_acount;
    private LinearLayout lin_remain_money;
    private TextView tv_bouns_quit_money;
    private TextView tv_get_count;
    private TextView tv_refund_num;
    private TextView tv_bouns_quit_person;
    private TextView tv_refund_amount;
    private TextView tv_bouns_accept_acount;
    private String bouns_total_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myadv_prepare_detail);
        activityTask.addFinishActivity(this);
        initView();
        init();
        initClick();
        initMyadvDetail();
    }


    private void initMyadvDetail() {
        //获取到达统计

    }

    private void initView() {
        initBackTopBar(R.string.adv_detail);
        //到达情况

        lin_get_count = (LinearLayout) findViewById(R.id.lin_get_count);
        lin_get_count.setVisibility(View.VISIBLE);
        tv_get_count = (TextView) findViewById(R.id.tv_get_count);
        tv_bouns_total_get = (TextView) findViewById(R.id.tv_bouns_total_get);
        tv_bouns_total_acount = (TextView) findViewById(R.id.tv_bouns_total_acount);
        tv_bouns_accept_acount = (TextView) findViewById(R.id.tv_bouns_accept_acount);

        //退回金额
        lin_remain_money = (LinearLayout) findViewById(R.id.lin_remain_money);
        lin_remain_money.setVisibility(View.GONE);
        tv_bouns_quit_money = (TextView) findViewById(R.id.tv_bouns_quit_money);
        tv_refund_num = (TextView) findViewById(R.id.tv_refund_num);
        tv_refund_amount = (TextView) findViewById(R.id.tv_refund_amount);
        tv_bouns_quit_person = (TextView) findViewById(R.id.tv_bouns_quit_person);

        //立即发布按钮
        tv_send_myadv = (TextView) findViewById(R.id.tv_send_myadv);
        tv_send_myadv.setText("再次发布");


    }

    private void initClick() {
        tv_send_myadv.setOnClickListener(this);
        tv_get_count.setOnClickListener(this);
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
                bund.putString(Constants.BOUNS_AMOUNT, bouns_total_amount);
                startActivity(ActivityGetCount.class, bund);
                break;
        }
    }

    @Override
    protected void getAcceptAmount(String accept) {
        super.getAcceptAmount(accept);
        bouns_total_amount =accept;
    }

    @Override
    protected void initGetData(BeanMyadv beanMyadv) {
        super.initGetData(beanMyadv);
        LogUtils.e("beanMyadv****" + beanMyadv);
        if (beanMyadv != null) {
            if (beanMyadv.getBonus_accepted_num() != null && beanMyadv.getBonus_total_num() != null) {
                tv_bouns_total_get.setText(beanMyadv.getBonus_accepted_num() + "/" + beanMyadv.getBonus_total_num());
            } else if (beanMyadv.getBonus_total_num() != null) {
                tv_bouns_total_get.setText(0 + "/" + beanMyadv.getBonus_total_num());
            }
            if (beanMyadv.getBonus_accepted_amount() != null) {
                tv_bouns_accept_acount.setText("￥" + NumberUtils.getTwoPoint(beanMyadv.getBonus_accepted_amount()));
            } else {
                tv_bouns_accept_acount.setText("￥" + 0);
            }

            if (beanMyadv.getStatus() != null && Integer.parseInt(beanMyadv.getStatus()) == 12) {
                if (beanMyadv.getRefund_status() != null) {
                    int refundStatus = Integer.parseInt(beanMyadv.getRefund_status());
                    if (refundStatus == 1 || refundStatus == 2 || refundStatus == 8 || refundStatus == 9) {
                        if (refundStatus == 9) {
                            tv_refund_num.setText("退回红包:");
                            tv_refund_amount.setText("退回金额");
                        } else {
                            tv_refund_num.setText("可退红包:");
                            tv_refund_amount.setText("可退金额:");
                        }
                        lin_remain_money.setVisibility(View.VISIBLE);
                        if (beanMyadv.getBonus_total_num() != null && beanMyadv.getBonus_accepted_num() != null) {
                            tv_bouns_quit_person.setText("" + (Long.parseLong(beanMyadv.getBonus_total_num()) - Long.parseLong(beanMyadv.getBonus_accepted_num())));
                        }
                        tv_bouns_quit_money.setText("￥" + NumberUtils.getTwoPoint(beanMyadv.getRefund_total_amount()));

                    } else {
                        lin_remain_money.setVisibility(View.GONE);
                        tv_get_count.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
}























