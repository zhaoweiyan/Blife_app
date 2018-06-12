package com.blife.blife_app.utils.appvercode;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.blife.blife_app.R;
import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.utils.util.DateFormatUtils;


/**
 * Created by w on 2016/8/3.
 */
public class VerCodeManager {
    //版本结果回调
    protected static AppVerCodeResultCallback callback;

    /**
     * 是否更新方法
     *
     * @param context 上下文
     * @param verName 新版本名
     */
    public static void update(Context context, String verName) {
        update(context, -1, verName);
    }

    /**
     * 是否更新方法
     *
     * @param context 上下文
     * @param verCode 新版本号
     */
    public static void update(Context context, int verCode) {
        update(context, verCode, "");
    }


    /**
     * 是否更新方法
     *
     * @param context 上下文
     * @param verCode 新版本号
     * @param verName 新版本名
     */
    public static void update(Context context, int verCode, String verName) {
        if (!TextUtils.isEmpty(verName)) {
            String currVerName = getAppVerName(context);
            if (VerNameCompare(verName, currVerName)) {
                if (callback != null) {
                    callback.onUpdate();
                }
            } else {
                if (callback != null) {
                    callback.onNewVer();
                }
            }
        } else if (verCode >= 0) {
            int currVerCode = getAppVerCode(context);
            if (verCode > currVerCode) {
                if (callback != null) {
                    callback.onUpdate();
                }
            } else {
                if (callback != null) {
                    callback.onNewVer();
                }
            }
        } else {
            if (callback != null) {
                callback.onNewVer();
            }
        }
    }

    /**
     * 设置更新结果回调
     *
     * @param appVerCodeResultCallback 结果回调
     */
    public static void setAppVerCodeResultCallback(AppVerCodeResultCallback appVerCodeResultCallback) {
        callback = appVerCodeResultCallback;
    }

    /**
     * 获取版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    public static int getAppVerCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取版本名
     *
     * @param context 上文
     * @return 版本名
     */
    public static String getAppVerName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 版本名对比版本
     *
     * @param verName     新版本名
     * @param currVerName 当前版本名
     * @return 是否更新
     */
    protected static boolean VerNameCompare(String verName, String currVerName) {
        int len = verName.length() < currVerName.length() ? currVerName.length() : verName.length();
        String ver = addString(verName, len).replace(".", "").trim();
        String currVer = addString(currVerName, len).replace(".", "").trim();
        int v = -1;
        int cv = -1;
        try {
            v = Integer.valueOf(ver);
            cv = Integer.valueOf(currVer);
        } catch (Exception e) {
            return false;
        }
        if (v > cv) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 补全位数
     *
     * @param text   原字符串
     * @param length 目标长度
     * @return 结果字符串
     */
    protected static String addString(String text, int length) {
        String result = "";
        if (text.length() < length) {
            result += text;
            int l = text.length();
            for (int i = l; i < length; i++) {
                result += "0";
            }
            return result;
        } else {
            return text;
        }
    }

    public static String getDownLoadFileName() {
        return BlifeApplication.AppContext.getResources().getString(R.string.app_name)
                + DateFormatUtils.getTimeHStr(System.currentTimeMillis() / 1000, DateFormatUtils.format)
                + ".apk";
    }
}
