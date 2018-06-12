package com.blife.blife_app.application;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.blife.blife_app.tools.SDCardUtil;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.configs.LogConfig;
import com.blife.blife_app.utils.logcat.CrashExceptionLog;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.util.LogUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by w on 2016/8/22.
 */
public class BlifeApplication extends Application {

    public static Context AppContext;
    public static int jpushjump = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        LogConfig.isDEBUG = true;
        L.setDebug(true);
        //地图
        SDKInitializer.initialize(getApplicationContext());
        AppContext = getApplicationContext();
        SDCardUtil.CreateAppDir();
        CrashExceptionLog.getInstance().init(getApplicationContext()).setOpenCrashLog(true);
        initShare();
    }

    private void initShare() {
        UMShareAPI.get(this);
        // QQ和QZone appId appKey
        PlatformConfig.setQQZone(Constants.QQ_APPID, Constants.QQ_APP_KEY);
        //新浪微博 appKey appSecret
        PlatformConfig.setSinaWeibo(Constants.SINA_APP_KEY, Constants.SINA_APPSRCERT);
        //微信 appId appSecret
        PlatformConfig.setWeixin(Constants.WEIXIN_APPID, Constants.WEIXIN_APPSRCERT);
        L.e("TAG", "屏幕宽度：" + ScreenUtils.getScreenWidth(getApplicationContext()));
        L.e("TAG", "屏幕高度：" + ScreenUtils.getScreenHeight(getApplicationContext()));
    }


}
