package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import butterknife.BindView;

/**
 * Created by admin on 2018/8/24.
 */

public class ProjectIssueDetailsActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.id_project_broadcast_start_date)
    TextView idProjectBroadcastStartDate;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_project_issue_details);
        initTitle();
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
