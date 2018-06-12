package com.blife.blife_app.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;


import com.blife.blife_app.utils.exception.bitmapexception.BitmapException;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by w on 2016/7/27.
 */
public class BitmapUtils {

    /**
     * 根据期望的宽度和高度或者压缩比例压缩图片
     *
     * @param filePath   文件路径
     * @param width      期望宽度
     * @param height     期望高度
     * @param SampleSize 缩放比例
     * @return 压缩后的Bitmap
     */
    public static Bitmap compressBitmap(String filePath, int width, int height, int SampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        LogUtils.e("拍照****decodeFile filePath****" + filePath);
        BitmapFactory.decodeFile(filePath, options);
        LogUtils.e("拍照****options****" + options.outHeight + "****" + options.outWidth);
        calculateInSampleSize(options, SampleSize, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 处理压缩使用的Options
     *
     * @param options    原图Options
     * @param SampleSize 缩放比例
     * @param reqWidth   期望宽度
     * @param reqHeight  期望高度
     */
    //计算图片的缩放值
    protected static void calculateInSampleSize(BitmapFactory.Options options, int SampleSize, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (height <= 700 || width <= 700) {
            options.inSampleSize = 1;
            return;
        }
        L.e("TAG", "压缩前W:" + width);
        L.e("TAG", "压缩前H:" + height);
        float inSampleSize = 1;
        if (SampleSize <= 0) {
            if (height > reqHeight || width > reqWidth) {
                if (reqHeight > 0 && reqWidth > 0) {
                    float heightRatio = (float) height / (float) reqHeight;
                    float widthRatio = (float) width / (float) reqWidth;
                    inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
                }
            }
        } else {
            inSampleSize = SampleSize;
        }
        options.outHeight = (int) (height / inSampleSize);
        options.outWidth = (int) (width / inSampleSize);
        L.e("TAG", "压缩后W:" + options.outWidth);
        L.e("TAG", "压缩后H:" + options.outHeight);
        options.inSampleSize = Math.round(inSampleSize);
    }

    /**
     * 根据图片路径与最大期望大小压缩图片
     *
     * @param path      原图片路径
     * @param maxLength 最大期望大小（KB）
     * @return 图片大小大于期望值，返回的压缩后的图片
     */
    public static Bitmap scaleBitmap(String path, int maxLength) {
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        long fileMaxSize = maxLength * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;
            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            L.e("TAG", "缩放值:" + scale);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
//            options.inSampleSize = (int) (scale);
            options.inSampleSize = (int) Math.round(scale);
            L.e("TAG", "缩放值outHeight:" + options.outHeight);
            L.e("TAG", "缩放值outWidth:" + options.outWidth);
            L.e("TAG", "缩放值inSampleSize:" + options.inSampleSize);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(path, options);
        }
        return BitmapFactory.decodeFile(path);
    }

    /**
     * Bitmap转成File
     *
     * @param bitmap   要保存的Bitmap
     * @param SavePath 要形成的File路径
     * @param quality  形成文件时的压缩比例
     * @param format   形成的图片的格式（JPEG,PNG，WEBP）
     * @return 保存成功返回File
     */
    public static File BitmapToFile(Bitmap bitmap, String SavePath, int quality, Bitmap.CompressFormat format) throws BitmapException {
        return compressBmpToFile(bitmap, SavePath, format, quality, false, 0);
    }

    /**
     * Bitmap转成File
     *
     * @param bitmap     原图Bitmap
     * @param SavePath   保存路径
     * @param format     文件格式（JPEG,PNG,WEBP）
     * @param quality    压缩基数
     * @param isCompress 是否压缩
     * @param KBLength   期望最大长度
     * @return 保存成功返回File，失败返回null
     */
    public static File compressBmpToFile(Bitmap bitmap, String SavePath, Bitmap.CompressFormat format, int quality, boolean isCompress, int KBLength) throws BitmapException {
        try {
            if (TextUtils.isEmpty(SavePath)) {
                return null;
            }
            File file = new File(SavePath);//将要保存图片的路径
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int options = quality;
            if (options < 0 || options > 100) {
                options = 100;
            }
            if (isCompress) {
                if (KBLength < 100) {
                    KBLength = 100;
                }
                L.e("TAG", "开始压缩");
                bitmap.compress(format, options, byteArrayOutputStream);
                L.e("TAG", "压缩前图片大小：" + byteArrayOutputStream.toByteArray().length);
                while (byteArrayOutputStream.toByteArray().length / 1024 > KBLength) {
                    L.e("TAG", "压缩中图片大小：" + byteArrayOutputStream.toByteArray().length);
                    byteArrayOutputStream.reset();
                    options = options > 10 ? options - 10 : 1;
                    bitmap.compress(format, options, byteArrayOutputStream);
                }
                L.e("TAG", "压缩完图片大小：" + byteArrayOutputStream.toByteArray().length);
            } else {
                L.e("TAG", "不压缩");
                bitmap.compress(format, options, byteArrayOutputStream);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArrayOutputStream.toByteArray());
            fos.flush();
            fos.close();
            return file;
        } catch (FileNotFoundException e) {
            throw new BitmapException("save file not found");
        } catch (IOException e) {
            throw new BitmapException("IO exception");
        }
    }


    /**
     * 图片缩放
     *
     * @param path       原图路径
     * @param outWidth   输出的宽度
     * @param outHeight  输出的高度
     * @param SampleSize 缩放比例
     * @return 缩放结果的图
     */
    public static Bitmap matrixScaleBitmap(String path, int outWidth, int outHeight, float SampleSize) throws FileNotFoundException {
        if (TextUtils.isEmpty(path) || !new File(path).exists()) {
            throw new FileNotFoundException("file path is null");
        }
        return matrixScaleBitmap(BitmapFactory.decodeFile(path), outWidth, outHeight, SampleSize);
    }

    /**
     * 图片缩放
     *
     * @param bitmap     原图
     * @param outWidth   输出的宽度
     * @param outHeight  输出的高度
     * @param SampleSize 缩放比例
     * @return 缩放结果的图
     */
    public static Bitmap matrixScaleBitmap(Bitmap bitmap, int outWidth, int outHeight, float SampleSize) {

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        L.e("TAG", "原图宽：" + bitmapWidth);
        L.e("TAG", "原图高：" + bitmapHeight);
        Bitmap result;
        if (SampleSize <= 0) {
            float scaleW = (float) outWidth / (float) bitmapWidth;
            float scaleH = (float) outHeight / (float) bitmapHeight;
            L.e("TAG", "宽缩放：" + scaleW);
            L.e("TAG", "高缩放：" + scaleH);
            float scale = scaleW > scaleH ? scaleH : scaleW;
            if (scale > 0) {
                L.e("TAG", "缩放比例" + scale);
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale); //长和宽放大缩小的比例
                result = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
            } else {
                L.e("TAG", "原图缩放为0");
                result = bitmap;
            }
        } else {
            Matrix matrix = new Matrix();
            matrix.postScale(SampleSize, SampleSize); //长和宽放大缩小的比例
            result = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        }

        L.e("TAG", "结果图宽：" + result.getWidth());
        L.e("TAG", "结果图高：" + result.getHeight());
        return result;
    }

}
