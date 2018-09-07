package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.model.HashrateModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/6/22.
 */

public class HashrateActivity extends BaseActivity {


    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.id_calculation_force_Inviting_friends_Invitation)
    Button idCalculationForceInvitingFriendsInvitation;
    @BindView(R.id.id_calculation_force_authentication)
    Button idCalculationForceAuthentication;
    @BindView(R.id.id_calculation_force_study)
    Button idCalculationForceStudy;
    @BindView(R.id.id_calculation_force_Sign_in)
    Button idCalculationForceSignIn;
    @BindView(R.id.id_calculation_force_Read)
    Button idCalculationForceRead;
    @BindView(R.id.iv_hashrate_explain)
    ImageView ivHashrateExplain;
    @BindView(R.id.id_basic_calculation_force)
    TextView idBasicCalculationForce;
    @BindView(R.id.id_temporary_calculation_force)
    TextView idTemporaryCalculationForce;
    @BindView(R.id.tv_all_hashrate)
    TextView tvAllHashrate;

    private HashrateModel hashrateModel;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case Contants.GET_DATA_FAILURE:
                        ToastUtils.showToast("数据异常");
                        break;
                    case Contants.STUDY_SUCCESS:
                        ToastUtils.showToast("学习成功");
                        break;
                    case Contants.CKECKIN_SUCCESS:
                        ToastUtils.showToast("签到成功");
                        break;
                    case Contants.CKECKIN_ALREADY:
                        ToastUtils.showToast((String) message.obj);
                        break;
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_hashrate);
        initTitle();
        hashrateModel = (HashrateModel) getIntent().getSerializableExtra("model");
        if(hashrateModel != null){
            idBasicCalculationForce.setText(hashrateModel.getFdnQtyC() + "");
            idTemporaryCalculationForce.setText(hashrateModel.getFdnQtyD() + "");
            tvAllHashrate.setText((hashrateModel.getFdnQtyC()+hashrateModel.getFdnQtyD()) + "");
        }

    }



    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "算力", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.id_calculation_force_Inviting_friends_Invitation, R.id.id_calculation_force_authentication, R.id.id_calculation_force_study, R.id.id_calculation_force_Sign_in, R.id.id_calculation_force_Read, R.id.iv_hashrate_explain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_hashrate_explain:
                ActivityUtils.startActivity(this, HashrateRecordActivity.class);
                break;
            case R.id.id_calculation_force_Inviting_friends_Invitation:
                ActivityUtils.startActivity(this, InviteFriendActivity.class);
                break;
            case R.id.id_calculation_force_authentication:
                ActivityUtils.startActivity(this, IdentityAuthenticateActivity.class);
                break;
            case R.id.id_calculation_force_study:
                if(UserInfoUtils.isUserLogin(this)){
                    dayLearn();
                }else{
                    ToastUtils.showToast("请先登录");
                    ActivityUtils.startActivity(this,LoginActivity.class);
                    finish();
                }
                break;
            case R.id.id_calculation_force_Sign_in:
                if(UserInfoUtils.isUserLogin(this)){
                    dayCheckIn();
                }else{
                    ToastUtils.showToast("请先登录");
                    ActivityUtils.startActivity(this,LoginActivity.class);
                    finish();
                }

                break;
            case R.id.id_calculation_force_Read:
                EventBus.getDefault().post(new MessageEvent(Contants.EVENT_INFORMATION));
                finish();
                break;
        }
    }

    /**
     * 每日签到
     */
    private void dayCheckIn() {
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(this, Contants.USER_ID) + "");
        RequestBody requestBody = mbody.build();

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/arith/checkin")
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
                LogUtils.d("/arith/checkin-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        handler.sendEmptyMessage(Contants.CKECKIN_SUCCESS);
                    }
                    if ("500".equals(object.getString("code"))) {
                        Message m = new Message();
                        m.what = Contants.CKECKIN_ALREADY;
                        m.obj = object.getString("message");
                        handler.sendMessage(m);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 每日学习
     */
    private void dayLearn() {
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(this, Contants.USER_ID) + "");
        RequestBody requestBody = mbody.build();

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/arith/learn")
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
                LogUtils.d("/arith/learn-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        handler.sendEmptyMessage(Contants.STUDY_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
