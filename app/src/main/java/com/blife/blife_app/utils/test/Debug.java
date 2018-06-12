package com.blife.blife_app.utils.test;

import java.io.File;

/**
 * Created by w on 2016/7/27.
 */
public class Debug {

    /**
     * 文件大小
     *
     * @return String
     */
    public static String getFileSize(File file) {
        long fileSize = file.length();
        float K = 1024;
        float M = 1024 * 1024;
        float G = 1024 * 1024 * 1024;
        String type;
        float size = 0;
        if (fileSize < K) {
            type = "字节";
        } else if (fileSize < M) {
            type = "KB";
            size = fileSize / K;
        } else if (fileSize < G) {
            type = "MB";
            size = fileSize / M;
        } else {
            type = "GB";
            size = fileSize / G;
        }
        return "大小:" + (((int) (size * 100)) / 100.0) + type;
    }


}
