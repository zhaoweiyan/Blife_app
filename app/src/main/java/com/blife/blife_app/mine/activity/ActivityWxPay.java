package com.blife.blife_app.mine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.mine.adapter.AdapterNotice;
import com.blife.blife_app.mine.api.API_Balance;
import com.blife.blife_app.mine.api.API_Withdraw;
import com.blife.blife_app.mine.bean.BeanAmount;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.pulltorefresh.pulltorefreshview.PullToRefreshRecyclerView;
import com.blife.blife_app.tools.wx.WXStart;
import com.blife.blife_app.utils.configs.LogConfig;
import com.blife.blife_app.utils.logcat.LogcatManager;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.NumberUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * Created by Somebody on 2016/9/18.
 */
public class ActivityWxPay extends BaseActivity implements View.OnClickListener {
    private TextView wxpay_tv_nametag;
    private TextView wxpay_tv_withdraw_count;
    private TextView wxpay_tv_commit;
    private TextView wxpay_et_name;
    private String balance = "";
    private EditText wxpay_et_balance;
    private String openid = "";
    private List<String> transferList;
    private AdapterNotice adapterNotice;
    private PullToRefreshRecyclerView refresh_recycler;
    private String transferMinMoney;
    private int transferLimitTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay);
        showLoadingDialog();
        initPre();
        initView();
        initClick();
        initBalance();
        initAdapter();
    }

    //填写注意事项
    private void initAdapter() {
        adapterNotice = new AdapterNotice(this, transferList);
        refresh_recycler.setCanRefresh(false);
        refresh_recycler.setAdapter(adapterNotice);
        refresh_recycler.setBackgroundResource(R.color.color_main_backround);
        refresh_recycler.setCanLoadMore(false);
    }

    private void initBalance() {
        API_Balance api_balance = new API_Balance(ACCESS_TOKEN);
        dataManager.getServiceData(api_balance);

    }

    private void initPre() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(Constants.OPENID)) {
            openid = extras.getString(Constants.OPENID);
        }

        transferList = shardPreferName.getStringList(Constants.TRANSFER_REQUEST_NOTICE);
        transferMinMoney = shardPreferName.getStringData(Constants.TRANSFER_MIN_MONEY, "");
        transferLimitTime = shardPreferName.getIntData(Constants.TRANSFER_LIMIT_TIME, 0);
    }

    private void initClick() {
        wxpay_tv_commit.setOnClickListener(this);
        textListener();
    }


    private void textListener() {
        wxpay_et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(wxpay_et_name.getText().toString().trim()) && !TextUtils.isEmpty(wxpay_et_balance.getText().toString().trim())) {
                    wxpay_tv_commit.setBackgroundResource(R.drawable.selector_myadv_send);
                    wxpay_tv_commit.setEnabled(true);
                } else {
                    wxpay_tv_commit.setBackgroundResource(R.drawable.shape_myadv_send_defalut);
                    wxpay_tv_commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        wxpay_et_balance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(wxpay_et_name.getText().toString().trim()) && !TextUtils.isEmpty(wxpay_et_balance.getText().toString().trim())) {
                    wxpay_tv_commit.setBackgroundResource(R.drawable.selector_myadv_send);
                    wxpay_tv_commit.setEnabled(true);
                } else {
                    wxpay_tv_commit.setBackgroundResource(R.drawable.shape_myadv_send_defalut);
                    wxpay_tv_commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        initBackTopBar(R.string.wxplay);
        refresh_recycler = (PullToRefreshRecyclerView) findViewById(R.id.refresh_recycler);
        wxpay_tv_nametag = (TextView) findViewById(R.id.wxpay_tv_nametag);
        wxpay_tv_nametag.setText(openid);
        wxpay_et_name = (TextView) findViewById(R.id.wxpay_et_name);
        wxpay_et_balance = (EditText) findViewById(R.id.wxpay_et_balance);
        wxpay_tv_withdraw_count = (TextView) findViewById(R.id.wxpay_tv_withdraw_count);
        wxpay_tv_withdraw_count.setText(shardPreferName.getStringData(Constants.TRANSFER_REQUEST, ""));
        wxpay_tv_commit = (TextView) findViewById(R.id.wxpay_tv_commit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wxpay_tv_commit:

                if (StringUtils.isImpty(wxpay_et_name.getText().toString())) {
                    showFailedDialog("微信姓名不能为空", wxpay_tv_commit);
                    return;
                }
                if (StringUtils.isImpty(wxpay_et_balance.getText().toString())) {
                    showFailedDialog("请输入提现金额", wxpay_tv_commit);
                    return;
                }
                if (Double.parseDouble(balance) < Double.parseDouble(wxpay_et_balance.getText().toString().trim())) {
                    showFailedDialog("提现金额不足", wxpay_tv_commit);
                    return;
                }
                if (Double.parseDouble(wxpay_et_balance.getText().toString().trim()) * 100 < Double.parseDouble(transferMinMoney)) {
                    showFailedDialog("提现金额不低于" + NumberUtils.getTwoPoint(transferMinMoney) + "元", wxpay_tv_commit);
                    return;
                }
                showLoadingDialog();
                API_Withdraw api_withdraw = new API_Withdraw(ACCESS_TOKEN, 1, wxpay_tv_nametag.getText().toString().trim(), wxpay_et_name.getText().toString().trim(), wxpay_et_balance.getText().toString().trim());
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
                    LogUtils.e("微信提现****" + json);
                    ToastUtils.showShort(ActivityWxPay.this, "平台已受理");
                    LogcatManager.v(ActivityWxPay.this, "TAG", json, true);
                    cancelLoadingDialog();
                    finish();
                }
                if (tag.equals(API_Balance.TAG)) {
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(ActivityWxPay.this);
                        return;
                    }

                    BeanAmount beanAmount = (BeanAmount) JsonObjUItils.fromJson(json, BeanAmount.class);
                    if (beanAmount.getBalance() != null && beanAmount.getBalance().getWallet_balance() != null) {
                        wxpay_et_balance.setHint("可提现金额" + NumberUtils.getTwoPoint(beanAmount.getBalance().getWallet_balance()));
                        balance = NumberUtils.getTwoPoint(beanAmount.getBalance().getWallet_balance());
                    }
                }
            }

            @Override
            public void onError(Object tag, String message) {
//                if (message != null)
//                    ToastUtils.showCenterShort(ActivityWxPay.this,JsonObjUItils.getERRORJsonDetail(message));
                cancelLoadingDialog();
            }
        };
    }
}
