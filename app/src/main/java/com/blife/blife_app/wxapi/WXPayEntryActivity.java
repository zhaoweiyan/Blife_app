package com.blife.blife_app.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.bonus.bean.BeanMessageEvent;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.share.ShareUtils;
import com.blife.blife_app.utils.logcat.L;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler, View.OnClickListener {

    private IWXAPI api;
    private ImageView iv_pay_result_icon;
    private TextView tv_pay_result, tv_pay_result_hint;
    private Button button_back_advlist;
    private Button button_pay_success_share;
    private int pay_code = -1;
    private boolean isResume;
    private int messageEventType;

    private ShareUtils shareUtils;

    private boolean isPaySuccess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechatpay);
        initTopBar(R.string.payment_pay_result_title);
        initView();
        api = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APPID);
        api.handleIntent(getIntent(), this);
    }

    private void initView() {
        iv_pay_result_icon = (ImageView) findViewById(R.id.iv_pay_result_icon);
        tv_pay_result = (TextView) findViewById(R.id.tv_pay_result);
        tv_pay_result_hint = (TextView) findViewById(R.id.tv_pay_result_hint);
        button_back_advlist = (Button) findViewById(R.id.button_back_advlist);
        button_pay_success_share = (Button) findViewById(R.id.button_pay_success_share);
        button_back_advlist.setOnClickListener(this);
        button_pay_success_share.setOnClickListener(this);
    }

    private void initData() {
        shareUtils = new ShareUtils(instance, WXPayEntryActivity.this);
        int pay_result = shardPreferName.getIntData(Constants.PAYMENT_RESULT, 0);
        if (pay_result != 0) {
            if (pay_result == Constants.PAYMENT_SUCCESS) {
                pay_code = 0;
                payResultSuccess();
            } else {
                payResultFailed();
            }
            shardPreferName.setIntData(Constants.PAYMENT_RESULT, 0);
        }
        String title = shardPreferName.getStringData(Constants.SHARE_INFO_TITLE_ON_PAY_SUCCESS)
                .replace(Constants.SHARE_INFO_DESCRIPTION_ON_PAY_SUCCESS_REPLACE_TEXT,
                        shardPreferName.getStringData(Constants.SHARE_INFO_PAY_SUCCESS_ADV_TITLE));
        String description = shardPreferName.getStringData(Constants.SHARE_INFO_DESCRIPTION_ON_PAY_SUCCESS)
                .replace(Constants.SHARE_INFO_DESCRIPTION_REPLACE_MONEY,
                        shardPreferName.getStringData(Constants.PAYMENT_AMOUNT));
        String link = shardPreferName.getStringData(Constants.SHARE_INFO_LINK);
        String image = shardPreferName.getStringData(Constants.SHARE_INFO_PAY_SUCCESS_ADV_FIRST_IMAGE);
        L.e("TAG", "/****/" + title + "---" + description + "---" + link + "---" + image);
        shareUtils.setShareLink(link);
        shareUtils.setShareText(description);
        shareUtils.setShareTitle(title);
        shareUtils.setShareImagePath(image);

    }

    /**
     * 支付成功
     */
    private void payResultSuccess() {
        finishActivity();
        isPaySuccess = true;
        L.e("TAG", "支付成功");
        messageEventType = 0x100;
        iv_pay_result_icon.setImageResource(R.mipmap.pay_success);
        tv_pay_result.setText(R.string.payment_pay_result_success);
        button_pay_success_share.setVisibility(View.VISIBLE);
    }

    /**
     * 支付失败
     */
    private void payResultFailed() {
        isPaySuccess = false;
        L.e("TAG", "支付失败");
        messageEventType = 0x200;
        button_pay_success_share.setVisibility(View.GONE);
        iv_pay_result_icon.setImageResource(R.mipmap.pay_failed);
        tv_pay_result.setText(R.string.payment_pay_result_failed);
        tv_pay_result_hint.setText(R.string.payment_pay_result_failed_hint);
        button_back_advlist.setText(R.string.payment_pay_restart);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            setIntent(intent);
            api.handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        isResume = true;
        finishActivity();
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            pay_code = resp.errCode;
            if (pay_code == 0) {
                payResultSuccess();
            } else {
                payResultFailed();
            }
        }
    }

    /**
     * 关闭发广告界面
     */
    private void finishActivity() {
        if (isResume && pay_code == 0) {
            activityTask.FinishAllAddActivity();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back_advlist:
                finish();
                break;
            case R.id.button_pay_success_share:
                shareUtils.showSharePopWindow(button_pay_success_share);
                break;
        }
    }

    private void RefreshData() {
        BeanMessageEvent messageEvent = new BeanMessageEvent(messageEventType);
        EventBus.getDefault().post(messageEvent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPaySuccess)
            RefreshData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareUtils.onActivityResult(requestCode, resultCode, data);
    }
}