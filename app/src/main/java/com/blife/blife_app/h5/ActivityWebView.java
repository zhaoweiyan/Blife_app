package com.blife.blife_app.h5;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.util.Tools;

/**
 * Created by w on 2016/8/30.
 */
public class ActivityWebView extends BaseActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webview = (WebView) findViewById(R.id.webview);
        Tools.WebViewSetting(instance, webview, true);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String title = bundle.getString(Constants.H5_TITLE_TAG);
            if (!TextUtils.isEmpty(title)) {
                initBackTopBar(title);
            } else {
                initBackTopBar(R.string.app_name);
            }
            String url = bundle.getString(Constants.H5_URL_TAG);
            if (!TextUtils.isEmpty(url)) {
                webview.loadUrl(url);
            }
        } else {
            initBackTopBar(R.string.app_name);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
