package com.blife.blife_app.utils.code.scancode_zxing.decode;

import android.graphics.Bitmap;
import android.os.Handler;

import com.blife.blife_app.utils.code.scancode_zxing.camera.CameraManager;
import com.blife.blife_app.utils.code.scancode_zxing.view.ViewfinderView;
import com.google.zxing.Result;

public interface DecodeCallback {

	CameraManager getCameraManager();

	Handler getHandler();

	ViewfinderView getViewfinderView();
	
	void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor);
	
	void drawViewfinder();

}
