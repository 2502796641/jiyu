package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
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
 * 填写邀请码
 * Created by admin on 2018/7/5.
 */

public class InvitationCodeActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_invitation_code)
    EditText etInvitationCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_invitation_code);
        initTitle();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this,titlebar,"填写邀请码",R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.addAction(new TitleBar.TextAction("保存") {
            @Override
            public void performAction(View view) {
                String inviteCode = etInvitationCode.getText().toString();
                if(TextUtils.isEmpty(inviteCode)){
                    ToastUtils.showToast("验证码不能为空!");
                    return;
                }
                inviteCode(inviteCode);
            }
        });
    }

    private void inviteCode(String inviteCode) {
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(this, Contants.USER_ID) + "");
        mbody.addFormDataPart("mobileNo", UserInfoUtils.getString(this,Contants.MOBILE_NO));
        mbody.addFormDataPart("invitationCode", inviteCode);
        RequestBody requestBody = mbody.build();

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/invite/code")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this, Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure-----", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Log.d("result-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
