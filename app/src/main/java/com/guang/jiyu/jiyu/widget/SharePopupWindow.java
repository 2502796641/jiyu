package com.guang.jiyu.jiyu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.guang.jiyu.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/6/22.
 */

public class SharePopupWindow extends PopupWindow implements View.OnClickListener {
    RelativeLayout rlWeibo;
    RelativeLayout rlWechat;
    RelativeLayout rlFriendsCircle;
    RelativeLayout rlQq;
    private View popView;
    private OnItemClickListener mListener;

    public SharePopupWindow(Context context) {
        super(context);
        init(context);
        setPopupWindow();
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        popView = inflater.inflate(R.layout.popupwindow_share, null);
        rlWeibo = popView.findViewById(R.id.rl_weibo);
        rlWechat = popView.findViewById(R.id.rl_wechat);
        rlFriendsCircle = popView.findViewById(R.id.rl_friends_circle);
        rlQq = popView.findViewById(R.id.rl_qq);
        rlWeibo.setOnClickListener(this);
        rlWechat.setOnClickListener(this);
        rlFriendsCircle.setOnClickListener(this);
        rlQq.setOnClickListener(this);
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(popView);// 设置View
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setBackgroundDrawable(new BitmapDrawable());//设置popwindow边框无黑线
        this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setOutsideTouchable(true);
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
