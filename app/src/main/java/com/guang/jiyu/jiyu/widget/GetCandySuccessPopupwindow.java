package com.guang.jiyu.jiyu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.guang.jiyu.R;

import butterknife.BindView;

/**
 * Created by admin on 2018/7/12.
 */

public class GetCandySuccessPopupwindow extends PopupWindow implements View.OnClickListener {

    @BindView(R.id.iv_take_success)
    ImageView ivTakeSuccess;
    @BindView(R.id.iv_close)
    ImageView ivClose;


    private ImageView iv_close;
    private RelativeLayout rl_main;
    private View popView;
    private OnItemClickListener mListener;

    public GetCandySuccessPopupwindow(Context context) {
        super(context);
        init(context);
        setPopupWindow();

    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        popView = inflater.inflate(R.layout.popup_get_candy_success, null);
        rl_main = popView.findViewById(R.id.rl_main);
        iv_close = popView.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(popView);// 设置View
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的高
        this.setBackgroundDrawable(new BitmapDrawable());//设置popwindow边框无黑线
        //this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        //this.setOutsideTouchable(true);


    }


    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.setOnItemClick(v);
        }
    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
