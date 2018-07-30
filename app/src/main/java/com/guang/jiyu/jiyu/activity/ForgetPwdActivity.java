package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.net.BaseCallBack;
import com.guang.jiyu.jiyu.net.BaseOkHttpClient;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.MyTextUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/7/16.
 */

public class ForgetPwdActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.btn_get_verity_code)
    SuperButton btnGetVerityCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    SuperButton btnLogin;

    private String mobileNo;
    private String verifyCode;
    private String loginPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_forget_pwd);
    }

    @OnClick({R.id.btn_get_verity_code, R.id.btn_login})
    public void onViewClicked(View view) {
        mobileNo = etMobile.getText().toString();
        verifyCode = etVerifyCode.getText().toString();
        loginPwd = etPwd.getText().toString();

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
                if(TextUtils.isEmpty(verifyCode) || TextUtils.isEmpty(verifyCode) || TextUtils.isEmpty(loginPwd)){
                    ToastUtils.showToast("请先完善所有信息");
                    return;
                }

                resetPwd();
                break;
        }
    }

    /**
     * 重置密码
     */

    private void resetPwd() {
        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", String.valueOf(UserInfoUtils.getInt(this, Contants.USER_ID)));
        mbody.addFormDataPart("mobileNo", mobileNo);
        mbody.addFormDataPart("loginPwd",  MyTextUtils.md5Password(loginPwd));
        mbody.addFormDataPart("code", verifyCode);
        RequestBody requestBody =mbody.build();
        Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/user/resetpwd")
                .addHeader("encoding", "UTF-8")
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this,Contants.AUTHORIZATION))
                .post(requestBody)
                //取出本地保存的sessionId
                .build();
        Log.d("-----","--"+UserInfoUtils.getString(this,Contants.AUTHORIZATION));
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("resetpwd-----",e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("result-----",result);
                try {
                    JSONObject object = new JSONObject(result);
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
        mbody.addFormDataPart("type", "1");
        RequestBody requestBody =mbody.build();

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/user/sendSmsCode")
                .post(requestBody)
                .build();
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("register-----",e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("result-----",result);
                try {
                    JSONObject object = new JSONObject(result);
                    if("200".equals(object.getString("code"))){
                        //ToastUtils.showToast("验证码已发送");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
