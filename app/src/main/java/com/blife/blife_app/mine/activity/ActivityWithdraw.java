package com.blife.blife_app.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.mine.api.API_Balance;
import com.blife.blife_app.mine.bean.BeanAmount;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.wx.WXStart;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.NumberUtils;
import com.blife.blife_app.utils.util.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Random;

/**
 * Created by Somebody on 2016/9/18.
 */
public class ActivityWithdraw extends BaseActivity implements View.OnClickListener {

    private TextView withdraw_tv_balance;
    private RelativeLayout withdraw_rl_alipay, withdraw_rl_wxpay;
    private String balance = "";
    private IWXAPI api;
    private int identify = 0;
    private LinearLayout withdraw_lin_identify, no_identify_lin;
    //    private LinearLayout noidentify_lin_go;
//    private TextView noidentify_tv;
    private TextView withdarw_tv_config;
    private WXStart wxStart;
//    private LinearLayout withdraw_lin_no_iddentify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        showLoadingDialog();
        wxStart = WXStart.getInstance(ActivityWithdraw.this);
//        initPre();
        initView();
        initCilck();
    }
//
//    private void initPre() {
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            //0.未认证，1.审核中2。认证通过3.认证拒绝4.其他
//            identify = bundle.getInt(Constants.IS_IDENTIFY, 0);
//        }
//    }

    @Override
    protected void onResume() {
        initBalance();
        super.onResume();
        LogUtils.e("onResume");
    }

    private void initBalance() {
        API_Balance api_balance = new API_Balance(ACCESS_TOKEN);
        dataManager.getServiceData(api_balance);

    }

    private void initCilck() {
        withdraw_rl_alipay.setOnClickListener(this);
        withdraw_rl_wxpay.setOnClickListener(this);
    }

    private void initView() {
        initBackTopBar(R.string.withdraw, R.string.income_expense);
        withdraw_lin_identify = (LinearLayout) findViewById(R.id.withdraw_lin_identify);
        no_identify_lin = (LinearLayout) findViewById(R.id.no_identify_lin);
//        withdraw_lin_no_iddentify = (LinearLayout) findViewById(R.id.withdraw_lin_no_iddentify);
//        noidentify_lin_go = (LinearLayout) findViewById(R.id.noidentify_lin_go);
//        noidentify_tv = (TextView) findViewById(R.id.noidentify_tv);

//        //0.未认证，1.审核中2。认证通过3.认证拒绝4.其他
//        if (identify == 2) {
//            withdraw_lin_identify.setVisibility(View.VISIBLE);
//            withdraw_lin_no_iddentify.setVisibility(View.GONE);
//            noidentify_lin_go.setEnabled(false);
//        } else {
//            withdraw_lin_identify.setVisibility(View.GONE);
//            withdraw_lin_no_iddentify.setVisibility(View.VISIBLE);
//            noidentify_tv.setText(getString(R.string.identify_withdraw));
//            if (identify == 0) {
//                noidentify_lin_go.setEnabled(true);
//            } else if (identify == 1) {
//                noidentify_lin_go.setEnabled(false);
//            } else if (identify == 3) {
//                noidentify_lin_go.setEnabled(true);
//            }
//        }


        withdraw_tv_balance = (TextView) findViewById(R.id.withdraw_tv_balance);
        withdarw_tv_config = (TextView) findViewById(R.id.withdarw_tv_config);
        withdarw_tv_config.setText(shardPreferName.getStringData(Constants.TRANSFER_REQUEST, ""));
        withdraw_rl_alipay = (RelativeLayout) findViewById(R.id.withdraw_rl_alipay);
        withdraw_rl_wxpay = (RelativeLayout) findViewById(R.id.withdraw_rl_wxpay);
    }

    @Override
    protected void TopRightClick() {
        super.TopRightClick();
        startActivity(ActivityIncomeExpense.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdraw_rl_alipay:
                LogUtils.e("balance===" + balance);
                Bundle bundleali = new Bundle();
                bundleali.putString(Constants.BALANCE, balance);
                startActivity(ActivityAliPay.class, bundleali);
                break;
            case R.id.withdraw_rl_wxpay:
                //        微信的openid获取
                wxStart.startWx();
                break;
//            case R.id.noidentify_lin_go:
//                startFinishActivity(ActivityPassIdentify.class);
//                break;
        }
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_Balance.TAG)) {
                    cancelLoadingDialog();
                    LogUtils.e("获取我的余额**" + json);
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(ActivityWithdraw.this);
                        return;
                    }
                    BeanAmount beanAmount = (BeanAmount) JsonObjUItils.fromJson(json, BeanAmount.class);
                    if (beanAmount.getBalance() != null && beanAmount.getBalance().getWallet_balance() != null) {
                        withdraw_tv_balance.setText("￥" + NumberUtils.getTwoPoint(beanAmount.getBalance().getWallet_balance()));
                    }
                    balance = NumberUtils.getTwoPoint(beanAmount.getBalance().getWallet_balance());
                }
            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
                if (message != null) {
                    Message message1 = new Message();
                    message1.obj = message;
                    mHandler.sendMessageDelayed(message1, 500);
                }
            }
        };
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message = (String) msg.obj;
            LogUtils.e("message****" + message);
            JsonObjUItils.getJsonCode(message, withdraw_rl_alipay, ActivityWithdraw.this, R.string.get_filer);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
//        wxStart.stopWx();
    }

}

