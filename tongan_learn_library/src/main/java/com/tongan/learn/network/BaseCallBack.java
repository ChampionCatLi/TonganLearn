package com.tongan.learn.network;

import android.os.Handler;
import android.os.Looper;

import static com.tongan.learn.network.StringUtil.getRetString;

/**
 * @author chao
 * @date 2019/7/5 14:09
 */
public abstract class BaseCallBack<T> {
    static Handler mMainHandler = new Handler(Looper.getMainLooper());

    public void onProgress(float progress, long total) {
    }

    void onError(final BaseResponse response) {

        final String errorMessage;
        if (response.inputStream != null) {
            errorMessage = getRetString(response.inputStream);
        } else if (response.errorStream != null) {
            errorMessage = getRetString(response.errorStream);
        } else if (response.exception != null) {
            errorMessage = response.exception.getMessage();
        } else {
            errorMessage = "";
        }
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(response.code, errorMessage);
            }
        });
    }

    protected abstract void onFailure(int code, String errorMessage);

    void onSeccess(BaseResponse response) {
        final T obj = onParseResponse(response);
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                onResponse(obj);
            }
        });
    }

    protected abstract void onResponse(T obj);

    protected abstract T onParseResponse(BaseResponse response);


}
