package com.blife.blife_app.utils.net.Imageloader;

import android.widget.ImageView;

import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.util.Contant;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;

/**
 * Created by w on 2016/10/20.
 */
public class ImageLoader {

    private static ImageLoader mInstance;
    private static BitmapUtils bitmapUtils;

    private ImageLoader() {

    }

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader();
                }
                if (bitmapUtils == null) {
                    bitmapUtils = new BitmapUtils(BlifeApplication.AppContext, Contant.AppDirPath + File.separator + Constants.IMAGE_CACHE);
                }
            }
        }
        return mInstance;
    }

    public void loadImage(String path, ImageView imageView, boolean isFromNet) {
        bitmapUtils.display(imageView, path);
    }
}
