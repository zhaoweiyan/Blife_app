package com.blife.blife_app.tools;

import android.content.Context;

import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.util.Contant;
import com.blife.blife_app.utils.util.LogUtils;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.socialize.media.Constant;

import java.io.File;


public class BitmapHelp {
    private BitmapHelp() {
    }

    ;
    private static BitmapUtils bitmapUtils;

    public static BitmapUtils getBitmapUtils(Context context) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(context, Contant.AppDirPath + File.separator + Constants.IMAGE_CACHE);
            LogUtils.e("cache bitmap****"+Contant.AppDirPath + File.separator + Constants.IMAGE_CACHE);
        }
        return bitmapUtils;
    }
}
