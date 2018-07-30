package com.guang.jiyu.jiyu.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.guang.jiyu.jiyu.application.JIYUApp;

/**
 * Created by admin on 2018/6/20.
 */

public class ToastUtils {
    public static Context context = JIYUApp.getInstance();
    public static void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void showToastInActivity(final Activity activity, final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void longToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showToast(int resId) {
        Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show();
    }
}
