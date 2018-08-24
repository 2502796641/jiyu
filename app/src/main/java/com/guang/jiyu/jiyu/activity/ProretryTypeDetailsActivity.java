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
import com.guang.jiyu.jiyu.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/8/20.
 */

public class ProretryTypeDetailsActivity extends BaseActivity {
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.id_draw_money_name)
    TextView idDrawMoneyName;
    @BindView(R.id.id_draw_money_balance)
    TextView idDrawMoneyBalance;
    @BindView(R.id.id_draw_money_market_value)
    TextView idDrawMoneyMarketValue;
    @BindView(R.id.btn_login)
    SuperButton btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_propetry_type_details);
        initTitle();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "资产详情", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.addAction(new TitleBar.TextAction("钱包记录") {
            @Override
            public void performAction(View view) {
                ActivityUtils.startActivity(ProretryTypeDetailsActivity.this,MyPropetryRecordActivity.class);
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
    }
}
