package com.tongan.studydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tongan.learn.StudyActivity;
import com.tongan.learn.StudyMessage;
import com.tongan.learn.TaConstant;
import com.tongan.learn.camera.CameraActivity;

public class MainActivity extends AppCompatActivity {
//

    //    String url = "http://flandrescarlet.gitee.io/tools/test-v/dist/#/";
    String url = "http://192.168.3.95:8080/c/";
    String themColor = "#666666";
    String statusBarColor = "#666666";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = findViewById(R.id.edit_text);
//        editText.setText(url);
        findViewById(R.id.textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                url=editText.getText().toString();
//                if (TextUtils.isEmpty(url)) {
//                    Toast.makeText(MainActivity.this, "请输入URL", Toast.LENGTH_SHORT).show();
//                } else {
//                    goStudyActivity();
//                }
                goCameraActivity();
            }
        });
    }

    private void goCameraActivity() {
        startActivity(new Intent(this, CameraActivity.class));
    }

    private void goStudyActivity() {
//        new StudyMessage.Builder().setStudyUrl(url).setStatusBarColor(statusBarColor).setThemColor(themColor).builder().study(this);
        Intent intent = new Intent(this, StudyActivity.class);
        intent.putExtra(TaConstant.TONGAN_LMS_URL, url);
        startActivity(intent);

    }

}




