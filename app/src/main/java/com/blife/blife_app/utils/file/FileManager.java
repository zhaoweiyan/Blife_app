package com.blife.blife_app.utils.file;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.blife.blife_app.utils.configs.CacheConfig;
import com.blife.blife_app.utils.exception.filexception.FileException;
import com.blife.blife_app.utils.exception.filexception.IOFileException;
import com.blife.blife_app.utils.exception.filexception.SdcardException;
import com.blife.blife_app.utils.util.Contant;
import com.blife.blife_app.utils.util.GetPathFromUri4kitkat;
import com.blife.blife_app.utils.util.LogUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/7/29.
 */
public class FileManager {

    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    /**
     * @return hasSD
     */
    public static boolean checkHasSD() {
        boolean hasSD = true;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (!externalStorageDirectory.exists()) {
                hasSD = false;
            }
        } else {
            hasSD = false;
        }
        return hasSD;
    }

    /**
     * 获取sd根目录
     *
     * @return
     */
    public static String getSdDir() {
        File sdDir = null;
        if (checkHasSD()) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir == null ? "" : sdDir.toString();
    }


    /**
     * 递归创建文件目录
     *
     * @param path
     */
    public static void CreateDir(String path) throws FileException {
        if (!SDcardManger.checkSDCard())
            throw new SdcardException("file SDCard is not exist");
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                throw new FileException("CreateDir is lose");
            }
        }
    }

    // 先要生成文件夹
    public static void makeRootDirectory(String filePath) throws FileException {
        if (!SDcardManger.checkSDCard())
            throw new SdcardException("file SDCard is not exist");
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            throw new FileException(" makeRootDirectory is lose");
        }

    }

    // 再生成文件
    public static File makeFilePath(String filePath, String fileName) throws FileException {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            throw new FileException(" makeFilePath is lose:" + e.getMessage());
        }
        return file;
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static boolean isFileExist(String fileName) throws IOException {
        File file = new File(Contant.AppDirPath + fileName);
        return file.exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @param fileName
     * @return
     * @throws IOException
     */
    public static boolean isFileExist(String path, String fileName) throws IOException {
        File file = new File(path + File.separator + fileName);
        return file.exists();
    }

    /**
     * 根据完整文件名获取文件
     *
     * @param fileName 文件路径名
     * @return 如果文件不存在返回null
     * @throws IOException
     */
    public static File getExitFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists())
            return file;
        else
            return null;
    }


    /**
     * 根据目录和文件名获取文件
     *
     * @param path
     * @param fileName
     * @return 若文件不存在，则返回null
     * @throws IOException
     */
    public static File getExitFile(String path, String fileName) throws IOException {
        File file = new File(path + File.separator + fileName);
        if (file.exists())
            return file;
        else
            return null;
    }


    //读取文本文件中的内容
    public static String ReadTxtFile(String strFilePath) throws IOFileException {
        String content = "";
        File file = new File(strFilePath);
        if (!file.exists() || !file.isFile()) {
            throw new FileException("file file is not exist ");
        }
        content = readTextFile(file);
        return content;
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readTextFile(File file) throws IOFileException {
        String text = null;
        InputStream is = null;
        if (!file.exists() || !file.isFile()) {
            throw new FileException("file file  is not exist or not file");
        }
        try {
            is = new FileInputStream(file);
            text = readTextInputStream(is);
            ;
        } catch (IOException e) {
            throw new IOFileException(" ReadTxtFile 失败: " + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new IOFileException(" ReadTxtFile close 失败:" + e.getMessage());
                }
            }
        }
        return text;
    }

    /**
     * 从流中读取文件
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String readTextInputStream(InputStream is) throws IOFileException {
        StringBuffer strbuffer = new StringBuffer();
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                strbuffer.append(line).append("\r\n");
            }
        } catch (IOException e) {
            throw new IOFileException("readTextInputStream 失败： " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new IOFileException(" readTextInputStream close islose: " + e.getMessage());
                }
            }
        }
        return strbuffer.toString();
    }

    /**
     * 将文本内容写入文件，不能追加文件，直接取代
     *
     * @param file
     * @param str
     * @throws IOException
     */
    public static void writeTextFile(File file, String str) throws IOFileException {
        makeFilePath(file.getParent(), file.getName());
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(file));
            out.write(str.getBytes());
        } catch (IOException e) {
            throw new IOFileException("writeTextFile失败：" + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new IOFileException("writeTextFile close is lose ：" + e.getMessage());
                }
            }
        }
    }

    /**
     * 将文本内容写入文件,可以追加文件
     *
     * @param fileName
     * @param str
     * @throws IOException
     */
    public static void writeTextFile(String fileName, String str) throws IOFileException {
        File file = new File(fileName);
        makeFilePath(file.getParent(), file.getName());
        BufferedWriter out = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            out = new BufferedWriter(outputStreamWriter);
            out.write(str);
        } catch (IOException e) {
            throw new IOFileException("writeTextFile失败： " + e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                throw new IOFileException("writeTextFile close is lose ：" + e.getMessage());
            }
        }
    }

    /**
     * 把输入流的文件写入SD卡
     *
     * @param path
     * @param fileName
     * @param is
     * @return 操作失败则返回null
     */
    public static File writeToSD(String path, String fileName, InputStream is) throws FileException {
        OutputStream os = null;
        File file = new File(path + File.separator + fileName);
        try {
            if (!file.exists())
                makeFilePath(path, fileName);

            os = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];

            while (is.read(buffer) != -1) {
                os.write(buffer);
            }

            os.flush();
        } catch (IOException e) {
            throw new FileException("流读取失败");
        } finally {
            try {
                if (os != null)
                    os.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 复制文件夹
     *
     * @param oldPath 源地址
     * @param newPath 目标地址
     * @return
     * @throws Exception
     * @since 1.0
     */
    public static boolean copyFolder(String oldPath, String newPath)
            throws Exception {
        if (!oldPath.endsWith(File.separator))
            oldPath = oldPath + File.separator;
        if (!newPath.endsWith(File.separator))
            newPath = newPath + File.separator;

        File oldFolder = new File(oldPath);
        if (oldFolder.exists()) {
            if (!oldFolder.isDirectory())
                return false;// 非有效文件夹路径，则返回。
            (new File(newPath)).mkdirs(); // 如果新文件夹不存在 则创建该文件夹

            String[] list = oldFolder.list();
            int length = list.length;
            File tmp = null;
            for (int i = 0; i < length; i++) {
                tmp = new File(oldPath + list[i]);
                if (tmp.isFile()) {
                    FileInputStream input = new FileInputStream(tmp);
                    FileOutputStream output = new FileOutputStream(newPath
                            + (tmp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                } else if (tmp.isDirectory()) {
                    copyFolder(oldPath + list[i], newPath + list[i]);
                }
            }
            return true;
        } else {
            return false;// 旧文件夹不存在，则返回。
        }
    }


    /**
     * 读取表情配置文件
     *
     * @param context
     * @return
     */
    public static List<String> getEmojiFile(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji");// 文件名字为rose.txt
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重命名文件或文件夹
     *
     * @param oldName
     * @param newName
     * @return
     * @since 1.0
     */
    public static boolean reNameFile(String oldName, String newName) {
        File file = new File(oldName);
        if (file.exists()) {
            file.renameTo(new File(newName));
            return true;
        } else
            return false;

    }

    /**
     * 将字节数组写入到SD卡文件，如果文件不存在将自动创建该文件。
     *
     * @param path
     * @param fileName
     * @param bytes
     * @return 操作失败则返回null
     */
    public static File writeToSD(String path, String fileName, byte[] bytes) throws FileException {
        OutputStream os = null;
        File file = new File(path + File.separator + fileName);
        try {
            if (!file.exists())
                makeFilePath(path, fileName);
            os = new FileOutputStream(file);
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            throw new FileException("writeToSD写入失败");
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) throws IOFileException {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            throw new IOFileException("getFileOrFilesSize失败：" + e.getMessage());
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) throws IOFileException {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            throw new IOFileException("getAutoFileOrFilesSize失败：" + e.getMessage());
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的字节
     */
    public static double getFileOrFilesSize(String filePath) throws IOFileException {
        return getFileOrFilesSize(filePath, SIZETYPE_B);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws IOFileException {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new FileException("file 文件不存在：" + e.getMessage());
            }
            try {
                size = fis.available();
            } catch (IOException e) {
                throw new IOFileException("file io is lose :" + e.getMessage());
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new FileException("file 创建文件失败： " + e.getMessage());
            }
        }
        if (size > 20 * 1024) {
            return size;
        } else {
            return 0;
        }
    }

    /**
     * 获取指定文件夹大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception, IOFileException {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        if (size > 20 * 1024) {
            return size;
        } else {
            return 0;
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    public static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }


    /**
     * @param bm
     * @param dir
     * @param picName
     * @return 全路径
     */
    public static String saveBitmap(Bitmap bm, String dir, String picName) throws IOFileException {

        String filePath = "";
        try {

            File file = new File(dir);
            if (!file.exists() || (!file.isDirectory())) {
                file.mkdirs();
            }
            filePath = dir + picName + ".png";
            File f = new File(filePath);
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return filePath;
        } catch (FileNotFoundException e) {
            throw new FileException("saveBitmap失败：" + e.getMessage());
        } catch (IOException e) {
            throw new IOFileException("saveBitmap失败：" + e.getMessage());
        }
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param b
     */
    public static void saveBitmap(Bitmap b) throws IOFileException {

        String path = CacheConfig.getDirCameraCache();
        File f = new File(path);
        if (!f.exists() || (!f.isDirectory())) {
            f.mkdirs();
        }
        long dataTake = System.currentTimeMillis();
        String jpegName = path + dataTake + ".jpg";
        Log.i("TAG", "saveBitmap:jpegName = " + jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i("TAG", "saveBitmap成功");
        } catch (IOException e) {
            throw new IOFileException("saveBitmap失败：" + e.getMessage());
        }
    }

    //保存到临时文件夹 图片压缩后小于200KB，失真度不明显
    public static Bitmap revitionImageSize(String path) throws IOFileException {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            long fileSize = in.available();
//			LogUtil.i(TAG, "初始fileSize:"+fileSize);
            in.close();

            int scale = 1;
            while (true) {
                if (fileSize < CacheConfig.BITMAP_MAX_SIZE) {
//					LogUtil.i(TAG, "fileSize:"+fileSize);
                    break;
                }
                fileSize = fileSize / 2;
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = new BufferedInputStream(new FileInputStream(new File(path)));
            return BitmapFactory.decodeStream(in, null, o2);

        } catch (FileNotFoundException e) {
            throw new FileException("revitionImageSize文件未发现：  " + e.getMessage());
        } catch (IOException e) {
            throw new IOFileException(" revitionImageSize io失败 ：" + e.getMessage());
        }
    }

    public static String getFilePathByUri(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        //4.4 kitkat以上及以下根据uri获取路径的方法
        if (isKitKat) {
            return GetPathFromUri4kitkat.getPath(context, uri);
        } else {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            return picturePath;
        }
    }

    public static Bitmap getImage(String path) {
        if (path == null || TextUtils.isEmpty(path)) {
            throw new NullPointerException();
        }
        try {
            URL url = new URL(path);
            HttpURLConnection httpURLconnection = (HttpURLConnection) url
                    .openConnection();
            httpURLconnection.setRequestMethod("GET");
            httpURLconnection.setReadTimeout(6 * 1000);
            InputStream in = null;
            byte[] b = new byte[1024];
            int len = -1;
            if (httpURLconnection.getResponseCode() == 200) {
                in = httpURLconnection.getInputStream();
                byte[] result = readStream(in);
                in.close();

                Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readStream(InputStream in) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        in.close();
        return outputStream.toByteArray();
    }

    /**
     * 删除文件夹及文件
     *
     * @param file
     */
    public static void deleteFile(File file) throws FileException {
        if (!file.exists()) {
            throw new FileException("文件不存在");
        }
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法
            }
            if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                if (files == null || files.length == 0) {
                    file.delete();
                    return;
                }
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
                file.delete();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param fileName
     */
    public static void deleteFile(String fileName) throws FileException {
        File file = new File(fileName);
        deleteFile(file);
    }

    //删除文件夹
    public static void deleteDir(String mdir) throws FileException {
        File dir = new File(mdir);
        deleteFile(dir);
    }


    /**
     * 向文件里面写入数据
     *
     * @param path    文件路径
     * @param content 文件内容
     */
    public static void writeFile(String path, String content) throws IOException {
        File file = new File(path);
        writeFile(file, content);
    }

    /**
     * 向文件里面写入数据
     *
     * @param file    文件
     * @param content 文件内容
     */
    public static void writeFile(File file, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        // 创建FileWriter对象，用来写入字符流
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);// 将缓冲对文件的输出
        bufferedWriter.write(content);
        fileWriter.flush();
        bufferedWriter.flush();
        fileWriter.close();
        bufferedWriter.close();
    }

    /**
     * @param file 需要进行文件搜索的目录
     * @param ext  过滤搜索文件类型
     * @detail 搜索sdcard文件
     */
    public static List<String> search(File file, String[] ext) throws FileException {
        List<String> list = new ArrayList<>();
        if (file == null || ext == null || ext.length == 0) {
            throw new NullPointerException();
        }
        if (!file.exists()) {
            throw new FileException("文件不存在");
        }
        if (file != null) {
            if (file.isDirectory()) {
                File[] listFile = file.listFiles();
                if (listFile != null) {
                    for (int i = 0; i < listFile.length; i++) {
                        search(listFile[i], ext);
                    }
                }
            } else {
                String filename = file.getAbsolutePath();
                for (int i = 0; i < ext.length; i++) {
                    if (filename.endsWith(ext[i])) {
                        list.add(filename);
                    }
                }
            }
        }
        return list;
    }
}
