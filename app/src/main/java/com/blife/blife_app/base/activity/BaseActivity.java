package com.blife.blife_app.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.tools.data.DataManager;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.http.HttpManager;
import com.blife.blife_app.tools.view.LoadingDialog;
import com.blife.blife_app.tools.view.MessageDialog;
import com.blife.blife_app.utils.activity.ActivityManager;
import com.blife.blife_app.utils.activity.ActivityTask;
import com.blife.blife_app.utils.cache.SQLiteCacheManager;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.GsonUtil;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.ShardPreferUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by w on 2016/8/22.
 */
public class BaseActivity extends AppCompatActivity {
    //网络请求
    public HttpManager httpManager;
    public GsonUtil gsonUtil;
    public ShardPreferUtil shardPreferName;
    public SQLiteCacheManager sqLiteCacheManager;
    //获取数据
    public DataManager dataManager;
    //基类
    public Activity baseActivity;
    public Context instance;
    //TopBar布局
    private View line;
    private LinearLayout lin_topbar;
    private FrameLayout frameLayout_topleft;
    private FrameLayout frameLayout_topright;
    private TextView tv_toptitle;
    private ImageView iv_topleft;
    private TextView tv_topright;
    //消息提示
    public MessageDialog messageDialog;

    public ActivityTask activityTask;

