package com.blife.blife_app.utils.logcat;

import android.content.Context;

import com.blife.blife_app.utils.configs.LogConfig;
import com.blife.blife_app.utils.exception.filexception.FileException;
import com.blife.blife_app.utils.file.FileManager;
import com.blife.blife_app.utils.util.LogUtils;

import java.io.File;

/**
 * Created by zhaoxuan.li on 2015/9/17.
 */
public class LogcatManager {
    /**
     * Log输出机制：  LogLevel：Log输出级别控制
     * if（ 要输出的日志级别  >= LogLevel ）
     *      输出；
     * else
     *      不输出；
     *
     * Log上传和保存机制：
     * if（UploadSwitch == false）
     *      什么都不做
     * else
     *      if  （调用Log上传方法）
     *           上传并保存
     *      else
     *          if（flag>=LogConfig.UploadLevel）
     *                  上传
     *          if(flag>=LogConfig.SaveFileLevel)
     *                  保存
     *
     */

    /**
     * 打印 VERBOSE 信息  4个重载方法
     *
     * @param tagString     Tag标志
     * @param explainString 描述信息
     * @param throwable     异常信息
     * @param isSave        是否保存信息
     */
    public static void v(Context context, String tagString, String explainString, Throwable throwable, boolean isSave) {
        println(context, LogConfig.VERBOSE, tagString, explainString, throwable, isSave);
    }

    public static void v(Context context, String tagString, String explainString) {
        println(context, LogConfig.VERBOSE, tagString, explainString, false);
    }

    public static void v(Context context, String tagString, String explainString, boolean isSave) {
        println(context, LogConfig.VERBOSE, tagString, explainString, isSave);
    }

    public static void v(Context context, String tagString, String explainString, Throwable throwable) {
        println(context, LogConfig.VERBOSE, tagString, explainString, throwable, false);
    }

    /**
     * 打印 DEBUG 信息  4个重载方法
     *
     * @param tagString     Tag标志
     * @param explainString 描述信息
     * @param throwable     异常信息
     * @param isSave        是否保存文件
     */
    public static void d(Context context, String tagString, String explainString, Throwable throwable, boolean isSave) {
        println(context, LogConfig.DEBUG, tagString, explainString, throwable, isSave);
    }

    public static void d(Context context, String tagString, String explainString) {
        println(context, LogConfig.DEBUG, tagString, explainString, false);
    }

    public static void d(Context context, String tagString, String explainString, boolean isSave) {
        println(context, LogConfig.DEBUG, tagString, explainString, isSave);
    }

    public static void d(Context context, String tagString, String explainString, Throwable throwable) {
        println(context, LogConfig.DEBUG, tagString, explainString, throwable, false);
    }

    /**
     * 打印 INFO 信息  4个重载方法
     *
     * @param tagString     Tag标志
     * @param explainString 描述信息
     * @param throwable     异常信息
     * @param isSave        是否保存日志
     */
    public static void i(Context context, String tagString, String explainString, Throwable throwable, boolean isSave) {
        println(context, LogConfig.INFO, tagString, explainString, throwable, isSave);
    }

    public static void i(Context context, String tagString, String explainString) {
        println(context, LogConfig.INFO, tagString, explainString, true);
    }

    public static void i(Context context, String tagString, String explainString, boolean isSave) {
        println(context, LogConfig.INFO, tagString, explainString, isSave);
    }

    public static void i(Context context, String tagString, String explainString, Throwable throwable) {
        println(context, LogConfig.INFO, tagString, explainString, throwable, true);
    }

    /**
     * 打印 WARN 信息  4个重载方法
     *
     * @param tagString     Tag标志
     * @param explainString 描述信息
     * @param throwable     异常信息
     * @param isSave        是否保存日志
     */
    public static void w(Context context, String tagString, String explainString, Throwable throwable, boolean isSave) {
        println(context, LogConfig.WARN, tagString, explainString, throwable, isSave);
    }

    public static void w(Context context, String tagString, String explainString) {
        println(context, LogConfig.WARN, tagString, explainString, false);
    }

    public static void w(Context context, String tagString, String explainString, boolean isSave) {
        println(context, LogConfig.WARN, tagString, explainString, isSave);
    }

    public static void w(Context context, String tagString, String explainString, Throwable throwable) {
        println(context, LogConfig.WARN, tagString, explainString, throwable, false);
    }

    /**
     * 打印 ERROR 信息    4个重载方法
     *
     * @param tagString     Tag标志
     * @param explainString 描述信息
     * @param throwable     异常信息
     * @param isSave        是否保存日志
     */
    public static void e(Context context, String tagString, String explainString, Throwable throwable, boolean isSave) {
        println(context, LogConfig.ERROR, tagString, explainString, throwable, isSave);
    }

    public static void e(Context context, String tagString, String explainString) {
        println(context, LogConfig.ERROR, tagString, explainString, true);
    }

    public static void e(Context context, String tagString, String explainString, boolean isSave) {
        println(context, LogConfig.ERROR, tagString, explainString, isSave);
    }

    public static void e(Context context, String tagString, String explainString, Throwable throwable) {
        println(context, LogConfig.ERROR, tagString, explainString, throwable, true);
    }


