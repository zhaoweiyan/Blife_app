package com.blife.blife_app.tools.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by w on 2016/8/26.
 */
public class AlarmServiceUtil {
    private Context context;
    private AlarmManager alarmManager;
    private PendingIntent alarmSender;

    public AlarmServiceUtil(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * 初始化任务
     *
     * @param cls 任务
     */
    public void init(Class cls) {
        Intent startIntent = new Intent(context, cls);
        alarmSender = PendingIntent.getService(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 开启定时
     *
     * @param ELAPSED_TIME 启动间隔
     */
    public void start(long ELAPSED_TIME) {
        if (alarmSender != null) {
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ELAPSED_TIME, alarmSender);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ELAPSED_TIME, alarmSender);
        }
    }

    /**
     * 关闭定时
     */
    public void cancel() {
        if (alarmSender != null)
            alarmManager.cancel(alarmSender);
    }

}
