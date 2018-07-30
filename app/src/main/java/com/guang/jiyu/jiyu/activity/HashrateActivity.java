package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.LoadMoreAdapter;
import com.guang.jiyu.jiyu.listener.OnItemListener;
import com.guang.jiyu.jiyu.listener.OnScrollListener;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/6/22.
 */

public class HashrateActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tv_hashrate)
    TextView tvHashrate;
    @BindView(R.id.tv_explain)
    TextView tvExplain;
    @BindView(R.id.rl_sign_in)
    RelativeLayout rlSignIn;
    @BindView(R.id.rl_id_identification)
    RelativeLayout rlIdIdentification;
    @BindView(R.id.rl_invite_friend)
    RelativeLayout rlInviteFriend;
    @BindView(R.id.rl_read_task)
    RelativeLayout rlReadTask;
    @BindView(R.id.lv)
    ListView lv;

    private LoadMoreAdapter adapter;
    private Button loadMore;
    private Handler handler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_hashrate);
        handler = new Handler();
        initData();
        initTitle();
        initBottomView();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "算力", R.mipmap.icon_return_with_txt);
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
        loadMore.setOnClickListener(new HashrateActivity.ButtonClickListener());
        lv.addFooterView(bottomView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemListener());
        lv.setOnScrollListener(new OnScrollListener(isLastRow));
    }

    @OnClick({R.id.tv_explain, R.id.rl_sign_in, R.id.rl_id_identification, R.id.rl_invite_friend, R.id.rl_read_task})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_explain:
                ActivityUtils.startActivity(this, HashrateExplainActivity.class);
                break;
            case R.id.rl_sign_in:
                break;
            case R.id.rl_id_identification:
                break;
            case R.id.rl_invite_friend:
                break;
            case R.id.rl_read_task:
                break;
        }
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
