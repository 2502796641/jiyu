package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.HashrateRecordAdapter;
import com.guang.jiyu.jiyu.adapter.LoadMoreAdapter;
import com.guang.jiyu.jiyu.listener.OnItemListener;
import com.guang.jiyu.jiyu.listener.OnScrollListener;
import com.guang.jiyu.jiyu.model.HashrateRecordModel;
import com.guang.jiyu.jiyu.model.InformationModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
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
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/8/23.
 */

public class HashrateRecordActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.lv)
    ListView lv;

    //private LoadMoreAdapter adapter;
    private HashrateRecordAdapter adapter;
    private Button loadMore;
    private Handler handler;
    private int totalRow = -1;
    private boolean isLastPage;
    private int pageNumber = 1;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message != null){
                switch (message.what){
                    case Contants.GET_DATA_SUCCESS:
                            list = (List<HashrateRecordModel>) message.obj;
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();
                            if(isLastPage){
                                loadMore.setText("没有更多了");
                                loadMore.setClickable(false);
                            }
                        break;
                    case Contants.GET_DATA_NODATA:
                        loadMore.setText("暂无记录");
                        loadMore.setClickable(false);
                        break;
                    case Contants.GET_DATA_FAILURE:
                        ToastUtils.showToast("数据异常");
                        loadMore.setText("暂无记录");
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
        setView(R.layout.activity_hashrate_record);
        handler = new Handler();
        list = new ArrayList<>();
        adapter = new HashrateRecordAdapter(list,this);
        initTitle();
        initBottomView();
        if(UserInfoUtils.isUserLogin(this)){
            loadMore.setVisibility(View.VISIBLE);
            getHashrateRecord(pageNumber);
        }else{
            loadMore.setVisibility(View.GONE);
        }

    }

    /**
     * 获取算力记录
     */
    private List<HashrateRecordModel> list;
    private void getHashrateRecord(int pageNumber) {
        final MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(this, Contants.USER_ID) + "");
        mbody.addFormDataPart("pageSize", 5 + "");
        mbody.addFormDataPart("pageNumber", pageNumber + "");
        mbody.addFormDataPart("totalRow", totalRow + "");
        RequestBody requestBody = mbody.build();
        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/arith/record")
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
                LogUtils.d("/arith/record-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    if("200".equals(object.getString("code"))){
                        JSONObject data = new JSONObject(object.getString("data"));
                        JSONArray arr = data.getJSONArray("list");
                        if(arr.length() == 0){
                            mHandler.sendEmptyMessage(Contants.GET_DATA_NODATA);
                        }
                        for(int i = 0;i < arr.length();i++){
                            JSONObject item = new JSONObject(arr.get(i).toString());
                            HashrateRecordModel model = new HashrateRecordModel();
                            model.id = item.getInt("id");
                            model.fdcType = item.getString("fdcType");
                            model.fdnQty = item.getInt("fdnQty");
                            model.createdAt = item.getString("createdAt");
                            list.add(model);
                        }
                        isLastPage = data.getBoolean("lastPage");
                        Message m = new Message();
                        m.what = Contants.GET_DATA_SUCCESS;
                        m.obj = list;
                        mHandler.sendMessage(m);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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



    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loadMore.setText("数据加载中");
            pageNumber++;
            getHashrateRecord(pageNumber);
        }
    }

    //加载数据
    public void loadData() {
        int count = adapter.getCount() + 1;
        for (int i = count; i < count + 4; i++) {

        }
    }

    //是否到达ListView底部
    boolean isLastRow = false;
}
