package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.model.ProretryTypeModel;
import com.guang.jiyu.jiyu.model.WalletModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.MyTextUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.SharePopupWindow;
import com.guang.jiyu.jiyu.widget.TitleBar;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/8/23.
 */

public class ReadBroadCastActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.btn_login)
    SuperButton btnLogin;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.imageView3)
    ImageView imageView3;

    private SharePopupWindow sharePopupWindow;
    private ProretryTypeModel proretryTypeModel;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message != null){
                switch (message.what){
                    case Contants.CURRENCY_GET_SUCCESS:
                        ToastUtils.showToast("领取成功！");
                        finish();
                        break;
                    case Contants.GET_DATA_FAILURE:
                        ToastUtils.showToast("数据异常");
                        break;
                }
            }
            return false;
        }
    });
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_read_broadcast);
        initTitle();
        proretryTypeModel = (ProretryTypeModel) getIntent().getSerializableExtra("model");
        LogUtils.d("ProretryTypeModel","---" + proretryTypeModel.toString());
        initData();
    }

    private void initData() {
        tvContent.setText(proretryTypeModel.getBroadcastContent());
        if(!MyTextUtils.isEmpty(proretryTypeModel.getBroadcastImages1())){
            imageView1.setVisibility(View.VISIBLE);
            Picasso.get().load(proretryTypeModel.getBroadcastImages1()).into(imageView1);
        }
        if(!MyTextUtils.isEmpty(proretryTypeModel.getBroadcastImages2())){
            imageView2.setVisibility(View.VISIBLE);
            Picasso.get().load(proretryTypeModel.getBroadcastImages2()).into(imageView2);
        }
        if(!MyTextUtils.isEmpty(proretryTypeModel.getBroadcastImages3())){
            imageView3.setVisibility(View.VISIBLE);
            Picasso.get().load(proretryTypeModel.getBroadcastImages3()).into(imageView3);
        }
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "广播详情", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.addAction(new TitleBar.ImageAction(R.mipmap.icon_share) {
            @Override
            public void performAction(View view) {
                initPopWindow();
            }
        });
    }

    private void initPopWindow() {
        sharePopupWindow = new SharePopupWindow(this);
        sharePopupWindow.setOnItemClickListener(new SharePopupWindow.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_weibo:
                        ToastUtils.showToast("微博");
                        break;
                    case R.id.rl_wechat:
                        ToastUtils.showToast("微信");
                        break;
                    case R.id.rl_friends_circle:
                        ToastUtils.showToast("朋友圈");
                        break;
                    case R.id.rl_qq:
                        ToastUtils.showToast("QQ");
                        break;
                }
            }
        });
        sharePopupWindow.showAtLocation(ReadBroadCastActivity.this.findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
        setWindowAttr(0.6f);
        sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAttr(1f);
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        readRadio();
        finish();
    }

    /**
     * 阅读广播后领取项目币
     */
    private void readRadio() {
        if(!UserInfoUtils.isUserLogin(this)){
            ToastUtils.showToast("请先登录");
            ActivityUtils.startActivity(this,LoginActivity.class);
            return;
        }
        LogUtils.d("readRadio",UserInfoUtils.getInt(this, Contants.USER_ID) + "----" + proretryTypeModel.getItemId());
        JSONObject object = new JSONObject();
        try {
            object.put("userId", UserInfoUtils.getInt(this, Contants.USER_ID));
            object.put("itemId", proretryTypeModel.getItemId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/api/project/readRadio")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this, Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(Contants.GET_DATA_FAILURE);

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("readRadio","----" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if("200".equals(object.getString("code"))){
                        EventBus.getDefault().post(new MessageEvent(Contants.EVENT_REFRESH_FRAGMENT_DATA));
                        handler.sendEmptyMessage(Contants.CURRENCY_GET_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
