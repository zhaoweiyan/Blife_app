package com.blife.blife_app.tools;

import android.os.Environment;

import com.blife.blife_app.utils.logcat.L;

import java.io.File;

/**
 * Created by w on 2016/5/25.
 */
public class SDCardUtil {

    private static String RootName = "BLife";
    private static String CorpDirName = "crop";
    private static String DownLoadDirName = "download";
    private static String CrashDirName = "crash";
    public static String AppDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + RootName + File.separator;
    public static String AppCorpDirPath = AppDirPath + CorpDirName + File.separator;
    public static String AppCorpDownLoadPath = AppDirPath + DownLoadDirName + File.separator;
    public static String AppCorpCrashPath = AppDirPath + CrashDirName + File.separator;


    /**
     * 创建App文件夹
     */
    public static void CreateAppDir() {
        if (!isSDCardEnable()) {
            return;
        }
        try {
            L.e("TAG", "创建" + AppDirPath);
            createDir(AppDirPath);
            L.e("TAG", "创建" + AppCorpDirPath);
            createDir(AppCorpDirPath);
            L.e("TAG", "创建" + AppCorpDownLoadPath);
            createDir(AppCorpDownLoadPath);
            L.e("TAG", "创建" + AppCorpCrashPath);
            createDir(AppCorpCrashPath);
        } catch (Exception e) {
            e.printStackTrace();
            L.e("TAG", "创建异常");
        }
    }

    private static void createDir(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            boolean flag = file.mkdirs();
            if (flag) {
                L.e("TAG", "创建成功" + file.getAbsolutePath());
            } else {
                L.e("TAG", "创建失败" + file.getAbsolutePath());
            }
        } else {
            L.e("TAG", file.getAbsolutePath() + "已存在");
        }
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }
}
