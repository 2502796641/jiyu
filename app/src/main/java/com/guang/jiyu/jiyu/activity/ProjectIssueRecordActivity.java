package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.ProjectIssueRecordAdapter;
import com.guang.jiyu.jiyu.listener.OnScrollListener;
import com.guang.jiyu.jiyu.model.ProjectIssueRecordModel;
import com.guang.jiyu.jiyu.model.ProretryTypeModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

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
 * Created by admin on 2018/8/24.
 */

public class ProjectIssueRecordActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.ll_no_record)
    LinearLayout llNoRecord;

    private ProjectIssueRecordAdapter adapter;
    private List<ProjectIssueRecordModel> list;
    private Button loadMore;
    private int pageNum = 1;
    private List<ProretryTypeModel> dataList;
    private Handler handler;
    private boolean isLastPage;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case Contants.GET_DATA_SUCCESS:
                        LogUtils.d("sendRecord", "----GET_DATA_SUCCESS");
                        lv.setVisibility(View.VISIBLE);
                        llNoRecord.setVisibility(View.GONE);
                        dataList = (List<ProretryTypeModel>) message.obj;
/*                        if(adapter == null){
                            adapter = new ProjectIssueRecordAdapter(ProjectIssueRecordActivity.this,dataList);
                        }else{
                        }*/
                        adapter.setList(dataList);
                        adapter.notifyDataSetChanged();
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                ActivityUtils.startActivityWithModel(ProjectIssueRecordActivity.this, ProjectIssueDetailsActivity.class,dataList.get(i));
                            }
                        });
                        break;
                    case Contants.GET_DATA_FAILURE:
                        ToastUtils.showToast("数据异常");
                        break;
                    case Contants.GET_DATA_NODATA:
                        lv.setVisibility(View.GONE);
                        llNoRecord.setVisibility(View.VISIBLE);
                        break;
                    case Contants.GET_DATA_NOMOREDATA:
                        ToastUtils.showToast("没有更多数据了");
                        loadMore.setText("没有更多了");
                        loadMore.setClickable(false);
                        break;
                }
            }
            return false;
        }
    });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_project_issue_record);
        handler = new Handler();
        initTitle();
        initBottomView();
        sendRecord(pageNum);
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
        dataList = new ArrayList<>();
        adapter = new ProjectIssueRecordAdapter(this, dataList);
        lv.setAdapter(adapter);
        lv.setOnScrollListener(new OnScrollListener(isLastRow));

    }



    private void sendRecord(int pageNum) {
        LogUtils.d("sendRecordpageNumber", "----" + pageNum);
        JSONObject object = new JSONObject();
        try {
            object.put("userId", UserInfoUtils.getInt(this, Contants.USER_ID) +"");
            object.put("pageNumber", pageNum + "");
            object.put("pageSize", 5 + "");
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    .url(LinkParams.REQUEST_URL + "/api/project/sendRecord")
                    .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this, Contants.AUTHORIZATION))
                    .post(requestBody)
                    .build();
            OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mHandler.sendEmptyMessage(Contants.GET_DATA_FAILURE);

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    LogUtils.d("sendRecord", "----" + result);
                    try {
                        JSONObject object = new JSONObject(result);
                        if ("200".equals(object.getString("code"))) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            JSONArray arr = data.getJSONArray("list");
                            isLastPage = data.getBoolean("lastPage");

                            if(isLastPage){
                                mHandler.sendEmptyMessage(Contants.GET_DATA_NOMOREDATA);
                            }

                            if (arr.length() == 0) {
                                mHandler.sendEmptyMessage(Contants.GET_DATA_NODATA);
                            }
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject item = new JSONObject(arr.get(i).toString());
                                ProretryTypeModel model = new ProretryTypeModel();
                                model.itemId = item.getInt("itemId");
                                model.theIssuingParty = item.getString("theIssuingParty");
                                model.broadcastContent = item.getString("broadcastContent");
                                model.broadcastImages1 = item.getString("broadcastImages1");
                                model.broadcastImages2 = item.getString("broadcastImages2");
                                model.broadcastImages3 = item.getString("broadcastImages3");
                                model.broadcastStartTime = item.getString("broadcastStartTime");
                                model.icon = item.getString("icon");
                                model.sendTheTotal = item.getInt("sendTheTotal");
                                model.currency = item.getString("currency");
                                model.actuallyPay = item.getString("actuallyPay");
                                model.average = item.getString("average");
                                model.peopleNumber = item.getString("peopleNumber");
                                dataList.add(model);
                            }

                            Message m = new Message();
                            m.what = Contants.GET_DATA_SUCCESS;
                            m.obj = dataList;
                            mHandler.sendMessage(m);
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

    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loadMore.setText("数据加载中");
            pageNum = pageNum + 1;
            sendRecord(pageNum);
            //listView.setSelection(5);
            loadMore.setText("加载更多");
        }
    }


    //是否到达ListView底部
    boolean isLastRow = false;
}
