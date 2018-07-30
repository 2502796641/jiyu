package com.guang.jiyu.jiyu.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.activity.AboutUsActivity;
import com.guang.jiyu.jiyu.activity.GettingStartedActivity;
import com.guang.jiyu.jiyu.activity.InviteFriendActivity;
import com.guang.jiyu.jiyu.activity.LoginActivity;
import com.guang.jiyu.jiyu.activity.MessageRecordActivity;
import com.guang.jiyu.jiyu.activity.MyCollectionActivity;
import com.guang.jiyu.jiyu.activity.MyPropetryActivity;
import com.guang.jiyu.jiyu.activity.SettingActivity;
import com.guang.jiyu.jiyu.event.GetCandySuccessEvent;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.event.ShareInformationEvent;
import com.guang.jiyu.jiyu.event.UserInfoEvent;
import com.guang.jiyu.jiyu.model.AirCandyModel;
import com.guang.jiyu.jiyu.model.FastInformationModel;
import com.guang.jiyu.jiyu.model.UserInfoModel;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 2018/6/19.
 */

public class UserFragment extends BaseFragment {
    @BindView(R.id.title_user)
    TitleBar titleUser;
    Unbinder unbinder;
    @BindView(R.id.iv_user_icon)
    CircleImageView ivUserIcon;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_userId)
    TextView tvUserId;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.rl_my_property)
    RelativeLayout rlMyProperty;
    @BindView(R.id.rl_my_collect)
    RelativeLayout rlMyCollect;
    @BindView(R.id.rl_newhand)
    RelativeLayout rlNewhand;
    @BindView(R.id.rl_invite_friend)
    RelativeLayout rlInviteFriend;
    @BindView(R.id.rl_about_us)
    RelativeLayout rlAboutUs;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    @OnClick({R.id.iv_setting, R.id.rl_my_property, R.id.rl_my_collect, R.id.rl_newhand, R.id.rl_invite_friend, R.id.rl_about_us,R.id.tv_login,R.id.tv_username})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                checkUserState(SettingActivity.class);
                break;
            case R.id.rl_my_property:
                checkUserState(MyPropetryActivity.class);
                break;
            case R.id.rl_my_collect:
                checkUserState(MyCollectionActivity.class);
                break;
            case R.id.rl_newhand:
                ActivityUtils.startActivity(getContext(), GettingStartedActivity.class);
                break;
            case R.id.rl_invite_friend:
                checkUserState(InviteFriendActivity.class);
                break;
            case R.id.rl_about_us:
                ActivityUtils.startActivity(getContext(), AboutUsActivity.class);
                break;
            case R.id.tv_login:
                ActivityUtils.startActivity(getContext(), LoginActivity.class);
                break;
            case R.id.tv_username:
                if(UserInfoUtils.isUserLogin(getContext())){
                    checkUserState(SettingActivity.class);
                }else{
                    ActivityUtils.startActivity(getContext(), LoginActivity.class);
                }

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initUserData();
    }

    /**
     * 判断用户是否登录
     * @param objectClass
     */
    private void checkUserState(Class objectClass) {
        if(!UserInfoUtils.isUserLogin(getContext())){
            ToastUtils.showToast("请先登录");
            ActivityUtils.startActivity(getContext(), LoginActivity.class);
            return;
        }else{
            ActivityUtils.startActivity(getContext(), objectClass);
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_user;
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initTitle();
        initUserData();
        return rootView;
    }

    private void initUserData() {
        if(UserInfoUtils.isUserLogin(getContext())){
            tvUsername.setText(UserInfoUtils.getString(getContext(),Contants.USER_NAME));
            tvUserId.setText("ID:" + UserInfoUtils.getInt(getContext(),Contants.USER_ID));
        }else{
            tvUsername.setText("请登录");
            tvUserId.setText("");
        }
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarNoImg((Activity) getContext(), titleUser, "个人中心");
        titleUser.addAction(new TitleBar.ImageAction(R.mipmap.icon_msg_remind) {
            @Override
            public void performAction(View view) {
                if(!UserInfoUtils.isUserLogin(getContext())){
                    ToastUtils.showToast("请先登录");
                    ActivityUtils.startActivity(getContext(),LoginActivity.class);
                    return;
                }
                ActivityUtils.startActivity(getContext(), MessageRecordActivity.class);
            }
        });

    }

    @Subscribe
    public void onEventMainThread(BaseEvent baseEvent) {
        if(baseEvent instanceof UserInfoEvent){
            UserInfoEvent event = (UserInfoEvent) baseEvent;
            UserInfoModel model = event.getModel();
            tvUsername.setText(model.getUserName());
            tvUserId.setText("ID:" + model.getUserId());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
