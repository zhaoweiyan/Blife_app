package com.blife.blife_app.tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.blife.blife_app.index.receiver.JPushReceiver;
import com.blife.blife_app.index.receiver.JPushReceiverListener;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.ShardPreferUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Somebody on 2016/8/24.
 */
public class JPushUtils {

    private static JPushUtils jPushUtils;
    private static final int MSG_SET_ALIAS = 1001;
    private Context context;
    private AlarmManager alarmManager;
    private PendingIntent pi;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    LogUtils.e("Set alias in handler.");
                    JPushInterface.setAliasAndTags(context, (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    LogUtils.e("Unhandled msg - " + msg.what);
            }
        }
    };

    private JPushUtils(Context context) {
        this.context = context;
    }

    public static synchronized JPushUtils getInstance(Context context) {
        if (jPushUtils == null) {
            jPushUtils = new JPushUtils(context);
        }
        return jPushUtils;
    }

    public void initJPush(Context context, JpushJson jpushJson) {
        JPushInterface.init(context);
        initReceiver(context, jpushJson);
//        setAlarm(context);
//        setAlias();
    }
//    private void setAlarm(Context context) {
//        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
//        //②指定要启动的Service,并指明动作是Servce:
//        Intent intent = new Intent(context, UploadPoiService.class);
//        pi = PendingIntent.getService(context, 0, intent, 0);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 60 * 1000, pi);
//    
    //  setAlarm(context);
    // }

//    private void setAlarm(Context context) {
//        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
//        //②指定要启动的Service,并指明动作是Servce:
//        Intent intent = new Intent(context, UploadPoiService.class);
//        pi = PendingIntent.getService(context, 0, intent, 0);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 60 * 1000, pi);
//    }

    private void initReceiver(Context context, final JpushJson jpushJson) {
        JPushReceiver.RegisterJpushBoardCast(context);
        JPushReceiver.setJPushReceiverListener(new JPushReceiverListener() {
            @Override
            public void onReceiverJpush(Bundle bundle) {
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
                LogUtils.e("jpush title===" + title + " content=" + content + "  json==" + type);
                jpushJson.getJpushJson(type);
            }
        });
    }
    public void setAlias() {
        ShardPreferUtil shardPreferName = ShardPreferUtil.getInstance(context, Constants.BlifeName);
        String alias = shardPreferName.getStringData(Constants.JPUSH_ALIAS, "");
        LogUtils.e("alisa==" + alias);
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    //设置别名成功
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    LogUtils.e(logs);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    LogUtils.e(logs);
            }
        }

    };

    public interface JpushJson {
        void getJpushJson(String json);
    }
}
