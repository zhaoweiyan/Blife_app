package com.blife.blife_app.tools;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.blife.blife_app.R;

/**
 * Created by w on 2016/9/29.
 */
public class NotificationUtils {

    private int NotificationID = 0;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private long time = 0;
    private long progressTime = 800;

    public NotificationUtils(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(context.getString(R.string.updatever_noti_title));
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setNumber(0);
        builder.setAutoCancel(true);
    }

    public void showProgress(int total, int progress) {
        if (time == 0 || System.currentTimeMillis() - time >= progressTime) {
            if (builder != null) {
                float i = progress * 1.0f / total * 1.0f;
                builder.setContentInfo(((int) (i * 100)) + "%");
                builder.setProgress(total, progress, false);
            }
            show();
        }
    }

    public void show() {
        if (builder != null) {
            time = System.currentTimeMillis();
            notificationManager.notify(NotificationID, builder.build());
        }
    }

    public void clear() {
        if (builder != null) {
            notificationManager.cancel(NotificationID);
        }
    }

    public int getNotificationID() {
        return NotificationID;
    }
}
