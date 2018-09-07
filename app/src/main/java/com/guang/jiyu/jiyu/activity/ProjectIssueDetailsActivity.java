package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.model.ProretryTypeModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.MyTextUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/8/24.
 */

public class ProjectIssueDetailsActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.id_project_broadcast_start_time)
    TextView idProjectBroadcastStartTime;
    @BindView(R.id.id_project_broadcast_image)
    ImageView idProjectBroadcastImage;
    @BindView(R.id.id_project_broadcast_Send_currency)
    TextView idProjectBroadcastSendCurrency;
    @BindView(R.id.id_project_broadcast_Send_object)
    TextView idProjectBroadcastSendObject;
    @BindView(R.id.id_project_broadcast_Sending_limit)
    TextView idProjectBroadcastSendingLimit;
    @BindView(R.id.id_project_broadcast_sent_1)
    TextView idProjectBroadcastSent1;
    @BindView(R.id.id_project_broadcast_sent_2)
    TextView idProjectBroadcastSent2;
    @BindView(R.id.id_project_broadcast_Get_set_up_1)
    TextView idProjectBroadcastGetSetUp1;
    @BindView(R.id.id_project_broadcast_Get_set_up_2)
    TextView idProjectBroadcastGetSetUp2;
    @BindView(R.id.id_project_broadcast_Total_cost)
    TextView idProjectBroadcastTotalCost;
    @BindView(R.id.id_project_broadcast_service_charge)
    TextView idProjectBroadcastServiceCharge;
    @BindView(R.id.id_project_broadcast_actual_costs)
    TextView idProjectBroadcastActualCosts;
    @BindView(R.id.tv_currency_resource)
    TextView tvCurrencyResource;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.tv_boardcast_content)
    TextView tvBoardcastContent;
    private int[] check_icon = new int[]{R.mipmap.b1_01, R.mipmap.b1_02, R.mipmap.b1_03, R.mipmap.b1_04,
            R.mipmap.b1_05, R.mipmap.b1_06, R.mipmap.b1_07, R.mipmap.b1_08};
    private ProretryTypeModel proretryTypeModel;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message != null){
                switch (message.what){
                    //发送对象:0->注册用户；1->身份认证；2->代币
                    //发送限制:0->无限制;1:需要阅读广播
                    case Contants.GET_DATA_SUCCESS:
                        proretryTypeModel = (ProretryTypeModel) message.obj;
                        initDetails(proretryTypeModel);


                        break;
                }
            }
            return false;
        }
    });

    private void initDetails(ProretryTypeModel proretryTypeModel) {
        tvCurrencyResource.setText(proretryTypeModel.getTheIssuingParty());
        tvBoardcastContent.setText(proretryTypeModel.getBroadcastContent());
        idProjectBroadcastStartTime.setText(proretryTypeModel.getBroadcastStartTime());
        int icon = Integer.parseInt(proretryTypeModel.getIcon());
        idProjectBroadcastImage.setImageResource(check_icon[icon]);
        idProjectBroadcastSendCurrency.setText(proretryTypeModel.getCurrency());
        String sendingLimit = proretryTypeModel.getSendingLimit();
        String sendObject = proretryTypeModel.getSendObject();

        if(!MyTextUtils.isEmpty(proretryTypeModel.getBroadcastImages1())){
            imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(proretryTypeModel.getBroadcastImages1()).into(imageView);
        }else{
            imageView.setVisibility(View.GONE);
        }

        if(!MyTextUtils.isEmpty(proretryTypeModel.getBroadcastImages2())){
            imageView2.setVisibility(View.VISIBLE);
            Picasso.get().load(proretryTypeModel.getBroadcastImages2()).into(imageView2);
        }else{
            imageView2.setVisibility(View.GONE);
        }

        if(!MyTextUtils.isEmpty(proretryTypeModel.getBroadcastImages3())){
            imageView3.setVisibility(View.VISIBLE);
            Picasso.get().load(proretryTypeModel.getBroadcastImages3()).into(imageView3);
        }else{
            imageView3.setVisibility(View.GONE);
        }


        if("0".equals(sendingLimit)){
            idProjectBroadcastSendingLimit.setText("无限制");
        }else if("1".equals(sendingLimit)){
            idProjectBroadcastSendingLimit.setText("需要阅读广播");
        }

        if("0".equals(sendObject)){
            idProjectBroadcastSendObject.setText("注册用户");
        }else if("1".equals(sendObject)){
            idProjectBroadcastSendObject.setText("身份认证");
        }else if("2".equals(sendObject)){
            idProjectBroadcastSendObject.setText("代币");
        }
        idProjectBroadcastSent1.setText(proretryTypeModel.getAverage());
        idProjectBroadcastSent2.setText(proretryTypeModel.getPeopleNumber());
        idProjectBroadcastGetSetUp1.setText(proretryTypeModel.getAverage());
        idProjectBroadcastTotalCost.setText(proretryTypeModel.getSendTheTotal() + "");
        idProjectBroadcastServiceCharge.setText(MyTextUtils.doubleFormat(proretryTypeModel.getSendTheTotal() * 0.02) + "");
        idProjectBroadcastActualCosts.setText(proretryTypeModel.getActuallyPay());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_project_issue_details);
        initTitle();
        proretryTypeModel = (ProretryTypeModel) getIntent().getSerializableExtra("model");
        projectRecordDetails();

    }

    private void projectRecordDetails() {
        JSONObject object = new JSONObject();
        try {
            object.put("itemId", proretryTypeModel.getItemId());
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    .url(LinkParams.REQUEST_URL + "/api/project/recordDetails")
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
                    LogUtils.d("sendRecordDetail", "----" + result);
                    try {
                        JSONObject object = new JSONObject(result);
                        if ("200".equals(object.getString("code"))) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            proretryTypeModel.theIssuingParty = data.getString("theIssuingParty");
                            proretryTypeModel.broadcastContent = data.getString("broadcastContent");
                            proretryTypeModel.broadcastImages1 = data.getString("broadcastImages1");
                            proretryTypeModel.broadcastImages2 = data.getString("broadcastImages2");
                            proretryTypeModel.broadcastImages3 = data.getString("broadcastImages3");
                            proretryTypeModel.broadcastStartTime = data.getString("broadcastStartTime");
                            proretryTypeModel.icon = data.getString("icon");
                            proretryTypeModel.currency = data.getString("currency");
                            proretryTypeModel.sendingLimit = data.getString("sendingLimit");
                            proretryTypeModel.average = data.getString("average");
                            proretryTypeModel.peopleNumber = data.getString("peopleNumber");
                            proretryTypeModel.sendTheTotal = Integer.parseInt(data.getString("sendTheTotal"));
                            proretryTypeModel.actuallyPay = data.getString("actuallyPay");
                            proretryTypeModel.sendObject = data.getString("sendObject");

                            Message m = new Message();
                            m.what = Contants.GET_DATA_SUCCESS;
                            m.obj = proretryTypeModel;
                            handler.sendMessage(m);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "发布详情", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
