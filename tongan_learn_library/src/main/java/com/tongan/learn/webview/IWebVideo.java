package com.tongan.learn.webview;

import android.view.View;
import android.webkit.WebChromeClient;


public interface IWebVideo {
    void onShowCustonView(View view, WebChromeClient.CustomViewCallback customViewCallback);

    void onHideCustomeView();

    boolean isVideoState();

}
