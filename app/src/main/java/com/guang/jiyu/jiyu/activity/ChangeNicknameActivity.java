package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

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
 * 修改昵称
 * Created by admin on 2018/6/20.
 */

public class ChangeNicknameActivity extends BaseActivity {
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    private String nickname;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_change_nickname);
        initTitle();
    }


    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this,titlebar,"修改昵称",R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.addAction(new TitleBar.TextAction("完成") {
            @Override
            public void performAction(View view) {
                nickname = etNickname.getText().toString();
                if(TextUtils.isEmpty(nickname)){
                    ToastUtils.showToast("输入不能为空");
                    return;
                }
                changeNickName();
            }
        });
    }

    private void changeNickName() {
        Log.d("USER_ID-----","" + UserInfoUtils.getInt(this, Contants.USER_ID));
        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(this, Contants.USER_ID)+"");
        mbody.addFormDataPart("userName", nickname);
        RequestBody requestBody =mbody.build();

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/user/modifyNickName")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this,Contants.AUTHORIZATION))
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UserInfoUtils.saveString(ChangeNicknameActivity.this,Contants.USER_NAME,nickname);
                                ToastUtils.showToastInActivity(ChangeNicknameActivity.this,"修改成功！");
                                ChangeNicknameActivity.this.finish();
                            }
                        });
                        //ToastUtils.showToast("验证码已发送");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
