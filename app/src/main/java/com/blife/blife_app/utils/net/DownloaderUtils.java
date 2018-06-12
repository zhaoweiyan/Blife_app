package com.blife.blife_app.utils.net;

import android.text.TextUtils;

import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.DownloadCallback;
import com.blife.blife_app.utils.util.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by w on 2016/9/30.
 */
public class DownloaderUtils implements Callback {

    private String TAG;
    private static DownloaderUtils instance;
    private OkHttpClient client;
    private OkHttpClient.Builder builder;
    private DownloadCallback downloadCallback;
    private String SavePath;

    private final int HTTP_OK = 200;

    public DownloaderUtils() {
        if (builder == null) {
            builder = new OkHttpClient.Builder();
            client = builder.build();
        }
    }

    public static DownloaderUtils getInstance() {
        if (instance == null)
            instance = new DownloaderUtils();
        return instance;
    }

    public void download(String url, String saveFile, DownloadCallback downloadCallback) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(saveFile)) {
            downloadCallback.onError("TAG", "URL is NUll");
        } else {
            TAG = url + saveFile;
            SavePath = saveFile;
            this.downloadCallback = downloadCallback;
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(this);
        }
    }

    private void createFile(Response response) {
        try {
            InputStream inputStream = null;
            byte[] bytes = new byte[2048];
            int length = 0;
            FileOutputStream fileOutputStream = null;
            inputStream = response.body().byteStream();
            long totalLength = response.body().contentLength();
            File saveFile = new File(SavePath);

            fileOutputStream = new FileOutputStream(saveFile);
            long sum = 0;

            while ((length = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
                sum += length;
                downloadCallback.onProgress(((int) sum), ((int) totalLength));
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            downloadCallback.onFinish(saveFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            downloadCallback.onError(response.request().tag(), "save File not found");
            L.e("save File not found");
        } catch (IOException e) {
            e.printStackTrace();
            downloadCallback.onError(response.request().tag(), "stream read exception");
            L.e("stream read exception");
        }


    }

    @Override
    public void onFailure(Call call, IOException e) {
        downloadCallback.onError(call.request().tag(), e.getMessage() != null ? e.getMessage() : "");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.code() == HTTP_OK) {
            createFile(response);
        } else {
            downloadCallback.onError(response.request().tag(), Tools.Unicode2GBK(response.body().string()));
        }
    }

    public String getTAG() {
        return TAG;
    }
}
