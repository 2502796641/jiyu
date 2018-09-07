package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.InfoAdapter;
import com.guang.jiyu.jiyu.model.InformationDetailModel;
import com.guang.jiyu.jiyu.model.InformationModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 资讯搜索界面
 * Created by admin on 2018/6/21.
 */

public class InformationSearchActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_clear_history)
    ImageView ivClearHistory;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    @BindView(R.id.ll_flowlayout)
    LinearLayout llFlowlayout;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    @BindView(R.id.rl_nothing)
    RelativeLayout rlNothing;
    @BindView(R.id.iv_nothing)
    ImageView ivNothing;
    @BindView(R.id.lv_search_result)
    ListView lvSearchResult;
    @BindView(R.id.ll_search_result)
    LinearLayout llSearchResult;

    private TagAdapter<String> mAdapter;
    private String[] mVals;
    private List<InformationModel> list;
    private List<String> value_list;
    private InfoAdapter adapter;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case Contants.INFOSearch_GET_NOTHING:
                        ToastUtils.showToast("暂无该类信息");
                        rlNothing.setVisibility(View.VISIBLE);
                        llFlowlayout.setVisibility(View.GONE);
                        llSearchResult.setVisibility(View.GONE);
                        break;
                    case Contants.INFOSearch_GET_FAILURE:
                        ToastUtils.showToast("数据异常");
                        break;
                    case Contants.INFOSearch_GET_SUCCESS:
                        rlNothing.setVisibility(View.GONE);
                        llFlowlayout.setVisibility(View.GONE);
                        llSearchResult.setVisibility(View.VISIBLE);
                        list = (List<InformationModel>) message.obj;
                        adapter = new InfoAdapter(list,InformationSearchActivity.this);
                        lvSearchResult.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        lvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                ActivityUtils.startActivityWithModel(InformationSearchActivity.this,InformationDetailsActivity.class,list.get(i));
                            }
                        });
                        break;
                }
            }
            return false;
        }
    });

    @OnClick({R.id.tv_search, R.id.iv_clear_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                String str = etSearch.getText().toString();
                rlNothing.setVisibility(View.GONE);
                llFlowlayout.setVisibility(View.VISIBLE);
                llSearchResult.setVisibility(View.GONE);
                if (TextUtils.isEmpty(str)) {
                    ToastUtils.showToast("关键字不能为空!");
                    return;
                }
                infoSearch(str);
                break;
            case R.id.iv_clear_history:
                UserInfoUtils.remove(this,Contants.INFOSearch_KEYWORD);
                initFlowLayout();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_information_search);
        initFlowLayout();
    }

    /**
     * 资讯搜索
     *
     * @param str
     */
    private void infoSearch(String str) {
        saveKeyWord(str);
        JSONObject object = new JSONObject();
        try {
            object.put("kw", str);
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    .url(LinkParams.REQUEST_URL + "/info/infoSearch")
                    .post(requestBody)
                    .build();

            OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtils.d("onFailure-----", e.toString());
                    handler.sendEmptyMessage(Contants.INFOSearch_GET_FAILURE);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    try {
                        JSONObject object = new JSONObject(result);
                        if ("200".equals(object.getString("code"))) {
                            list = new ArrayList<>();
                            JSONArray data = object.getJSONArray("data");
                            for(int i = 0;i <data.length();i++){
                                InformationModel model = new InformationModel();
                                JSONObject item = new JSONObject(data.get(i).toString());
                                model.content = item.getString("content");
                                model.infoId = item.getString("infoId");
                                model.resource = item.getString("infoSource");
                                model.img_url = item.getString("photo");
                                model.reading_count = item.getString("click");
                                list.add(model);
                            }

                            Message m = new Message();
                            m.what = Contants.INFOSearch_GET_SUCCESS;
                            m.obj = list;
                            handler.sendMessage(m);
                        }

                        if ("500".equals(object.getString("code"))) {
                            handler.sendEmptyMessage(Contants.INFOSearch_GET_NOTHING);
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

    /**
     * 将搜索的关键字历史记录保存在本地
     * @param str
     */
    private void saveKeyWord(String str) {
        boolean isExict = false;
        value_list = UserInfoUtils.getDataList(this,Contants.INFOSearch_KEYWORD);
        for(String s:value_list){
            if(s.equals(str)){
                isExict = true;
            }
        }
        if(!isExict){
            value_list.add(str);
        }
        UserInfoUtils.setDataList(this,Contants.INFOSearch_KEYWORD,value_list);
    }


    private void initFlowLayout() {
        value_list = UserInfoUtils.getDataList(this,Contants.INFOSearch_KEYWORD);
        mVals = value_list.toArray(new String[value_list.size()]);
        final LayoutInflater mInflater = LayoutInflater.from(this);
        idFlowlayout.setAdapter(mAdapter = new TagAdapter<String>(mVals) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.layout_flowlayout_tv,
                        idFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });
        /**
         * 点击标签事件
         */
        idFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(InformationSearchActivity.this, mVals[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        idFlowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                setTitle("choose:" + selectPosSet.toString());
            }
        });
    }


}