    public String ACCESS_TOKEN;
    private LoadingDialog loadingDialog;
//    private Handler popupHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    View view = (View) msg.obj;
//                    loadingDialog = LoadingDialog.getInstance(baseActivity, R.layout.dialog_loading);
//                    loadingDialog.show(view);
//                    break;
//            }
//        }
//
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActivityManager.setTranslucentStatus(this);
        ActivityManager.setLopStatBar(this);
        ActivityManager.setMiuiStatusBarDarkMode(this, true);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        baseActivity = BaseActivity.this;
        instance = getApplicationContext();
        dataManager = new DataManager(getUIRequestCallback());
        httpManager = new HttpManager(getUIRequestCallback());
        gsonUtil = GsonUtil.getInstance();
        shardPreferName = ShardPreferUtil.getInstance(this, Constants.BlifeName);
        activityTask = ActivityTask.newInstance();
        activityTask.addActivity(this);
        sqLiteCacheManager = SQLiteCacheManager.getInstance(instance);
        loadingDialog = new LoadingDialog(baseActivity);
        initData();
        initDialog();
    }


    private void initData() {
        try {
            ACCESS_TOKEN = sqLiteCacheManager.getNetCache(Constants.CACHE_ACCESS_TOKEN_KEY);
        } catch (DBCacheException e) {
            e.printStackTrace();
            ACCESS_TOKEN = "";
        }
        L.e("TAG", "ACCESS_TOKEN：---" + ACCESS_TOKEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("当前的baseActivity****" + this);
        activityTask.ActivityResume(this);
        JPushInterface.onResume(this);
    }

    public void showLoadingDialog() {
        loadingDialog.showDialog();
    }

    public void cancelLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancle();
        }
    }

    /**
     * 初始化消息弹出框
     */
    private void initDialog() {
        messageDialog = new MessageDialog(instance, R.layout.dialog_hintmsg);
        messageDialog.setViewDismiss(true);
    }

    /**
     * 显示错误信息
     *
     * @param message
     * @param view
     */
    public void showFailedDialog(int message, View view) {
        messageDialog.setTitle(R.string.dialog_phone_failed_title);
        messageDialog.setMessage(message);
        messageDialog.show(view);
    }

    /**
     * 显示错误信息
     *
     * @param message
     * @param view
     */
    public void showFailedDialog(String message, View view) {
        messageDialog.setTitle(R.string.dialog_phone_failed_title);
        messageDialog.setMessage(message);
        messageDialog.show(view);
    }

    /**
     * 显示信息
     *
     * @param message
     * @param view
     */
    public void showFailedDialog(String title, String message, View view) {
        messageDialog.setTitle(title);
        messageDialog.setMessage(message);
        messageDialog.show(view);
    }

    /**
     * 显示信息
     *
     * @param message
     * @param view
     */
    public void showFailedDialog(String title, int message, View view) {
        messageDialog.setTitle(title);
        messageDialog.setMessage(message);
        messageDialog.show(view);
    }


    /**
     * 设置TopBar
     */
    public void initTopBar() {
        initTopBar(false, "", R.color.colorRedLoginNormal, 0, 0);
    }

    public void initTopBar(String title) {
        initTopBar(false, title, R.color.colorRedLoginNormal, 0, 0);
    }

    public void initTopBar(int title) {
        initTopBar(false, getString(title), R.color.colorRedLoginNormal, 0, 0);
    }

    public void initBackTopBar() {
        initTopBar(true, "", R.color.colorRedLoginNormal, 0, 0);
    }

    public void initBackTopBar(String title) {
        initTopBar(true, title, R.color.colorRedLoginNormal, 0, 0);
    }

    public void initBackTopBar(int title) {
        initTopBar(true, getString(title), R.color.colorRedLoginNormal, 0, 0);
    }

    public void initBackTopBar(int title, int right) {
        initTopBar(true, getString(title), R.color.colorRedLoginNormal, 0, 0, getString(right));
    }

    public void initBackTopBarColor(int title, int background, int titleColor, int topleft) {
        initTopBar(true, getString(title), background, titleColor, topleft);
    }

    private void initTopBar(boolean isBack, String title, int background, int titleColor, int topleft) {
        initTopBar(isBack, title, background, titleColor, topleft, "");
    }

    /**
     * 设置TopBar
     *
     * @param isBack
     * @param title
     * @param background
     * @param titleColor
     */
    private void initTopBar(boolean isBack, String title, int background, int titleColor, int topleft, String topright) {
        lin_topbar = (LinearLayout) findViewById(R.id.lin_basetopbar);
        frameLayout_topleft = (FrameLayout) findViewById(R.id.framelayout_topleft);
        frameLayout_topright = (FrameLayout) findViewById(R.id.framelayout_topright);
        tv_toptitle = (TextView) findViewById(R.id.tv_top_title);
        iv_topleft = (ImageView) findViewById(R.id.iv_topleft);
        tv_topright = (TextView) findViewById(R.id.tv_topright);
        if (isBack) {
            frameLayout_topleft.setVisibility(View.VISIBLE);
            frameLayout_topleft.setOnClickListener(new TopClick());
        } else {
            frameLayout_topleft.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(title)) {
            tv_toptitle.setText(title);
            if (titleColor != 0) {
                tv_toptitle.setTextColor(getResources().getColor(titleColor));
            }
        } else {
            tv_toptitle.setText("");
        }
        if (topleft != 0) {
            iv_topleft.setImageResource(topleft);
        }
        if (TextUtils.isEmpty(topright)) {
            frameLayout_topright.setVisibility(View.INVISIBLE);
        } else {
            frameLayout_topright.setVisibility(View.VISIBLE);
            frameLayout_topright.setOnClickListener(new TopClick());
            tv_topright.setText(topright);
        }
        if (background != 0) {
            lin_topbar.setBackgroundColor(getResources().getColor(background));
        }

    }

    /**
     * TopBar监听
     */
    class TopClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.framelayout_topleft:
                    TopBack();
                    finish();
                    break;
                case R.id.framelayout_topright:
                    TopRightClick();
                    break;
            }
        }
    }

    protected void TopBack() {

    }

    protected void TopRightClick() {

    }

    /**
     * 界面跳转
     *
     * @param cls
     */
    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * 界面跳转，关闭当前界面
     *
     * @param cls
     */
    public void startFinishActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }

    /**
     * 界面跳转，传值
     *
     * @param cls
     * @param bundle
     */
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 界面跳转，关闭当前界面，传值
     *
     * @param cls
     * @param bundle
     */
    public void startFinishActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(new Intent(this, cls), requestCode);
    }

    public void startActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 网络结果回调
     *
     * @return
     */
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {

            }

            @Override
            public void onError(Object tag, String message) {

            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityTask.ActivityDestroy(this);
//        dataManager.setStopRequest(true);
        if (messageDialog.isShowing()) {
            messageDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
