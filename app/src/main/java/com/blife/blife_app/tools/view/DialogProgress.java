package com.blife.blife_app.tools.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.interfacer.DialogListener;
import com.blife.blife_app.mine.I_DownLoadListner;
import com.blife.blife_app.tools.data.DataManager;
import com.blife.blife_app.utils.file.FileManager;
import com.blife.blife_app.utils.net.request.DownloadCallback;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.ToastUtils;

import java.io.File;

/**
 * Created by Somebody on 2016/9/20.
 */
public class DialogProgress {

    private static DialogProgress dialogProgress;
    private View view;
    private ConfirmDialog confirmDialog;
    private DataManager dataManger;
    private ProgressDialog m_progressDlg;
    private Context context;
    private I_DownLoadListner i_downLoadListner;

    public DialogProgress(Context context, View view, DataManager dataManager) {
        this.context = context;
        this.dataManger = dataManager;
        this.view = view;
        m_progressDlg = new ProgressDialog(context);
        m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        m_progressDlg.setIndeterminate(false);
        confirmDialog = new ConfirmDialog(context, R.layout.dialog_confirm, "确认升级新版本？");
        confirmDialog.setViewDismiss(true);
    }
//    public static synchronized DialogProgress getInstance(Context context, View view, DataManager dataManager) {
//        if (dialogProgress == null) {
//            dialogProgress = new DialogProgress(context, view, dataManager);
//        }
//        return dialogProgress;
//    }

    public void setProgress(boolean isMandatory, boolean isUpdate, String downLoadLink, String filePath) {
        downLoadApk(isMandatory, isUpdate, downLoadLink, filePath);
    }

    //判断及下载
    private void downLoadApk(boolean isMandatory, boolean isUpdate, String downLoadLink, String filePath) {
        if (isUpdate && !TextUtils.isEmpty(downLoadLink)) {
            if (!isMandatory) {
                //不强制下载，会有提示，如果确定则下载，取消则不下载
                remindUpdate(downLoadLink, filePath);
            } else if (isMandatory) {
                updateApk(downLoadLink, filePath);
            }
        } else {
//            ToastUtils.showShort(context, "版本无须更新");
        }
    }

    //提示下载或不下载
    private void remindUpdate(final String downLoadLink, final String filePath) {
        confirmDialog.show(view);
        confirmDialog.setDialogListener(new DialogListener() {
            @Override
            public void dialogConfirmListener() {
                updateApk(downLoadLink, filePath);
                confirmDialog.dismiss();
//                i_downLoadListner.confirmDownload();   //确认更新
            }

            @Override
            public void dialogCacleListener() {
                confirmDialog.dismiss();
//                i_downLoadListner.cancelDownload();    //取消更新
            }
        });
    }

    //确认更新和取消更新的监听
    public void setDownLoadListner(I_DownLoadListner i_downLoadListner) {
        this.i_downLoadListner = i_downLoadListner;
    }

    //下载及进程
    private void updateApk(String downLoadLink, String filePath) {
        dataManger.download(downLoadLink, filePath, new DownloadCallback() {
            @Override
            public void onProgress(int progress, int total) {
                downloadProgress(progress, total);
            }

            @Override
            public void onFinish(File file) {
                installApk(file);
            }

            @Override
            public void onError(Object tag, String message) {

            }
        });
    }

    //下载框
    private void downloadProgress(int progress, int total) {
        m_progressDlg.setTitle("正在下载");
        m_progressDlg.setMessage("请稍候...");
        m_progressDlg.show();
        m_progressDlg.setMax(total);
        m_progressDlg.setProgress(progress);
        m_progressDlg.setProgressNumberFormat(FileManager.FormetFileSize(progress) + "/" + FileManager.FormetFileSize(total));
        if (progress == total) {
            m_progressDlg.dismiss();
        }
    }
    //安装apk
    protected void installApk(File file) {
        try {
            Intent intent = new Intent();
            //执行动作
            intent.setAction(Intent.ACTION_VIEW);
            //执行的数据类型
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");//编者按：此处Android应为android，否则造成安装不了
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort(context, "版本安装错误");
        }
    }
}
