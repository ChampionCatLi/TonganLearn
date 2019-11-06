package com.tongan.learn.network;

import java.io.InputStream;


public class BaseResponse {
    public InputStream inputStream;
    public InputStream errorStream;
    public int code;
    public long contentLength;
    public Exception exception;

}
