package com.blife.blife_app.welcome;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.index.activity.ActivityMain;
import com.blife.blife_app.login.activity.ActivityPasswordLogin;
import com.blife.blife_app.login.bean.TokenBean;
import com.blife.blife_app.mine.api.API_Version;
import com.blife.blife_app.mine.bean.BeanVersion;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.SDCardUtil;
import com.blife.blife_app.tools.UpdateNewVerService;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.location.InterfaceLocationCallback;
import com.blife.blife_app.tools.location.LocationUtil;
import com.blife.blife_app.tools.permissionutil.InterfacePermissionResult;
import com.blife.blife_app.tools.permissionutil.PermissionUtils;
import com.blife.blife_app.tools.view.DialogPopWindows;
import com.blife.blife_app.tools.view.MenuPopWindows;
import com.blife.blife_app.utils.appvercode.VerCodeManager;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.DownloadCallback;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.Tools;

import java.io.File;

/**
 * Created by w on 2016/8/24.
 */
public class ActivityWelcome extends BaseActivity implements CheckTokenStatus.onTokenStatus, InterfacePermissionResult {

    private ImageView iv_welcome;
    private Handler handler;
    private final int TAG_LOGIN = 100;
    private final int TAG_MAIN = 102;
    private final int TAG_GUIDED = 101;
    private static final long openTime = 1500;
    //启动时间
    private long startTime;

    private CheckTokenStatus checkTokenStatus;
    //新版本文件名
    private boolean isMandatory = false;
    private DialogPopWindows dialogPopWindows;
    private String updateNewVerDownLoadUrl, updateNewVerFileName;

    private MenuPopWindows menuPopWindows;
    private ProgressBar progressBar;
    private TextView tv_update;
    private PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        checkPermission();
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(instance, ActivityWelcome.this);
        permissionUtils.setInterfacePermissionResult(this);
        permissionUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onGranted() {
        L.e("TAG", "允许");
        initialize();
    }

    @Override
    public void onDenied() {
        L.e("TAG", "禁止");
        permissionUtils.showDeniedDialog(getString(R.string.permission_denied_title), getString(R.string.permission_denied_storage_message));
    }

    @Override
    public void onNotShowRationale() {
        L.e("TAG", "不再提示");
        permissionUtils.showApplyDialog(getString(R.string.permission_storage_title), getString(R.string.permission_storage_message));
    }

    @Override
    public void onCancelShowRationale() {
        L.e("TAG", "取消");
        finish();
        activityTask.finishAllActivity();
    }

    @Override
    public void onDeniedDialogPositive() {
        finish();
        activityTask.finishAllActivity();
    }

    @Override
    public void onDeniedDialogNegative() {
        finish();
        activityTask.finishAllActivity();
    }

