package com.blife.blife_app.utils.net.request;

import android.text.TextUtils;


import com.blife.blife_app.utils.exception.netexception.NetException;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.NetWorkUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by w on 2016/8/8.
 */
public class HttpRequest {

    private OKHttpManager okHttpManager;
    private Map<String, String> header;
    private Map<String, String> params;
    private ResultCallback callback;

    public HttpRequest(ResultCallback callback) {
        okHttpManager = OKHttpManager.getInstance();
        this.callback = callback;
    }

    /**
     * 获取请求头
     *
     * @return
     */
    public Map<String, String> getHeader() {
        try {
            header = okHttpManager.getHeader();
        } catch (NetException e) {
            e.printStackTrace();
            header = new HashMap<>();
        }
        return header;
    }

    /**
     * 获取请求参数
     *
     * @return
     */
    public Map<String, String> getParams() {
        try {
            params = okHttpManager.getParams();
        } catch (NetException e) {
            e.printStackTrace();
            params = new HashMap<>();
        }
        return params;
    }

    /**
     * get
     *
     * @param tag
     * @param url
     */
    public void get(Object tag, String url) {
        Request.Builder builder = new Request.Builder();
        try {
            builderHeaderAndParamsAndTag(builder, url, tag);
            builder.get();
            createRequestOption(builder);
        } catch (NetException e) {
            e.printStackTrace();
        }
    }

    /**
     * POSTPayload
     *
     * @param tag
     * @param url
     * @param json
     */
    public void postPayload(Object tag, String url, String json) {
        try {
            Request.Builder builder = new Request.Builder();
            RequestBody body = createRequestJsonBody(json);
            builderHeaderAndParamsAndTag(builder, url, tag);
            builder.post(body);
            createRequestOption(builder);
        } catch (NetException e) {
            e.printStackTrace();
        }
    }

    /**
     * POSTPayload,上传位置为了不回调，单独拿出来
     *
     * @param tag
     * @param url
     * @param json
     */
    public void postPayload(Object tag, String url, String json, boolean isLocation) {
        try {
            Request.Builder builder = new Request.Builder();
            RequestBody body = createRequestJsonBody(json);
            builderHeaderAndParamsAndTag(builder, url, tag);
            builder.post(body);
            createRequestOption(builder, isLocation);
        } catch (NetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Post表单
     *
     * @param tag
     * @param url
     */
    public void postForm(Object tag, String url) {
        try {
            Request.Builder builder = new Request.Builder();
            FormBody body = createFormBody();
            builderHeaderAndParamsAndTag(builder, url, tag);
            builder.post(body);
            createRequestOption(builder);
        } catch (NetException e) {
            e.printStackTrace();
        }
    }

    /**
     * PUTPayload
     *
     * @param tag
     * @param url
     * @param json
     */
    public void putPayload(Object tag, String url, String json) {
        try {
            Request.Builder builder = new Request.Builder();
            RequestBody body = createRequestJsonBody(json);
            builderHeaderAndParamsAndTag(builder, url, tag);
            builder.put(body);
            createRequestOption(builder);
        } catch (NetException e) {
            e.printStackTrace();
        }

    }

    /**
     * PUT上传文件
     *
     * @param tag
     * @param url
     * @param file
     */
    public void putFile(Object tag, String url, File file) {
        try {
            Request.Builder builder = new Request.Builder();
            RequestBody body = createRequestFileBody(file);
            builderHeaderAndParamsAndTag(builder, url, tag);
            builder.put(body);
//            createRequestOption(builder);
            RequestOption option = new RequestOption();
            option.setPutFile(true);
            option.setRequest(builder.build());
            request(option, callback);
        } catch (NetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除Payload
     *
     * @param tag
     * @param url
     * @param json
     */
    public void deletePayload(Object tag, String url, String json) {
        try {
            Request.Builder builder = new Request.Builder();
            RequestBody body = createRequestJsonBody(json);
            builderHeaderAndParamsAndTag(builder, url, tag);
            builder.delete(body);
            createRequestOption(builder);
        } catch (NetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载
     *
     * @param url
     * @param saveFile
     * @param downloadCallback
     */
    public void download(String url, String saveFile, DownloadCallback downloadCallback) {
        if (TextUtils.isEmpty(url)) {
            downloadCallback.onError("TAG", "URL is NUll");
            return;
        }
        Request request = new Request.Builder().url(url).build();
        RequestOption option = new RequestOption();
        option.setRequest(request);
        option.setFilePath(saveFile);
        request(option, downloadCallback);
    }

    /**
     * 创建表单
     *
     * @return
     */
    private FormBody createFormBody() {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        params.clear();
        return builder.build();
    }

    /**
     * 创建文件上传Payload
     *
     * @param file
     * @return
     */
    private RequestBody createRequestFileBody(File file) {
        String name = file.getName();
        String dex = name.substring(name.indexOf(".") + 1);
        MediaType mediaType = MediaType.parse("image/" + dex);
        return RequestBody.create(mediaType, file);
    }


    /**
     * 创建Json请求体
     *
     * @param json
     * @return
     */
    private RequestBody createRequestJsonBody(String json) {
        MediaType mediaType = MediaType.parse("application/json");
        return RequestBody.create(mediaType, json);
    }

    /**
     * 设置请求头和请求参数
     *
     * @param builder
     * @param url
     * @param tag
     * @throws NetException
     */
    private void builderHeaderAndParamsAndTag(Request.Builder builder, String url, Object tag) throws NetException {
        if (TextUtils.isEmpty(url)) {
            throw new NetException("URL is NULL");
        }
        builder.tag(tag);
        if (params.isEmpty()) {
            builder.url(url);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
            String pam = stringBuilder.toString();
            String sUrl = url + "?" + pam.substring(0, pam.length() - 1);
            builder.url(sUrl);
        }
        if (!header.isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        params.clear();
        header.clear();
    }

    /**
     * 构建请求
     *
     * @param builder
     */
    private void createRequestOption(Request.Builder builder) {
        RequestOption option = new RequestOption();
        option.setRequest(builder.build());
        L.e("HTTP_TAG", "URL:" + builder.build().url());
        request(option, callback);
    }

    /**
     * 构建请求
     *
     * @param builder     isLocation 为了区分是否上传位置
     */
    private void createRequestOption(Request.Builder builder, boolean isLocation) {
        RequestOption option = new RequestOption();
        option.setRequest(builder.build());
        option.setLocation(isLocation);
        L.e("HTTP_TAG", "URL:" + builder.build().url());
        request(option, callback);
    }

    /**
     * 发起请求
     *
     * @param option
     * @param callback
     */
    private void request(RequestOption option, ResultCallback callback) {
        okHttpManager.baseRequest(option, callback);
    }


    /**
     * 关闭请求
     *
     * @param tag
     */
    public void closeRequest(Object... tag) {
        okHttpManager.closeRequest(tag);
    }

    /**
     * 关闭全部请求
     */
    public void closeAllRequest() {
        okHttpManager.closeAllRequest();
    }


}
