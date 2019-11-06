package com.tongan.learn.bean;

import android.content.Context;
import androidx.annotation.Keep;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.tongan.learn.TaConstant;
import com.tongan.learn.contract.StudyContract;


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
        TaConstant.jsessionId = TaConstant.JSESSION_HEADER + jsessionId;
    }


    /**
     * get data and take photo
     *
     * @param clazzId 培训计划ID
     * @param type    类型
     */
    @JavascriptInterface
    public void recognition(String clazzId, String type) {
        if (studyContract != null) {
            studyContract.startGetFacePhoto(clazzId, type);
        }
    }

}
