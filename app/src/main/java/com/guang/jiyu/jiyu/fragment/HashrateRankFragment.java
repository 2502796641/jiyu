package com.guang.jiyu.jiyu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.activity.CompletedRankActivity;
import com.guang.jiyu.jiyu.adapter.LoadMoreAdapter;
import com.guang.jiyu.jiyu.listener.OnItemListener;
import com.guang.jiyu.jiyu.listener.OnScrollListener;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 2018/7/10.
 */

public class HashrateRankFragment extends BaseFragment {

    @BindView(R.id.listview)
    ListView listview;
    Unbinder unbinder;

    private LoadMoreAdapter adapter;
    private Button loadMore;
    private Handler handler;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_hashrate_rank;
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        handler = new Handler();
        initListData();
        initBottomView();
        return rootView;
    }

    /**
     * 添加底部按钮
     */
    private void initBottomView() {
        View bottomView = getLayoutInflater().inflate(R.layout.bottom, null);
        loadMore = (Button) bottomView.findViewById(R.id.load);
        loadMore.setOnClickListener(new HashrateRankFragment.ButtonClickListener());
        listview.addFooterView(bottomView);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemListener());
        listview.setOnScrollListener(new OnScrollListener(isLastRow));
    }


    public void initListData() {
        List<Integer> datas = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            datas.add(i + 1);
        }
        adapter = new LoadMoreAdapter(datas, getContext(), Contants.TAG_COMEPLETE_RANK);
    }



    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loadMore.setText("数据加载中");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.out.println("hello");
                    loadData();
                    adapter.notifyDataSetChanged();
                    //listView.setSelection(5);
                    loadMore.setText("加载更多");
                }
            }, 2000);
        }
    }

    //加载数据
    public void loadData() {
        int count = adapter.getCount() + 1;
        for (int i = count; i < count + 10; i++) {
            adapter.addItem(i);
        }
    }

    //是否到达ListView底部
    boolean isLastRow = false;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
