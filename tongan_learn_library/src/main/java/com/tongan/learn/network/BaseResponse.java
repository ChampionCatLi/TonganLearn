package com.tongan.learn.network;

import java.io.InputStream;

/**
 * @author chao
 * @date 2019/7/5 14:10
 */
public class BaseResponse {
    public InputStream inputStream;
    public InputStream errorStream;
    public int code;
    public long contentLength;
    public Exception exception;

}
