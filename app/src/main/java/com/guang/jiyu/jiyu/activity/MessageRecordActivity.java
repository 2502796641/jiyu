package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/7/27.
 */

public class MessageRecordActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.rl_no_content)
    RelativeLayout rlNoContent;
    private int pageNumber = 1;
    private int totalRow = -1;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case Contants.MSGRecord_GET_Nodata:
                        lv.setVisibility(View.GONE);
                        rlNoContent.setVisibility(View.VISIBLE);
                        break;
                    case Contants.MSGRecord_GET_FAILURE:
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
        setView(R.layout.activity_message_record);
        initTitle();
        getMsgRecord();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "消息记录", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getMsgRecord() {
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(this, Contants.USER_ID) + "");
        mbody.addFormDataPart("pageSize", 10 + "");
        mbody.addFormDataPart("pageNumber", pageNumber + "");
        mbody.addFormDataPart("totalRow", totalRow + "");
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
                Log.d("result-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray arr = data.getJSONArray("list");
                        if (arr.length() == 0) {
                            handler.sendEmptyMessage(Contants.MSGRecord_GET_Nodata);
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
}
