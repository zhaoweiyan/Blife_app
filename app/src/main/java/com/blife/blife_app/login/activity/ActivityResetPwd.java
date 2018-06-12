package com.blife.blife_app.login.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.login.api.API_LoginCode;
import com.blife.blife_app.login.api.API_ResetPwd;
import com.blife.blife_app.login.api.API_ServiceConfig;
import com.blife.blife_app.login.bean.ConfigBean;
import com.blife.blife_app.login.reciver.SMSReceiver;
import com.blife.blife_app.login.reciver.SMSReceiverListener;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.permissionutil.InterfacePermissionResult;
import com.blife.blife_app.tools.permissionutil.PermissionUtils;
import com.blife.blife_app.utils.encryption.EncryptionManager;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.NetWorkUtil;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;
import com.blife.blife_app.utils.util.Tools;

import org.json.JSONException;

/**
 * Created by Somebody on 2016/8/22.
 */
public class ActivityResetPwd extends BaseActivity implements View.OnClickListener, InterfacePermissionResult {
    private EditText et_resetphone, et_reset_vercode, et_reset_newpwd;
    private TextView tv_reset_vercode;
    private TextView tv_newpwd_commit;
    private final String CODETYPE = "reset_password";
    private String password_secret_key;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String phone1 = et_resetphone.getText().toString().trim();
                String code = et_reset_vercode.getText().toString().trim();
                String pwd = et_reset_newpwd.getText().toString().trim();
                String aesPwd = shardPreferName.getStringData(Constants.PWD_KEY, "");
                if (StringUtils.isImpty(phone1)) {
                    showFailedDialog(R.string.dialog_login_account_empty, tv_newpwd_commit);
                    return;
                }
                if (!StringUtils.regularTel(phone1)) {
                    showFailedDialog(R.string.dialog_phone_failed_message, tv_newpwd_commit);
                    return;
                }
                if (StringUtils.isImpty(code)) {
                    showFailedDialog(R.string.dialog_login_code_empty, tv_newpwd_commit);
                    return;
                }
                if (StringUtils.isImpty(pwd)) {
                    showFailedDialog(R.string.dialog_login_pwd_empty, tv_newpwd_commit);
                    return;
                }
                if (!StringUtils.regularNumberPwd(pwd)) {
                    showFailedDialog(R.string.dialog_pwd_nomatch, tv_newpwd_commit);
                    return;
                }
                API_ResetPwd api_resetPwd = new API_ResetPwd(ACCESS_TOKEN, phone1, code, encryptionPWD(pwd));
                dataManager.getServiceData(api_resetPwd);
            }
        }
    };
    private TextView tv_back;
    private PermissionUtils permissionUtils;

    /**
     * 密码加密
     *
     * @param pwd
     * @return
     */
    private String encryptionPWD(String pwd) {
        String code = EncryptionManager.encodeBase64(password_secret_key + pwd + password_secret_key);
        L.e("TAG", "加密的密文：" + code);
        return code;

    }

    private void checkSmsPermission() {
        permissionUtils = new PermissionUtils(instance, ActivityResetPwd.this);
        permissionUtils.setInterfacePermissionResult(this);
        permissionUtils.checkPermission(Manifest.permission.RECEIVE_SMS, 105);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    private void RegisterSmsBoardCast() {
        SMSReceiver.RegisterSmsBoardCast(instance);
        SMSReceiver.setSmsReceiverListener(new SMSReceiverListener() {
            @Override
            public void onReceiverCode(String code) {
                et_reset_vercode.setText(code);

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);
        initBackTopBar("重置密码");
        initView();
        initClick();
        checkSmsPermission();
    }

    private void initView() {
        et_resetphone = (EditText) findViewById(R.id.et_resetphone);
        et_reset_vercode = (EditText) findViewById(R.id.et_reset_vercode);
        et_reset_newpwd = (EditText) findViewById(R.id.et_reset_newpwd);

        tv_reset_vercode = (TextView) findViewById(R.id.tv_reset_vercode);
        tv_newpwd_commit = (TextView) findViewById(R.id.tv_newpwd_commit);
        tv_back = (TextView) findViewById(R.id.tv_back);
    }

    private void initClick() {
        tv_reset_vercode.setOnClickListener(this);
        tv_newpwd_commit.setOnClickListener(this);
        tv_back.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reset_vercode:
                String phone = et_resetphone.getText().toString().trim();
                if (StringUtils.isImpty(phone)) {
                    showFailedDialog(R.string.dialog_login_account_empty, tv_reset_vercode);
                    return;
                }
                if (!StringUtils.regularTel(phone)) {
                    showFailedDialog(R.string.dialog_phone_failed_message, tv_reset_vercode);
                    return;
                }
                if (NetWorkUtil.isNetworkConnected(this)) {
                    API_LoginCode apiLoginCode = new API_LoginCode(ACCESS_TOKEN, phone, CODETYPE);
                    dataManager.getServiceData(apiLoginCode);
                } else {
                    showFailedDialog(R.string.toast_network_Available, tv_reset_vercode);
                }
                break;
            case R.id.tv_newpwd_commit:
                if (NetWorkUtil.isNetworkConnected(this)) {
                    API_ServiceConfig api_serviceConfig = new API_ServiceConfig(ACCESS_TOKEN);
                    dataManager.getServiceData(api_serviceConfig);
                } else {
                    showFailedDialog(R.string.toast_network_Available, tv_reset_vercode);
                }
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                //获取验证码
                if (tag.equals(API_LoginCode.TAG)) {
                    Tools.YanZhengMaTimer(tv_reset_vercode);
                }
                //获取加密通用key
                if (tag.equals(API_ServiceConfig.TAG)) {
                    try {
                        if (JsonObjUItils.getJsonObject(json)) {
                            ConfigBean configBean = (ConfigBean) gsonUtil.FromJson(json, "data", ConfigBean.class);
                            if (configBean != null) {
                                password_secret_key = configBean.getPassword_secret_key();
                                shardPreferName.setStringData(Constants.PWD_KEY, configBean.getPassword_secret_key());
                            }
                        }
                        mHandler.sendEmptyMessage(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //重置
                if (tag.equals(API_ResetPwd.TAG)) {
//                    startFinishActivity(ActivityPasswordLogin.class);
                    cancelLoadingDialog();
                    finish();
                }
            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
                if (tag.equals(API_LoginCode.TAG)) {
                    JsonObjUItils.getJsonCode(message, tv_newpwd_commit, ActivityResetPwd.this, R.string.dialog_login_code_empty);
                } else if (tag.equals(API_ServiceConfig.TAG)) {
                    JsonObjUItils.getJsonCode(message, tv_newpwd_commit, ActivityResetPwd.this, R.string.reset_pwd_file);
                } else {
                    JsonObjUItils.getJsonCode(message, tv_newpwd_commit, ActivityResetPwd.this, R.string.reset_pwd_file);
                }

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSReceiver.unRegisterSmsBoardCast(instance);
    }
}
