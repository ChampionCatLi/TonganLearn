package com.tongan.learn;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Keep;
import android.text.TextUtils;

@Keep
public class StudyMessage {
    private Builder builder;

    private StudyMessage(Builder builder) {
        this.builder = builder;
    }

    public void study(Activity activity) {
        Intent intent = new Intent(activity, StudyActivity.class);
        if (!TextUtils.isEmpty(builder.studyUrl)) {
            intent.putExtra(TaConstant.TONGAN_LMS_URL, builder.studyUrl);
        }
        if (!TextUtils.isEmpty(builder.themColor)) {
            intent.putExtra(TaConstant.TONGAN_LMS_THEM, builder.themColor);
        }
        if (!TextUtils.isEmpty(builder.statusBarColor)) {
            intent.putExtra(TaConstant.TONGAN_LMS_STATUS, builder.statusBarColor);
        }

        activity.startActivity(intent);

    }


    public static class Builder {
        private String studyUrl;
        private String themColor;
        private String statusBarColor;


        public Builder setStudyUrl(String studyUrl) {
            this.studyUrl = studyUrl;
            return this;
        }

        /**
         * 设置主题颜色 具体影响到的范围请查阅
         * @param themColor
         * @return
         */
        public Builder setThemColor(String themColor) {
            this.themColor = themColor;
            return this;
        }

        /**
         * 设置状态栏 颜色值
         * @param statusBarColor  eg:  #666666
         * @return
         */
        public Builder setStatusBarColor(String statusBarColor) {
            this.statusBarColor = statusBarColor;
            return this;
        }

        public StudyMessage builder() {
            return new StudyMessage(this);
        }
    }

}
