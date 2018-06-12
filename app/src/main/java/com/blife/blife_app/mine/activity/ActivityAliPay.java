package com.blife.blife_app.mine.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.adapter.AdapterMyadvFinished;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.mine.adapter.AdapterNotice;
import com.blife.blife_app.mine.api.API_Withdraw;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.pulltorefresh.baseview.PullToRefreshLayout;
import com.blife.blife_app.tools.pulltorefresh.pulltorefreshview.PullToRefreshRecyclerView;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.NumberUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/9/18.
 */
public class ActivityAliPay extends BaseActivity implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener {

    private EditText alipay_et_name, alipay_et_account, alipay_et_balance;
    private TextView alipay_tv_withdraw_count, alipay_tv_commit;
    private String balance = "";
    private PullToRefreshRecyclerView refresh_recycler;
    private AdapterNotice adapterNotice;
    private List<String> transferList;
    private String transferMinMoney;
    private int transferLimitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        initPre();
        initView();
        initClick();
        initAdapter();
    }

    //填写注意事项
    private void initAdapter() {
        adapterNotice = new AdapterNotice(this, transferList);
        refresh_recycler.setCanRefresh(false);
        refresh_recycler.setCanLoadMore(false);
        refresh_recycler.setAdapter(adapterNotice);
        refresh_recycler.setBackgroundResource(R.color.color_main_backround);
    }

    private void initPre() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(Constants.BALANCE)) {
            balance = extras.getString(Constants.BALANCE);
        }
        transferList = shardPreferName.getStringList(Constants.TRANSFER_REQUEST_NOTICE);
        transferMinMoney = shardPreferName.getStringData(Constants.TRANSFER_MIN_MONEY, "");
        transferLimitTime = shardPreferName.getIntData(Constants.TRANSFER_LIMIT_TIME, 0);
    }

    private void initClick() {
        alipay_tv_commit.setOnClickListener(this);
        textListener();
    }

    private void textListener() {
        alipay_et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(alipay_et_name.getText().toString().trim()) && !TextUtils.isEmpty(alipay_et_account.getText().toString().trim()) && !TextUtils.isEmpty(alipay_et_balance.getText().toString().trim())) {
                    alipay_tv_commit.setBackgroundResource(R.drawable.selector_myadv_send);
                    alipay_tv_commit.setEnabled(true);
                } else {
                    alipay_tv_commit.setBackgroundResource(R.drawable.shape_myadv_send_defalut);
                    alipay_tv_commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        alipay_et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(alipay_et_name.getText().toString().trim()) && !TextUtils.isEmpty(alipay_et_account.getText().toString().trim()) && !TextUtils.isEmpty(alipay_et_balance.getText().toString().trim())) {
                    alipay_tv_commit.setBackgroundResource(R.drawable.selector_myadv_send);
                    alipay_tv_commit.setEnabled(true);
                } else {
                    alipay_tv_commit.setBackgroundResource(R.drawable.shape_myadv_send_defalut);
                    alipay_tv_commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        alipay_et_balance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(alipay_et_name.getText().toString().trim()) && !TextUtils.isEmpty(alipay_et_account.getText().toString().trim()) && !TextUtils.isEmpty(alipay_et_balance.getText().toString().trim())) {
                    alipay_tv_commit.setBackgroundResource(R.drawable.selector_myadv_send);
                    alipay_tv_commit.setEnabled(true);
                } else {
                    alipay_tv_commit.setBackgroundResource(R.drawable.shape_myadv_send_defalut);
                    alipay_tv_commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        initBackTopBar(R.string.alipay);
        refresh_recycler = (PullToRefreshRecyclerView) findViewById(R.id.refresh_recycler);

        alipay_et_name = (EditText) findViewById(R.id.alipay_et_name);
        alipay_et_account = (EditText) findViewById(R.id.alipay_et_account);
        alipay_et_balance = (EditText) findViewById(R.id.alipay_et_balance);
        LogUtils.e("balance====" + balance);
        alipay_et_balance.setHint("可提现金额" + balance);
        alipay_tv_withdraw_count = (TextView) findViewById(R.id.alipay_tv_withdraw_count);
        alipay_tv_withdraw_count.setText(shardPreferName.getStringData(Constants.TRANSFER_REQUEST, ""));
        alipay_tv_commit = (TextView) findViewById(R.id.alipay_tv_commit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alipay_tv_commit:
//
//                if (StringUtils.isImpty(alipay_et_name.getText().toString())) {
//                    showFailedDialog("未添加收款人姓名", alipay_tv_commit);
//                    return;
//                }
//                if (StringUtils.isImpty(alipay_et_account.getText().toString())) {
//                    showFailedDialog("未添加支付宝账号", alipay_tv_commit);
//                    return;
//                }
//                if (StringUtils.isImpty(alipay_et_balance.getText().toString())) {
//                    showFailedDialog("未添加提现金额", alipay_tv_commit);
//                    return;
//                }
                //还有一种提现小于2元的情况，等赵总
                if (Double.parseDouble(balance) < Double.parseDouble(alipay_et_balance.getText().toString().trim())) {
                    showFailedDialog("账户余额不足", alipay_tv_commit);
                    return;
                }

                if (Double.parseDouble(alipay_et_balance.getText().toString().trim()) * 100 < Double.parseDouble(transferMinMoney)) {
                    showFailedDialog("提现金额不低于" + NumberUtils.getTwoPoint(transferMinMoney) + "元", alipay_tv_commit);
                    return;
                }

                showLoadingDialog();
                API_Withdraw api_withdraw = new API_Withdraw(ACCESS_TOKEN, 2, alipay_et_account.getText().toString().trim(), alipay_et_name.getText().toString().trim(), alipay_et_balance.getText().toString().trim());
                dataManager.getServiceData(api_withdraw);
                break;

        }
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_Withdraw.TAG)) {
                    LogUtils.e("支付宝提现****" + json);
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(ActivityAliPay.this);
                        return;
                    }
                    ToastUtils.showShort(ActivityAliPay.this, "平台已受理");
                    finish();
                }
            }

            @Override
            public void onError(Object tag, String message) {
//                if (message != null)
//                    ToastUtils.showCenterShort(ActivityAliPay.this, JsonObjUItils.getERRORJsonDetail(message));
                cancelLoadingDialog();
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




























