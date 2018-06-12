package com.blife.blife_app.utils.logcat;

import android.content.Context;
import android.os.Build;


import com.blife.blife_app.utils.configs.LogConfig;
import com.blife.blife_app.utils.file.FileManager;
import com.blife.blife_app.utils.file.SDcardManger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author
 */
public class AppException implements UncaughtExceptionHandler {

    private static String TAG = "AppException";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static AppException INSTANCE;
    private UncaughtExceptionHandler defaultUEH;
    private Context mContext;

    public static AppException getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppException();
        }
        return INSTANCE;
    }

    public void init(Context ctx) {
        mContext = ctx;
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        String errorName = throwable.toString();
        StringBuffer errorSb = new StringBuffer();
        errorSb.append(errorName);

        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            errorSb.append("   " + "\r\n")
                    .append("\tat ")
                    .append(stackTrace[i])
                    .append("\n\r");
        }

        Throwable cause = throwable.getCause();
        while (cause != null) {
            errorSb.append("   " + "\r\n");
            errorSb.append("Caused by: ");
            errorSb.append(cause.toString());
            errorSb.append("\n\r");

            StackTraceElement[] stackTrace2 = cause.getStackTrace();
            for (int i = 0; i < stackTrace2.length; i++) {
                errorSb.append("   " + "\r\n")
                        .append("\tat ")
                        .append(stackTrace2[i])
                        .append("\n\r");
            }

            cause = cause.getCause();
        }

        errorSb.append("\r\n" + "/***********************************************************************************/\n\r");

        printErrorLogToFileMee(errorSb.toString());

        System.exit(0);


    }

    public static void printErrorLogToFileMee(String errorStr) {
        boolean checkHasSD = SDcardManger.checkSDCard();
        if (!checkHasSD) return;
        File filedir = new File(LogConfig.getDirErrorLogCache());
        if (!filedir.exists() || filedir.isFile()) {
            filedir.mkdirs();
        }
        try {
            File file = new File(filedir, LogConfig.ERROR_FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.length() > LogConfig.LOG_FILE_SIZE) {
                file.delete();
                file.createNewFile();
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            pw.println("date: " + sdf.format(new Date()) + "\r\n");
            pw.println(errorStr);
            pw.close();
        } catch (Exception e) {

        }
    }

    /**
     * 输出异常的信息记录，包括类名、方法名、出错的代码行位置
     *
     * @param exception
     * @param className
     */
    public static void printErrorLog(Exception exception, Context context, String className) {
        try {
            StackTraceElement[] messages = exception.getStackTrace();
            int length = messages.length;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < length; i++) {
                String errorString = messages[i].toString();
                printErrorLogToFile(className, context, errorString, "errorLog.txt");
                sb.append(errorString);
            }
        } catch (Exception e) {
            printErrorLog(e, context, AppException.class.getName());
        }
    }

    /**
     * 把出错的Log记录输出到SD卡的error.txt文件中，如果没有SD卡，则不作处理
     *
     * @param className
     * @param errorContent
     */
    public static void printErrorLogToFile(String className, Context context, String errorContent, String errorFileName) {
        boolean checkHasSD = SDcardManger.checkSDCard();
        if (!checkHasSD) return;
        File filedir = new File(LogConfig.getDirErrorLogCache());
        if (!filedir.exists() || filedir.isFile()) {
            filedir.mkdirs();
        }
        try {
            File file = new File(filedir, errorFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            if ((file.length() / 1024 / 1024) > 2) {
                file.delete();
                file.createNewFile();
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            pw.println("Class name: " + className + "\r\n");
            pw.println("Date: " + sdf.format(new Date()) + "\r\n");
            pw.println("Error content: " + errorContent);
            pw.println("");
            pw.close();
        } catch (Exception e) {
        }
    }

    /**
     * 把出错的Log记录输出到SD卡的error.txt文件中，如果没有SD卡，则不作处理
     *
     * @param className
     * @param errorContent
     */
    public static void printErrorLogToFile2(String className, Context context, String errorContent, String errorFileName) {

        boolean checkHasSD = FileManager.checkHasSD();
        if (!checkHasSD) return;
        File filedir = new File(LogConfig.getDirErrorLogCache());
        if (!filedir.exists() || filedir.isFile()) {
            filedir.mkdirs();
        }
        try {
            File file = new File(filedir, errorFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            if ((file.length() / 1024 / 1024) > 2) {
                file.delete();
                file.createNewFile();
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            pw.println("threa name: " + className);
            pw.println("Date: " + sdf.format(new Date()));
            pw.println("Error content: " + errorContent);
            pw.println("");
            pw.close();
        } catch (Exception e) {

        }

    }

    public static String getWebErrorContent(Context context, int errorCode, String description, String failingUrl) {
        StringBuffer errorSB = new StringBuffer();

        String osver = android.os.Build.VERSION.SDK;
        String mobileModel = "";
//		String ver = "";
//		String imei = PhoneInfo.getIMEI(context);
//		String chlId = context.getResources().getString(R.string.chlId);
        String mobileOs = android.os.Build.VERSION.RELEASE;
        try {
            Class<Build> build_class = android.os.Build.class;
            java.lang.reflect.Field field2 = build_class.getField("MODEL");
            mobileModel = (String) field2.get(new android.os.Build());
//			PackageInfo info = context.getPackageManager().getPackageInfo(Constants.PACKAGE_NAME, 0);
//			ver = info.versionCode + "";
        } catch (Exception e) {
        }

        errorSB.append("错误码：" + errorCode);
        errorSB.append("    错误描述：" + description);
        errorSB.append("    链接：" + failingUrl);
        errorSB.append("    操作系统版本号：" + osver);
        errorSB.append("    机型：" + mobileModel);
//		errorSB.append("    软件版本号：" + ver);
//		errorSB.append("    渠道号：" + chlId);
//		errorSB.append("    IMEI：" + imei);
        errorSB.append("    手机操作系统：" + mobileOs);
        return errorSB.toString();
    }

}
