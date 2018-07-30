package com.guang.jiyu.jiyu.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.widget.TitleBar;

/**
 * Created by admin on 2018/6/20.
 */

public class TitleBarUtils {

    public static void setTitleBarWithImg(final Activity context, TitleBar titleBar, String title, int leftImgResid){
        titleBar.setTitle(title);
        titleBar.setBackgroundColor(context.getResources().getColor(R.color.white));//设置默认titleBar颜色
        titleBar.setLeftImageResource(leftImgResid);
/*        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });*/
    }
    public static void setTitleBarNoImg(final Activity context, TitleBar titleBar, String title){
        titleBar.setTitle(title);
        titleBar.setBackgroundColor(context.getResources().getColor(R.color.white));//设置默认titleBar颜色
/*        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });*/
    }

    public static void setTitleBarWithSpecialImg(final Activity context, TitleBar titleBar, String title, int leftImgResid){
        titleBar.setTitle(title);
        titleBar.setBackgroundColor(context.getResources().getColor(R.color.white));//设置默认titleBar颜色
        titleBar.setLeftImageResourceSpeacial(leftImgResid);
/*        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });*/
    }

}
