package com.blife.blife_app.utils.net.request;

import java.io.File;

/**
 * Created by w on 2016/8/12.
 */
public interface DownloadCallback extends ResultCallback {

    void onProgress(int progress, int total);

    void onFinish(File file);

}
