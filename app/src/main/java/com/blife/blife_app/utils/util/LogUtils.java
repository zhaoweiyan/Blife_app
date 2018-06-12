package com.blife.blife_app.utils.util;

import android.util.Log;

import com.blife.blife_app.utils.configs.LogConfig;


/**
 * Created by Somebody on 2016/8/1.
 */
public class LogUtils {
    public static void v(String str) {
        if (LogConfig.isDEBUG) {
            Log.v("TAG", str);
        }
    }

    public static void d(String str) {
        if (LogConfig.isDEBUG) {
            Log.d("TAG", str);
        }
    }

    public static void i(String str) {
        if (LogConfig.isDEBUG) {
            Log.i("TAG", str);
        }
    }

    public static void w(String str) {
        if (LogConfig.isDEBUG) {
            Log.w("TAG", str);
        }
    }

    public static void e(String str) {
        if (LogConfig.isDEBUG) {
            Log.e("TAG", str);
        }
    }

    //日志过多，使用分行输出的方法

    /**
     * @param str      输出的内容
     * @param logLevel 日志级别  例如LogConfig.ERROR
     */
    public static void showLog(String str, int logLevel) {
        str = str.trim();
        int index = 0;
        int maxLength = 4000;
        String finalString;
        while (index < str.length()) {
            if (str.length() <= index + maxLength) {
                finalString = str.substring(index);
            } else {
                finalString = str.substring(index, index + maxLength);
            }
            index += maxLength;
            switch (logLevel) {
                case LogConfig.VERBOSE:
                    v(finalString.trim());
                    break;
                case LogConfig.DEBUG:
                    d(finalString.trim());
                    break;
                case LogConfig.INFO:
                    i(finalString.trim());
                    break;
                case LogConfig.WARN:
                    w(finalString.trim());
                    break;
                case LogConfig.ERROR:
                    e(finalString.trim());
                    break;
                default:
                    break;
            }
        }


    }
}
