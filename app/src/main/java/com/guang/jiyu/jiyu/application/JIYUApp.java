package com.guang.jiyu.jiyu.application;

import android.app.Application;

/**
 * Created by admin on 2018/6/20.
 */

public class JIYUApp extends Application{

    public static JIYUApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static JIYUApp getInstance() {
        return instance;
    }
}
