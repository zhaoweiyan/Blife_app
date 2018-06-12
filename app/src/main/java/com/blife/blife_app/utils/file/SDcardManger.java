package com.blife.blife_app.utils.file;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by Administrator on 2016/7/31.
 */
public class SDcardManger {

    // sd卡挂载且可用
    public static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    // 获取SD卡路径
    public static String getExternalStoragePath() {
        // 获取SdCard状态
        String state = Environment.getExternalStorageState();
        // 判断SdCard是否存在并且是可用的
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Environment.getExternalStorageDirectory().canWrite()) {
                return Environment.getExternalStorageDirectory()
                        .getPath();
            }

        }
        return null;

    }

    /**
     * 　　* 获取存储卡的剩余容量，单位为字节
     * <p/>
     * 　　* @param filePath
     * <p/>
     * 　　* @return availableSpare
     */

    public static long getAvailableStore() {
        // 取得sdcard文件路径
        File filePath = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(filePath.getPath());

        // 获取block的SIZE

        long blocSize = statFs.getBlockSize();

        // 获取BLOCK数量

        // long totalBlocks = statFs.getBlockCount();

        // 可使用的Block的数量

        long availaBlock = statFs.getAvailableBlocks();

        // long total = totalBlocks * blocSize;

        long availableSpare = availaBlock * blocSize;

        return availableSpare;

    }
//获取sd卡总容量
    public long getSDAllSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大小
        return allBlocks * blockSize; // 单位Byte
    }


}
