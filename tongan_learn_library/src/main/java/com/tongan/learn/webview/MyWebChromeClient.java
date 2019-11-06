package com.tongan.learn.webview;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class MyWebChromeClient extends WebChromeClient {

    private TitleListener titleListener;
    private final IWebVideo iWebVideo;


    public MyWebChromeClient(IWebVideo iWebVideo) {
        this.iWebVideo = iWebVideo;
    }


    public void setTitleListener(TitleListener titleListener) {
        this.titleListener = titleListener;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (!TextUtils.isEmpty(title) && titleListener != null) {

            titleListener.setTitle(title);


        }

    }

    @Override
    public void onHideCustomView() {
        if (iWebVideo != null) {
            iWebVideo.onHideCustomeView();
        }

    }


    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (iWebVideo != null) {
            iWebVideo.onShowCustonView(view, callback);
        }
    }
}