    /**
     * 初始化
     */
    private void initialize() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case TAG_LOGIN:
                        startFinishActivity(ActivityPasswordLogin.class);
                        break;
                    case TAG_MAIN:
                        startFinishActivity(ActivityMain.class);
                        break;
                    case TAG_GUIDED:
                        startFinishActivity(ActivitySpashInto.class);
                        break;
                }
            }
        };
        if (shardPreferName.getBooleanData(Constants.APP_FIRST_OPEN, true)) {
            shardPreferName.setBooleanData(Constants.APP_FIRST_OPEN, false);
            startTime = System.currentTimeMillis();
            gotoGuided();
        } else {
            initData();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
        initUpdateDialog();
        checkTokenStatus = new CheckTokenStatus(instance);
        checkTokenStatus.setOnStatus(this);
        startTime = System.currentTimeMillis();
        checkTokenStatus.check();
    }

    /**
     * 初始化弹窗
     */
    private void initUpdateDialog() {
        menuPopWindows = new MenuPopWindows(instance, R.layout.popmandatoryupdate);
        menuPopWindows.setViewDismiss(false);
        menuPopWindows.setViewFocusable(false);
        progressBar = (ProgressBar) menuPopWindows.getRootView().findViewById(R.id.progress_update);
        tv_update = (TextView) menuPopWindows.getRootView().findViewById(R.id.tv_update_progress);

        dialogPopWindows = new DialogPopWindows(instance);
        dialogPopWindows.setText(DialogPopWindows.TYPE_TITLE, getString(R.string.updatever_title));
        dialogPopWindows.setText(DialogPopWindows.TYPE_CONFIRM, getString(R.string.updatever_confirm));
    }

    @Override
    public void onEmpty() {
        gotoLogin();
    }

    @Override
    public void onRefresh(String token) {
        refreshToken(token);
    }

    @Override
    public void onContinue() {
        checkNewVer();
    }

    /**
     * 检查版本更新
     */
    private void checkNewVer() {
//        gotoMain();
        API_Version api_version = new API_Version(ACCESS_TOKEN, VerCodeManager.getAppVerName(instance));
        dataManager.getServiceData(api_version);
    }

    /**
     * 置换Token
     */
    public void refreshToken(String token) {
        API_RefreshToken api_refreshToken = new API_RefreshToken();
        api_refreshToken.setOldToken(token);
        dataManager.getServiceData(api_refreshToken);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_RefreshToken.TAG)) {
                    try {
                        TokenBean tokenBean = (TokenBean) JsonObjUItils.fromJson(json, TokenBean.class);
                        String token = tokenBean.getAccesstoken();
                        ACCESS_TOKEN = token;
                        sqLiteCacheManager.insertNetCache(Constants.CACHE_ACCESS_TOKEN_KEY, token, tokenBean.getTime() * 1000, -1);
                    } catch (DBCacheException e) {
                        e.printStackTrace();
                    }
                    checkNewVer();
                }
                if (tag.equals(API_Version.TAG)) {
                    BeanVersion beanVersion = (BeanVersion) JsonObjUItils.fromJson(json, BeanVersion.class);
                    dealUpdateVer(beanVersion);
                }
            }

            @Override
            public void onError(Object tag, String message) {
                if (tag.equals(API_RefreshToken.TAG)) {//刷新Token失败
                    checkNewVer();
                }
                if (tag.equals(API_Version.TAG)) {//检查版本更新失败
                    gotoMain();
                }
            }
        };
    }

    /**
     * 处理版本更新
     *
     * @param beanVersion
     */
    private void dealUpdateVer(BeanVersion beanVersion) {
        if (beanVersion == null) return;
        isMandatory = !beanVersion.isMandatory();
        boolean upgrade = beanVersion.isUpgrade();
        if (upgrade) {
            updateNewVerFileName = SDCardUtil.AppCorpDownLoadPath + VerCodeManager.getDownLoadFileName();
            if (beanVersion.getPackages() != null && beanVersion.getPackages().size() > 0) {
                updateNewVerDownLoadUrl = beanVersion.getPackages().get(0);
            }
            showUpdateDialog();
        } else {
            gotoMain();
        }
    }

    /**
     * 更新提示弹窗
     */
    private void showUpdateDialog() {
        if (isMandatory) {
            dialogPopWindows.setText(DialogPopWindows.TYPE_MESSAGE, getString(R.string.updatever_mandatory_message));
        } else {
            dialogPopWindows.setText(DialogPopWindows.TYPE_MESSAGE, getString(R.string.updatever_message));
        }
        dialogPopWindows.show(iv_welcome);
        dialogPopWindows.setConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPopWindows.dismiss();
                if (TextUtils.isEmpty(updateNewVerDownLoadUrl) || TextUtils.isEmpty(updateNewVerFileName))
                    return;
                if (isMandatory) {
                    menuPopWindows.show(iv_welcome);
                    downloadNewVerApk();
                } else {
                    startServerDownLoad();
                    gotoMain();
                }
            }
        });
        dialogPopWindows.setCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPopWindows.dismiss();
                if (isMandatory) {
                    activityTask.finishAllActivity();
                } else {
                    gotoMain();
                }

            }
        });
    }

    /**
     * 开启后台更新
     */
    private void startServerDownLoad() {
        Intent intent = new Intent(this, UpdateNewVerService.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.UPDATE_URL, updateNewVerDownLoadUrl);
        bundle.putString(Constants.UPDATE_SAVE_PATH, updateNewVerFileName);
        intent.putExtras(bundle);
        startService(intent);
    }

    /**
     * 开始强制下载
     */
    private void downloadNewVerApk() {
        dataManager.download(updateNewVerDownLoadUrl, updateNewVerFileName, new DownloadCallback() {
            @Override
            public void onProgress(int progress, int total) {
                if (isMandatory) {
                    menuPopWindows.show(iv_welcome);
                    progressBar.setMax(total);
                    progressBar.setProgress(progress);
                    float a = progress * 1.0f / total * 1.0f;
                    tv_update.setText(((int) (a * 100)) + "%");
                }
            }

            @Override
            public void onFinish(File file) {
                if (isMandatory) {
                    menuPopWindows.dismiss();
                }
                try {
                    activityTask.finishAllActivity();
                    Tools.installApp(instance, file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object tag, String message) {
                gotoMain();
            }
        });
    }

    /**
     * 跳转到引导页
     */
    private void gotoGuided() {
        sendMessage(TAG_GUIDED);
    }


    /**
     * 跳转到登录页
     */
    private void gotoLogin() {
        sendMessage(TAG_LOGIN);
    }

    /**
     * 跳转到主页
     */
    private void gotoMain() {
        sendMessage(TAG_MAIN);
    }

    /**
     * 发送消息
     *
     * @param TAG
     */
    private void sendMessage(int TAG) {
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        L.e("TAG", "时间" + time);
        if (time >= openTime) {
            handler.sendEmptyMessage(TAG);
        } else {
            handler.sendEmptyMessageDelayed(TAG, openTime - time);
        }
    }


}
