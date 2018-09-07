package com.guang.jiyu.jiyu.application;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;

/**
 * Created by admin on 2018/6/20.
 */

public class JIYUApp extends MultiDexApplication{

    public static JIYUApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Utils.init(this);

    }

    public static JIYUApp getInstance() {
        return instance;
    }
}
