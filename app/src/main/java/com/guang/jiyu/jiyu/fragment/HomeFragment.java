package com.guang.jiyu.jiyu.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.jiyu.activity.HashrateActivity;
import com.guang.jiyu.jiyu.activity.InviteFriendActivity;
import com.guang.jiyu.jiyu.activity.MiningCheatsActivity;
import com.guang.jiyu.jiyu.activity.ProjectIntroActivity;
import com.guang.jiyu.jiyu.activity.RankListActivity;
import com.guang.jiyu.jiyu.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import q.rorbin.badgeview.QBadgeView;

/**
 * 首页fragment
 * Created by admin on 2018/6/19.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.btn_hashrate)
    SuperButton btnHashrate;


    Unbinder unbinder;
    @BindView(R.id.tv_currency)
    TextView tvCurrency;
    @BindView(R.id.tv_value)
    TextView tvValue;
    @BindView(R.id.ll_invite_friend)
    LinearLayout llInviteFriend;
    @BindView(R.id.ll_get_hashrate)
    LinearLayout llGetHashrate;
    @BindView(R.id.ll_mining_cheats)
    LinearLayout llMiningCheats;
    @BindView(R.id.ll_ranking_list)
    LinearLayout llRankingList;


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
        new QBadgeView(getContext()).bindTarget(llRankingList).setBadgeNumber(7);
        new QBadgeView(getContext()).bindTarget(btnHashrate).setBadgeNumber(7);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_hashrate, R.id.ll_invite_friend, R.id.ll_get_hashrate, R.id.ll_mining_cheats, R.id.ll_ranking_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_hashrate:
                ActivityUtils.startActivity(getContext(), HashrateActivity.class);
                //ActivityUtils.startActivity(getContext(), LoadMoreStateActivity.class);
                break;
            case R.id.ll_invite_friend:
                ActivityUtils.startActivity(getContext(), InviteFriendActivity.class);
                break;
            case R.id.ll_get_hashrate:
                ActivityUtils.startActivity(getContext(), HashrateActivity.class);
                break;
            case R.id.ll_mining_cheats:
                ActivityUtils.startActivity(getContext(), MiningCheatsActivity.class);
                break;
            case R.id.ll_ranking_list:
                ActivityUtils.startActivity(getContext(), RankListActivity.class);
                break;
        }
    }
}
