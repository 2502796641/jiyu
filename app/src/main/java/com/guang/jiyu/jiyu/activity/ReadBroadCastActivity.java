package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.widget.SharePopupWindow;
import com.guang.jiyu.jiyu.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/8/23.
 */

public class ReadBroadCastActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.btn_login)
    SuperButton btnLogin;

    private SharePopupWindow sharePopupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_read_broadcast);
        initTitle();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this,titlebar,"关于我们",R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.addAction(new TitleBar.ImageAction(R.mipmap.ic_launcher) {
            @Override
            public void performAction(View view) {
                initPopWindow();
            }
        });
    }

    private void initPopWindow() {
        sharePopupWindow = new SharePopupWindow(this);
        sharePopupWindow.setOnItemClickListener(new SharePopupWindow.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_weibo:
                        ToastUtils.showToast("微博");
                        break;
                    case R.id.rl_wechat:
                        ToastUtils.showToast("微信");
                        break;
                    case R.id.rl_friends_circle:
                        ToastUtils.showToast("朋友圈");
                        break;
                    case R.id.rl_qq:
                        ToastUtils.showToast("QQ");
                        break;
                }
            }
        });
        sharePopupWindow.showAtLocation(ReadBroadCastActivity.this.findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
        setWindowAttr(0.6f);
        sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAttr(1f);
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        ToastUtils.showToast("领取成功！");
        finish();
    }
}
