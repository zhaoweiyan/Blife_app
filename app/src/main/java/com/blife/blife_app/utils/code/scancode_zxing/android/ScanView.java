package com.blife.blife_app.utils.code.scancode_zxing.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.blife.blife_app.utils.code.scancode_zxing.view.ViewfinderView;


public class ScanView extends FrameLayout {

    private ScanManager scanManager;

    public ScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScanView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        ViewfinderView viewfinderView = new ViewfinderView(context, null);
        SurfaceView surfaceView = new SurfaceView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        addView(surfaceView, params);
        addView(viewfinderView, params);
        scanManager = new ScanManager(context, surfaceView, viewfinderView);
    }

    /**
     * 设置扫描区域
     *
     * @param width
     * @param height
     */
    public void setScanRect(int width, int height) {
        scanManager.setScanRect(width, height);
    }

    public void onResume() {
        scanManager.onResume();
    }

    public void onPause() {
        scanManager.onPause();
    }

    public void setonScanResult(OnScanResult onScanResult) {
        scanManager.setOnScanResult(onScanResult);
    }

    public void restart() {
        if (scanManager != null) {
            scanManager.restart();
        }
    }
}
