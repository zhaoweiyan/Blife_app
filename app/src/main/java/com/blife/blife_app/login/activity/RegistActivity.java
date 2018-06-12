package com.blife.blife_app.login.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.blife.blife_app.login.api.API_RegistCommit;
import com.blife.blife_app.login.api.API_ServiceConfig;
import com.blife.blife_app.login.bean.ConfigBean;
import com.blife.blife_app.login.bean.TokenBean;
import com.blife.blife_app.login.reciver.SMSReceiver;
import com.blife.blife_app.login.reciver.SMSReceiverListener;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.location.InterfaceLocationCallback;
import com.blife.blife_app.tools.location.LocationUtil;
import com.blife.blife_app.tools.permissionutil.InterfacePermissionResult;
import com.blife.blife_app.tools.permissionutil.PermissionUtils;
import com.blife.blife_app.utils.encryption.EncryptionManager;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
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
public class RegistActivity extends BaseActivity implements View.OnClickListener, InterfacePermissionResult {
    private Button button_vercode;
    private EditText et_registPhone;
    private EditText et_registCode;
    private EditText et_registpwd;
    private final String CODETYPE = "reg_code";
    private TextView btn_regist;
    private String password_secret_key;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String phone1 = et_registPhone.getText().toString().trim();
                String code = et_registCode.getText().toString().trim();
                String pwd = et_registpwd.getText().toString().trim();
                String aesPwd = shardPreferName.getStringData(Constants.PWD_KEY, "");
                if (StringUtils.isImpty(phone1)) {
                    showFailedDialog(R.string.dialog_login_account_empty, btn_regist);
                    return;
                }
                if (!StringUtils.regularTel(phone1)) {
                    showFailedDialog(R.string.dialog_phone_failed_message, btn_regist);
                    return;
                }
                if (StringUtils.isImpty(code)) {
                    showFailedDialog(R.string.dialog_login_code_empty, btn_regist);
                    return;
                }
                if (StringUtils.isImpty(pwd)) {
                    showFailedDialog(R.string.dialog_login_pwd_empty, btn_regist);
                    return;
                }

                if (!StringUtils.regularNumberPwd(pwd)) {
                    showFailedDialog(R.string.dialog_pwd_nomatch, btn_regist);
                    return;
                }
                if (!checkbox_blife_agreement.isChecked()) {
                    showFailedDialog(R.string.dialog_login_agreement_empty, btn_regist);
                    return;
                }
                API_RegistCommit api_registCommit = new API_RegistCommit(ACCESS_TOKEN, phone1, code, encryptionPWD(pwd), LOGIN_LNG, LOGIN_LAT);
                dataManager.getServiceData(api_registCommit);
            }
        }
    };
    private CheckBox checkbox_blife_agreement;
    private LinearLayout lin_agreement;
    private ImageView iv_registPhone;
    private ImageView iv_registlock;
    private ImageView iv_registeye;
    private LinearLayout lin_eye;
    private boolean isEye = false;
    private PermissionUtils permissionUtils;
    private boolean isLogin = false;
//    private TextView tv_back;

    private LocationUtil locationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initBackTopBar();
        initView();
        initClick();
        initData();
        initLocation();
        checkSmsPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String LOGIN_LAT = "1", LOGIN_LNG = "1";

    private void initLocation() {
        locationUtil.startLocation(new InterfaceLocationCallback() {
            @Override
            public void onLocationSuccess(BDLocation bdLocation) {
                LOGIN_LAT = bdLocation.getLatitude() + "";
                LOGIN_LNG = bdLocation.getLongitude() + "";
                if (isLogin) {
                    API_ServiceConfig api_serviceConfig = new API_ServiceConfig(ACCESS_TOKEN);
                    dataManager.getServiceData(api_serviceConfig);
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

    private void initData() {
        locationUtil = LocationUtil.getInstance(RegistActivity.this);
    }

    private void initView() {
        button_vercode = (Button) findViewById(R.id.button_vercode);
        btn_regist = (TextView) findViewById(R.id.btn_regist);
        et_registPhone = (EditText) findViewById(R.id.et_registPhone);
        et_registCode = (EditText) findViewById(R.id.et_registCode);
        et_registpwd = (EditText) findViewById(R.id.et_registpwd);
        checkbox_blife_agreement = (CheckBox) findViewById(R.id.checkbox_blife_agreement);
        iv_registPhone = (ImageView) findViewById(R.id.iv_registPhone);
        iv_registlock = (ImageView) findViewById(R.id.iv_registlock);
        iv_registeye = (ImageView) findViewById(R.id.iv_registeye);
        lin_agreement = (LinearLayout) findViewById(R.id.lin_agreement);
        lin_eye = (LinearLayout) findViewById(R.id.lin_eye);
    }

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
        permissionUtils = new PermissionUtils(instance, RegistActivity.this);
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

    private void RegisterSmsBoardCast() {
        SMSReceiver.RegisterSmsBoardCast(instance);
        SMSReceiver.setSmsReceiverListener(new SMSReceiverListener() {
            @Override
            public void onReceiverCode(String code) {
                et_registCode.setText(code);
            }
        });
    }

    private void initClick() {
        button_vercode.setOnClickListener(this);
        btn_regist.setOnClickListener(this);
        lin_agreement.setOnClickListener(this);
        lin_eye.setOnClickListener(this);
        checkbox_blife_agreement.setEnabled(false);
        et_registpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals("")) {
                    iv_registlock.setImageResource(R.mipmap.login_password_unlock);
                } else {
                    iv_registlock.setImageResource(R.mipmap.login_password_lock);
                }
            }
        });

        //账号图标亮
        et_registPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals("")) {
                    iv_registPhone.setImageResource(R.mipmap.login_unphone_icon);
                } else {
                    iv_registPhone.setImageResource(R.mipmap.login_phone_icon);
                }
            }
        });
