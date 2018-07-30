package com.guang.jiyu.jiyu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 行情fag
 * Created by admin on 2018/6/19.
 */

public class MarketFragmant extends BaseFragment {
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.rl_no_content)
    RelativeLayout rlNoContent;
    Unbinder unbinder;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_market;
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initTitle();
        return rootView;
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarNoImg(getActivity(), titlebar, "行情");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
