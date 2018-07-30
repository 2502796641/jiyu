package com.guang.jiyu.jiyu.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.activity.InformationDetailsActivity;
import com.guang.jiyu.jiyu.adapter.InfoAdapter;
import com.guang.jiyu.jiyu.event.NewsFlashRefreshEvent;
import com.guang.jiyu.jiyu.model.InformationModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.widget.PullToRefreshLayout.BaseRefreshListener;
import com.guang.jiyu.jiyu.widget.PullToRefreshLayout.PullToRefreshLayout;
import com.zhl.cbdialog.CBDialogBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


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
 * Created by admin on 2018/6/21.
 */

public class InfoFragment extends BaseFragment {
        @BindView(R.id.lv_fast_info)
        ListView lv;
        @BindView(R.id.refresh_layout)
        PullToRefreshLayout refreshLayout;
        Unbinder unbinder;
        private InfoAdapter adapter;
        private List<InformationModel> list;
        private int pageNum = 1;
        private Message m;
        private Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message != null) {
                    switch (message.what) {
                        case Contants.INFO_REFRESH_SUCCESS:
                            refreshLayout.finishRefresh();
                            refreshLayout.finishLoadMore();
                            ToastUtils.showToast("刷新成功");
                            list = (List<InformationModel>) message.obj;
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    ActivityUtils.startActivityWithModel(getContext(), InformationDetailsActivity.class,list.get(i));
                                }
                            });
                            break;
                        case Contants.INFO_REFRESH_NOMORE:
                            refreshLayout.finishLoadMore();
                            ToastUtils.showToast("没有更多数据了");
                            break;
                        case Contants.INFO_GET_FAILURE:
                            refreshLayout.finishRefresh();
                            refreshLayout.finishLoadMore();
                            ToastUtils.showToast("数据异常");
                            break;
                    }
                }
                return false;
            }
        });



    @Override
        public int getLayoutID() {
            return R.layout.fragment_fast_information;
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
                adapter = new InfoAdapter(list, getContext());
                lv.setAdapter(adapter);

                return rootView;
            }


        private void initInfomation(int pageNum) {
            JSONObject object = new JSONObject();
            try {
                object.put("pageNumber", pageNum + "");
                object.put("pageSize", 5 + "");
                object.put("types", 1 + "");
                RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
                final Request request = new Request.Builder()
                        //.addHeader("Content-Type",)
                        .url(LinkParams.REQUEST_URL + "/info/list")
                        .post(requestBody)
                        .build();

                OkHttpManage.getClient(getContext()).newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("onFailure-----", e.toString());
                        handler.sendEmptyMessage(Contants.INFO_GET_FAILURE);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("onResponse-----", result);
                        try {
                            JSONObject object = new JSONObject(result);
                            //有数据
                            if ("200".equals(object.getString("code"))) {
                                JSONObject data = new JSONObject(object.getString("data"));
                                JSONArray arr = data.getJSONArray("list");
                                for (int i = 0; i < arr.length(); i++) {
                                    InformationModel model = new InformationModel();
                                    JSONObject item = new JSONObject(arr.get(i).toString());
                                    model.title = item.getString("title");
                                    model.infoId = item.getString("infoId");
                                    model.content = item.getString("content");
                                    model.resource = item.getString("infoSource");
                                    model.reading_count = item.getString("click");
                                    model.img_url = item.getString("photo");

                                    String html = model.getContent();
                                    Document doc = Jsoup.parseBodyFragment(html);
                                    model.data = doc.toString();
                                    Element body = doc.body();


                                    list.add(model);
                                }
                                m = new Message();
                                m.what = Contants.INFO_REFRESH_SUCCESS;
                                m.obj = list;
                                handler.sendMessage(m);
                            }
                            //无数据
                            if ("500".equals(object.getString("code"))) {
                                handler.sendEmptyMessage(Contants.INFO_REFRESH_NOMORE);
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

        @Subscribe
        public void onEventMainThread(BaseEvent baseEvent) {
            if (baseEvent instanceof NewsFlashRefreshEvent) {
                NewsFlashRefreshEvent event = (NewsFlashRefreshEvent) baseEvent;
                if (event.getType() == 1) {
                    setRefreshListen();
                }
            }
        }

        private void setRefreshListen() {
            Log.d("setRefreshListen-----", "----------------------------");
            refreshLayout.autoRefresh();
            refreshLayout.setRefreshListener(new BaseRefreshListener() {
                @Override
                public void refresh() {
                    initInfomation(pageNum);
                }

                @Override
                public void loadMore() {
                    initInfomation(pageNum++);
                }
            });
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }
}
