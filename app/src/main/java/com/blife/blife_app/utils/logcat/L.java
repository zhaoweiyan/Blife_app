package com.blife.blife_app.utils.logcat;

import android.util.Log;

/**
 * Created by Administrator on 2015/12/22.
 */
public class L {

    /**
     * 是否打开Log日志输出
     */
    public static boolean openDebug;
    /**
     * 默认日志Tag
     */
    public static String TAG = L.class.getSimpleName();

    private final static int defaultPriority = Log.DEBUG;

    /**
     * 输出V级别以上日志
     *
     * @param msg 日志信息
     */
    public static void v(String msg) {
        OUT(Log.VERBOSE, TAG, msg);
    }

    /**
     * 输出V级别以上日志
     *
     * @param tag 日志Tag
     * @param msg 日志信息
     */
    public static void v(String tag, String msg) {
        OUT(Log.VERBOSE, TAG, msg);
    }

    /**
     * 输出i级别以上日志
     *
     * @param msg 日志信息
     */
    public static void i(String msg) {
        OUT(Log.INFO, TAG, msg);
    }

    /**
     * 输出i级别以上日志
     *
     * @param tag 日志Tag
     * @param msg 日志信息
     */
    public static void i(String tag, String msg) {
        OUT(Log.INFO, TAG, msg);
    }

    /**
     * 输出d级别以上日志
     *
     * @param msg 日志信息
     */
    public static void d(String msg) {
        OUT(Log.DEBUG, TAG, msg);
    }

    /**
     * 输出d级别以上日志
     *
     * @param tag 日志Tag
     * @param msg 日志信息
     */
    public static void d(String tag, String msg) {
        OUT(Log.DEBUG, TAG, msg);
    }

    /**
     * 输出e级别以上日志
     *
     * @param msg 日志信息
     */
    public static void e(String msg) {
        OUT(Log.ERROR, TAG, msg);
    }

    /**
     * 输出e级别以上日志
     *
     * @param tag 日志Tag
     * @param msg 日志信息
     */
    public static void e(String tag, String msg) {
        OUT(Log.ERROR, tag, msg);
    }

    /**
     * 输出自定义级别以上日志
     *
     * @param priority 日志级别
     * @param tag      日志Tag
     * @param msg      日志信息
     */
    private static void OUT(int priority, String tag, String msg) {
        if (openDebug) {
            if (priority >= Log.VERBOSE && priority <= Log.ASSERT) {
                Log.println(priority, tag, msg);
            } else {
                Log.println(defaultPriority, tag, msg);
            }
        }
    }

    public static void setDebug(boolean isDebug) {
        openDebug = isDebug;
    }

    public static boolean getDebug() {
        return openDebug;
    }

}
