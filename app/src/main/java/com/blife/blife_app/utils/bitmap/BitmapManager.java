package com.blife.blife_app.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;


import com.blife.blife_app.utils.exception.bitmapexception.BitmapException;
import com.blife.blife_app.utils.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by w on 2016/7/26.
 */
public class BitmapManager {

    /**
     * [压缩]
     * 根据图片路径，压缩比例，期望宽度和高度，压缩成对应格式的图片
     *
     * @param filePath   图片路径
     * @param width      期望宽度
     * @param height     期望高度
     * @param SampleSize 压缩比例
     * @return 压缩后的Bitmap
     */
    public static Bitmap compressSaveBitmap(String filePath, int width, int height, int SampleSize) {
        return BitmapUtils.compressBitmap(filePath, width, height, SampleSize);
    }

    /**
     * [压缩]
     * 根据图片路径，期望宽度和高度，压缩图片
     *
     * @param filePath 图片路径
     * @param width    输出宽度
     * @param height   输出高度
     * @return 压缩后的Bitmap
     */
    public static Bitmap compressBitmap(String filePath, int width, int height) {
        return BitmapUtils.compressBitmap(filePath, width, height, 0);
    }

    /**
     * [压缩]
     * 根据图片路径，压缩比例，压缩图片
     *
     * @param filePath   图片路径
     * @param SampleSize 压缩比例
     * @return 压缩后的Bitmap
     */
    public static Bitmap compressBitmap(String filePath, int SampleSize) {
        LogUtils.e("拍照****filePath****" + filePath);
        return BitmapUtils.compressBitmap(filePath, 0, 0, SampleSize);
    }

    /**
     * [放大，缩小]
     * 根据图片路径，输出高宽缩放图片
     *
     * @param path      图片路径
     * @param outWidth  输出宽度
     * @param outHeight 输出高度
     * @return 缩放后的Bitmap
     */
    public static Bitmap scaleBitmap(String path, int outWidth, int outHeight) {
        Bitmap bitmap;
        try {
            bitmap = BitmapUtils.matrixScaleBitmap(path, outWidth, outHeight, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * [放大，缩小]
     * 根据图片路径，输出高宽缩放图片
     *
     * @param path       图片路径
     * @param SampleSize 缩放比例 （如果大于1表示放大，小于1表示缩小）
     * @return 缩放后的Bitmap
     */
    public static Bitmap scaleBitmap(String path, float SampleSize) {
        Bitmap bitmap;
        try {
            bitmap = BitmapUtils.matrixScaleBitmap(path, 0, 0, SampleSize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * [放大，缩小]
     * 根据原Bitmap，输出高宽，缩放图片
     *
     * @param bitmap    原Bitmap
     * @param outWidth  输出宽度
     * @param outHeight 输出高度
     * @return 缩放后的Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int outWidth, int outHeight) {
        return BitmapUtils.matrixScaleBitmap(bitmap, outWidth, outHeight, 0);
    }

    /**
     * [放大，缩小]
     * 根据原Bitmap，输出高宽缩放图片
     *
     * @param bitmap     原Bitmap
     * @param SampleSize 缩放比例 （如果大于1表示放大，小于1表示缩小）
     * @return 缩放后的Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, float SampleSize) {
        return BitmapUtils.matrixScaleBitmap(bitmap, 0, 0, SampleSize);
    }

    /**
     * [保存]
     * Bitmap转成PNG格式File
     *
     * @param bitmap   要保存的Bitmap
     * @param savePath 要形成的File
     * @return 保存的File
     */
    public static File savePNGBitmap(Bitmap bitmap, String savePath) {
        File file;
        try {
            file = BitmapUtils.BitmapToFile(bitmap, savePath, 100, Bitmap.CompressFormat.PNG);
        } catch (BitmapException e) {
            e.printStackTrace();
            file = null;
        }
        return file;
    }

    /**
     * [保存]
     * Bitmap转成JPEG格式File
     *
     * @param bitmap   要保存的Bitmap
     * @param savePath 要形成的File
     * @return 保存的File
     */
    public static File saveJPEGBitmap(Bitmap bitmap, String savePath) {
        File file;
        try {
            LogUtils.e("拍照****3");
            file = BitmapUtils.BitmapToFile(bitmap, savePath, 100, Bitmap.CompressFormat.JPEG);
        } catch (BitmapException e) {
            LogUtils.e("compressBitmap6");
            e.printStackTrace();
            file = null;
        }
        return file;
    }

    /**
     * [压缩,保存]
     * 用于图片上传前的文件压缩
     *
     * @param path     原图路径
     * @param KBLength 期望大小
     * @param savePath 压缩文件路径
     * @return 压缩后文件
     */
    public static File saveCompressBitmap(String path, int KBLength, String savePath) {
        File file;
        try {
            file = BitmapUtils.compressBmpToFile(BitmapFactory.decodeFile(path), savePath, Bitmap.CompressFormat.JPEG, 100, true, KBLength);
        } catch (BitmapException e) {
            e.printStackTrace();
            file = null;
        }
        return file;
    }

    /**
     * [压缩,保存]
     * 用于图片上传前的文件压缩
     *
     * @param path     原图路径
     * @param KBLength 期望大小
     * @param savePath 压缩文件路径
     * @return 压缩后文件
     */
    public static File saveCompressBitmap(String path, int KBLength, String savePath, Bitmap.CompressFormat format) {
        File file;
        try {
            file = BitmapUtils.compressBmpToFile(BitmapFactory.decodeFile(path), savePath, format, 100, true, KBLength);
        } catch (BitmapException e) {
            e.printStackTrace();
            file = null;
        }
        return file;
    }
    /**
     * [压缩,保存]
     * 用于图片上传前的文件压缩
     *
     * @param bitmap   原图Bitmap
     * @param KBLength 期望大小
     * @param savePath 压缩文件路径
     * @return 压缩后 文件
     */
    public static File saveCompressBitmap(Bitmap bitmap, int KBLength, String savePath) {
        File file;
        try {
            file = BitmapUtils.compressBmpToFile(bitmap, savePath, Bitmap.CompressFormat.JPEG, 500, true, KBLength);
        } catch (BitmapException e) {
            e.printStackTrace();
            file = null;
        }
        return file;
    }


    /**
     * [显示]
     * ImageView显示图片路径
     *
     * @param imageView 图片控件
     * @param imagePath 图片路径
     */
    public static void viewBitmap(ImageView imageView, String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
    }

    /**
     * [显示]
     * ImageView显示图片路径
     *
     * @param imageView 图片控件
     * @param imageFile 图片文件
     */
    public static void viewBitmap(ImageView imageView, File imageFile) {
        if (!imageFile.exists())
            return;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        imageView.setImageBitmap(bitmap);
    }
}
