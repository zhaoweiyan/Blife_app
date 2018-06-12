package com.blife.blife_app.utils.net;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

/**
 * Created by Somebody on 2016/7/28.
 */
public class NetWorkUtil {

  /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */

    public static boolean isNetwork(Context context)
    {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private static ConnectivityManager connManager = null;
    /**
     * 网络是否已经连接
     *
     * @return true, 可用； false， 不可用
     */
    public static boolean isNetworkConnected(Context context) {
        if (isNetworkAvailable(context)) {
            int type = getConnectionType(context);
            if (type == 0 || type == 1) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    /**
     * 对网络连接是否可用
     *
     * @return true, 可用； false， 不可用
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 对wifi连接状态进行判断
     *
     * @return true, 可用； false， 不可用
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo info = connManager.getActiveNetworkInfo();
            if (info.getType() == connManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }
    /**
     * 对MOBILE网络连接状态进行判断
     *
     * @return true, 可用； false， 不可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo info = connManager.getActiveNetworkInfo();
            if (info.getType() == connManager.TYPE_MOBILE) {
                return true;
            }
            // NetworkInfo mobileInfo = connManager
            // .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            // if (mobileInfo != null) {
            // return mobileInfo.isAvailable();
            // }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     *
     * @return one of TYPE_MOBILE, TYPE_WIFI, TYPE_WIMAX, TYPE_ETHERNET,
     *         TYPE_BLUETOOTH, or other types defined by ConnectivityManager
     *         int值分别为：0、1、6、9、7
     */
    public static int getConnectionType(Context context) {
        if (context != null) {
            connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                return networkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 提示设置网络连接
     *
     */
    public static void alertSetNetwork(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示：网络异常").setMessage("是否对网络进行设置?");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                try {
                    int sdkVersion = android.os.Build.VERSION.SDK_INT;
                    if (sdkVersion > 10) {
                        intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    } else {
                        intent = new Intent();
                        ComponentName comp = new ComponentName("com.android.settings",
                                "com.android.settings.WirelessSettings");
                        intent.setComponent(comp);
                        intent.setAction("android.intent.action.VIEW");
                    }
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public static void destroy() {
        if (connManager != null) {
            connManager = null;
        }
    }

}
