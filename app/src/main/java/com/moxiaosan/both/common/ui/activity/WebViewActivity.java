package com.moxiaosan.both.common.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moxiaosan.both.R;
import com.utils.ui.base.BaseActivity;

public class WebViewActivity extends BaseActivity {
    private String webUrl;
    private String title;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webUrl = getIntent().getStringExtra("webUrl");
        title = getIntent().getStringExtra("title");
        showActionBar(true);
        setActionBarName(title);

        webView = (WebView) findViewById(R.id.webId);

        if (Build.VERSION.SDK_INT >= 19) // KITKAT
        {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        // 使能JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        // 支持中文，否则页面中中文显示乱码
        webView.getSettings().setDefaultTextEncodingName("GBK");

        webView.getSettings().setBuiltInZoomControls(false);

        // 限制在WebView中打开网页，而不用默认浏览器
        webView.setWebViewClient(new MyWebViewClient());
        showLoadingDialog();
        webView.loadUrl(webUrl);
    }

    @Override
    protected String getUrl(String nameUrl) {
        return null;
    }

    private class MyWebViewClient extends WebViewClient {

        // 重写父类方法，让新打开的网页在当前的WebView中显示
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissLoadingDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
