package com.blife.blife_app.login.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.h5.ActivityWebView;
import com.blife.blife_app.index.activity.ActivityMain;
import com.blife.blife_app.login.api.API_LoginPwd;
import com.blife.blife_app.login.api.API_ServiceConfig;
import com.blife.blife_app.login.bean.ConfigBean;
import com.blife.blife_app.login.bean.TokenBean;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.location.InterfaceLocationCallback;
import com.blife.blife_app.tools.location.LocationUtil;
import com.blife.blife_app.utils.encryption.EncryptionManager;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.NetWorkUtil;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;

import org.json.JSONException;

/**
 * Created by w on 2016/8/22.
 */
public class ActivityPasswordLogin extends BaseActivity implements View.OnClickListener {

    private EditText et_account, et_pwd;
    private ImageView iv_accounticon, iv_pwdicon, iv_visibilitypwd;
    private Button button_login;
    private CheckBox checkBox_agreement;
    private TextView tv_forgetpwd, tv_register, tv_quicklogin;
    private FrameLayout framelayout_visibilitypwd;
    private boolean PWD_VISIBLE = false;
    private String password_secret_key;
    private String LOGIN_LAT = "1", LOGIN_LNG = "1";
    private LinearLayout lin_agreement;
    private boolean loginIng;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showFailedDialog(R.string.toast_token_invalid, button_login);
        }
    };
    private boolean isLogin = false;

    private LocationUtil locationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_login);
        initView();
        initClick();
        initLoction();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (shardPreferName.getBooleanData(Constants.TOKEN_STATE_INVALID, false)) {
//            shardPreferName.setBooleanData(Constants.TOKEN_STATE_INVALID, false);
//            mHandler.sendEmptyMessageDelayed(0, 500);
//        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (shardPreferName.getBooleanData(Constants.TOKEN_STATE_INVALID, false)) {
            shardPreferName.setBooleanData(Constants.TOKEN_STATE_INVALID, false);
//            mHandler.sendEmptyMessageDelayed(0, 500);
            showFailedDialog(R.string.toast_token_invalid, button_login);
        }
    }

    private void initClick() {
        et_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals("")) {
                    iv_pwdicon.setImageResource(R.mipmap.login_password_unlock);
                } else {
                    iv_pwdicon.setImageResource(R.mipmap.login_password_lock);
                }
            }
        });

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
                    iv_accounticon.setImageResource(R.mipmap.login_unphone_icon);
                } else {
                    iv_accounticon.setImageResource(R.mipmap.login_phone_icon);
                }
            }
        });
    }

    private void initData() {
        API_ServiceConfig api_serviceConfig = new API_ServiceConfig();
        dataManager.getServiceData(api_serviceConfig);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initView() {
        et_account = (EditText) findViewById(R.id.edit_account);
        et_pwd = (EditText) findViewById(R.id.edit_pwd);
        iv_accounticon = (ImageView) findViewById(R.id.iv_accounticon);
        iv_pwdicon = (ImageView) findViewById(R.id.iv_pwdicon);
        iv_visibilitypwd = (ImageView) findViewById(R.id.iv_visibilitypwd);
        button_login = (Button) findViewById(R.id.button_login);
        lin_agreement = (LinearLayout) findViewById(R.id.lin_agreement);
        checkBox_agreement = (CheckBox) findViewById(R.id.checkbox_agreement);
        checkBox_agreement.setEnabled(false);
//        tv_agreement = (TextView) findViewById(R.id.tv_agreement);
        tv_forgetpwd = (TextView) findViewById(R.id.tv_forgetpwd);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_quicklogin = (TextView) findViewById(R.id.tv_quicklogin);
        framelayout_visibilitypwd = (FrameLayout) findViewById(R.id.framelayout_visibilitypwd);
        button_login.setOnClickListener(this);
//        tv_agreement.setOnClickListener(this);
        tv_forgetpwd.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_quicklogin.setOnClickListener(this);
        framelayout_visibilitypwd.setOnClickListener(this);
        lin_agreement.setOnClickListener(this);
        locationUtil = LocationUtil.getInstance(ActivityPasswordLogin.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                isLogin = true;
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
            case R.id.tv_forgetpwd:
                startActivity(ActivityResetPwd.class);
                break;
            case R.id.tv_register:
                startActivity(RegistActivity.class);
                break;
            case R.id.tv_quicklogin:
                startActivity(ActivityCodeLogin.class);
                break;
            case R.id.framelayout_visibilitypwd:
                dealPwdVisibility();
                break;
        }
    }

    /**
     * 处理密码的显示
     */
    private void dealPwdVisibility() {
        if (PWD_VISIBLE) {
            iv_visibilitypwd.setImageResource(R.mipmap.login_uneyes_icon);
            et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            iv_visibilitypwd.setImageResource(R.mipmap.login_eyes_icon);
            et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        PWD_VISIBLE = !PWD_VISIBLE;
        et_pwd.postInvalidate();
        //切换后将EditText光标置于末尾
        CharSequence charSequence = et_pwd.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    /**
     * 处理登录输入信息
     */
    private void dealLogin() {
        if (!checkBox_agreement.isChecked()) {
            showFailedDialog(R.string.dialog_login_agreement_empty, button_login);
            return;
        }
        String account = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            showFailedDialog(R.string.dialog_login_account_empty, button_login);
//            showFailedDialog("请打开"+"\""+"我的"+"\""+"→"+"\""+"个人中心"+"\""+"去完成实名认证", button_login);
            return;
        }
        if (!StringUtils.regularTel(account)) {
            showFailedDialog(R.string.dialog_phone_failed_message, button_login);
            return;
        }
        String pwd = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            showFailedDialog(R.string.dialog_login_pwd_empty, button_login);
            return;
        }
        if (!StringUtils.regularNumberPwd(pwd)) {
            showFailedDialog(R.string.dialog_pwd_nomatch, button_login);
            return;
        }
        login(account, pwd);
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

    /**
     * 登录
     */
    public void login(String account, String pwd) {
        if (TextUtils.isEmpty(password_secret_key)) {
            loginIng = true;
            API_ServiceConfig api_serviceConfig = new API_ServiceConfig();
            dataManager.getServiceData(api_serviceConfig);
            return;
        }
        API_LoginPwd loginPwd = new API_LoginPwd(account, encryptionPWD(pwd), LOGIN_LAT, LOGIN_LNG);
        dataManager.getServiceData(loginPwd);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_ServiceConfig.TAG)) {
                    cancelLoadingDialog();
                    try {
                        if (JsonObjUItils.getJsonObject(json)) {
                            ConfigBean configBean = (ConfigBean) gsonUtil.FromJson(json, "data", ConfigBean.class);
                            if (configBean != null)
                                password_secret_key = configBean.getPassword_secret_key();
                            if (loginIng && !TextUtils.isEmpty(password_secret_key)) {
                                dealLogin();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        password_secret_key = "";
                    }
                }
                if (tag.equals(API_LoginPwd.TAG)) {
                    cancelLoadingDialog();
                    loginSuccess(json);
                }
            }

            @Override
            public void onError(Object tag, String message) {
                L.e("TAG", "登录失败" + message);
                cancelLoadingDialog();
                if (tag.equals(API_ServiceConfig.TAG) && loginIng) {
                    JsonObjUItils.getJsonCode(message, button_login, ActivityPasswordLogin.this, R.string.login_file);
                }

                if (tag.equals(API_LoginPwd.TAG)) {
                    JsonObjUItils.getJsonCode(message, button_login, ActivityPasswordLogin.this, R.string.login_file);
                }
                cancelLoadingDialog();
            }
        };
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
                L.e("TAG", "***ACCESS_TOKEN:" + token);
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
        }
    }

}
