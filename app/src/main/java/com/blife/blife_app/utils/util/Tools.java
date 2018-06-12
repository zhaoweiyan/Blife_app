package com.blife.blife_app.utils.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blife.blife_app.utils.logcat.L;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by w on 2016/8/4.
 */
public class Tools {

    public static void WebViewSetting(Context context, WebView webView) {
        WebViewSetting(context, webView, false);
    }

    /**
     * 设置WebView
     *
     * @param context 上下文
     * @param webView WebView控件
     */
    public static void WebViewSetting(final Context context, final WebView webView, boolean viewLoadUrl) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不支持缓存


        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        if (viewLoadUrl) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    L.e("TAG", "WEBVIEW--URL:" + url);
                    webView.loadUrl(url);
                    return true;
                }
            });
        }
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                L.e("TAG", "WEBVIEW--DOWNLOAD--URL:" + url);
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    L.e("TAG", "WEBVIEW--DOWNLOAD--URL--E:" + e.toString());
                }

            }
        });
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

    /**
     * 安装更新包
     *
     * @param context
     * @param _file
     */
    public static void installApp(Context context, File _file) {
        if (_file.getName().endsWith(".apk")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(_file),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    /**
     * 验证码倒计时
     *
     * @param button
     */
    public static void YanZhengMaTimer(final Button button) {
        final String text = button.getText().toString();
        final int time = 120;

        Handler handler = new Handler() {
            int s = time;
            int i = 0;

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (i <= time) {
                    i++;
                    button.setText(s + "s");
                    s--;
                    sendMessageDelayed(obtainMessage(), 1000);
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                    button.setText(text);
                }
            }
        };
        handler.sendMessage(handler.obtainMessage());
    }

    /**
     * 验证码倒计时
     *
     * @param button
     */
    public static void YanZhengMaTimer(final TextView button) {
        final String text = button.getText().toString();
        final int time = 120;

        Handler handler = new Handler() {
            int s = time;
            int i = 0;

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (i <= time) {
                    i++;
                    button.setText(s + "s");
                    s--;
                    sendMessageDelayed(obtainMessage(), 1000);
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                    button.setText(text);
                }
            }
        };
        handler.sendMessage(handler.obtainMessage());
    }

    public static void setEmojiInputFilter(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher matcher = pattern.matcher(source);
                if (matcher.find()) {
                    return "";
                }
                return null;
            }
        }});
    }

    public static void setEmojiAndLengthInputFilter(EditText editText, final int mMax) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher matcher = pattern.matcher(source);
                if (matcher.find()) {
                    return "";
                }
                int keep = mMax - (dest.length() - (dend - dstart));
                if (keep <= 0) {
                    return "";
                } else if (keep >= end - start) {
                    return null; // keep original
                } else {
                    keep += start;
                    if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                        --keep;
                        if (keep == start) {
                            return "";
                        }
                    }
                    return source.subSequence(start, keep);
                }
            }
        }});
    }

}
