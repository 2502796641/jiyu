package com.guang.jiyu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.event.ShareInformationEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

/**
 * Created by admin on 2018/6/19.
 */

public abstract class BaseFragment extends Fragment{

    public abstract int getLayoutID(); //获取布局资源文件
    public abstract void initData();//初始化数据，如：网络请求获取数据
    protected View convertView;//显示的converView


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(getLayoutID(),container,false);//用布局填充器填充布局
        EventBus.getDefault().register(this);
        return convertView;
    }

    @Subscribe
    public void onEventMainThread(BaseEvent baseEvent) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
