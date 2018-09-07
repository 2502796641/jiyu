package com.guang.jiyu.jiyu.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.activity.HashrateActivity;
import com.guang.jiyu.jiyu.activity.InviteFriendActivity;
import com.guang.jiyu.jiyu.activity.MyPropetryActivity;
import com.guang.jiyu.jiyu.activity.ReadBroadCastActivity;
import com.guang.jiyu.jiyu.activity.WhatIsBlackchainActivity;
import com.guang.jiyu.jiyu.adapter.GridHomeAdapter;
import com.guang.jiyu.jiyu.adapter.GridItemAdapter;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.event.NewsFlashRefreshEvent;
import com.guang.jiyu.jiyu.model.HashrateModel;
import com.guang.jiyu.jiyu.model.ProretryTypeModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.MyTextUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.AlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 首页fragment
 * Created by admin on 2018/6/19.
 */

public class HomeFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.tv_hashrate)
    TextView tvHashrate;
    @BindView(R.id.ll_wallet)
    LinearLayout llWallet;
    @BindView(R.id.ll_invite_friend)
    LinearLayout llInviteFriend;
    @BindView(R.id.grid)
    GridView grid;

    private GridItemAdapter gridItemAdapter;
    private HashrateModel hashrateModel;
    private List<ProretryTypeModel> list;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message != null){
                switch (message.what){
                    case Contants.GET_DATA_SUCCESS:
                        initList();
                        break;
                    case Contants.GET_HASHRATE_SUCCESS:
                        hashrateModel = (HashrateModel) message.obj;
                        tvHashrate.setText("算力：" + (hashrateModel.getFdnQtyC()+hashrateModel.getFdnQtyD()));
                        break;
                    case Contants.GET_HASHRATE_FAILURE:
                        hashrateModel = null;
                        tvHashrate.setText("算力：");
                        break;
                    case Contants.CURRENCY_GET_SUCCESS:
                        ToastUtils.showToast("领取成功");
                        queryProjectCoin();
                        break;
                    case Contants.GET_DATA_FAILURE:
                        ToastUtils.showToast("数据异常");
                        break;
                    case Contants.GET_DATA_NODATA:
                        list.clear();
                        gridItemAdapter.setList(list);
                        gridItemAdapter.notifyDataSetChanged();
                        break;
                }
            }
            return false;
        }
    });
    @Override
    public int getLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        list = new ArrayList<>();
        dataList = new ArrayList<>();
        gridItemAdapter = new GridItemAdapter(getContext(),list);
            //sendRecord();
        getHashrateInfo();
        return rootView;
    }

    /**
     * 获取算力信息
     */
    private void getHashrateInfo() {
        final MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(getContext(), Contants.USER_ID) + "");
        RequestBody requestBody = mbody.build();
        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/arith/info")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(getContext(), Contants.AUTHORIZATION))
                .post(requestBody)
                .build();

        OkHttpManage.getClient(getContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(Contants.GET_DATA_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("/arith/info-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        if(MyTextUtils.isEmpty(object.getString("data"))){
                            handler.sendEmptyMessage(Contants.GET_HASHRATE_FAILURE);
                        }else{
                            JSONObject data = new JSONObject(object.getString("data"));
                            hashrateModel = new HashrateModel();
                            hashrateModel.id = data.getInt("id");
                            hashrateModel.fdnQtyC = data.getInt("fdnQtyC");
                            hashrateModel.fdnQtyD = data.getInt("fdnQtyD");
                            Message m = new Message();
                            m.what = Contants.GET_HASHRATE_SUCCESS;
                            m.obj = hashrateModel;
                            handler.sendMessage(m);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(BaseEvent baseEvent) {
        if(baseEvent instanceof MessageEvent){
            MessageEvent event = (MessageEvent) baseEvent;
            if(Contants.EVENT_REFRESH_FRAGMENT_DATA.equals(event.getMessage())){
                queryProjectCoin();
                //getHashrateInfo();
/*                if(UserInfoUtils.isUserLogin(getContext())){

                }*/


            }
        }
    }

    @Override
    public void onResume() {
        LogUtils.d("onResume-----", "-----");
        super.onResume();
        queryProjectCoin();
        getHashrateInfo();
    }

    private void queryProjectCoin() {
        dataList.clear();
        JSONObject object = new JSONObject();
        try {
            object.put("userId", UserInfoUtils.getInt(getContext(),Contants.USER_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/api/project/queryProjectCoin")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(getContext(), Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(getContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("onFailure-----", e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("queryProjectCoin","----" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if("200".equals(object.getString("code"))){
                        dataList.clear();
                        JSONObject data = new JSONObject(object.getString("data"));
                        JSONArray arrProject = data.getJSONArray("projectList");
                        JSONArray arrStream = data.getJSONArray("streamList");
                        for(int i = 0;i < arrStream.length();i++){
                            JSONObject item = new JSONObject(arrStream.get(i).toString());
                            ProretryTypeModel model = new ProretryTypeModel();
                            model.itemId = item.getInt("itemId");
                            model.currency = item.getString("currency");
                            model.average = item.getString("average");
                            model.icon = item.getString("icon");
                            model.sendingLimit = item.getString("sendingLimit");
                            dataList.add(model);
                        }
                        for(int i = 0;i < arrProject.length();i++){
                            JSONObject item = new JSONObject(arrProject.get(i).toString());
                            ProretryTypeModel model = new ProretryTypeModel();
                            model.itemId = item.getInt("itemId");
                            model.theIssuingParty = item.getString("theIssuingParty");
                            model.broadcastContent = item.getString("broadcastContent");
                            model.broadcastImages1 = item.getString("broadcastImages1");
                            model.broadcastImages2 = item.getString("broadcastImages2");
                            model.broadcastImages3 = item.getString("broadcastImages3");
                            model.broadcastStartTime = item.getString("broadcastStartTime");
                            model.sendingLimit = item.getString("sendingLimit");
                            model.sendObject = item.getString("sendObject");
                            model.icon = item.getString("icon");
                            model.average = item.getString("average");
                            model.currency = item.getString("currency");
                            model.actuallyPay = item.getString("actuallyPay");
                            dataList.add(model);
                        }
                        handler.sendEmptyMessage(Contants.GET_DATA_SUCCESS);
                    }
                    if("500".equals(object.getString("code"))){
                        handler.sendEmptyMessage(Contants.GET_DATA_NODATA);
                    }


                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<ProretryTypeModel> dataList;
    private void initList() {
        list.clear();
/*        list = new ArrayList<ProretryTypeModel>();
        for (int i = 0; i < 21; i++) {
            ProretryTypeModel model = new ProretryTypeModel();
            if(i%2==1){
                model.setIconTag(1);
                model.setCount(String.valueOf(index));
                model.setType(String.valueOf(i));
                index++;
            }else{
                model.setCount("1");
                model.setType("1");
            }
            list.add(model);

        }

        GridHomeAdapter gridHomeAdapter = new GridHomeAdapter(getContext(),list);
        grid.setAdapter(gridHomeAdapter);*/


        for(int i = 0;i < dataList.size();i++){
            if(i < 9){
                list.add(dataList.get(i));
            }

        }

        grid.setAdapter(gridItemAdapter);
        gridItemAdapter.setList(dataList);
        gridItemAdapter.notifyDataSetChanged();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final ProretryTypeModel model = getCoin(i);
                if(dataList.size() > 0){
                    if("1".equals(dataList.get(i).getSendingLimit())){
                        EventBus.getDefault().post(new MessageEvent(Contants.EVENT_CHANGE_BLIGHTNESS));
                        String msg = "恭喜你获得1" + list.get(i).getCurrency() + ",需阅读广播方能领取，是否前去阅读？";
                        AlertDialog dialog = new AlertDialog(getActivity(), "去阅读", "放弃", msg , true);
                        dialog.setSampleDialogListener(new AlertDialog.OnDialogButtonClickListener() {
                            @Override
                            public void onConfirm() {
                                ActivityUtils.startActivityWithModel(getContext(), ReadBroadCastActivity.class,model);
                            }

                            @Override
                            public void onCancel() {
                                EventBus.getDefault().post(new MessageEvent(Contants.EVENT_RECOVER_BLIGHTNESS));
                            }
                        });
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                EventBus.getDefault().post(new MessageEvent(Contants.EVENT_RECOVER_BLIGHTNESS));
                            }
                        });
                    }
                }


            }
        });
    }

    /**
     * 点击领取项目币(判断是否需要阅读广播)
     * @param i
     */
    private ProretryTypeModel getCoin(int i) {
        JSONObject object = new JSONObject();
        final ProretryTypeModel model = new ProretryTypeModel();
        try {
            object.put("userId", UserInfoUtils.getInt(getContext(), Contants.USER_ID));
            object.put("itemId", list.get(i).getItemId());
            Log.d("getCoinobject",i + "----" + object.toString() + "----" + list.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/api/project/getCoin")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(getContext(), Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(getContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(Contants.GET_DATA_FAILURE);

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("getCoin","----" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if("200".equals(object.getString("code"))){
                        if(MyTextUtils.isEmpty(object.getString("data"))){
                            handler.sendEmptyMessage(Contants.CURRENCY_GET_SUCCESS);
                            return;
                        }
                        JSONObject item = new JSONObject(object.getString("data"));
                        model.theIssuingParty = item.getString("theIssuingParty");
                        model.itemId = item.getInt("itemId");
                        model.broadcastContent = item.getString("broadcastContent");
                        model.broadcastImages1 = item.getString("broadcastImages1");
                        model.broadcastImages2 = item.getString("broadcastImages2");
                        model.broadcastImages3 = item.getString("broadcastImages3");
                        model.broadcastStartTime = item.getString("broadcaststarttime");
                        model.sendingLimit = item.getString("sendingLimit");
                        model.icon = item.getString("icon");
                        model.average = item.getString("average");
                        model.currency = item.getString("currency");
                        model.actuallyPay = item.getString("actuallyPay");
                        //handler.sendEmptyMessage(Contants.CURRENCY_GET_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return model;
    }

    private void readRadio(int i) {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", UserInfoUtils.getInt(getContext(), Contants.USER_ID));
            object.put("itemId", list.get(i).getItemId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/api/project/readRadio")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(getContext(), Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(getContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(Contants.GET_DATA_FAILURE);

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("readRadio","----" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if("200".equals(object.getString("code"))){
                        handler.sendEmptyMessage(Contants.CURRENCY_GET_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_hashrate, R.id.ll_wallet, R.id.ll_invite_friend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_hashrate:
                ActivityUtils.startActivityWithModel(getContext(), HashrateActivity.class,hashrateModel);
                break;
            case R.id.ll_wallet:
                ActivityUtils.startActivity(getContext(), WhatIsBlackchainActivity.class);
                break;
            case R.id.ll_invite_friend:
                ActivityUtils.startActivity(getContext(), InviteFriendActivity.class);
                break;
        }
    }
}
