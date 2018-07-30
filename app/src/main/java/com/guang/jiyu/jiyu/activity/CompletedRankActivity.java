package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.LoadMoreAdapter;
import com.guang.jiyu.jiyu.listener.OnItemListener;
import com.guang.jiyu.jiyu.listener.OnScrollListener;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 查看完整排行act
 * Created by admin on 2018/6/25.
 */

public class CompletedRankActivity extends BaseActivity {
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.listview)
    ListView listview;

    private LoadMoreAdapter adapter;
    private Button loadMore;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_complete_rank);
        handler = new Handler();
        initData();
        initTitle();
        initBottomView();
    }

    /**
     * 添加底部按钮
     */
    private void initBottomView() {
        View bottomView = getLayoutInflater().inflate(R.layout.bottom, null);
        loadMore = (Button) bottomView.findViewById(R.id.load);
        loadMore.setOnClickListener(new ButtonClickListener());
        listview.addFooterView(bottomView);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemListener());
        listview.setOnScrollListener(new OnScrollListener(isLastRow));
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

    public void initData() {
        List<Integer> datas = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            datas.add(i + 1);
        }
        adapter = new LoadMoreAdapter(datas, this, Contants.TAG_COMEPLETE_RANK);
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

}
