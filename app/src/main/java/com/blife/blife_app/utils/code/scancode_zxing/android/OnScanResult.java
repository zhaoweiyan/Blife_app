package com.blife.blife_app.utils.code.scancode_zxing.android;

import android.graphics.Bitmap;

/**
 * Created by w on 2016/8/4.
 */
public interface OnScanResult {

    void onSuccess(String result, Bitmap bitmap);

    void onFailed();

}
