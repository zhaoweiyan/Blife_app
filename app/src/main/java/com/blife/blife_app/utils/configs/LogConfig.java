package com.blife.blife_app.utils.configs;


import com.blife.blife_app.utils.util.Contant;

import java.io.File;

/**
 * Log 级别信息
 * Created by zhaoxuan.li on 2015/9/17.
 */
public class LogConfig {

    public static boolean isDEBUG = true;
    //日志目录
    public static final String DIR_LOG = Contant.AppDirPath + "log" + File.separator;
    //所有级别日志 文件
    public static final String LOG_FILE_NAME = "log.txt";
    //v级别日志 文件
    public static final String VERBOSE_FILE_NAME = "verboseLog.txt";
    //d日志 文件
    public static final String DEBUG_FILE_NAME = "debugLog.txt";
    //i日志 文件
    public static final String INFO_FILE_NAME = "infoLog.txt";
    //w日志 文件
    public static final String WARN_FILE_NAME = "warnLog.txt";
    //e日志 文件
    public static final String ERROR_FILE_NAME = "errorLog.txt";
    //a日志 文件
    public static final String ASSERT_FILE_NAME = "assertLog.txt";

    //获取日志文件夹
    public static String getDirErrorLogCache() {
        return DIR_LOG;
    }

    /*  Log输出级别 */
    public static int LogLevel = LogConfig.VERBOSE;
    //日志文件名称，根据级别命名
    public static String dirLog = DIR_LOG;
    public static String logVerboseName = dirLog + "verson" + File.separator + VERBOSE_FILE_NAME;
    public static String logDebugName = dirLog + DEBUG_FILE_NAME;
    public static String logInfoName = dirLog + INFO_FILE_NAME;
    public static String logWarnName = dirLog + WARN_FILE_NAME;
    public static String logErrorName = dirLog + ERROR_FILE_NAME;

    public static String logFileName = logVerboseName;//储存文件的名称


    /*  Log保存和上传级别 */
    public static int UploadLevel = LogConfig.VERBOSE;
    public static int SaveFileLevel = LogConfig.VERBOSE;
    /*Log保存和上传开关*/
    public static boolean UploadSwitch = true;
    public static boolean SaveSwitch = true;


    //错误日志文件最大2M，超过2M则删除新建
    public static final long LOG_FILE_SIZE = 2 * 1024 * 1024;

    //各类日志的清理时间 暂未使用
    public final static long VERBOSE_TIMEOUT = 60 * 1000;
    public final static long DEBUG_TIMEOUT = 60 * VERBOSE_TIMEOUT;
    public final static long INFO_TIMEOUT = 24 * DEBUG_TIMEOUT;
    public final static long WARN_TIMEOUT = 7 * INFO_TIMEOUT;
    public final static long ERROR_TIMEOUT = 4 * 7 * WARN_TIMEOUT;


    public static final int NO_OUTPUT = 1;  //什么都不输出
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;

    public static final String DEFAULT_TAG = "COM.PATH";

    /**
     * 在赋值同时，做一些权限判断
     *
     * @param level 要设定的Log输出级别
     */
    public static void setLogLevel(int level) {
        LogLevel = level;
    }

    public static int getLogLevel() {
        return LogLevel;
    }

    public static void setUploadLevel(int level) {
        UploadLevel = level;
    }

    public static int getUploadLevel() {
        return UploadLevel;
    }

    public static void setSaveFileLevel(int level) {
        SaveFileLevel = level;
    }

    public static int getSaveFileLevel() {
        return SaveFileLevel;
    }


    //根据
    public static String getLogLevelName(int flag) {
        if (flag == -1)
            flag = LogLevel;
        switch (flag) {
            case NO_OUTPUT:
                return "NO_OUTPUT";
            case VERBOSE:
                return "VERBOSE";
            case DEBUG:
                return "DEBUG";
            case INFO:
                return "INFO";
            case WARN:
                return "WARN";
            case ERROR:
                return "ERROR";
            default:
                return "NO_OUTPUT";
        }
    }


}
