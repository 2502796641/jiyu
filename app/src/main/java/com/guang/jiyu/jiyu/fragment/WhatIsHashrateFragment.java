package com.guang.jiyu.jiyu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.jiyu.activity.HashrateActivity;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.MyTextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by admin on 2018/6/25.
 */

public class WhatIsHashrateFragment extends BaseFragment {
    @BindView(R.id.tv_what_is_hashrate)
    TextView tvWhatIsHashrate;
    @BindView(R.id.btn_get)
    SuperButton btnGet;
    Unbinder unbinder;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_whatis_hashrate;
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initTxt();
        return rootView;
    }

    /**
     * 获取txt文件显示
     */
    private void initTxt() {
        tvWhatIsHashrate.setText(MyTextUtils.getTxtString(("what_is_hashrate.txt")));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @OnClick(R.id.btn_get)
    public void onViewClicked() {
        //ActivityUtils.startActivity(getContext(), HashrateActivity.class);
    }
}
