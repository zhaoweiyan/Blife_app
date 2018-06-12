package com.blife.blife_app.login.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.utils.util.Tools;

/**
 * Created by Somebody on 2016/8/23.
 */
public class ActivityAgreement extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreemnet);

        WebView webview = (WebView) findViewById(R.id.webview);
        Tools.WebViewSetting(instance,webview);
        webview.loadUrl("http://cdn.blife-tech.com/agreement/com_agreement.html");
    }
}
