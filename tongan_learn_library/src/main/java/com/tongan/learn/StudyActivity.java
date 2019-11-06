package com.tongan.learn;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongan.learn.base.BaseActivity;
import com.tongan.learn.camera.CameraActivity;
import com.tongan.learn.contract.StudyContract;
import com.tongan.learn.webview.Eventlnterceptor;
import com.tongan.learn.webview.TitleListener;
import com.tongan.learn.webview.WebHelper;

public class StudyActivity extends BaseActivity implements StudyContract, TitleListener {

    private WebHelper webHelper;
    private WebView webview;
    private Eventlnterceptor eventlnterceptor;
    private TextView titleText;
    private ImageView titleBack;
    private String tongAnStudyUrl;
    private boolean isChangeThemColor;

    @Override
    protected void getIntentData() {
        super.getIntentData();
        Intent intent = getIntent();
        tongAnStudyUrl = intent.getStringExtra(TaConstant.TONGAN_LMS_URL);
        String themColor = intent.getStringExtra(TaConstant.TONGAN_LMS_THEM);
        String statusBaeColor = intent.getStringExtra(TaConstant.TONGAN_LMS_STATUS);
        String cameraTipsColor = intent.getStringExtra(TaConstant.TONGAN_LMS_CAMERA_TIPS);
        if (!TextUtils.isEmpty(themColor)) {
            TaConstant.themColor = Color.parseColor(themColor);
            isChangeThemColor = true;
        }
        if (!TextUtils.isEmpty(cameraTipsColor)) {
            TaConstant.cameraTipsColor = Color.parseColor(cameraTipsColor);
        }
        if (!TextUtils.isEmpty(statusBaeColor)) {
            initStatusTitle(Color.parseColor(statusBaeColor));
        } else {
            initStatusTitle(TaConstant.themColor);
        }

    }

    @Override
    protected void initStatusTitle(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.tong_an_activity_study;
    }

    @Override
    protected void initView() {
        webview = findViewById(R.id.webview);
        titleText = findViewById(R.id.tong_an_title_text);
        titleBack = findViewById(R.id.tong_an_title_back);
        if (isChangeThemColor) {
            titleText.setTextColor(TaConstant.themColor);
        }
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(tongAnStudyUrl)) {
            webHelper = new WebHelper().setWebView(webview).init(this).LoadUrl(tongAnStudyUrl).setTitleListener(this);
            eventlnterceptor = webHelper.getEvent();
        } else {
            Toast.makeText(this, getString(R.string.tong_an_learn_url_err_tips), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void initEvent() {
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    StudyActivity.this.finish();
                }

            }
        });
    }


    @Override
    public void startGetFacePhoto(String clazzId, String type) {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(TaConstant.TYPE, type);
        intent.putExtra(TaConstant.CAZZ_ID, clazzId);
        startCameraActivity(intent, TaConstant.STUDY_ACTIVITY_CODE);
    }

    /**
     * 向js 发出通知验证成功
     */
    protected void faceRecognitionSuccess() {
        if (webview != null) {
            webview.post(new Runnable() {
                @Override
                public void run() {
                    webview.evaluateJavascript("window.TongAnBridge.recognitionSuccess()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });
                }
            });
        }
    }

    /**
     * 向js 发出人脸验证失败
     */
    protected void faceRecognitionFailed() {
        if (webview != null) {
            webview.post(new Runnable() {
                @Override
                public void run() {
                    webview.evaluateJavascript("window.TongAnBridge.recognitionFailed()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }
            });
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && eventlnterceptor != null && keyCode == KeyEvent.KEYCODE_BACK) {
            eventlnterceptor.event();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webHelper != null) {
            webHelper.onResume();
        }

    }

    @Override
    protected void onPause() {
        if (webHelper != null) {
            webHelper.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (webHelper != null) {
            webHelper.onDestroy();
        }
        super.onDestroy();
    }


    @Override
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title) && titleText != null) {
            titleText.setText(title);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == TaConstant.STUDY_ACTIVITY_CODE) {
            boolean isSuccess = data.getBooleanExtra(TaConstant.FACE_IS_SUCCESS, false);
            if (isSuccess) {
                faceRecognitionSuccess();
            } else {
                faceRecognitionFailed();
            }

        }
    }


}
