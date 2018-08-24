package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
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
 * Created by admin on 2018/8/23.
 */

public class HashrateRecordActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.lv)
    ListView lv;

    private LoadMoreAdapter adapter;
    private Button loadMore;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_hashrate_record);
        handler = new Handler();
        initData();
        initTitle();
        initBottomView();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "算力记录", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 添加底部按钮
     */
    private void initBottomView() {
        View bottomView = getLayoutInflater().inflate(R.layout.bottom, null);
        loadMore = (Button) bottomView.findViewById(R.id.load);
        loadMore.setOnClickListener(new HashrateRecordActivity.ButtonClickListener());
        lv.addFooterView(bottomView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemListener());
        lv.setOnScrollListener(new OnScrollListener(isLastRow));
    }

    public void initData() {
        List<Integer> datas = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            datas.add(i + 1);
        }
        adapter = new LoadMoreAdapter(datas, this, Contants.TAG_HASHRATE_RECORD);
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
        for (int i = count; i < count + 4; i++) {
            adapter.addItem(i);
        }
    }

    //是否到达ListView底部
    boolean isLastRow = false;
}
