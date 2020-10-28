package com.tongan.studydemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tongan.learn.StudyActivity;
import com.tongan.learn.StudyMessage;
import com.tongan.learn.TaConstant;
import com.tongan.learn.TaHandleUrl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    //    String url = "http://flandrescarlet.gitee.io/tools/test-v/dist/#/";
//    String url = "http://59.110.139.185/c/?_t=5&cc=hangzhou&name=%E9%A9%AC%E6%9E%97&tid=1&ts=1602486630020&id=malin111&sign=EAC3BCFAAB0DABA19EA2BFC96D77E944";
    String url = "http://59.110.139.185/c";
    //    String  url ="http://192.168.3.104:8080/c#/test";
//    String  url ="http://www.anjia365.com/app/yszc/";
    String themColor = "#ff83b912";
    String statusBarColor = "#ff83b912";

    String testUrl1 = "http://www.baidu.com/c/";
    String testUrl2 = "http://www.baidu.com/c/#/class/list";
    String testUrl3 = "http://www.baidu.com/c/index.html#/class/list";
    String testUrl4 = "http://www.baidu.com/c/index.html#/class/list?_cid=1&tid=210";
    String testUrl5 = "http://www.baidu.com/c/?_t=5&cc=28&name=%E5%91%A8%E5%BB%BA%E5%8D%8E&tid=4&ts=1603174065088&id=a513cee6aafc11e991f2506b4b1d72d2&sign=4510B6F28DDE62984E011D37CCF4B3BD";
    String testUrl6 = "http://www.baidu.com/c/index.html?_t=5&cc=28&name=%E5%91%A8%E5%BB%BA%E5%8D%8E&tid=4&ts=1603174065088&id=a513cee6aafc11e991f2506b4b1d72d2&sign=4510B6F28DDE62984E011D37CCF4B3BD";
    String testUrl7 = "http://www.baidu.com/c/?_t=5&cc=hangzhou&name=%E9%A9%AC%E6%9E%97&tid=1&ts=1602486630020&id=malin111&sign=EAC3BCFAAB0DABA19EA2BFC96D77E944#/class/detail";
    String testUrl8 = "http://www.baidu.com/c/?_t=5&cc=hangzhou&name=%E9%A9%AC%E6%9E%97&tid=1&ts=1602486630020&id=malin111&sign=EAC3BCFAAB0DABA19EA2BFC96D77E944#/class/detail?_cid=132&tid=288";
    String testUrl9 = "http://www.baidu.com/c/index.html?_t=5&cc=hangzhou&name=%E9%A9%AC%E6%9E%97&tid=1&ts=1602486630020&id=malin111&sign=EAC3BCFAAB0DABA19EA2BFC96D77E944#/class/detail?_cid=132&tid=288";

    int testUrlIndex = 1;
    private TextView showUrl;
    private TextView egUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        egUrl = findViewById(R.id.eg_url);
        showUrl = findViewById(R.id.show_url);
        findViewById(R.id.textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goStudyActivityByMessage();

//                getTestUrl(testUrlIndex);
//                testUrlIndex++;

            }
        });
    }


    private void getTestUrl(int index) {
        String tempUrl = "";
        switch (index) {
            case 1:
                tempUrl = testUrl1;
                break;
            case 2:
                tempUrl = testUrl2;
                break;
            case 3:
                tempUrl = testUrl3;
                break;
            case 4:
                tempUrl = testUrl4;
                break;
            case 5:
                tempUrl = testUrl5;
                break;
            case 6:
                tempUrl = testUrl6;
                break;
            case 7:
                tempUrl = testUrl7;
                break;
            case 8:
                tempUrl = testUrl8;
                break;
            case 9:
                tempUrl = testUrl9;
                break;
        }
        if (testUrlIndex==9){
            testUrlIndex=1;
        }
        testUrl(tempUrl);
    }

    /**
     * 采用链式跳转
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


    private void testUrl(String url) {
        egUrl.setText("测试url:   "+url);
        String getUrl = TaHandleUrl.handleUrl(url);
        showUrl.setText("结果url:   "+getUrl);
        Log.i("cat-chao", "testUrlIndex:::::" + testUrlIndex + "::::::getUrl:::" + getUrl);
    }

}




