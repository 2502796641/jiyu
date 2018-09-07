package com.guang.jiyu.jiyu.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.InfoAdapter;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.model.InformationModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.PullToRefreshLayout.BaseRefreshListener;
import com.guang.jiyu.jiyu.widget.PullToRefreshLayout.PullToRefreshLayout;
import com.guang.jiyu.jiyu.widget.TitleBar;
import com.zhl.cbdialog.CBDialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/7/9.
 */

public class MyCollectionActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.btn_jump_information)
    SuperButton btnJumpInformation;
    @BindView(R.id.rl_no_information)
    RelativeLayout rlNoInformation;
    @BindView(R.id.refresh_layout)
    PullToRefreshLayout refreshLayout;
    @BindView(R.id.lv)
    ListView lv;

    private InfoAdapter adapter;
    private Message m;
    private int pageNumber = 1;
    private int totalRow = -1;
    private boolean isLastPage;
    private boolean isFirstPage;
    private List<InformationModel> list;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case Contants.INFOCollect_NOData:
                        rlNoInformation.setVisibility(View.VISIBLE);
                        refreshLayout.setVisibility(View.GONE);
                        break;
                    case Contants.INFOCollect_Failure:
                        rlNoInformation.setVisibility(View.VISIBLE);
                        refreshLayout.setVisibility(View.GONE);
                        ToastUtils.showToast((String) message.obj);
                        break;
                    case Contants.INFOCollect_HaveData:
                        ToastUtils.showToast("刷新成功");
                        rlNoInformation.setVisibility(View.GONE);
                        refreshLayout.setVisibility(View.VISIBLE);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        list = (List<InformationModel>) message.obj;
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                ActivityUtils.startActivityWithModel(MyCollectionActivity.this, InformationDetailsActivity.class,list.get(i));
                            }
                        });
                        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                initDialog(i);
                                return true;
                            }
                        });
                        break;
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_my_collection);
        list = new ArrayList<>();
        adapter = new InfoAdapter(list, this);
        lv.setAdapter(adapter);
        initTitle();
        setRefreshListen();
    }

    private void initDialog(final int i) {
        final CBDialogBuilder cbDialogBuilder = new CBDialogBuilder(this);
        cbDialogBuilder.setTouchOutSideCancelable(true)
                .setTitle("是否取消收藏该资讯？")
                .setMessage("")
                .showCancelButton(true)
                .setConfirmButtonText("确定")
                .setCancelButtonText("取消")
                .create().show();

        //.setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_SLID_BOTTOM)
        cbDialogBuilder.setButtonClickListener(true,new CBDialogBuilder.onDialogbtnClickListener() {
            @Override
            public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                switch (whichBtn) {
                    case BUTTON_CONFIRM:
                        cancelCollection(list.get(i));
                        list.remove(list.get(i));
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        break;
                    case BUTTON_CANCEL:
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    /**
     * 取消收藏
     * @param informationModel
     */
    private void cancelCollection(InformationModel informationModel) {
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(this, Contants.USER_ID) + "");
        mbody.addFormDataPart("infoId", informationModel.getInfoId() + "");
        RequestBody requestBody = mbody.build();
        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/favorite/delete")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this, Contants.AUTHORIZATION))
                .delete(requestBody)
                .build();
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("cancelCollectionfail", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("cancelCollectionsuccess", result);
            }
        });
    }

    /**
     * 调用收藏接口
     */
    private void initData(int pageNumber) {
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(this, Contants.USER_ID) + "");
        mbody.addFormDataPart("pageSize", 5 + "");
        mbody.addFormDataPart("pageNumber", pageNumber + "");
        mbody.addFormDataPart("totalRow", totalRow + "");
        RequestBody requestBody = mbody.build();

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/favorite/list")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this, Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("register-----", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("result-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        JSONObject data = new JSONObject(object.getString("data"));
                        JSONArray arr = data.getJSONArray("list");
                        if (arr.length() == 0) {
                            handler.sendEmptyMessage(Contants.INFOCollect_NOData);
                        }else{
                            totalRow = data.getInt("totalRow");
                            isLastPage = data.getBoolean("lastPage");
                            isFirstPage = data.getBoolean("firstPage");
                            for (int i = 0; i < arr.length(); i++) {
                                InformationModel model = new InformationModel();
                                JSONObject item = new JSONObject(arr.get(i).toString());
                                model.infoId = item.getString("infoId");
                                model.title = item.getString("title");
                                model.content = item.getString("content");
                                model.resource = item.getString("infoSource");
                                model.reading_count = item.getString("click");
                                model.img_url = item.getString("photo");
                                list.add(model);
                            }
                            m = new Message();
                            m.what = Contants.INFOCollect_HaveData;
                            m.obj = list;
                            handler.sendMessage(m);

                        }
                    }

                    if("500".equals(object.getString("code"))){
                        m = new Message();
                        m.what = Contants.INFOCollect_Failure;
                        m.obj = object.getString("message");
                        handler.sendMessage(m);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "我的收藏", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_jump_information)
    public void onViewClicked() {
        EventBus.getDefault().post(new MessageEvent(Contants.EVENT_INFORMATION));
        finish();
    }

    private void setRefreshListen() {
        refreshLayout.autoRefresh();
        refreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                if(isFirstPage){
                    refreshLayout.finishLoadMore();
                    refreshLayout.finishRefresh();
                    ToastUtils.showToast("已是最新数据");
                }else{
                    initData(pageNumber);
                }

            }

            @Override
            public void loadMore() {
                if(isLastPage){
                    refreshLayout.finishLoadMore();
                    refreshLayout.finishRefresh();
                    ToastUtils.showToast("没有更多数据了");
                }else{
                    initData(pageNumber++);
                }

            }
        });
    }
}
