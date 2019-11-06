package com.tongan.learn.webview;

import android.view.View;
import android.webkit.WebChromeClient;

/**
 * @author lichao
 * @date 2019/5/22 11:22
 */
public interface IWebVideo {
    void onShowCustonView(View view, WebChromeClient.CustomViewCallback customViewCallback);

    void onHideCustomeView();

    boolean isVideoState();

}
