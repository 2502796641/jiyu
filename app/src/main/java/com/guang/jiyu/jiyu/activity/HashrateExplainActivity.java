package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.jiyu.adapter.FragmentAdapter;
import com.guang.jiyu.jiyu.fragment.HowToGetHashrateFragment;
import com.guang.jiyu.jiyu.fragment.WhatIsHashrateFragment;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2018/6/25.
 */

public class HashrateExplainActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private List<BaseFragment> mFragmentList;
    private List<String> mTitleList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_hashrate_explain);
        initTitle();
        initFragment();
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new WhatIsHashrateFragment());
        mFragmentList.add(new HowToGetHashrateFragment());
        vpContent.setAdapter(new FragmentAdapter(getSupportFragmentManager(),mFragmentList,mTitleList));
        tab.setupWithViewPager(vpContent);
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this,titlebar,"算力",R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitleList = new ArrayList<>();
        mTitleList.add(getResources().getString(R.string.string_whatis_hashrate));
        mTitleList.add(getResources().getString(R.string.string_how_to_get));
        //设置tablayout模式
        tab.setTabMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        tab.addTab(tab.newTab().setText(mTitleList.get(0)));
        tab.addTab(tab.newTab().setText(mTitleList.get(1)));
    }


}
