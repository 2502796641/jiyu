package com.guang.jiyu.jiyu.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.jiyu.fragment.HashrateRankFragment;
import com.guang.jiyu.jiyu.fragment.MBTCRankFragment;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/7/10.
 */

public class RankListActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.btn_hashrate_rank)
    SuperButton btn_hashrate_rank;
    @BindView(R.id.btn_mbtc_rank)
    SuperButton btn_mbtc_rank;
    @BindView(R.id.ll_main)
    LinearLayout llMain;


    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment mCurrentFragment;
    private final static String TAG_hashrateRank = "hashrateRank";
    private final static String TAG_mbtcRank = "mbtcRank";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_rank_list);
        initTitle();
        initFragment();
        switchTabState(0);
        switchFragment(TAG_mbtcRank,TAG_hashrateRank);
    }

    private void initFragment() {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        HashrateRankFragment hashrateRankFragment = new HashrateRankFragment();
        MBTCRankFragment mbtcRankFragment = new MBTCRankFragment();
        ft.add(R.id.ll_main,hashrateRankFragment,TAG_hashrateRank)
          .add(R.id.ll_main,mbtcRankFragment,TAG_mbtcRank).hide(mbtcRankFragment)
          .commit();
    }

    public void switchFragment(String fromTag, String toTag) {
        Fragment from = fm.findFragmentByTag(fromTag);
        Fragment to = fm.findFragmentByTag(toTag);
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = fm.beginTransaction();
            if (!to.isAdded()) {//判断是否被添加到了Activity里面去了
                transaction.hide(from).add(R.id.ll_main, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

    @OnClick({R.id.btn_hashrate_rank, R.id.btn_mbtc_rank})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_hashrate_rank:
                switchTabState(0);
                switchFragment(TAG_mbtcRank,TAG_hashrateRank);
                break;
            case R.id.btn_mbtc_rank:
                switchTabState(1);
                switchFragment(TAG_hashrateRank,TAG_mbtcRank);
                break;
        }
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "排行榜", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @SuppressLint("ResourceAsColor")
    private void switchTabState(int i) {
        switch (i){
            case 0:
                setBtnAttr(btn_hashrate_rank,btn_mbtc_rank);
                break;
            case 1:
                setBtnAttr(btn_mbtc_rank,btn_hashrate_rank);
                break;
        }
    }

    private void setBtnAttr(SuperButton btn_1, SuperButton btn_2) {
        btn_1.setShapeType(SuperButton.RECTANGLE)
                .setShapeCornersRadius(8)
                .setShapeSolidColor(setColor(R.color.theme_color))
                .setUseShape();
        btn_1.setTextColor(setColor(R.color.white));
        btn_2.setShapeType(SuperButton.RECTANGLE)
                .setShapeCornersRadius(8)
                .setShapeSolidColor(setColor(R.color.white))
                .setUseShape();
        btn_2.setTextColor(setColor(R.color.colorBlack));
    }


    private int setColor(int resId) {
        return getResources().getColor(resId);
    }
}