    /**
     * 判断输出级别，决定是否输出
     *
     * @param flag          日志级别
     * @param explainString 说明信息
     */
    private static void println(Context context, int flag, String tagString, String explainString, boolean isSave) {
        if (flag >= LogConfig.LogLevel) {    //符合输出级别，输出
            LogPrint.println(flag, tagString, explainString);
        }
        if (flag == LogConfig.VERBOSE) {
            LogConfig.logFileName = LogConfig.logVerboseName;
        } else if (flag == LogConfig.DEBUG) {
            LogConfig.logFileName = LogConfig.logDebugName;
        } else if (flag == LogConfig.INFO) {
            LogConfig.logFileName = LogConfig.logInfoName;
        } else if (flag == LogConfig.WARN) {
            LogConfig.logFileName = LogConfig.logWarnName;
        } else if (flag == LogConfig.ERROR) {
            LogConfig.logFileName = LogConfig.logErrorName;
        }
        if (LogConfig.SaveSwitch) {   //保存开关已打开
            if (isSave) {   //调用保存方法
                if (flag >= LogConfig.SaveFileLevel) {   //符合保存级别
                    LogPrint.saveLogToFile(context, flag, explainString);
                }
            }
        }
    }

    /**
     * 重载方法 ，判断输出级别，决定是否输出，增加异常信息输出
     *
     * @param flag          日志级别
     * @param explainString 说明信息
     * @param throwable     异常对象
     */
    private static void println(Context context, int flag, String tagString, String explainString, Throwable throwable, boolean isSave) {
        if (flag >= LogConfig.LogLevel) {    //符合输出级别，输出
            LogPrint.println(flag, tagString, explainString, throwable);
        }
        if (flag == LogConfig.VERBOSE) {
            LogConfig.logFileName = LogConfig.logVerboseName;
        } else if (flag == LogConfig.DEBUG) {
            LogConfig.logFileName = LogConfig.logDebugName;
        } else if (flag == LogConfig.INFO) {
            LogConfig.logFileName = LogConfig.logInfoName;
        } else if (flag == LogConfig.WARN) {
            LogConfig.logFileName = LogConfig.logWarnName;
        } else if (flag == LogConfig.ERROR) {
            LogConfig.logFileName = LogConfig.logErrorName;
        }
        if (LogConfig.SaveSwitch) {   //保存开关已打开
            if (isSave) {   //调用保存方法
                if (flag >= LogConfig.SaveFileLevel) {   //符合保存级别
                    LogPrint.saveLogToFile(context, flag, explainString, throwable);
                }
            }
        }
    }

    //获取对应日志文件的大小
    /*
    清理log文件夹下所有文件
     */
    public static void deleteLogDir(String dirpath) {
        try {
            FileManager.deleteDir(dirpath);
        } catch (FileException e) {
            LogUtils.e(e.getMessage());
        }
    }

    /*
    清理根文件夹下所有文件
     */
    public static void deleteLogDir() {
        deleteLogDir(LogConfig.dirLog);
    }

    /*
    清理对应级别的文件
     */
    public static void deleteLogfile(int LogLevel) {
        switch (LogLevel) {
            case LogConfig.VERBOSE:
                try {
                    FileManager.deleteFile(LogConfig.logVerboseName);
                } catch (FileException e) {
                    LogUtils.e(e.getMessage());
                }
                break;
            case LogConfig.DEBUG:
                try {
                    FileManager.deleteFile(LogConfig.logDebugName);
                } catch (FileException e) {
                    LogUtils.e(e.getMessage());
                }
                break;
            case LogConfig.INFO:
                try {
                    FileManager.deleteFile(LogConfig.logInfoName);
                } catch (FileException e) {
                    LogUtils.e(e.getMessage());
                }
                break;
            case LogConfig.WARN:
                try {
                    FileManager.deleteFile(LogConfig.logWarnName);
                } catch (FileException e) {
                    LogUtils.e(e.getMessage());
                }
                break;
            case LogConfig.ERROR:
                try {
                    FileManager.deleteFile(LogConfig.logErrorName);
                } catch (FileException e) {
                    LogUtils.e(e.getMessage());
                }
                break;
            default:
                break;
        }
    }

    /*
    清理文件
     */
    public static void deleteLogfile(String filePath) {
        try {
            FileManager.deleteFile(filePath);
        } catch (FileException e) {
            LogUtils.e(e.getMessage());
        }
    }

    //文件日志存储时间
    public static long fileTime(String filePath) {
        if (filePath == null) {
            return 0;
        }
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return System.currentTimeMillis() - file.lastModified();
        } else {
            return 0;
        }
    }

    //文件超出日志存储时间，则删除
    public static void deleteLogFileTimeout(String filePath, long timeOut) {
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (fileTime(filePath) > timeOut) {
                deleteLogfile(filePath);
            }
        }
        if (file.exists() && file.isDirectory()) {
            if (fileTime(filePath) > timeOut) {
                deleteLogDir(filePath);
            }
        }
    }

    //文件超出日志存储大小，则删除
    public static void deleteLogFileSizeout(String filePath, long sizeOut) {
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
//            LogUtils.e("file.length()===" + file.length());

            if (file.length() > sizeOut) {
                deleteLogfile(filePath);
            }
        }
        if (file.exists() && file.isDirectory()) {
            if (file.length() > sizeOut) {
                deleteLogDir(filePath);
            }
        }
    }

    //文件超出日志存储大小和时间，则删除
    public static void deleteLogFileSizeTimeout(String filePath, long sizeOut, long timeOut) {
        deleteLogFileSizeout(filePath, sizeOut);
        deleteLogFileTimeout(filePath, timeOut);
    }
}
