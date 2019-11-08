package com.tongan.studydemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.tongan.learn.StudyActivity;
import com.tongan.learn.StudyMessage;
import com.tongan.learn.TaConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    //    String url = "http://flandrescarlet.gitee.io/tools/test-v/dist/#/";
    String url = "";
    String themColor = "#666666";
    String statusBarColor = "#666666";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goStudyActivityByMessage();
            }
        });
    }

    /**
     * 采用单利模式
     */
    private void goStudyActivityByMessage() {
        new StudyMessage.Builder().setStudyUrl(url).setStatusBarColor(statusBarColor).setThemColor(themColor).builder().study(this);
    }

    /***
     * 采用传统的 intent ;
     */
    private void goStudyActivityByIntent() {
        Intent intent = new Intent(this, StudyActivity.class);
        intent.putExtra(TaConstant.TONGAN_LMS_URL, url);
        intent.putExtra(TaConstant.TONGAN_LMS_STATUS, "设置状态栏颜色值");
        intent.putExtra(TaConstant.TONGAN_LMS_THEM, "设置相机页面 button 颜色值");
        startActivity(intent);
    }

}




