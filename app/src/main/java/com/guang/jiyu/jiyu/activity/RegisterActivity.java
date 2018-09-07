package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.allen.library.SuperButton;
import com.google.gson.JsonObject;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.event.RegisterSuccessEvent;
import com.guang.jiyu.jiyu.model.PingNetModel;
import com.guang.jiyu.jiyu.net.BaseCallBack;
import com.guang.jiyu.jiyu.net.BaseOkHttpClient;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.MyTextUtils;
import com.guang.jiyu.jiyu.utils.PingNet;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/7/5.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.btn_get_verity_code)
    SuperButton btnGetVerityCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_again_pwd)
    EditText etAgainPwd;
    @BindView(R.id.btn_login)
    SuperButton btnLogin;

    private String mobileNo;
    private String verifyCode;
    private String pwd,pwd_again;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message != null){
                switch (message.what){

                }
            }
            return false;
        }
    });
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_register);
    }

    @OnClick({R.id.btn_get_verity_code, R.id.btn_login})
    public void onViewClicked(View view) {
        mobileNo = etMobile.getText().toString();
        verifyCode = etVerifyCode.getText().toString();
        pwd = etPwd.getText().toString();
        pwd_again = etAgainPwd.getText().toString();


        if(TextUtils.isEmpty(mobileNo)){
            ToastUtils.showToast("手机号不能为空！");
            return;
        }

        switch (view.getId()) {
            case R.id.btn_get_verity_code:
                if(MyTextUtils.isMobileNum(mobileNo)){
                    sendVerifyCode(mobileNo);
                }else{
                    ToastUtils.showToast("手机号码格式不正确");
                }
                break;
            case R.id.btn_login:
                if(TextUtils.isEmpty(verifyCode) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd_again)){
                    ToastUtils.showToast("请先完善所有信息");
                    return;
                }

                if(!pwd.equals(pwd_again)){
                    ToastUtils.showToast("两次所输密码不一致");
                    return;
                }

                doRegister(mobileNo,verifyCode,pwd);

                break;
        }
    }

    /**
     * 注册
     * @param mobileNo
     * @param verifyCode
     * @param pwd
     */
    private void doRegister(final String mobileNo, String verifyCode, final String pwd) {
        LogUtils.d("doRegister-----",mobileNo +"--" + verifyCode + "--" + pwd);
        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("mobileNo", mobileNo);
        mbody.addFormDataPart("loginPwd", MyTextUtils.md5Password(pwd));
        mbody.addFormDataPart("code", verifyCode);
        RequestBody requestBody =mbody.build();
        final Request request = new Request.Builder()
                //.addHeader("Content-Type",)
                .url(LinkParams.REQUEST_URL + "/user/register")
                .post(requestBody)
                .build();

        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("register-----",e.toString());
                Toast.makeText(RegisterActivity.this, "失败：" + e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("result-----",result);
                try {
                    JSONObject object = new JSONObject(result);
                    if("200".equals(object.getString("code"))){
                        UserInfoUtils.saveString(RegisterActivity.this, Contants.USER_NAME,mobileNo);
                        UserInfoUtils.saveString(RegisterActivity.this,Contants.USER_PWD,pwd);
                        EventBus.getDefault().post(new RegisterSuccessEvent(mobileNo,pwd));
                        ToastUtils.showToastInActivity(RegisterActivity.this,"注册成功！");
                        finish();
                    }
                    if("500".equals(object.getString("code"))){
                        ToastUtils.showToastInActivity(RegisterActivity.this,object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 发送验证码
     * @param mobile
     */
    private void sendVerifyCode(String mobile) {
        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("mobileNo", mobile);
        mbody.addFormDataPart("type", "0");
        RequestBody requestBody =mbody.build();

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/user/sendSmsCode")
                .post(requestBody)
                .build();
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("register-----",e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("result-----",result);
                try {
                    JSONObject object = new JSONObject(result);
                    if("200".equals(object.getString("code"))){
                        ToastUtils.showToastInActivity(RegisterActivity.this,"验证码已发送");
                    }

                    if("500".equals(object.getString("code"))){
                        ToastUtils.showToastInActivity(RegisterActivity.this,object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void ping() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PingNetModel pingNetEntity=new PingNetModel("192.168.2.73:8080",3,5,new StringBuffer());
                pingNetEntity= PingNet.ping(pingNetEntity);
                LogUtils.i("testPing",pingNetEntity.getIp());
                LogUtils.i("testPing","time="+pingNetEntity.getPingTime());
                LogUtils.i("testPing",pingNetEntity.isResult()+"");
            }
        }).start();
    }
}
