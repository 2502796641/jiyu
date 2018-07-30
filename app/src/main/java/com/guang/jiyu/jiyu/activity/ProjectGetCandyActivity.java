package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.AirCandyLoadAdapter;
import com.guang.jiyu.jiyu.adapter.CandyActivityAdapter;
import com.guang.jiyu.jiyu.event.GetCandySuccessEvent;
import com.guang.jiyu.jiyu.model.AirCandyModel;
import com.guang.jiyu.jiyu.model.ProjectDetailModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.PullToRefreshLayout.BaseRefreshListener;
import com.guang.jiyu.jiyu.widget.PullToRefreshLayout.PullToRefreshLayout;
import com.guang.jiyu.jiyu.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 空投糖果
 * Created by admin on 2018/7/16.
 */

public class ProjectGetCandyActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    @BindView(R.id.titlebar)
    TitleBar titlebar;

    private List<ProjectDetailModel> current_list;
    private List<ProjectDetailModel> older_list;
    private List<AirCandyModel> candy_list;
    private CandyActivityAdapter candyActivityAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_project_get_candy);
        initTitle();
        initData();
    }

    private void initTitle() {
        titlebar.setTitle("空投糖果");
        titlebar.setBackgroundColor(getResources().getColor(R.color.white));//设置默认titleBar颜色
        titlebar.setLeftImageResource(R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titlebar.addAction(new TitleBar.ImageAction(R.mipmap.nav_jilu) {
            @Override
            public void performAction(View view) {
                if(!UserInfoUtils.isUserLogin(ProjectGetCandyActivity.this)){
                    ToastUtils.showToast("请先登录");
                    ActivityUtils.startActivity(ProjectGetCandyActivity.this,LoginActivity.class);
                    return;
                }
                ActivityUtils.startActivity(ProjectGetCandyActivity.this, AirDropCandyActivity.class);
            }
        });
    }

    private void initData() {
        current_list = new ArrayList<>();
        older_list = new ArrayList<>();
        candy_list = new ArrayList<>();
        JSONObject object = new JSONObject();
        try {
            object.put("userId", UserInfoUtils.getInt(this,Contants.USER_ID) + "");
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    //.addHeader("Content-Type",)
                    .url(LinkParams.REQUEST_URL + "/api/project/listProject")
                    .post(requestBody)
                    .build();

            OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("onFailure-----", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.d("result-----", result);
                    try {
                        JSONObject object = new JSONObject(result);
                        if ("200".equals(object.getString("code"))) {
                            final JSONArray arr = object.getJSONArray("data");
                            for(int i = 0;i < arr.length();i++){
                                ProjectDetailModel projectDetailModel = new ProjectDetailModel();
                                JSONObject item = new JSONObject(arr.get(i).toString());
                                projectDetailModel.itemId = item.getInt("itemId");
                                projectDetailModel.projectIntro = item.getString("summary");
                                projectDetailModel.iconUrl = item.getString("photo");
                                projectDetailModel.statu = item.getInt("status");

                                AirCandyModel airCandyModel = new AirCandyModel();
                                airCandyModel.itemId = item.getString("itemId");
                                airCandyModel.currency = item.getString("currency");
                                airCandyModel.amount = item.getString("amount");
                                if(projectDetailModel.statu == 0){
                                    current_list.add(projectDetailModel);
                                    candy_list.add(airCandyModel);
                                }else if(projectDetailModel.statu == 1){
                                    older_list.add(projectDetailModel);
                                }
                            }
                            current_list.addAll(older_list);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    candyActivityAdapter = new CandyActivityAdapter(ProjectGetCandyActivity.this,current_list,older_list.size());
                                    rv.setLayoutManager(new LinearLayoutManager(ProjectGetCandyActivity.this));
                                    rv.setAdapter(candyActivityAdapter);
                                    setRefreshListen();
                                    candyActivityAdapter.setOnItemClickListener(new CandyActivityAdapter.OnRecyclerViewItemClickListener() {
                                        @Override
                                        public void onClick(View view, CandyActivityAdapter.ViewName viewName, int position) {
                                            switch (viewName) {
                                                case RL_MAIN:
                                                    ToastUtils.showToast("点击了LL_MAIN" + position);
                                                    break;
                                                case BTN_GET:
                                                    EventBus.getDefault().post(new GetCandySuccessEvent(candy_list.get(position)));
                                                    finish();
                                                    break;
                                            }
                                        }

                                    });
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setRefreshListen() {
        refreshLayout.autoRefresh();
        refreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                    }
                }, 1000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadMore();
                    }
                }, 1000);
            }
        });
    }
}
