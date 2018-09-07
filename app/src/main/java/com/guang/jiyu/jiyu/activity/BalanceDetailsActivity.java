package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.BalanceDetailsAdapter;
import com.guang.jiyu.jiyu.listener.OnItemListener;
import com.guang.jiyu.jiyu.listener.OnScrollListener;
import com.guang.jiyu.jiyu.model.BalanceDetailsModel;
import com.guang.jiyu.jiyu.model.WalletModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
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
 * Created by admin on 2018/7/24.
 */

public class BalanceDetailsActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.rl_no_content)
    RelativeLayout rlNoContent;

    private BalanceDetailsAdapter adapter;
    private Button loadMore;

    private List<BalanceDetailsModel> list;
    private WalletModel walletModel;
    private int pageNumber = 1;
    private int totalRow = -1;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case Contants.WALLETDetail_GET_Nodata:
                        lv.setVisibility(View.GONE);
                        rlNoContent.setVisibility(View.VISIBLE);
                        break;
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_balance_details);
        walletModel = (WalletModel) getIntent().getSerializableExtra("model");
        list = new ArrayList<>();
        adapter = new BalanceDetailsAdapter(list, this);
        initTitle();
        //initData();
        initBottomView();
        walletRecordList(pageNumber, totalRow);
    }

    private void walletRecordList(int pageNumber, int totalRow) {
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("walletId", walletModel.getWalletId() + "");
        mbody.addFormDataPart("pageSize", 10 + "");
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
                handler.sendEmptyMessage(Contants.WALLET_GET_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("result-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray arr = data.getJSONArray("list");
                        if (arr.length() == 0) {
                            handler.sendEmptyMessage(Contants.WALLETDetail_GET_Nodata);
                        } else {
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

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "空投糖果", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            BalanceDetailsModel model = new BalanceDetailsModel();
            model.state = "支出";
            model.updateTimes = "2015-10-11  19:09:10";
            model.price = "+9.99";
            list.add(model);
        }
        adapter.notifyDataSetChanged();
    }

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
                    //initData();
                    adapter.notifyDataSetChanged();
                    loadMore.setText("加载更多");
                }
            }, 2000);
        }
    }

    //是否到达ListView底部
    boolean isLastRow = false;
}
