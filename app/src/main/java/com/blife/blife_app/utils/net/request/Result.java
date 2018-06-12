package com.blife.blife_app.utils.net.request;

import android.text.TextUtils;


import com.blife.blife_app.R;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by w on 2016/8/12.
 */
public class Result implements Callback {

    private UIHandler UIHandler;
    private RequestOption option;
    private final int HTTP_OK = 200;

    public Result(RequestOption option, UIHandler handler, ResultCallback callback) {
        this.option = option;
        this.UIHandler = handler;
        UIHandler.setCallback(callback);
    }

    @Override
    public void onResponse(Call call, Response response) {
        try {
            if (response.code() == HTTP_OK) {
                if (!TextUtils.isEmpty(option.getFilePath())) {
                    createFile(UIHandler, response, option.getFilePath());
                } else {
                    sendSuccessMessage(response.request().tag(), Unicode2GBK(response.body().string()));
                }
            } else {
                LogUtils.e("null*****后台错误" + "**tag==" + call.request().tag());
                sendErrorMessage(response.request().tag(), Unicode2GBK(response.body().string()));
//                sendErrorMessage(response.request().tag(), response.body().string());
            }
        } catch (IOException e) {

            LogUtils.e("null*****io异常" + "**tag==" + call.request().tag());
            e.printStackTrace();
            sendErrorMessage(response.request().tag(), e.getMessage() != null ? e.getMessage() : "");
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if(e.getCause()==null){
        }else{
            LogUtils.e("null*****请求异常" + "**tag==" +e.getCause());
            LogUtils.e("null*****请求异常" + "**tag==" +e.getCause().toString().trim().equals(SocketTimeoutException.class.getName().trim()));
        }
        if(e.getCause()!=null){
            if(e.getCause().toString().equals(SocketTimeoutException.class.getName())){
                UIHandler.sendErrorMessage(call.request().tag(), "{\"code\":\"44444444\",\"message\":\"ERROR\",\"detail\":\"请检查网络设置\"}");
            }else{
                UIHandler.sendErrorMessage(call.request().tag(), e.getMessage() != null ? e.getMessage() : "");
            }
        }else{
            UIHandler.sendErrorMessage(call.request().tag(), "{\"code\":\"44444444\",\"message\":\"ERROR\",\"detail\":\"请检查网络设置\"}");
        }
    }

    private void sendSuccessMessage(Object tag, String message) {
        UIHandler.sendSuccessMessage(tag, message);
    }

    private void sendErrorMessage(Object tag, String message) {
        UIHandler.sendErrorMessage(tag, message);
    }

    private void createFile(UIHandler handler, Response response, String savePath) {
        try {
            InputStream inputStream = null;
            byte[] bytes = new byte[1024];
            int length = 0;
            FileOutputStream fileOutputStream = null;
            inputStream = response.body().byteStream();
            long totalLength = response.body().contentLength();
            File saveFile = new File(savePath);

            fileOutputStream = new FileOutputStream(saveFile);
            long sum = 0;

            while ((length = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
                sum += length;
                handler.sendDownLoadProgressMessage(((int) sum), ((int) totalLength));
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            handler.sendDownLoadFinishMessage(saveFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            handler.sendErrorMessage(response.request().tag(), "save File not found");
            L.e("save File not found");
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendErrorMessage(response.request().tag(), "stream read exception");
            L.e("stream read exception");
        }


    }

    public static String Unicode2GBK(String dataStr) {
        int index = 0;
        StringBuffer buffer = new StringBuffer();

        int li_len = dataStr.length();
        while (index < li_len) {
            if (index >= li_len - 1
                    || !"\\u".equals(dataStr.substring(index, index + 2))) {
                buffer.append(dataStr.charAt(index));

                index++;
                continue;
            }

            String charStr = "";
            charStr = dataStr.substring(index + 2, index + 6);

            char letter = (char) Integer.parseInt(charStr, 16);

            buffer.append(letter);
            index += 6;
        }

        return buffer.toString();
    }
}
