package com.blife.blife_app.utils.code.barcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.blife.blife_app.utils.logcat.L;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by w on 2016/7/25.
 */
public class BarCodeManager {

    private static int BLACK = 0xff000000;
    private static int WHITE = 0xffffffff;
    private static BarcodeFormat codeFormat;

    public static Bitmap createBarCode(String content, int width, int height,
                                       boolean viewCode) {
        return createBarCode(BarcodeFormat.CODE_128, content, width, height, viewCode);
    }

    public static Bitmap createBarCode(BarcodeFormat format, String content, int width,
                                       int height, boolean viewCode) {
        codeFormat = format;
        Bitmap result = null;
        Bitmap barCodeBitmap = barCode(content, width, height);
        if (viewCode) {
            Bitmap bitmap = createTextBitmap(content, width, height);
            result = mixtureBitmap(barCodeBitmap, bitmap, new PointF(0, height));
        } else {
            result = barCodeBitmap;
        }
        return result;
    }

    public static Bitmap barCode(String content, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(content, codeFormat, width, height);
            if (width <= 0 || height <= 0) {
                return null;
            }
            int[] pixels = new int[width * height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = BLACK;
                    } else {
                        pixels[y * width + x] = WHITE;
                    }
                }
            }
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
            L.e("BarCode barCode :" + e.getMessage());
        }
        return bitmap;
    }

    public static Bitmap createTextBitmap(String content, int width, int height) {
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        int size = 30;
        p.setTextSize(size);
        float textWidth = p.measureText(content);
        float scale = width / textWidth;
        p.setTextSize(size * scale);
        p.setAntiAlias(true);
        // FontMetrics对象
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        int textHeight = (int) (Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent));
        L.e("textHeight:" + textHeight);
        Bitmap bitmap = Bitmap.createBitmap(width, textHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.YELLOW);
        canvas.drawText(content, 0, size * scale, p);
        return bitmap;
    }

    protected static Bitmap mixtureBitmap(Bitmap barcode, Bitmap codeBitmap, PointF pointF) {
        if (barcode == null || codeBitmap == null || pointF == null) {
            return null;
        }
        int w = Math.max(barcode.getWidth(), codeBitmap.getWidth());
        Bitmap bitmap = Bitmap.createBitmap(w,
                barcode.getHeight() + codeBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawBitmap(barcode, 0, 0, null);
        canvas.drawBitmap(codeBitmap, 0, pointF.y, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }

}
