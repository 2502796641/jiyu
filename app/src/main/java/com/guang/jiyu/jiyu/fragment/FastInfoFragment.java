package com.guang.jiyu.jiyu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.FastInfoAdapter;
import com.guang.jiyu.jiyu.event.ShareInformationEvent;
import com.guang.jiyu.jiyu.event.NewsFlashRefreshEvent;
import com.guang.jiyu.jiyu.model.FastInformationModel;
import com.guang.jiyu.jiyu.model.InformationWithDateModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.MyTextUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.widget.PullToRefreshLayout.BaseRefreshListener;
import com.guang.jiyu.jiyu.widget.PullToRefreshLayout.PullToRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

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
 * 快讯资讯fag
 * Created by admin on 2018/6/21.
 */

public class FastInfoFragment extends BaseFragment {
    @BindView(R.id.pull_refresh)
    PullToRefreshLayout pullRefresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    Unbinder unbinder;

    private int pageNum = 1;
    private List<FastInformationModel> list;
    private List<InformationWithDateModel> withDateList;
    private FastInfoAdapter informationAdapter;
    private boolean isOpening = true;
    private Message m;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message != null){
                switch (message.what){
                    case Contants.FASTINFO_REFRESH_SUCCESS:

                        ToastUtils.showToast("刷新成功");
                        pullRefresh.finishRefresh();
                        pullRefresh.finishLoadMore();
                        list = (List<FastInformationModel>) message.obj;

                        for(int i = 0;i <list.size();i++){
                            Log.d("list--------","" + list.get(i).toString());
                        }
                        informationAdapter = new FastInfoAdapter(list);
                        rv.setAdapter(informationAdapter);
                        informationAdapter.notifyDataSetChanged();
                        informationAdapter.setOnItemClickListener(new FastInfoAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onClick(View view, FastInfoAdapter.ViewName viewName, int position) {
                                switch (viewName){
                                    case LL_MAIN:
                                       // ToastUtils.showToast("点击了LL_MAIN" + position );
                                        break;
                                    case BULISH:
                                        updateGoodTimes(position,list.get(position).getInfoId());
                                        break;
                                    case BEARISH:
                                        updateBadTimes(position,list.get(position).getInfoId());
                                        break;
                                    case SHARE:
                                        EventBus.getDefault().post(new ShareInformationEvent(list.get(position)));
                                        break;
                                    case CONTENT:
                                        TextView tv = view.findViewById(R.id.tv_content);
                                        if(isOpening){
                                            isOpening = false;
                                            tv.setEllipsize(null);
                                            tv.setSingleLine(false);
                                        }else{
                                            isOpening = true;
                                            tv.setEllipsize(TextUtils.TruncateAt.END);
                                            tv.setLines(4);
                                        }
                                        break;
                                }
                            }
                        });
                        break;
                    case Contants.INFO_REFRESH_NOMORE:
                        pullRefresh.finishLoadMore();
                        ToastUtils.showToast("没有更多数据了");
                        break;
                    case Contants.INFO_BULISH:
                        informationAdapter.notifyDataSetChanged();
                        break;
                    case Contants.INFO_BEARISH:
                        informationAdapter.notifyDataSetChanged();
                        break;
                    case Contants.INFO_GET_FAILURE:
                        pullRefresh.finishRefresh();
                        pullRefresh.finishLoadMore();
                        ToastUtils.showToast("数据异常");
                        break;
                }
            }
            return false;
        }
    });

    /**
     * 看空
     * @param position
     * @param infoId
     */
    private void updateBadTimes(final int position, String infoId) {
        JSONObject object = new JSONObject();
        try {
            object.put("infoId", infoId);
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    .url(LinkParams.REQUEST_URL + "/info/updateBadTimes")
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
                    try {
                        JSONObject object = new JSONObject(result);
                        if ("200".equals(object.getString("code"))) {
                            String bearishCount = list.get(position).bearish;
                            bearishCount = String.valueOf(Integer.parseInt(bearishCount) + 1);
                            list.get(position).bearish = bearishCount;
                            handler.sendEmptyMessage(Contants.INFO_BEARISH);
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


    /**
     * 看多
     */
    private void updateGoodTimes(final int position, String infoId) {
        JSONObject object = new JSONObject();
        try {
            object.put("infoId", infoId);
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    .url(LinkParams.REQUEST_URL + "/info/updateGoodTimes")
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
                    Log.d("updategoodtime",result);
                    try {
                        JSONObject object = new JSONObject(result);
                        if ("200".equals(object.getString("code"))) {
                            String bulishCount = list.get(position).bulish;
                            bulishCount = String.valueOf(Integer.parseInt(bulishCount) + 1);
                            list.get(position).bulish = bulishCount;
                            handler.sendEmptyMessage(Contants.INFO_BULISH);

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

    @Override
    public int getLayoutID() {
        return R.layout.fragment_sub_information;
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        //initList();
        //initInfomation();
        return rootView;
    }



    private void initInfomation(int pageNum) {
        JSONObject object = new JSONObject();
        try {
            object.put("pageNumber", pageNum + "");
            object.put("pageSize", 5 + "");
            object.put("types", 2 + "");
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    //.addHeader("Content-Type",)
                    .url(LinkParams.REQUEST_URL + "/info/listAlerts")
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
                    Log.d("快讯-----", result);
                    try {
                        JSONObject object = new JSONObject(result);
                        //有数据
                        if ("200".equals(object.getString("code"))) {
                            withDateList = new ArrayList<>();

                            JSONArray data = object.getJSONArray("data");
                            for(int i = 0;i < data.length();i++){
                                JSONObject jb = new JSONObject(data.get(i).toString());
                                InformationWithDateModel informationWithDateModel = new InformationWithDateModel();
                                informationWithDateModel.dayForWeek = jb.getString("dayForWeek");
                                informationWithDateModel.mouths = jb.getString("months");
                                JSONArray arr = jb.getJSONArray("list");
                                for(int j = 0;j < arr.length();j++){
                                    FastInformationModel model = new FastInformationModel();
                                    JSONObject item = new JSONObject(arr.get(j).toString());
                                    model.dayForWeek = MyTextUtils.dateForWeek(jb.getString("dayForWeek"));
                                    model.mouths = jb.getString("months");
                                    model.infoId = item.getString("infoId");
                                    //model.content = item.getString("content");
                                    model.content = Jsoup.parse(item.getString("content")).body().text();
                                    model.title = item.getString("title");
                                    model.bulish = item.getString("goodTimes");
                                    model.bearish = item.getString("badTimes");
                                    model.share_times = item.getString("shareCount");
                                    model.time = item.getString("updatedtime");
                                    model.tag = j;
                                    list.add(model);
                                }
                                informationWithDateModel.setList(list);
                                withDateList.add(informationWithDateModel);
                            }
                            m = new Message();
                            m.what = Contants.FASTINFO_REFRESH_SUCCESS;
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
        if(baseEvent instanceof NewsFlashRefreshEvent){
            NewsFlashRefreshEvent event = (NewsFlashRefreshEvent) baseEvent;
            if(event.getType() == 2){
                setRefreshListen();
            }
        }
    }

    private void setRefreshListen() {
        pullRefresh.autoRefresh();
        pullRefresh.setRefreshListener(new BaseRefreshListener() {
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
