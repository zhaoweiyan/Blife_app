package com.blife.blife_app.utils.net.request;


import com.blife.blife_app.utils.exception.netexception.NetException;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.util.LogUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by w on 2016/8/12.
 */
public class OKHttpManager {

    private static OkHttpClient client;
    private static OkHttpClient clientFile;
    private OkHttpClient clientLocation;
    private static OkHttpClient.Builder builder;
    private UIHandler uiHandler;
    private static OKHttpManager instance;
    private Map<String, String> header;
    private Map<String, String> params;


    private OKHttpManager() {
        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }
        if (uiHandler == null)
            uiHandler = new UIHandler();
        header = new HashMap<>();
        params = new HashMap<>();
    }

    /**
     * 获取参数
     *
     * @return
     * @throws NetException
     */
    public Map<String, String> getParams() throws NetException {
        if (params == null)
            throw new NetException("Params is NUll");
        return params;
    }

    /**
     * 获取头部
     *
     * @return
     * @throws NetException
     */
    public Map<String, String> getHeader() throws NetException {
        if (header == null)
            throw new NetException("Header is NUll");
        return header;
    }

    /**
     * 单例
     *
     * @return
     */
    public static OKHttpManager getInstance() {
        if (instance == null) {
            instance = new OKHttpManager();
        }
        return instance;
    }

    /**
     * 设置HttpOption
     *
     * @param httpOption
     */
    public void setOption(HttpOption httpOption) {
        if (builder != null) {
            try {
                builder.connectTimeout(httpOption.getConnectTimeout(), TimeUnit.MILLISECONDS);
                builder.readTimeout(httpOption.getReadTimeout(), TimeUnit.MILLISECONDS);
                builder.writeTimeout(httpOption.getWriteTimeout(), TimeUnit.MILLISECONDS);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            client = builder.build();
        }
    }

    /**
     * 获取请求Client
     *
     * @return
     * @throws NetException
     */
    public static OkHttpClient getClient() throws NetException {
        if (builder == null) {
            throw new NetException("OKHttp Builder is Null");
        }
        if (client == null) {
            client = builder.build();
        }
        return client;
    }

    /**
     * 发起请求
     *
     * @param option
     * @param callback
     */
    public void baseRequest(RequestOption option, ResultCallback callback) {
        Request request = option.getRequest();
        if (option.isPutFile()) {
            if (clientFile == null) {
                builder.connectTimeout(30000, TimeUnit.MILLISECONDS);
                builder.writeTimeout(30000, TimeUnit.MILLISECONDS);
                builder.readTimeout(30000, TimeUnit.MILLISECONDS);
                clientFile = builder.build();
            }
            LogUtils.e("timeout****上传文件的client****" + clientFile.connectTimeoutMillis());
            clientFile.newCall(request).enqueue(new Result(option, uiHandler, callback));
        } else {
            if (option.isLocation()) {
                if (clientLocation == null) {
                    builder.connectTimeout(5000, TimeUnit.MILLISECONDS);
                    builder.writeTimeout(5000, TimeUnit.MILLISECONDS);
                    builder.readTimeout(5000, TimeUnit.MILLISECONDS);
                    clientLocation = builder.build();
                }
                LogUtils.e("timeout****上传位置的client****" + clientLocation.connectTimeoutMillis());
                clientLocation.newCall(request).enqueue(new CallBack());
            } else {
                if (client == null) {
                    builder.connectTimeout(5000, TimeUnit.MILLISECONDS);
                    builder.writeTimeout(5000, TimeUnit.MILLISECONDS);
                    builder.readTimeout(5000, TimeUnit.MILLISECONDS);
                    client = builder.build();
                }
                LogUtils.e("timeout****普通接口的client****" + client.connectTimeoutMillis());
                client.newCall(request).enqueue(new Result(option, uiHandler, callback));
            }

        }
    }

    class CallBack implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            LogUtils.e("上传位置失败****");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            LogUtils.e("上传位置成功****" + response.body().string());
        }
    }
    /**
     * 关闭指定请求
     *
     * @param tag
     */
    public void closeRequest(Object... tag) {
        if (client == null)
            return;
        List<Call> list = client.dispatcher().queuedCalls();
        for (Call call : list) {
            for (Object t : tag) {
                if (!call.isCanceled() && call.request().tag().equals(t)) {
                    call.cancel();
                }
            }
        }
    }

    /**
     * 关闭所有请求
     */
    public void closeAllRequest() {
        if (client == null)
            return;
        client.dispatcher().cancelAll();
    }

}
