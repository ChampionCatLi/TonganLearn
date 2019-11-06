package com.tongan.learn.network;

/**
 * @author chao
 * @date 2019/7/5 14:13
 */
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
