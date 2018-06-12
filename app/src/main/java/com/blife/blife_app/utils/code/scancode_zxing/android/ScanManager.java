package com.blife.blife_app.utils.code.scancode_zxing.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.blife.blife_app.utils.code.scancode_zxing.camera.CameraManager;
import com.blife.blife_app.utils.code.scancode_zxing.decode.DecodeCallback;
import com.blife.blife_app.utils.code.scancode_zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Collection;

public class ScanManager implements SurfaceHolder.Callback, DecodeCallback {

    // 相机控制
    private CameraManager cameraManager;
    private CameraDecodeHandler handler;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private Context context;
    private OnScanResult onScanResult;
    private int scanWidth = 0, scanHeight = 0;

    public ScanManager(Context context, SurfaceView surfaceView, ViewfinderView view) {
        this.context = context;
        this.viewfinderView = view;
        this.surfaceView = surfaceView;
    }

    public void setOnScanResult(OnScanResult onScanResult) {
        this.onScanResult = onScanResult;
    }

    public void setScanRect(int width, int height) {
        scanWidth = width;
        scanHeight = height;
    }

    public void onResume() {
        // CameraManager必须在这里初始化，而不是在onCreate()中。
        // 这是必须的，因为当我们第一次进入时需要显示帮助页，我们并不想打开Camera,测量屏幕大小
        // 当扫描框的尺寸不正确时会出现bug
        cameraManager = new CameraManager(context);
        if (scanWidth > 0 && scanHeight > 0) {
            cameraManager.setManualFramingRect(scanWidth, scanHeight);
        }
        viewfinderView.setCameraManager(cameraManager);
        handler = null;
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // activity在paused时但不会stopped,因此surface仍旧存在；
            // surfaceCreated()不会调用，因此在这里初始化camera
            initCamera(surfaceHolder);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }
        decodeFormats = null;
        characterSet = null;
    }

    public void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * 初始化Camera
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (handler == null) {
                handler = new CameraDecodeHandler(this, decodeFormats, null, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w("TAG", ioe);
        } catch (RuntimeException e) {
            Log.w("TAG", "Unexpected error initializing camera", e);
        }
    }

    /**
     * 扫描成功，处理反馈信息
     *
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    @Override
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        boolean fromLiveScan = barcode != null;
        // 这里处理解码完成后的结果，此处将参数回传到Activity处理
        if (fromLiveScan) {
            if (onScanResult != null) {
                onScanResult.onSuccess(rawResult.getText(),barcode);
            }
        } else {
            if (onScanResult != null) {
                onScanResult.onFailed();
            }
        }

    }

    @Override
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public void restart() {
        if (handler != null) {
            handler.restartPreviewAndDecode();
        }
    }

}
