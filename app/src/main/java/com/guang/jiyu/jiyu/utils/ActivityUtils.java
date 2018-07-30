package com.guang.jiyu.jiyu.utils;

import android.content.Context;
import android.content.Intent;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/6/20.
 */

public class ActivityUtils {

    public static void startActivity(Context context, Class<?> cls)
    {
        Intent intent = new Intent(context,cls);
        context.startActivity(intent);
    }

    public static void startActivityWithModel(Context context, Class<?> cls, BaseModel model)
    {
        Intent intent = new Intent(context,cls);
        intent.putExtra("model",model);
        context.startActivity(intent);
    }
}
