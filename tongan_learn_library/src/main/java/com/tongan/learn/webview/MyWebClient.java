package com.tongan.learn.webview;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MyWebClient extends WebViewClient {



    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        return false;
    }
}
