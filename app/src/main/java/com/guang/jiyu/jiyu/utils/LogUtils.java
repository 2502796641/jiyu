package com.guang.jiyu.jiyu.utils;

import android.util.Log;

/**
 * Created by admin on 2018/9/7.
 */

public class LogUtils{
    private static boolean isLog = false;

    public static void d(String tag,String str){
        if(isLog){
            Log.d(tag,str);
        }
    }
    public static void i(String tag,String str){
        if(isLog){
            Log.i(tag,str);
        }
    }
    public static void e(String tag,String str){
        if(isLog){
            Log.e(tag,str);
        }
    }
}
