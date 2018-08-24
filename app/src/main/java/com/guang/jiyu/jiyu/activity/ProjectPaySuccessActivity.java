package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/8/24.
 */

public class ProjectPaySuccessActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_currency)
    TextView tvCurrency;
    @BindView(R.id.btn_pay)
    SuperButton btnPay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_project_pay_success);
        initTitle();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "支付成功", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @OnClick(R.id.btn_pay)
    public void onViewClicked() {
        ActivityUtils.startActivity(this,ProjectIssueRecordActivity.class);
        finish();
    }
}
