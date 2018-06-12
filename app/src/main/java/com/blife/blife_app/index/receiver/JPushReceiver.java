package com.blife.blife_app.index.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.index.activity.ActivityMain;
import com.blife.blife_app.mine.activity.ActivityWithdraw;
import com.blife.blife_app.tools.http.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by w on 2016/5/19.
 */
public class JPushReceiver extends BroadcastReceiver {
    private static JPushReceiverListener jpushReceiverListener;
    private static JPushReceiver jpushReceiver;

    public static void setJPushReceiverListener(JPushReceiverListener listener) {
        jpushReceiverListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        保存服务器推送下来的通知的标题。
//        对应 API 通知内容的 title 字段。
//        对应 Portal 推送通知界面上的“通知标题”字段。
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//        保存服务器推送下来的通知内容。
//        对应 API 通知内容的alert字段。
//        对应 Portal 推送通知界面上的“通知内容”字段。
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
//        保存服务器推送下来的附加字段。这是个 JSON 字符串。
//        对应 API 消息内容的 extras 字段。
//        对应 Portal 推送消息界面上的“可选设置”里的附加字段。
        String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (type != null && type.length() > 0 && jpushReceiverListener != null) {
            try {
                JSONObject jsonObject = new JSONObject(type);
                if (jsonObject.has("type") && jsonObject.getString("type").trim().equals(Constants.JPUSH_BROADCAST)) {
                    jpushReceiverListener.onReceiverJpush(bundle);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        通知栏的Notification ID，可以用于清除Notification
        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//        唯一标识调整消息的 ID, 可用于上报统计等。
        String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);
//        Log.i("TAG", "title==   json" + title + "  content==" + content + "  bundleExtra" + type + "  bundleNotificationId" + notificationId + "  bundleMsgId" + file);
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.i("TAG", "json JPush用户注册成功");
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Bundle bundle1 = intent.getExtras();
            String message = bundle1.getString(JPushInterface.EXTRA_MESSAGE);
            Log.i("TAG", "json  接受到推送下来的自定义消息" + message + "  " + intent.getDataString() + "   " + intent.getAction().toString());
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i("TAG", "json  接受到推送下来的通知");
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.i("TAG", "json  JPush用户注册成功");
            Intent i = new Intent(context, ActivityMain.class);  //自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                JSONObject jsonObject = new JSONObject(type);
                if (jsonObject.has("type") && jsonObject.getString("type").trim().equals(Constants.JPUSH_NOTICE)) {
                    BlifeApplication.jpushjump = 1;
                } else {
                    BlifeApplication.jpushjump = 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            context.startActivity(i);
        } else {
            Log.i("TAG", "json   title  在JPushReceiver中 " + intent.getAction());
            Log.i("TAG", "json  联网情况" + bundle.getString(JPushInterface.ACTION_CONNECTION_CHANGE));
        }
    }

    public static void RegisterJpushBoardCast(Context context) {
        IntentFilter filter = new IntentFilter(Constants.ACTION_JPUSH);
        jpushReceiver = new JPushReceiver();
        context.registerReceiver(jpushReceiver, filter);
    }

    public static void unRegisterSmsBoardCast(Context context) {
        if (jpushReceiver != null) {
            try {
                context.unregisterReceiver(jpushReceiver);
            } catch (Exception e) {
            }
        }
    }
}
