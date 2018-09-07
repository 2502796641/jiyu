package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.jiyu.utils.ActivityUtils;

/**
 * Created by admin on 2018/9/7.
 */

public class WelcomeActivity extends BaseActivity{
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_welcome);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.startActivity(WelcomeActivity.this,MainActivity.class);
                finish();
            }
        },2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }
}