//        //点击的框
//        checkbox_blife_agreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    checkbox_blife_agreement.setButtonDrawable(R.mipmap.login_agree_checkbox);
//                } else {
//                    checkbox_blife_agreement.setButtonDrawable(R.mipmap.login_agree_uncheckbox);
//                }
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_vercode:
                String phone = et_registPhone.getText().toString().trim();
                if (StringUtils.isImpty(phone)) {
                    showFailedDialog(R.string.dialog_login_account_empty, button_vercode);
                    return;
                }
                if (!StringUtils.regularTel(phone)) {
                    showFailedDialog(R.string.dialog_phone_failed_message, button_vercode);
                    return;
                }
                if (NetWorkUtil.isNetworkConnected(this)) {
                    API_LoginCode apiLoginCode = new API_LoginCode(ACCESS_TOKEN, phone, CODETYPE);
                    dataManager.getServiceData(apiLoginCode);
                } else {
                    showFailedDialog(R.string.toast_network_Available, button_vercode);
                }

                break;
            case R.id.btn_regist:
                isLogin = true;
                if (NetWorkUtil.isNetworkConnected(this)) {
                    if (LOGIN_LAT.equals("1") && LOGIN_LNG.equals("1")) {
                        initLocation();
                    } else {
                        API_ServiceConfig api_serviceConfig = new API_ServiceConfig(ACCESS_TOKEN);
                        dataManager.getServiceData(api_serviceConfig);
                    }
                } else {
                    showFailedDialog(R.string.toast_network_Available, button_vercode);
                }
                break;
            case R.id.lin_eye:
                if (isEye == true) {
                    iv_registeye.setEnabled(true);
                    isEye = false;
                } else {
                    iv_registeye.setEnabled(false);
                    isEye = true;
                }
                if (iv_registeye.isEnabled()) {
                    et_registpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    et_registpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
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

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                //获取验证码
                if (tag.equals(API_LoginCode.TAG)) {
                    Tools.YanZhengMaTimer(button_vercode);
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
                //注册
                if (tag.equals(API_RegistCommit.TAG)) {
                    cancelLoadingDialog();
                    try {
                        if (JsonObjUItils.getJsonObject(json)) {
                            LogUtils.e("注册成功json==" + json);
                            TokenBean tokenBean = (TokenBean) JsonObjUItils.fromJson(json, TokenBean.class);
                            String token = tokenBean.getAccesstoken();
                            if (!TextUtils.isEmpty(token)) {
                                L.e("TAG", "***ACCESS_TOKEN:" + token);
                                sqLiteCacheManager.insertNetCache(Constants.CACHE_ACCESS_TOKEN_KEY, token, tokenBean.getTime() * 1000, -1);
                            } else {
                                ToastUtils.showShort(instance, R.string.toast_login_failed);
                            }
                            shardPreferName.setStringData(Constants.CACHE_USER_ACCOUNT_KEY, et_registPhone.getText().toString().trim());
                            activityTask.finishAllActivity();
                            startFinishActivity(ActivityMain.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showFailedDialog(R.string.toast_regist_failed, btn_regist);
                    } catch (DBCacheException e) {
                        e.printStackTrace();
                        showFailedDialog(R.string.toast_regist_failed, btn_regist);
                    }
                }
            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
                if (tag.equals(API_LoginCode.TAG)) {
                    JsonObjUItils.getJsonCode(message, btn_regist, RegistActivity.this, R.string.dialog_login_code_empty);
                } else if (tag.equals(API_ServiceConfig.TAG)) {
                    JsonObjUItils.getJsonCode(message, btn_regist, RegistActivity.this, R.string.regist_file);
                } else if (tag.equals(API_RegistCommit.TAG)) {
                    JsonObjUItils.getJsonCode(message, btn_regist, RegistActivity.this, R.string.regist_file);
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
