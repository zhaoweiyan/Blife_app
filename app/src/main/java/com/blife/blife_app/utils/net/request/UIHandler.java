package com.blife.blife_app.utils.net.request;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import com.blife.blife_app.utils.logcat.L;

import java.io.File;

/**
 * Created by w on 2016/8/12.
 */
public class UIHandler extends Handler {

    private final int MSG_SUCCESS = 100;
    private final int MSG_ERROR = 101;
    private final int MSG_DOWNLOAD_PROGRESS = 102;
    private final int MSG_DOWNLOAD_FINISH = 103;

    private String MSG_KEY = "msg_key";

    private ResultCallback callback;

    public void setCallback(ResultCallback callback) {
        this.callback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_SUCCESS:
                ((UIResultCallback) callback).onSuccess(msg.obj, msg.getData().getString(MSG_KEY));
                break;
            case MSG_ERROR:
                callback.onError(msg.obj, msg.getData().getString(MSG_KEY));
                break;
            case MSG_DOWNLOAD_FINISH:
                ((DownloadCallback) callback).onFinish((File) msg.obj);
                break;
            case MSG_DOWNLOAD_PROGRESS:
                ((DownloadCallback) callback).onProgress(msg.arg1, msg.arg2);
                break;
        }
    }


    public void sendSuccessMessage(Object tag, String message) {
        L.e("HTTP_TAG", "Success--" + message);
        Message msg = obtainMessage();
        msg.what = MSG_SUCCESS;
        msg.obj = tag;
        Bundle bundle = new Bundle();
        bundle.putString(MSG_KEY, message);
        msg.setData(bundle);
        sendMessage(msg);
    }

    public void sendErrorMessage(Object tag, String message) {
        L.e("HTTP_TAG", "Error--" + message);
        Message msg = obtainMessage();
        msg.what = MSG_ERROR;
        msg.obj = tag;
        Bundle bundle = new Bundle();
        bundle.putString(MSG_KEY, message);
        msg.setData(bundle);
        sendMessage(msg);
    }

    public void sendDownLoadFinishMessage(File file) {
        L.e("HTTP_TAG", "DownLoadFinish--File length:" + file.length());
        Message msg = obtainMessage();
        msg.what = MSG_DOWNLOAD_FINISH;
        msg.obj = file;
        sendMessage(msg);
    }

    public void sendDownLoadProgressMessage(int progress, int total) {
//        L.e("HTTP_TAG", "DownLoadProgress--File progress:" + progress + "--total" + total);
        Message msg = obtainMessage();
        msg.what = MSG_DOWNLOAD_PROGRESS;
        msg.arg1 = progress;
        msg.arg2 = total;
        sendMessage(msg);
    }
}
