package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/7/9.
 */

public class GettingStartedActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.rl_how_to_get_hashrate)
    RelativeLayout rlHowToGetHashrate;
    @BindView(R.id.rl_hashrate_update_explain)
    RelativeLayout rlHashrateUpdateExplain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_getting_started);
        initTitle();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this,titlebar,"新手入门",R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.rl_how_to_get_hashrate, R.id.rl_hashrate_update_explain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_how_to_get_hashrate:
                ActivityUtils.startActivity(this,WhatIsBlackchainActivity.class);
                break;
            case R.id.rl_hashrate_update_explain:
                ActivityUtils.startActivity(this,NewHandExplainActivity.class);
                break;
        }
    }
}
