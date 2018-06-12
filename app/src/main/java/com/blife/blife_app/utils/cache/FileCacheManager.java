package com.blife.blife_app.utils.cache;

import android.content.Context;


import com.blife.blife_app.utils.configs.CacheConfig;
import com.blife.blife_app.utils.exception.cacheException.FileCacheException;
import com.blife.blife_app.utils.exception.filexception.FileException;
import com.blife.blife_app.utils.exception.filexception.IOFileException;
import com.blife.blife_app.utils.file.FileManager;
import com.blife.blife_app.utils.net.NetWorkUtil;
import com.blife.blife_app.utils.util.LogUtils;

import java.io.File;

/**
 * Created by Administrator on 2016/7/31.
 */
public class FileCacheManager {

    private static FileCacheManager fileCacheManager = null;
    private Context context;
    private long timeOut = CacheConfig.EXPIRATION_WEEK;

    private FileCacheManager(Context context) {
        this.context = context;
    }

    public static synchronized FileCacheManager getInstance(Context context) {
        if (fileCacheManager == null) {
            fileCacheManager = new FileCacheManager(context);
        }
        return fileCacheManager;
    }

    //获取缓存文件路径
    public String getSavePath(String url) {
        if (url == null) {
            return "";
        }
        return CacheConfig.FileCachePath + getCacheDecodeString(url);
    }

    //获取缓存文件夹
    public String getSavePath() {
        return CacheConfig.FileCachePath;
    }

    //设置超时时间
    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public long getTimeOut() {
        return timeOut;
    }

    //获取缓存数据
    public String getUrlCache(String url) throws IOFileException {
        if (url == null) {
            return "";
        }
        String result = null;
        File file = new File(CacheConfig.FileCachePath + getCacheDecodeString(url));
        if (!file.exists() || !file.isFile()) {
            throw new FileException("file is not exist");
        }
        long expiredTime = System.currentTimeMillis() - file.lastModified();
        LogUtils.e(file.getAbsolutePath() + " expiredTime:" + expiredTime / 60000 + "min");
        if (NetWorkUtil.isNetworkConnected(context) && expiredTime <= 0) {
            return "";
        }
        if (NetWorkUtil.isWifiConnected(context)
                && expiredTime > timeOut) {
            FileManager.deleteFile(file);                   //超时就清理
            return "";
        } else if (NetWorkUtil.isMobileConnected(context)
                && expiredTime > timeOut) {
            FileManager.deleteFile(file);
            return "";
        }
        try {
            result = FileManager.readTextFile(file);
        } catch (IOFileException e) {
            throw new FileCacheException(" getUrlCache 失败：" + e.getMessage());
        }
        return result;
    }

    //放置缓存
    public void setUrlCache(String url, String data) throws IOFileException {
        File file = new File(CacheConfig.FileCachePath + getCacheDecodeString(url));
        //创建缓存数据到磁盘，就是创建文件
        try {
            FileManager.writeTextFile(file, data);
        } catch (IOFileException e) {
            throw new FileCacheException(" setUrlCache 失败：" + e.getMessage());
        }
    }

    //去除特殊字符
    public String getCacheDecodeString(String url) {
        //1. 处理特殊字符
        //2. 去除后缀名带来的文件浏览器的视图凌乱(特别是图片更需要如此类似处理，否则有的手机打开图库，全是我们的缓存图片)
        if (url != null) {
            return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
        }
        return "";
    }

    //删除缓存
    public void deleteCache(String url) throws FileException {
        if (url == null) {
            return;
        }
        File file = new File(CacheConfig.FileCachePath + getCacheDecodeString(url));
        FileManager.deleteFile(file);
    }

    //删除全部缓存
    public void deleteAllCache() throws FileException {
        File file = new File(CacheConfig.FileCachePath);
        FileManager.deleteFile(file);
    }
}
