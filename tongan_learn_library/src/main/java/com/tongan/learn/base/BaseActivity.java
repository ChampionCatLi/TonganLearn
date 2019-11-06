package com.tongan.learn.base;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(getLayoutId());
        initView();
        initData();
        initEvent();
    }

    protected abstract void initStatusTitle(int color);

    protected void getIntentData() {

    }



    /**
     * init layout
     *
     * @return 布局ID
     */
    protected abstract int getLayoutId();

    /**
     * init  view
     */
    protected abstract void initView();

    /**
     * init data
     */
    protected abstract void initData();

    /**
     * init listener
     */
    protected abstract void initEvent();

    /**
     *
     * @param intent
     * @param resultCode
     */
    protected void startCameraActivity(Intent intent, int resultCode) {
        startActivityForResult(intent, resultCode);
    }
    @Override
    public Resources getResources() {
        //获取到resources对象
        Resources res = super.getResources();
        //修改configuration的fontScale属性
        res.getConfiguration().fontScale = 1;
        //将修改后的值更新到metrics.scaledDensity属性上
        res.updateConfiguration(null,null);
        return res;
    }

}
