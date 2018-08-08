package com.guang.jiyu.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.Utils;
import com.guang.jiyu.jiyu.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

/**
 * Created by admin on 2018/6/19.
 */

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置app只保持竖屏
        EventBus.getDefault().register(this);
    }

    /**
     * 子类设置布局的方法
     * @param resId
     */
    protected void setView(int resId){
        View contentView = View.inflate(this, resId, null);
        setContentView(contentView);
        ButterKnife.bind(this);
    }

    /**
     * 设置activity的背景透明度
     * @param f
     */
    protected void setWindowAttr(float f){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    @Subscribe
    public void onEventMainThread(BaseEvent baseEvent) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
