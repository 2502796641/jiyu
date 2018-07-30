package com.guang.jiyu.jiyu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.guang.jiyu.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/6/19.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> mFragmentList;

    //添加标题的集合
    private List<String> mTilteLis;

    public FragmentAdapter(FragmentManager fm,List<BaseFragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
    }
    public FragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<String> tilteLis) {
        super(fm);
        mFragmentList = fragmentList;
        mTilteLis = tilteLis;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    //获取标题
    @Override
    public CharSequence getPageTitle(int position) {

        return mTilteLis.get(position);
    }
}
