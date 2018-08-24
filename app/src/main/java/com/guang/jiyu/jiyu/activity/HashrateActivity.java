package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/6/22.
 */

public class HashrateActivity extends BaseActivity {


    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.id_calculation_force_Inviting_friends_Invitation)
    Button idCalculationForceInvitingFriendsInvitation;
    @BindView(R.id.id_calculation_force_authentication)
    Button idCalculationForceAuthentication;
    @BindView(R.id.id_calculation_force_study)
    Button idCalculationForceStudy;
    @BindView(R.id.id_calculation_force_Sign_in)
    Button idCalculationForceSignIn;
    @BindView(R.id.id_calculation_force_Read)
    Button idCalculationForceRead;
    @BindView(R.id.iv_hashrate_explain)
    ImageView ivHashrateExplain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_hashrate);

        initTitle();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "算力", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.id_calculation_force_Inviting_friends_Invitation, R.id.id_calculation_force_authentication, R.id.id_calculation_force_study, R.id.id_calculation_force_Sign_in, R.id.id_calculation_force_Read,R.id.iv_hashrate_explain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_hashrate_explain:
                ActivityUtils.startActivity(this, HashrateRecordActivity.class);
                break;
            case R.id.id_calculation_force_Inviting_friends_Invitation:
                ActivityUtils.startActivity(this, InviteFriendActivity.class);
                break;
            case R.id.id_calculation_force_authentication:
                ActivityUtils.startActivity(this, IdentityAuthenticateActivity.class);
                break;
            case R.id.id_calculation_force_study:
                ToastUtils.showToast("学习！");
                break;
            case R.id.id_calculation_force_Sign_in:
                ToastUtils.showToast("签到成功！");
                break;
            case R.id.id_calculation_force_Read:
                EventBus.getDefault().post(new MessageEvent(Contants.EVENT_INFORMATION));
                finish();
                break;
        }
    }

}
