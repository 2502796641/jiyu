package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.event.RegisterSuccessEvent;
import com.guang.jiyu.jiyu.event.UserInfoEvent;
import com.guang.jiyu.jiyu.model.UserInfoModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.MyTextUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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
 * Created by admin on 2018/7/5.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    SuperButton btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;

    private String mobileNo;
    private String loginPwd;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message != null){
                switch (message.what){
                    case Contants.USER_LOGIN_SUCCESS:
                        ToastUtils.showToast("登录成功！");
                        EventBus.getDefault().post(new UserInfoEvent((UserInfoModel) message.obj));
                        finish();
                        break;
                    case Contants.LOGIN_failure:
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
        setView(R.layout.activity_login);
    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pwd})
    public void onViewClicked(View view) {
        mobileNo = etMobile.getText().toString();
        loginPwd = etPwd.getText().toString();
        switch (view.getId()) {
            case R.id.btn_login:
                if(!TextUtils.isEmpty(mobileNo) && !TextUtils.isEmpty(loginPwd)){
                    doLogin();
                }else{
                    ToastUtils.showToast("输入不能为空");
                }
                break;
            case R.id.tv_register:
                ActivityUtils.startActivity(this,RegisterActivity.class);
                break;
            case R.id.tv_forget_pwd:
                ActivityUtils.startActivity(this,ForgetPwdActivity.class);
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(BaseEvent baseEvent) {
        if(baseEvent instanceof RegisterSuccessEvent){
            RegisterSuccessEvent event = (RegisterSuccessEvent) baseEvent;
            etMobile.setText(event.getAccount());
            etPwd.setText(event.getPwd());
        }
    }

    private void doLogin() {
        LogUtils.d("doLogin-----",mobileNo +"--" + loginPwd);
        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("mobileNo", mobileNo);
        mbody.addFormDataPart("loginPwd", MyTextUtils.md5Password(loginPwd));
        mbody.addFormDataPart("uuid", UserInfoUtils.getString(this,Contants.USER_UUID));
        RequestBody requestBody =mbody.build();
        final Request request = new Request.Builder()
                //.addHeader("Content-Type",)
                .url(LinkParams.REQUEST_URL + "/user/login")
                .post(requestBody)
                .build();

        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("login-----",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                LogUtils.d("result-----",result + "----" + response.headers().get("authorization"));
                try {
                    JSONObject object = new JSONObject(result);
                    if("200".equals(object.getString("code"))){
                        JSONObject data = new JSONObject(object.get("data").toString());
                        UserInfoModel model = new UserInfoModel();
                        model.userId = data.getInt("userId");
                        model.mobileNo = data.getString("mobileNo");
                        model.userName = data.getString("userName");
                        UserInfoUtils.saveString(LoginActivity.this, Contants.USER_NAME,model.userName);
                        UserInfoUtils.saveString(LoginActivity.this, Contants.MOBILE_NO,model.mobileNo);
                        UserInfoUtils.saveString(LoginActivity.this,Contants.USER_PWD,loginPwd);
                        UserInfoUtils.saveString(LoginActivity.this,Contants.AUTHORIZATION,response.headers().get("authorization"));
                        UserInfoUtils.saveInt(LoginActivity.this,Contants.USER_ID,model.userId);
                        Message m = new Message();
                        m.what = Contants.USER_LOGIN_SUCCESS;
                        m.obj = model;
                        handler.sendMessage(m);
                    }
                    if("500".equals(object.getString("code"))){
                        Message m = new Message();
                        m.what = Contants.LOGIN_failure;
                        m.obj = object.getString("message");
                        handler.sendMessage(m);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
