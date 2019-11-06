package com.tongan.learn.webview;

import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author lichao
 * @date 2019/5/22 14:06
 */
public class MyWebClient extends WebViewClient {



    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        return false;
    }
}
