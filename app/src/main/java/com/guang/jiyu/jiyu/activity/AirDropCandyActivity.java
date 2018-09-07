package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.AirCandyLoadAdapter;
import com.guang.jiyu.jiyu.adapter.LoadMoreAdapter;
import com.guang.jiyu.jiyu.fragment.HashrateRankFragment;
import com.guang.jiyu.jiyu.listener.OnItemListener;
import com.guang.jiyu.jiyu.listener.OnScrollListener;
import com.guang.jiyu.jiyu.model.AirCandyModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;
import com.squareup.picasso.Picasso;

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
 * Created by admin on 2018/7/12.
 */

public class AirDropCandyActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tv_candy_price)
    TextView tvCandyPrice;
    @BindView(R.id.lv)
    ListView lv;

    private AirCandyLoadAdapter adapter;
    private Button loadMore;
    private Handler handler;
    private List<AirCandyModel> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_airdrop_candy);
        handler = new Handler();
        initTitle();
        initData();
        //initBottomView();
    }

    private void initData() {

        list = new ArrayList<>();
        JSONObject object = new JSONObject();
        try {
            object.put("userId", UserInfoUtils.getInt(this,Contants.USER_ID) + "");
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    //.addHeader("Content-Type",)
                    .url(LinkParams.REQUEST_URL + "/api/tgAccount/listTgAccount")
                    .post(requestBody)
                    .build();

            OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtils.d("onFailure-----", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    LogUtils.d("result-----", result);
                    try {
                        JSONObject object = new JSONObject(result);
                        if ("200".equals(object.getString("code"))) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            final String candyMoney = data.getString("moneySum");
                            final JSONArray arr = data.getJSONArray("list");
                            for(int i = 0;i < arr.length();i++){
                                AirCandyModel airCandyModel = new AirCandyModel();
                                JSONObject jb = new JSONObject(arr.get(i).toString());
                                airCandyModel.userId = jb.getString("userId");
                                airCandyModel.acctId = jb.getString("acctId");
                                airCandyModel.currency = jb.getString("currency");
                                airCandyModel.amount = jb.getString("amount");
                                airCandyModel.money = jb.getString("money");
                                airCandyModel.createTime = jb.getString("createTime");
                                airCandyModel.updateTime = jb.getString("updateTime");
                                list.add(airCandyModel);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvCandyPrice.setText("$" + candyMoney);
                                    adapter = new AirCandyLoadAdapter(list,AirDropCandyActivity.this);
                                    lv.setAdapter(adapter);
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

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this,titlebar,"空投糖果",R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    //loadData();
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
        for (int i = count; i < count + 10; i++) {

        }
    }

    //是否到达ListView底部
    boolean isLastRow = false;


}
