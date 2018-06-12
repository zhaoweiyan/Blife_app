package com.blife.blife_app.base.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.tools.data.DataManager;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.view.LoadingDialog;
import com.blife.blife_app.tools.view.MessageDialog;
import com.blife.blife_app.utils.cache.SQLiteCacheManager;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.GsonUtil;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.ShardPreferUtil;

/**
 * Created by w on 2016/8/25.
 */
public abstract class BaseFragment extends Fragment {
    /**
     * 缓存的view
     */
    public View rootView;
    //网络请求
    public GsonUtil gsonUtil;
    public ShardPreferUtil shardPreferName;
    public SQLiteCacheManager sqLiteCacheManager;
    //基类
    public Activity activity;
    public Context instance;
    //获取数据
    public DataManager dataManager;
    //TopBar布局
    private LinearLayout lin_topbar;
    private FrameLayout frameLayout_topleft;
    private FrameLayout frameLayout_topright;
    private TextView tv_toptitle;
    private ImageView iv_topleft;
    //消息提示
    public MessageDialog messageDialog;
    //Token
    public String ACCESS_TOKEN;
    private LoadingDialog loadingDialog;
//    private UIResultCallback uicallback;

    public void showLoadingDialog() {
        loadingDialog.showDialog();
    }

    public void cancelLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.cancle();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //缓存的View如果为空，则加载布局
        if (rootView == null) {
            rootView = inflater.inflate(getLayout(), container, false);
        }
        //缓存的View如果有parent，则从原parent中删除，避免出现已有parent的错误
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isAdded()) {
            return;
        }
        initialize();
        init();
    }

    /**
     * 初始化
     */
    private void initialize() {
        instance = getActivity();
        //网络请求解析
//        this.uicallback = getUIRequestCallback();
        dataManager = new DataManager(getUIRequestCallback());
        gsonUtil = GsonUtil.getInstance();
        shardPreferName = ShardPreferUtil.getInstance(instance, Constants.BlifeName);
        sqLiteCacheManager = SQLiteCacheManager.getInstance(instance);
        loadingDialog = new LoadingDialog(instance);
        initData();
        initDialog();
    }

    private void initData() {
        try {
            ACCESS_TOKEN = sqLiteCacheManager.getNetCache(Constants.CACHE_ACCESS_TOKEN_KEY);
            LogUtils.e("ACCESS_TOKEN****" + ACCESS_TOKEN);
        } catch (DBCacheException e) {
            e.printStackTrace();
            ACCESS_TOKEN = "";
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

    /**
     * 设置TopBar
     *
     * @param isBack
     * @param title
     * @param background
     * @param titleColor
     */
    private void initTopBar(boolean isBack, String title, int background, int titleColor, int topleft) {
        lin_topbar = (LinearLayout) rootView.findViewById(R.id.lin_basetopbar);
        frameLayout_topleft = (FrameLayout) rootView.findViewById(R.id.framelayout_topleft);
        frameLayout_topright = (FrameLayout) rootView.findViewById(R.id.framelayout_topright);
        tv_toptitle = (TextView) rootView.findViewById(R.id.tv_top_title);
        iv_topleft = (ImageView) rootView.findViewById(R.id.iv_topleft);
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
                    getActivity().finish();
                    break;
            }
        }
    }


    /**
     * 界面跳转
     *
     * @param cls
     */
    public void startActivity(Class<?> cls) {
        startActivity(new Intent(instance, cls));
    }

    /**
     * 界面跳转，关闭当前界面
     *
     * @param cls
     */
    public void startFinishActivity(Class<?> cls) {
        startActivity(new Intent(instance, cls));
        getActivity().finish();
    }

    /**
     * 界面跳转，传值
     *
     * @param cls
     * @param bundle
     */
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(instance, cls);
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
        Intent intent = new Intent(instance, cls);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * 网络结果回调
     *
     * @return
     */
    protected UIResultCallback getUIRequestCallback() {
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
    public void onDestroy() {
        super.onDestroy();
    }

    public abstract void init();

    public abstract int getLayout();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        dataManager.stopUiCallBack();
        cancelLoadingDialog();
        dataManager.setStopRequest(true);
    }
}
