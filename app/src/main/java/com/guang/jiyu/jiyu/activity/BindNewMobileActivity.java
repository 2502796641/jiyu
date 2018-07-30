package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.allen.library.SuperButton;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 绑定新手机activity
 * Created by admin on 2018/6/21.
 */

public class BindNewMobileActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_new_mobile_num)
    EditText etNewMobileNum;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.btn_send_verify_code)
    SuperButton btnSendVerifyCode;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message != null){
                switch (message.what){
                    case Contants.SendCode_ChangeMobile:
                        ToastUtils.showToast("验证码已发送");
                        break;
                    case Contants.SendCode_Failure:
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
        setView(R.layout.activity_bind_new_mobile);
        initTitle();
    }

    @OnClick({R.id.btn_send_verify_code})
    public void click(View view){
        switch (view.getId()){
            case R.id.btn_send_verify_code:
                sendVerifyCode(UserInfoUtils.getString(this,Contants.MOBILE_NO));
                break;
        }
    }


    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this,titlebar,"更换手机号",R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titlebar.addAction(new TitleBar.TextAction("完成") {
            @Override
            public void performAction(View view) {
                ToastUtils.showToast("完成");
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
        mbody.addFormDataPart("type", "2");
        RequestBody requestBody =mbody.build();

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/user/sendSmsCode")
                .post(requestBody)
                .build();
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ChangeMobileNumber-----",e.toString());
                handler.sendEmptyMessage(Contants.SendCode_Failure);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("result-----",result);
                try {
                    JSONObject object = new JSONObject(result);
                    if("200".equals(object.getString("code"))){
                        handler.sendEmptyMessage(Contants.SendCode_ChangeMobile);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
