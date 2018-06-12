package com.blife.blife_app.utils.configs;


import com.blife.blife_app.utils.util.Contant;

import java.io.File;

/**
 * Created by Somebody on 2016/8/18.
 */
public class CacheConfig {

    //文件缓存路径
    public static String  FileCachePath = Contant.AppDirPath+"filecache"+ File.separator;
    //网络数据存储目录
    public static final String DIR_CACHE_NAME = Contant.AppDirPath;
    //图片缓存目录
    public static final String DIR_IMAGE_CACHE = Contant.AppDirPath +  "ImageCache" + File.separator;
    //照片目录
    public static final String DIR_CAMERA_CACHE = DIR_IMAGE_CACHE + "Camera" + File.separator;

    //文件缓存的时间
    public final static String KEY_TEST = "ClassFrame";
    public final static long EXPIRATION_MINUTE = 60 * 1000;
    public final static long EXPIRATION_HOUR =  60 * EXPIRATION_MINUTE;
    public final static long EXPIRATION_DAY =  24 * EXPIRATION_HOUR;
    public final static long EXPIRATION_WEEK = 7 * EXPIRATION_DAY;

//图片缓存
    public static final long BITMAP_MAX_SIZE = 200 * 1024; //上传图片最大为200K，大于200K处理后再上传
    //图片缓存:内存20%
    public static final float MEMORY_CACHE_PERCENT = (float) 0.20;
    //图片缓存:SD卡：100M
    public static final int DISK_PIC_CACHE_SIZE = 100 * 1024 * 1024;
    //缓存图片宽高：
    public static final int CACHE_PIC_HEIGHT = 640;
    public static final int CACHE_PIC_WIDTH = 360;
    public static String getDirImageCache() {
        return  DIR_IMAGE_CACHE;
    }
    public static String getDirCameraCache() {
        return DIR_CAMERA_CACHE;
    }

}
