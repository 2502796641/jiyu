package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 身份认证activity
 * Created by admin on 2018/6/21.
 */

public class IdentityAuthenticateActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_idcard_code)
    EditText etIdcardCode;
    @BindView(R.id.iv_idcard_front)
    ImageView ivIdcardFront;
    @BindView(R.id.iv_idcard_back)
    ImageView ivIdcardBack;
    @BindView(R.id.iv_idcard_handon)
    ImageView ivIdcardHandon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_identity_authenticate);
        initTitle();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "身份认证", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titlebar.addAction(new TitleBar.TextAction("完成") {
            @Override
            public void performAction(View view) {
                ToastUtils.showToast("完成");
            }
        });
    }

    @OnClick({R.id.iv_idcard_front, R.id.iv_idcard_back, R.id.iv_idcard_handon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_idcard_front:
                ToastUtils.showToast("1");
                break;
            case R.id.iv_idcard_back:
                ToastUtils.showToast("2");
                break;
            case R.id.iv_idcard_handon:
                ToastUtils.showToast("3");
                break;
        }
    }
}
