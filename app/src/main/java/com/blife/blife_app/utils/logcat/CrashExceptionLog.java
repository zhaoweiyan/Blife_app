package com.blife.blife_app.utils.logcat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;


import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.index.activity.ActivityMain;
import com.blife.blife_app.tools.SDCardUtil;
import com.blife.blife_app.utils.activity.ActivityTask;
import com.blife.blife_app.utils.file.FileManager;
import com.blife.blife_app.utils.util.DateFormatUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by w on 2016/7/21.
 */
public class CrashExceptionLog implements Thread.UncaughtExceptionHandler {

    private static CrashExceptionLog INSTANCE;
    private Context mContext;
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    //使用Properties来保存设备的信息和错误堆栈信息
    private Map<String, String> CrashInfo;
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    //错误报告文件的扩展名
    private static final String CRASH_REPORTER_EXTENSION = ".cr";

    //日志收集标识
    private boolean openCrash = true;

    /**
     * 保证只有一个ExceptionManager实例
     */
    private CrashExceptionLog() {
    }

    /**
     * 获取ExceptionManager实例
     *
     * @return ExceptionManager实例
     */
    public static CrashExceptionLog getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashExceptionLog();
        }
        return INSTANCE;
    }

    /**
     * 初始化
     * 注册Context对象, 获取系统默认的UncaughtException处理器, 设置该ExceptionManager为程序的默认处理器
     *
     * @param context Context对象
     * @return ExceptionManager实例
     */
    public CrashExceptionLog init(Context context) {
        mContext = context;
        CrashInfo = new HashMap<String, String>();
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return getInstance();
    }

    public void setOpenCrashLog(boolean crash) {
        openCrash = crash;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (openCrash) {
            if (handlerException(ex)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Toast.makeText(mContext, "程序出现异常,错误日志收集完成", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }).start();
                //程序异常，强制退出************
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    L.e("error:" + System.err.toString());
//                }
//                //退出程序
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(1);
                //程序异常，强制退出************
                //程序异常，关闭当前界面****************
                ActivityTask.newInstance().finishAllActivity();
                ActivityTask.newInstance().finishActivity(ActivityTask.newInstance().getCurrentActivity());
                //程序异常，关闭当前界面****************
            } else {
                defaultExceptionHandler.uncaughtException(thread, ex);
            }
        } else {
            defaultExceptionHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return
     */
    private boolean handlerException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        collectCrashDeviceInfo();
        saveCrashInfoToFile(ex);
        return true;
    }

    /**
     * 收集程序崩溃的设备信息
     */
    private void collectCrashDeviceInfo() {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                CrashInfo.put(VERSION_NAME, packageInfo.versionName == null ? "not set" : packageInfo.versionName);
                CrashInfo.put(VERSION_CODE, packageInfo.versionCode + "");
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            L.e("Error while collect package info" + System.err.toString());
        }

        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                CrashInfo.put(field.getName(), field.get(null).toString());
                L.e(field.getName() + " : " + field.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                L.e("Error while collect crash info" + System.err.toString());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     */
    private String saveCrashInfoToFile(Throwable ex) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : CrashInfo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.append(key + "=" + value + "\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        // 将此 throwable 及其追踪输出到指定的 PrintWriter
        ex.printStackTrace(printWriter);

        // getCause() 返回此 throwable 的 cause；如果 cause 不存在或未知，则返回 null。
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = writer.toString();
        printWriter.close();
        builder.append(STACK_TRACE + "=" + result);
        L.e("TAG", STACK_TRACE + "=" + result);
        try {
            String fileName = "crash-" + getCurrentTime() + CRASH_REPORTER_EXTENSION;
            String path = SDCardUtil.AppCorpCrashPath + fileName;
            FileManager.writeFile(path, builder.toString());
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(System.currentTimeMillis()));
    }

}