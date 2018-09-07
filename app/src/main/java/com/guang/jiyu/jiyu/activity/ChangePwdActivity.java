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

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.MyTextUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

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
 * 修改密码
 * Created by admin on 2018/6/21.
 */

public class ChangePwdActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tv_binding_mobile)
    TextView tvBindingMobile;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.btn_send_verify_code)
    SuperButton btnSendVerifyCode;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_confirm_new_pwd)
    EditText etConfirmNewPwd;
    @BindView(R.id.btn_comfirm)
    SuperButton btnComfirm;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message != null){
                switch (message.what){
                    case Contants.SendCode_Success:
                        ToastUtils.showToast("验证码已发送");
                        break;
                    case Contants.Reset_PWD_SUCCESS:
                        ToastUtils.showToast("修改成功");
                        finish();
                        break;
                }
            }
            return false;
        }
    });

    @OnClick({R.id.btn_send_verify_code,R.id.btn_comfirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_send_verify_code:
                sendVerifyCode(UserInfoUtils.getString(this, Contants.MOBILE_NO));
                break;
            case R.id.btn_comfirm:
                String verity_code = etVerifyCode.getText().toString();
                String new_pwd = etNewPwd.getText().toString();
                String confirm_newPwd = etConfirmNewPwd.getText().toString();
                if(TextUtils.isEmpty(verity_code) || TextUtils.isEmpty(new_pwd) ||TextUtils.isEmpty(confirm_newPwd)){
                    ToastUtils.showToast("信息未完善！");
                    return;
                }

                if(!new_pwd.equals(confirm_newPwd)){
                    ToastUtils.showToast("两次输入的密码不一致！");
                    return;
                }

                resetPwd(new_pwd,verity_code);
                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_change_pwd);
        initTitle();

    }

    /**
     * 重置密码
     * @param new_pwd
     * @param verity_code
     */
    private void resetPwd(String new_pwd, String verity_code) {
        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", String.valueOf(UserInfoUtils.getInt(this, Contants.USER_ID)));
        mbody.addFormDataPart("mobileNo", UserInfoUtils.getString(this, Contants.MOBILE_NO));
        mbody.addFormDataPart("loginPwd",  MyTextUtils.md5Password(new_pwd));
        mbody.addFormDataPart("code", verity_code);
        RequestBody requestBody =mbody.build();
        Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/user/resetpwd")
                .addHeader("encoding", "UTF-8")
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this,Contants.AUTHORIZATION))
                .post(requestBody)
                //取出本地保存的sessionId
                .build();
        LogUtils.d("-----","--"+UserInfoUtils.getString(this,Contants.AUTHORIZATION));
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("resetpwd-----",e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("result-----",result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        handler.sendEmptyMessage(Contants.Reset_PWD_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "修改密码", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvBindingMobile.setText(UserInfoUtils.getString(this, Contants.MOBILE_NO));
    }
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
                LogUtils.d("register-----",e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("result-----",result);
                try {
                    JSONObject object = new JSONObject(result);
                    if("200".equals(object.getString("code"))){
                        handler.sendEmptyMessage(Contants.SendCode_Success);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
