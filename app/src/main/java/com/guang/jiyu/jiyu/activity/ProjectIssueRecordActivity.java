package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.jiyu.adapter.ProjectIssueRecordAdapter;
import com.guang.jiyu.jiyu.listener.OnItemListener;
import com.guang.jiyu.jiyu.listener.OnScrollListener;
import com.guang.jiyu.jiyu.model.ProjectIssueRecordModel;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2018/8/24.
 */

public class ProjectIssueRecordActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.lv)
    ListView lv;

    private ProjectIssueRecordAdapter adapter;
    private List<ProjectIssueRecordModel> list;
    private Button loadMore;
    private Handler handler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_project_issue_record);
        handler = new Handler();
        initData();
        initTitle();
        initBottomView();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "发布记录", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initBottomView() {
        View bottomView = getLayoutInflater().inflate(R.layout.bottom, null);
        loadMore = (Button) bottomView.findViewById(R.id.load);
        loadMore.setOnClickListener(new ButtonClickListener());
        lv.addFooterView(bottomView);
        lv.setAdapter(adapter);
        lv.setOnScrollListener(new OnScrollListener(isLastRow));
    }

    public void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ProjectIssueRecordModel model = new ProjectIssueRecordModel();
            model.issue_count="9999";
            model.update_time = "1999-09-09  09:09:09";

            list.add(model);
        }
        adapter = new ProjectIssueRecordAdapter(this,list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActivityUtils.startActivity(ProjectIssueRecordActivity.this,ProjectIssueDetailsActivity.class);
            }
        });
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
            ProjectIssueRecordModel model = new ProjectIssueRecordModel();
            model.issue_count="9999";
            model.update_time = "1999-09-09  09:09:09";
            //list.add(model);
            adapter.addItem(model);
        }
    }

    //是否到达ListView底部
    boolean isLastRow = false;
}
