package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.model.WalletModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
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
 * 我的钱包
 * Created by admin on 2018/7/9.
 */

public class MyPropetryActivity extends BaseActivity {

    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_currency)
    TextView tvCurrency;
    @BindView(R.id.ll_balance_details)
    LinearLayout llBalanceDetails;
    @BindView(R.id.tv_candybox_balance)
    TextView tvCandyboxBalance;
    @BindView(R.id.ll_candy_box)
    LinearLayout llCandyBox;
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.rl_candy_box)
    RelativeLayout rlCandyBox;

    private WalletModel model;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case Contants.WALLET_GET_SUCCESS:
                         model = (WalletModel) message.obj;
                        tvBalance.setText(model.getMbtc() + "");
                        break;
                    case Contants.WALLET_GET_FAILURE:
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
        setView(R.layout.activity_my_propetry);
        initTitle();
        getWalletInfo();
    }


    /**
     * 获取钱包信息
     */
    private void getWalletInfo() {
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(this, Contants.USER_ID) + "");
        RequestBody requestBody = mbody.build();

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/wallet/info")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this, Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("register-----", e.toString());
                handler.sendEmptyMessage(Contants.WALLET_GET_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("result-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        JSONObject data = object.getJSONObject("data");
                        JSONObject wallet = data.getJSONObject("wallet");
                         model = new WalletModel();
                        model.walletId = wallet.getInt("walletId");
                        model.mbtc = wallet.getInt("mbtc");
                        model.status = wallet.getInt("status");
                        Message m = new Message();
                        m.what = Contants.WALLET_GET_SUCCESS;
                        m.obj = model;
                        handler.sendMessage(m);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "钱包", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.ll_balance_details, R.id.ll_candy_box})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_balance_details:
                ActivityUtils.startActivityWithModel(this, BalanceDetailsActivity.class,model);
                break;
            case R.id.ll_candy_box:
                break;
        }
    }


}
