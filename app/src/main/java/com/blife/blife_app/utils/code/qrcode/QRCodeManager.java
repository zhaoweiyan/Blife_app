package com.blife.blife_app.utils.code.qrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;

import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.cache.ImageCache;
import com.blife.blife_app.utils.exception.filexception.FileException;
import com.blife.blife_app.utils.file.FileManager;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.util.Contant;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by w on 2016/7/25.
 */
public class QRCodeManager {

    public static Bitmap createQRCode(String content, int w, int h) {
        return createQRCode(content, w, h, 0, "", null, 0);
    }

    public static Bitmap createQRCode(String content, int w, int h, int margin) {
        return createQRCode(content, w, h, margin, "", null, 0);
    }

    public static Bitmap createQRCode(String content, int w, int h, String filePath) {
        return createQRCode(content, w, h, 0, filePath, null, 0);
    }

    public static Bitmap createQRCode(String content, int w, int h, int margin, String filePath) {
        return createQRCode(content, w, h, margin, filePath, null, 0);
    }

    /**
     * 生成二维码
     *
     * @param content
     * @param w
     * @param h
     * @param margin
     * @param filePath
     * @param logo
     * @param scale
     * @return
     */
    public static Bitmap createQRCode(String content, int w, int h, int margin, String filePath, Bitmap logo, int scale) {
        //生成二维码图片的格式 使用ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        try {
            if (TextUtils.isEmpty(content)) {
                L.e("TAG", "QRCodeUtil：content is null");
                return null;
            }
            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            if (margin != 0) {
                hints.put(EncodeHintType.MARGIN, margin);//默认是4
            } else {
                hints.put(EncodeHintType.MARGIN, 0);//默认是4
            }
            //图像数据转换，使用矩阵转换

            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            //按照二维码算法，逐个生成二维码图片
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * w + x] = 0xff000000;
                    } else {
                        pixels[y * w + x] = 0xffffffff;
                    }
                }
            }
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            if (logo != null) {
                if (scale == 0) {
                    scale = 5;
                }
                bitmap = addBitmap(bitmap, logo, scale);
            }
            if (bitmap != null) {
                if (!TextUtils.isEmpty(filePath)) {
                    LogUtils.e("file=========" + content);
                    File file = FileManager.makeFilePath(filePath,  content.replaceAll("[^a-z^A-Z^0-9]", "") + ".png");
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                }
            }
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            L.e("QRCode createQRCode :" + e.getMessage());
        } catch (FileException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 二维码中间加Logo
     *
     * @return
     */

    public static Bitmap addBitmap(Bitmap src, Bitmap logo, int scale) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的scale
        float scaleFactor = srcWidth * 1.0f / scale / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = src;
            e.getStackTrace();
            L.e("QRCode addBitmap :" + e.getMessage());
        }

        return bitmap;
    }

}
