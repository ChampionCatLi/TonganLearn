package com.tongan.learn.network;


public abstract class CallBackString extends BaseCallBack<String> {
    @Override
    protected String onParseResponse(BaseResponse response) {
        try {
            return StringUtil.getRetString(response.inputStream);
        } catch (Exception e) {
            throw new RuntimeException("callBackString failure");
        }
    }
}
