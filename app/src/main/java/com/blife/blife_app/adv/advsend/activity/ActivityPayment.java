package com.blife.blife_app.adv.advsend.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advsend.api.API_Payment;
import com.blife.blife_app.adv.advsend.bean.BeanPay;
import com.blife.blife_app.adv.advsend.bean.BeanWeChatPay;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.base.api.API_GetServerTime;
import com.blife.blife_app.base.bean.BeanServerTime;
import com.blife.blife_app.bonus.bean.BeanMessageEvent;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.payment.alipay.AliPayUtil;
import com.blife.blife_app.tools.payment.alipay.InterfaceAliPayCallBack;
import com.blife.blife_app.tools.payment.wechat.WeChatPayUtil;
import com.blife.blife_app.tools.view.DialogPopWindows;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.NetWorkUtil;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;
import com.blife.blife_app.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by w on 2016/9/5.
 */
public class ActivityPayment extends BaseActivity implements View.OnClickListener, InterfaceAliPayCallBack {

    private LinearLayout lin_payment_alipay, lin_payment_wechat;
    private Button button_confirm;
    private CheckBox checkBox_alipay, checkBox_wechat;
    private TextView tv_bonus_amount;

    //广告ID
    private String ADV_ID;
    private long startTime, endTime;
    //支付金额
    private long PAY_AMOUNT;

    //支付方式 微信支付: 1, 支付宝: 2
    private int PAY_TYPE;
    private int PAY_TYPE_ALIPAY = 2, PAY_TYPE_WECHAT = 1;

    //支付
    private AliPayUtil aliPay;
    private WeChatPayUtil weChatPay;

