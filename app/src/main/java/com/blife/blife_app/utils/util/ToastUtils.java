package com.blife.blife_app.utils.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Somebody on 2016/8/5.
 */
public class ToastUtils {

    private static boolean isShow = true;

    private static boolean isSingleView = true;
    private static Toast singleToast;

    /**
     * 短时间显示
     *
     * @param context
     * @param text
     */
    public static void showShort(Context context, String text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示
     *
     * @param context
     * @param text
     */
    public static void showShort(Context context, int text) {
        show(context, context.getString(text), Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示
     *
     * @param context
     * @param text
     */
    public static void showLong(Context context, String text) {
        show(context, text, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示
     *
     * @param context
     * @param text
     */
    public static void showLong(Context context, int text) {
        show(context, context.getString(text), Toast.LENGTH_LONG);
    }

    /**
     * 自定义时间显示
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void show(Context context, String text, int duration) {
        show(context, text, duration, 0, 0, 0);
    }

    /**
     * 屏幕中间长时间显示
     *
     * @param context
     * @param text
     */
    public static void showCenterLong(Context context, String text) {
        show(context, text, Toast.LENGTH_LONG, Gravity.CENTER, 0, 100);
    }
    /**
     * 屏幕中间长时间显示
     *
     * @param context
     * @param text
     * @param marginTop
     */
    public static void showCenterLong(Context context, String text, int marginTop) {
        show(context, text, Toast.LENGTH_LONG, Gravity.CENTER, 0, marginTop);
    }

    /**
     * 屏幕中间短时间显示
     *
     * @param context
     * @param text
     */
    public static void showCenterShort(Context context, String text) {
        showCenterShort(context, text, 100);
    }

    /**
     * 屏幕中间短时间显示
     *
     * @param context
     * @param text
     * @param marginTop
     */
    public static void showCenterShort(Context context, String text, int marginTop) {
        show(context, text, Toast.LENGTH_SHORT, Gravity.CENTER, 0, marginTop);
    }

    /**
     * 屏幕顶部长时间显示
     *
     * @param context
     * @param text
     */
    public static void showTopLong(Context context, String text) {
        showTopLong(context, text, 100);
    }

    /**
     * 屏幕顶部长时间显示
     *
     * @param context
     * @param text
     * @param marginTop
     */
    public static void showTopLong(Context context, String text, int marginTop) {
        show(context, text, Toast.LENGTH_LONG, Gravity.TOP, 0, marginTop);
    }

    /**
     * 屏幕顶部短时间显示
     *
     * @param context
     * @param text
     */
    public static void showTopShort(Context context, String text) {
        showTopShort(context, text, 100);
    }

    /**
     * 屏幕顶部短时间显示
     *
     * @param context
     * @param text
     * @param marginTop
     */
    public static void showTopShort(Context context, String text, int marginTop) {
        show(context, text, Toast.LENGTH_SHORT, Gravity.TOP, 0, marginTop);
    }

    public static void showBottomShort(Context context, String text, int marginTop) {
        show(context, text, Toast.LENGTH_SHORT, Gravity.BOTTOM, 0, marginTop);
    }

    /**
     * 完全自定义显示
     *
     * @param context
     * @param text
     * @param duration
     * @param gravity
     * @param margnLeft
     * @param margnTop
     */
    public static void show(Context context, String text, int duration, int gravity, int margnLeft, int margnTop) {
        if (isSingleView) {
            if (singleToast == null) {
                singleToast = Toast.makeText(context, text, duration);
            } else {
                singleToast.setDuration(duration);
                singleToast.setText(text);
            }
            if (gravity != 0) {
                singleToast.setGravity(gravity, margnLeft, margnTop);
            }
            if (isShow) {
                singleToast.show();
            }
        } else {
            Toast toast = Toast.makeText(context, text, duration);
            if (gravity != 0) {
                toast.setGravity(gravity, margnLeft, margnTop);
            }
            if (isShow) {
                toast.show();
            }
        }
    }

}

