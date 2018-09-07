package com.guang.jiyu.jiyu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.activity.AirDropCandyActivity;
import com.guang.jiyu.jiyu.activity.LoginActivity;
import com.guang.jiyu.jiyu.activity.MessageRecordActivity;
import com.guang.jiyu.jiyu.activity.ProjectGetCandyActivity;
import com.guang.jiyu.jiyu.activity.ProjectIntroActivity;
import com.guang.jiyu.jiyu.adapter.ProjectListAdapter;
import com.guang.jiyu.jiyu.model.ProjectDetailModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.PullToRefreshLayout.BaseRefreshListener;
import com.guang.jiyu.jiyu.widget.PullToRefreshLayout.PullToRefreshLayout;
import com.guang.jiyu.jiyu.widget.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/6/19.
 */

public class ProjectFragment extends BaseFragment {
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.lv)
    ListView lv;
    Unbinder unbinder;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;

    private ProjectListAdapter projectListAdapter;
    private List<ProjectDetailModel> list;
    private int page = 1;
    private boolean isLastPage;
    private boolean isFristLoading;
    private ProjectDetailModel projectDetailModel;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message != null){
                switch (message.what){
                    case Contants.PROJECT_GET_SUCCESS:
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        list = (List<ProjectDetailModel>) message.obj;
                        projectListAdapter.setList(list);
                        if (list.size() == 0) {
                            ivNoData.setVisibility(View.VISIBLE);
                            lv.setVisibility(View.GONE);
                        }else{
                            ivNoData.setVisibility(View.GONE);
                            lv.setVisibility(View.VISIBLE);
                        }
                        if (page == 1) {
                            lv.setAdapter(projectListAdapter);
                            projectListAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
            return false;
        }
    });

    @Override
    public int getLayoutID() {
        return R.layout.fragment_project;
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        list = new ArrayList<>();
        projectListAdapter = new ProjectListAdapter(getContext());
        initTitle();
        initProject(page, 10);
        initRefreshListener();
        initListener();
        return rootView;
    }

    private void initListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActivityUtils.startActivityWithModel(getContext(), ProjectIntroActivity.class, list.get(i));
            }
        });
    }

    private void initProject(final int page, int size) {
        JSONObject object = new JSONObject();
        try {
            object.put("pageNumber", page + "");
            object.put("pageSize", size + "");
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    //.addHeader("Content-Type",)
                    .url(LinkParams.REQUEST_URL + "/api/project/getProjectS")
                    .post(requestBody)
                    .build();

            OkHttpManage.getClient(getContext()).newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtils.d("login-----", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    LogUtils.d("result-----", result);
                    try {
                        JSONObject object = new JSONObject(result);
                        if ("200".equals(object.getString("code"))) {
                            JSONObject data = object.getJSONObject("data");
                            isLastPage = data.getBoolean("lastPage");//判断数据是否到了最后一页
                            JSONArray arr = data.getJSONArray("list");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject item = new JSONObject(arr.get(i).toString());
                                projectDetailModel = new ProjectDetailModel();
                                projectDetailModel.itemId = item.getInt("itemId");
                                projectDetailModel.title = item.getString("itemName");
                                projectDetailModel.projectIntro = item.getString("summary");
                                projectDetailModel.iconUrl = item.getString("photo");

                                list.add(projectDetailModel);
                            }
                            Message m = new Message();
                            m.what = Contants.PROJECT_GET_SUCCESS;
                            m.obj = list;
                            handler.sendMessage(m);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });




/*                            projectListAdapter = new ProjectListAdapter(list, getContext());
                            lv.setAdapter(projectListAdapter);*/
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

    private void initTitle() {
        titlebar.setTitle("项目");
        titlebar.setBackgroundColor(getContext().getResources().getColor(R.color.white));//设置默认titleBar颜色
        titlebar.setLeftImageResourceSpeacial(R.mipmap.icon_gift_box);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!UserInfoUtils.isUserLogin(getContext())){
                    ToastUtils.showToast("请先登录");
                    ActivityUtils.startActivity(getContext(),LoginActivity.class);
                    return;
                }
                ActivityUtils.startActivity(getContext(), ProjectGetCandyActivity.class);
                //ActivityUtils.startActivity(getContext(), AirDropCandyActivity.class);
            }
        });
        titlebar.addAction(new TitleBar.ImageAction(R.mipmap.icon_msg_remind) {
            @Override
            public void performAction(View view) {
                if(!UserInfoUtils.isUserLogin(getContext())){
                    ToastUtils.showToast("请先登录");
                    ActivityUtils.startActivity(getContext(),LoginActivity.class);
                    return;
                }
                ActivityUtils.startActivity(getContext(), MessageRecordActivity.class);

            }
        });
    }

    private void initRefreshListener() {
        refreshLayout.autoRefresh();
        refreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        refreshLayout.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                if (isLastPage) {
                    ToastUtils.showToast("没有更多了");
                    refreshLayout.finishLoadMore();
                    return;
                }

                initProject(page++, 10);
/*                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        projectListAdapter.notifyDataSetChanged();
                        refreshLayout.finishLoadMore();
                    }
                });*/

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
