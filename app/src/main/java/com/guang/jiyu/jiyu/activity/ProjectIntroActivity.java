package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.model.ProjectDetailModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 项目简介
 * Created by admin on 2018/7/12.
 */

public class ProjectIntroActivity extends BaseActivity {

    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_project_intro_content)
    TextView tvProjectIntroContent;
    @BindView(R.id.tv_issue_date)
    TextView tvIssueDate;
    @BindView(R.id.tv_offical_website)
    TextView tvOfficalWebsite;
    @BindView(R.id.tv_white_paper)
    TextView tvWhitePaper;
    @BindView(R.id.tv_issue_count)
    TextView tvIssueCount;
    @BindView(R.id.tv_circulate_count)
    TextView tvCirculateCount;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private ProjectDetailModel projectDetailModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_project_intro);
        projectDetailModel = (ProjectDetailModel) getIntent().getSerializableExtra("model");
        initProjectIntro();
    }

    private void initProjectIntro() {

        JSONObject object = new JSONObject();
        try {
            object.put("itemId", projectDetailModel.getItemId() + "");
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    //.addHeader("Content-Type",)
                    .url(LinkParams.REQUEST_URL + "/api/project/getProject")
                    .post(requestBody)
                    .build();

            OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtils.d("login-----", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    LogUtils.d("result-----", result);
                    try {
                        JSONObject object = new JSONObject(result);
                        if ("200".equals(object.getString("code"))) {
                            final JSONObject data = object.getJSONObject("data");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Picasso.get().load(data.getString("photo")).into(ivLogo);
                                        tvTitle.setText(data.getString("itemName"));
                                        tvProjectIntroContent.setText(data.getString("summary"));
                                        tvIssueDate.setText(data.getString("releaseTime"));
                                        tvOfficalWebsite.setText(data.getString("station"));
                                        tvWhitePaper.setText(data.getString("whitebook"));
                                        tvIssueCount.setText(data.getString("liquidity"));
                                        tvCirculateCount.setText(data.getString("circulation"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
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

    @OnClick(R.id.iv_return)
    public void onViewClicked() {
        finish();
    }
}
