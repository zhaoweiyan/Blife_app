package com.blife.blife_app.login.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.h5.ActivityWebView;
import com.blife.blife_app.index.activity.ActivityMain;
import com.blife.blife_app.login.api.API_LoginCode;
import com.blife.blife_app.login.api.API_LoginCodeCommit;
import com.blife.blife_app.login.bean.TokenBean;
import com.blife.blife_app.login.reciver.SMSReceiver;
import com.blife.blife_app.login.reciver.SMSReceiverListener;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.location.InterfaceLocationCallback;
import com.blife.blife_app.tools.location.LocationUtil;
import com.blife.blife_app.tools.permissionutil.InterfacePermissionResult;
import com.blife.blife_app.tools.permissionutil.PermissionUtils;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
import com.blife.blife_app.utils.net.NetWorkUtil;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;
import com.blife.blife_app.utils.util.Tools;

/**
 * Created by w on 2016/8/22.
 */
public class ActivityCodeLogin extends BaseActivity implements View.OnClickListener, InterfacePermissionResult {

    private EditText et_account, et_code;
    private ImageView iv_accounticon;
    private Button button_code, button_login;
    private CheckBox checkBox_agreement;
    private String CODETYPE = "login_code";
    private PermissionUtils permissionUtils;
    private boolean isLogin = false;
    private boolean isBtn = true;
    private LinearLayout lin_agreement;

    private LocationUtil locationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_login);
        initBackTopBar();
        initView();
        initClick();
        initLoction();
        checkSmsPermission();
    }

    private void checkSmsPermission() {
        permissionUtils = new PermissionUtils(instance, ActivityCodeLogin.this);
        permissionUtils.setInterfacePermissionResult(this);
        permissionUtils.checkPermission(Manifest.permission.RECEIVE_SMS, 105);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onGranted() {
        RegisterSmsBoardCast();
    }

    @Override
    public void onDenied() {

    }

    @Override
    public void onNotShowRationale() {

    }

    @Override
    public void onCancelShowRationale() {

    }

    @Override
    public void onDeniedDialogPositive() {

    }

    @Override
    public void onDeniedDialogNegative() {

    }

    private void initClick() {
        //账号图标亮
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals("")) {
                    iv_accounticon.setImageResource(R.mipmap.login_phone_icon);
                } else {
                    iv_accounticon.setImageResource(R.mipmap.login_phone_icon);
                }
            }
        });
    }

    private void initView() {
        et_account = (EditText) findViewById(R.id.edit_quicklogin_account);
        et_code = (EditText) findViewById(R.id.edit_quicklogin_code);
        iv_accounticon = (ImageView) findViewById(R.id.iv_quicklogin_account_icon);
        button_code = (Button) findViewById(R.id.button_quicklogin_code);
        button_login = (Button) findViewById(R.id.button_quicklogin);
        checkBox_agreement = (CheckBox) findViewById(R.id.checkbox_quicklogin);
        checkBox_agreement.setEnabled(false);
        lin_agreement = (LinearLayout) findViewById(R.id.lin_agreement);
        button_code.setOnClickListener(this);
        button_login.setOnClickListener(this);
        lin_agreement.setOnClickListener(this);
        locationUtil = LocationUtil.getInstance(ActivityCodeLogin.this);
    }

    private void RegisterSmsBoardCast() {
        SMSReceiver.RegisterSmsBoardCast(instance);
        SMSReceiver.setSmsReceiverListener(new SMSReceiverListener() {
            @Override
            public void onReceiverCode(String code) {
                et_code.setText(code);

            }
        });
    }

    int i = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_quicklogin_code:
                if (NetWorkUtil.isNetworkConnected(this)) {
                    getCode();
                } else {
                    showFailedDialog(R.string.toast_network_Available, view);
                }
                break;
            case R.id.button_quicklogin:
                isLogin = true;//分开是否在调用获取位置的时候调用登陆接口
                if (NetWorkUtil.isNetworkConnected(this)) {
                    if (LOGIN_LAT.equals("1") && LOGIN_LNG.equals("1")) {
                        initLoction();
                    } else {
                        dealLogin();
                    }
                } else {
                    showFailedDialog(R.string.toast_network_Available, view);
                }
                break;
            case R.id.lin_agreement:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.H5_TITLE_TAG, "使用协议");
                bundle.putString(Constants.H5_URL_TAG, Constants.AGREEMENT_COMPANY);
                startActivity(ActivityWebView.class, bundle);
