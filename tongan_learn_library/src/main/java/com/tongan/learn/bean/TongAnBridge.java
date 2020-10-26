package com.tongan.learn.bean;

import android.content.Context;

import androidx.annotation.Keep;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.tongan.learn.TaConstant;
import com.tongan.learn.contract.StudyContract;

import org.json.JSONException;
import org.json.JSONObject;


@Keep
public class TongAnBridge {

    private Context context;
    private StudyContract studyContract;

    public TongAnBridge(Context context, StudyContract studyContract) {
        this.context = context;
        this.studyContract = studyContract;

    }

    @JavascriptInterface
    public void alert(String message) {
        Toast.makeText(context, "hello!    " + message, Toast.LENGTH_LONG).show();
    }


    @JavascriptInterface
    public void call(String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * get jsession Id  from h5
     *
     * @param jsessionId very useful data
     */
    @JavascriptInterface
    public void setJSessionId(String jsessionId) {
        TaConstant.jsessionId = TaConstant.TA_JSESSION_HEADER + jsessionId;
    }

//  此方法 在1.1.1(包含)使用,之后将采用新方法 @androidRecognition(String infoStr)
//   2020年10月26日14:13:09
//    @JavascriptInterface
//    public void recognition(String clazzId, String type) {
//        Log.i("cat-chao","type:::"+type+":::clazzId::"+clazzId);
//    }

    @JavascriptInterface
    public void  androidRecognition(String infoStr){
      if (!TextUtils.isEmpty(infoStr)){
            studyContract.startGetFacePhoto(infoStr);
        }
    }


}
