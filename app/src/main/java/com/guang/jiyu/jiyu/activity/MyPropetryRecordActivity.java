package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.MyPropetryRecordAdapter;
import com.guang.jiyu.jiyu.listener.OnItemListener;
import com.guang.jiyu.jiyu.listener.OnScrollListener;
import com.guang.jiyu.jiyu.model.PropetryRecordModel;
import com.guang.jiyu.jiyu.model.WalletModel;
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

public class MyPropetryRecordActivity extends BaseActivity {
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.ll_no_record)
    LinearLayout llNoRecord;

    private MyPropetryRecordAdapter adapter;
    private List<PropetryRecordModel> list;
    private Button loadMore;
    private Handler handler;
    private WalletModel walletModel;
    private int pageNumber = 1;
    private int totalRow = -1;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case Contants.WALLET_GET_FAILURE:
                        ToastUtils.showToast("数据异常");
                        break;
                    case Contants.WALLETDetail_GET_Nodata:
                        lv.setVisibility(View.GONE);
                        llNoRecord.setVisibility(View.VISIBLE);
                        break;
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_my_propetry_record);
        handler = new Handler();
        walletModel = (WalletModel) getIntent().getSerializableExtra("model");
        //initData();
        initTitle();
        initBottomView();
        if(walletModel != null){
            walletRecordList(pageNumber, totalRow);
        }

    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "收支记录", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void walletRecordList(int pageNumber, int totalRow) {
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("walletId", walletModel.getWalletId() + "");
        mbody.addFormDataPart("pageSize", 5 + "");
        mbody.addFormDataPart("pageNumber", pageNumber + "");
        mbody.addFormDataPart("totalRow", totalRow + "");
        RequestBody requestBody = mbody.build();

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/walletRecord/list")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this, Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("register-----", e.toString());
                mHandler.sendEmptyMessage(Contants.WALLET_GET_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("walletRecord/list-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray arr = data.getJSONArray("list");
                        if (arr.length() == 0) {
                            mHandler.sendEmptyMessage(Contants.WALLETDetail_GET_Nodata);
                        } else {
                            /*for(int i = 0;i <arr.length();i++){
                                JSONObject item = new JSONObject(arr.get(i).toString());
                                PropetryRecordModel model = new PropetryRecordModel();
                                model.updateTime = item.getString("createdTime");

                            }*/
/*                            Message m = new Message();
                            m.what = Contants.WALLET_GET_SUCCESS;
                            m.obj = model;
                            handler.sendMessage(m);*/
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * 添加底部按钮
     */
    private void initBottomView() {
        View bottomView = getLayoutInflater().inflate(R.layout.bottom, null);
        loadMore = (Button) bottomView.findViewById(R.id.load);
        loadMore.setOnClickListener(new ButtonClickListener());
        lv.addFooterView(bottomView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemListener());
        lv.setOnScrollListener(new OnScrollListener(isLastRow));
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
            PropetryRecordModel model = new PropetryRecordModel();
            model.state = "收入";
            model.updateTime = "1999-09-09  09:09:09";
            model.balance = "0.05";
            //list.add(model);
            adapter.addItem(model);
        }
    }

    //是否到达ListView底部
    boolean isLastRow = false;
}
