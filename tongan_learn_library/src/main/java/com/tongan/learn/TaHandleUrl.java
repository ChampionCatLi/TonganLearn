package com.tongan.learn;

import android.text.TextUtils;

import androidx.annotation.Keep;

/**
 * Date: 2020/10/28
 * User: LiChao
 * 解析web url  添加版本
 */
@Keep
public class TaHandleUrl {


    public static String handleUrl(String studyUrl) {
        String TAG_J = "#";
        String TAG_C = "?";
        String TAG_A = "&";
        String endParams = "";
        String headerUrl = "";
        if (!TextUtils.isEmpty(studyUrl)) {

            if (studyUrl.contains(TAG_J)) {
                String[] splitStrArr = studyUrl.split(TAG_J);
                headerUrl = splitStrArr[0];
                endParams = TAG_J+splitStrArr[1];
            } else {
                headerUrl = studyUrl;
            }

            if (headerUrl.contains(TAG_C)) {
                headerUrl = headerUrl + TAG_A + TaConstant.TONGAN_WEB_VERSION;
            } else {
                headerUrl = headerUrl + TAG_C + TaConstant.TONGAN_WEB_VERSION;
            }

            if (!TextUtils.isEmpty(endParams)) {
                headerUrl = headerUrl + endParams;
            }
        }


        return headerUrl;

    }

}
