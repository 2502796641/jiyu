package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置activity
 * Created by admin on 2018/6/20.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.rl_nickname)
    RelativeLayout rlNickname;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.rl_mobile)
    RelativeLayout rlMobile;
    @BindView(R.id.rl_change_pwd)
    RelativeLayout rlChangePwd;
    @BindView(R.id.rl_id_identification)
    RelativeLayout rlIdIdentification;
    @BindView(R.id.rl_mark_invite_code)
    RelativeLayout rlMarkInviteCode;
    @BindView(R.id.btn_exit_account)
    Button btnExitAccount;

    @OnClick({R.id.rl_nickname,R.id.rl_mobile,R.id.rl_change_pwd,R.id.rl_id_identification,R.id.rl_mark_invite_code,R.id.btn_exit_account})
    public void click(View view){
        switch (view.getId()){
            case R.id.rl_nickname:
                if(UserInfoUtils.isUserLogin(this)){
                    ActivityUtils.startActivity(this,ChangeNicknameActivity.class);
                }else{
                    ToastUtils.showToast("请先登录");
                    ActivityUtils.startActivity(this,LoginActivity.class);
                }
                break;
                case R.id.rl_mobile:
                    if(UserInfoUtils.isUserLogin(this)){
                        ActivityUtils.startActivity(this,BindNewMobileActivity.class);
                    }else{
                        ToastUtils.showToast("请先登录");
                        ActivityUtils.startActivity(this,LoginActivity.class);
                    }

                break;
                case R.id.rl_change_pwd:
                    if(UserInfoUtils.isUserLogin(this)){
                        ActivityUtils.startActivity(this,ChangePwdActivity.class);
                    }else{
                        ToastUtils.showToast("请先登录");
                        ActivityUtils.startActivity(this,LoginActivity.class);
                    }

                break;
                case R.id.rl_id_identification:
                    if(UserInfoUtils.isUserLogin(this)){
                        ActivityUtils.startActivity(this,IdentityAuthenticateActivity.class);
                    }else{
                        ToastUtils.showToast("请先登录");
                        ActivityUtils.startActivity(this,LoginActivity.class);
                    }

                break;
                case R.id.rl_mark_invite_code:
                    if(UserInfoUtils.isUserLogin(this)){
                        ActivityUtils.startActivity(this,InvitationCodeActivity.class);
                    }else{
                        ToastUtils.showToast("请先登录");
                        ActivityUtils.startActivity(this,LoginActivity.class);
                    }

                break;
                case R.id.btn_exit_account:
                    tvMobile.setText("");
                    tvNickname.setText("");
                UserInfoUtils.saveInt(this,Contants.USER_ID,0);
                UserInfoUtils.saveString(this,Contants.MOBILE_NO,"");
                UserInfoUtils.saveString(this,Contants.USER_NAME,"");
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_setting);
        initTitle();
        initUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUserData();
    }

    private void initUserData() {
        tvNickname.setText(UserInfoUtils.getString(this, Contants.USER_NAME));
        tvMobile.setText(UserInfoUtils.getString(this,Contants.MOBILE_NO));
    }


    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "账号设置", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
