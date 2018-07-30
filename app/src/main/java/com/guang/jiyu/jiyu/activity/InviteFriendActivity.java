package com.guang.jiyu.jiyu.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.LoadMoreAdapter;
import com.guang.jiyu.jiyu.listener.OnItemListener;
import com.guang.jiyu.jiyu.listener.OnScrollListener;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.LinkParams;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/7/9.
 */

public class InviteFriendActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tv_invite_code)
    TextView tvInviteCode;
    @BindView(R.id.btn_copy_invite_code)
    SuperButton btnCopyInviteCode;
    @BindView(R.id.lv_remark)
    ListView lvRemark;
    @BindView(R.id.tv_no_record)
    TextView tvNoRecord;

    private LoadMoreAdapter adapter;
    private Button loadMore;
    private int pageNumber = 1;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case Contants.INVITE_FRIENDrecord_GET_Nodata:
                        lvRemark.setVisibility(View.GONE);
                        loadMore.setVisibility(View.GONE);
                        tvNoRecord.setVisibility(View.VISIBLE);
                        break;
                    case Contants.MSGRecord_GET_FAILURE:

                        break;
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_invite_friend);

        initTitle();
        //initData();
        initBottomView();
        inviteFriendList();
    }

    private void inviteFriendList() {
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(this, Contants.USER_ID) + "");
        mbody.addFormDataPart("pageSize", 3 + "");
        mbody.addFormDataPart("pageNumber", pageNumber + "");
        RequestBody requestBody = mbody.build();

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/msg/list")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this, Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("register-----", e.toString());
                handler.sendEmptyMessage(Contants.MSGRecord_GET_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                handler.sendEmptyMessage(Contants.INVITE_FRIENDrecord_GET_FAILURE);
                Log.d("result-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray arr = data.getJSONArray("list");
                        if (arr.length() == 0) {
                            handler.sendEmptyMessage(Contants.INVITE_FRIENDrecord_GET_Nodata);
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
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "邀请好友", R.mipmap.icon_return_with_txt);
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
        loadMore.setOnClickListener(new ButtonClickListener());
        lvRemark.addFooterView(bottomView);
        lvRemark.setAdapter(adapter);
        lvRemark.setOnItemClickListener(new OnItemListener());
        lvRemark.setOnScrollListener(new OnScrollListener(isLastRow));
    }

    @OnClick(R.id.btn_copy_invite_code)
    public void onViewClicked() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(tvInviteCode.getText());
        ToastUtils.showToast("复制成功");
    }

    public void initData() {
        List<Integer> datas = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            datas.add(i + 1);
        }
        adapter = new LoadMoreAdapter(datas, this, Contants.TAG_INVITE_REMARK);
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