    //弹窗
    private DialogPopWindows popWindows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initBackTopBar(R.string.payment_title);
        initView();
        initData();
        activityTask.FinishAllAddActivity();
        activityTask.addFinishActivity(this);
    }

    private void initView() {
        lin_payment_alipay = (LinearLayout) findViewById(R.id.lin_payment_alipay);
        lin_payment_wechat = (LinearLayout) findViewById(R.id.lin_payment_wechat);
        button_confirm = (Button) findViewById(R.id.button_payment_confirm);
        checkBox_alipay = (CheckBox) findViewById(R.id.checkbox_payment_alipay);
        checkBox_wechat = (CheckBox) findViewById(R.id.checkbox_payment_wechat);
        tv_bonus_amount = (TextView) findViewById(R.id.tv_bonus_amount);
        lin_payment_alipay.setOnClickListener(this);
        lin_payment_wechat.setOnClickListener(this);
        button_confirm.setOnClickListener(this);
        selectPaymentType(true);
        popWindows = new DialogPopWindows(instance);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ADV_ID = bundle.getString(Constants.CREATE_ADV_ID, "");
            PAY_AMOUNT = bundle.getLong(Constants.CREATE_ADV_TOTAL_AMOUNT);
            tv_bonus_amount.setText("￥" + StringUtils.dealMoney(PAY_AMOUNT + "", 100));
            startTime = bundle.getLong(Constants.ADV_CURRENT_START_TIME, 0);
            endTime = bundle.getLong(Constants.ADV_CURRENT_END_TIME, 0);
            String adv_title = bundle.getString(Constants.ADV_CURRENT_TITLE);
            String adv_first_image = bundle.getString(Constants.ADV_CURRENT_FIRST_IMAGE);
            L.e("TAG", "第一张图片PAY：" + adv_first_image);
            shardPreferName.setStringData(Constants.PAYMENT_AMOUNT, "￥" + StringUtils.dealMoney(PAY_AMOUNT + "", 100));
            shardPreferName.setStringData(Constants.SHARE_INFO_PAY_SUCCESS_ADV_TITLE, adv_title);
            shardPreferName.setStringData(Constants.SHARE_INFO_PAY_SUCCESS_ADV_FIRST_IMAGE, adv_first_image);
        }
        aliPay = new AliPayUtil(baseActivity, this);
        weChatPay = new WeChatPayUtil(instance, Constants.WEIXIN_APPID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_payment_alipay:
                selectPaymentType(true);
                break;
            case R.id.lin_payment_wechat:
                selectPaymentType(false);
                break;
            case R.id.button_payment_confirm:
                getPayOrder();
                break;
        }
    }

    /**
     * 获取支付订单
     */
    private void getPayOrder() {
        if (!NetWorkUtil.isNetwork(instance)) {
            ToastUtils.showShort(instance, R.string.toast_network_Available);
            return;
        }
        if (PAY_TYPE == PAY_TYPE_WECHAT && !weChatPay.isPaySupported()) {
            ToastUtils.showShort(instance, R.string.payment_wechatpay_no_supported);
            return;
        }
        if (startTime != 0 && endTime != 0) {
            showLoadingDialog();
            API_GetServerTime api_getServerTime = new API_GetServerTime(ACCESS_TOKEN);
            dataManager.getServiceData(api_getServerTime);
        } else {
            ToastUtils.showShort(instance, "广告投放时间获取失败");
        }
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                //{"code":"00000001","message":"OK","detail":"OK","data":{"time":"Mon, 12 Sep 2016 14:11:09 +0800","timestamp":1473660669}}
                if (tag.equals(API_GetServerTime.TAG)) {
                    BeanServerTime beanServerTime = (BeanServerTime) JsonObjUItils.fromJson(json, BeanServerTime.class);
                    checkTime(beanServerTime.getTimestamp());
                }
                if (tag.equals(API_Payment.TAG)) {
                    cancelLoadingDialog();
                    pay(json);
                }
            }

            @Override
            public void onError(Object tag, String message) {
                if (tag.equals(API_GetServerTime.TAG)) {
                    cancelLoadingDialog();
                }
                if (tag.equals(API_Payment.TAG)) {
                    if (JsonObjUItils.getERRORJsonCode(message).equals("13000040")) {
                        showFailedDialog(getString(R.string.payment_out_starttime), button_confirm);
                    }
                    cancelLoadingDialog();
                }
                L.e("TAG", "支付订单结果onError：" + message);
            }
        };
    }

    /**
     * 获取订单信息
     */
    private void checkTime(long currentTime) {
        if (startTime - 900 > currentTime) {//提前15分钟--可支付
            API_Payment api_payment = new API_Payment(ACCESS_TOKEN, ADV_ID, PAY_TYPE);
            dataManager.getServiceData(api_payment);
        } else {
            cancelLoadingDialog();
            showFailedDialog(getString(R.string.payment_out_starttime), button_confirm);
        }
    }

    private void pay(String json) {
        if (PAY_TYPE == PAY_TYPE_ALIPAY) {
            BeanPay beanPay = (BeanPay) JsonObjUItils.fromJson(json, BeanPay.class);
            L.e("TAG", "支付宝订单结果onSuccess：" + beanPay.getPay_uri());
            aliPay.pay(beanPay.getPay_uri());
            shardPreferName.setStringData(Constants.PAYMENT_TYPE, getString(R.string.payment_alipay));
        } else {
            //{"code":"00000001","message":"OK","detail":"OK","data":{"pay_info":{"appid":"wx36e34c361a0b2b3f","partnerid":"1385215002","prepayid":"wx20160906154020816fbb39db0584320539",
            // "package":"Sign=WXPay","noncestr":"1144k4gnjc8j706dq329e3ntz94j4b1w","timestamp":1473147620,"sign":"68AFAA1C8431273C498063DE637F7BE0"}}}
            BeanWeChatPay bean = (BeanWeChatPay) JsonObjUItils.fromJson(json, BeanWeChatPay.class);
            weChatPay.createPayReq(
                    bean.getPay_info().getPartnerid(),
                    bean.getPay_info().getPrepayid(),
                    bean.getPay_info().getPack(),
                    bean.getPay_info().getNoncestr(),
                    bean.getPay_info().getTimestamp(),
                    bean.getPay_info().getSign());
            shardPreferName.setStringData(Constants.PAYMENT_TYPE, getString(R.string.payment_wechat));
            weChatPay.pay();
            L.e("TAG", "微信订单结果onSuccess：" + json);
        }
    }

    /**
     * 选择支付方式
     *
     * @param default_pay
     */
    private void selectPaymentType(boolean default_pay) {
        checkBox_alipay.setChecked(default_pay);
        checkBox_wechat.setChecked(!default_pay);
        if (default_pay) {
            PAY_TYPE = PAY_TYPE_ALIPAY;
        } else {
            PAY_TYPE = PAY_TYPE_WECHAT;
        }
    }


    @Override
    public void aliPaySuccess() {
        shardPreferName.setIntData(Constants.PAYMENT_RESULT, Constants.PAYMENT_SUCCESS);
        startActivity(WXPayEntryActivity.class);
    }

    @Override
    public void aliPayFailed() {
        shardPreferName.setIntData(Constants.PAYMENT_RESULT, Constants.PAYMENT_FAILED);
        startActivity(WXPayEntryActivity.class);
    }

    @Override
    public void aliPayCancel() {
        shardPreferName.setIntData(Constants.PAYMENT_RESULT, Constants.PAYMENT_FAILED);
        startActivity(WXPayEntryActivity.class);
    }


    @Override
    protected void TopBack() {
        super.TopBack();
        RefreshData(0x300);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            RefreshData(0x300);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void RefreshData(int messageEventType) {
        BeanMessageEvent messageEvent = new BeanMessageEvent(messageEventType);
        EventBus.getDefault().post(messageEvent);
    }
}