//                startActivity(ActivityAgreement.class);
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        String account = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            showFailedDialog(R.string.dialog_phone_failed_message, button_login);
            return;
        }
        if (!StringUtils.regularTel(account)) {
            showFailedDialog(R.string.dialog_phone_failed_message, button_login);
            return;
        }
        API_LoginCode apiLoginCode = new API_LoginCode(ACCESS_TOKEN, account, CODETYPE);
        dataManager.getServiceData(apiLoginCode);
    }

    /**
     * 处理登录输入
     */
    private void dealLogin() {
        if (!checkBox_agreement.isChecked()) {
            showFailedDialog(R.string.dialog_login_agreement_empty, button_login);
            return;
        }
        String account = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            showFailedDialog(R.string.dialog_phone_failed_message, button_login);
            return;
        }
        if (!StringUtils.regularTel(account)) {
            showFailedDialog(R.string.dialog_phone_failed_message, button_login);
            return;
        }
        String code = et_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            showFailedDialog(R.string.dialog_login_code_empty, button_login);
            return;
        }
        login(account, code);
    }

    /**
     * 登录
     *
     * @param account
     * @param code
     */
    private void login(String account, String code) {
        API_LoginCodeCommit api_loginCodeCommit = new API_LoginCodeCommit(ACCESS_TOKEN, account, code, LOGIN_LAT, LOGIN_LNG);
        dataManager.getServiceData(api_loginCodeCommit);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_LoginCode.TAG)) {
                    Tools.YanZhengMaTimer(button_code);
                }
                if (tag.equals(API_LoginCodeCommit.TAG)) {
                    cancelLoadingDialog();
                    loginSuccess(json);
                    isBtn = true;
                }
            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
                if (tag.equals(API_LoginCode.TAG)) {
                    JsonObjUItils.getJsonCode(message, button_code, ActivityCodeLogin.this, R.string.toast_code_failed);
                }
                if (tag.equals(API_LoginCodeCommit.TAG)) {
                    JsonObjUItils.getJsonCode(message, button_code, ActivityCodeLogin.this, R.string.login_file);
                }
            }
        };
    }

    private String LOGIN_LAT = "1", LOGIN_LNG = "1";

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initLoction() {
        locationUtil.startLocation(new InterfaceLocationCallback() {
            @Override
            public void onLocationSuccess(BDLocation bdLocation) {
                LOGIN_LAT = bdLocation.getLatitude() + "";
                LOGIN_LNG = bdLocation.getLongitude() + "";
                if (isLogin) {
                    dealLogin();
                    isLogin = false;
                }
            }

            @Override
            public void onLocationError() {
                LOGIN_LAT = "1";
                LOGIN_LNG = "1";
            }

            @Override
            public void onCancelShowRationale() {

            }

            @Override
            public void onDeniedDialogPositive() {

            }

            @Override
            public void onDeniedDialogNegative() {

            }
        });
    }

    /**
     * 登录成功
     *
     * @param json
     */
    private void loginSuccess(String json) {
        try {
            TokenBean tokenBean = (TokenBean) JsonObjUItils.fromJson(json, TokenBean.class);
            String token = tokenBean.getAccesstoken();
            if (!TextUtils.isEmpty(token)) {
                sqLiteCacheManager.insertNetCache(Constants.CACHE_ACCESS_TOKEN_KEY, token, tokenBean.getTime() * 1000, -1);
                shardPreferName.setStringData(Constants.CACHE_USER_ACCOUNT_KEY, et_account.getText().toString().trim());
                activityTask.finishAllActivity();
                startFinishActivity(ActivityMain.class);
            } else {
                ToastUtils.showShort(instance, R.string.toast_login_failed);
            }
        } catch (DBCacheException e) {
            e.printStackTrace();
            ToastUtils.showShort(instance, R.string.toast_login_failed);
            LogUtils.e("异常  登陆失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSReceiver.unRegisterSmsBoardCast(instance);
    }

}

