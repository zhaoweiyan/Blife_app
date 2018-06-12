package com.blife.blife_app.tools;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.DownloaderUtils;
import com.blife.blife_app.utils.net.request.DownloadCallback;
import com.blife.blife_app.utils.net.request.ResultCallback;
import com.blife.blife_app.utils.util.ToastUtils;
import com.blife.blife_app.utils.util.Tools;

import java.io.File;

/**
 * Created by w on 2016/9/30.
 */
public class UpdateNewVerService extends Service implements ResultCallback {

    private String updateNewVerDownLoadUrl, updateNewVerFileName;
    private NotificationUtils notificationUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationUtils = new NotificationUtils(BlifeApplication.AppContext);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            updateNewVerDownLoadUrl = bundle.getString(Constants.UPDATE_URL);
            updateNewVerFileName = bundle.getString(Constants.UPDATE_SAVE_PATH);
            download();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void download() {
        DownloaderUtils.getInstance().download(updateNewVerDownLoadUrl, updateNewVerFileName, new DownloadCallback() {
            @Override
            public void onProgress(int progress, int total) {
                notificationUtils.showProgress(total, progress);
            }

            @Override
            public void onFinish(File file) {
                notificationUtils.showProgress(1, 1);
                try {
                    notificationUtils.clear();
                    L.e("UpdateNewVerService:开始安装");
                    Tools.installApp(BlifeApplication.AppContext, file);
                } catch (Exception e) {
                    e.printStackTrace();
                    L.e("UpdateNewVerService:安装失败");
                }
                stopSelf();
            }

            @Override
            public void onError(Object tag, String message) {
                ToastUtils.showShort(BlifeApplication.AppContext, "下载失败");
                notificationUtils.clear();
            }
        });
    }

    @Override
    public void onError(Object tag, String message) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
