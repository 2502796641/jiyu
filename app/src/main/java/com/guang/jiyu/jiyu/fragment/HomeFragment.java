package com.guang.jiyu.jiyu.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.activity.HashrateActivity;
import com.guang.jiyu.jiyu.activity.InviteFriendActivity;
import com.guang.jiyu.jiyu.activity.MyPropetryActivity;
import com.guang.jiyu.jiyu.activity.ReadBroadCastActivity;
import com.guang.jiyu.jiyu.adapter.GridItemAdapter;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.model.ProretryTypeModel;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.widget.AlertDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    private List<ProretryTypeModel> list;
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
            initList();
        return rootView;
    }

    private void initList() {
        list = new ArrayList<>();
        for(int i = 0;i < 9;i++){
            ProretryTypeModel model = new ProretryTypeModel();
            model.count = "9.99";
            model.type = "CBCB";
            list.add(model);
        }
        gridItemAdapter = new GridItemAdapter(getContext(),list);
        grid.setAdapter(gridItemAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                EventBus.getDefault().post(new MessageEvent(Contants.EVENT_CHANGE_BLIGHTNESS));
                String msg = "恭喜你获得" + list.get(i).count + list.get(i).type + ",需阅读广播方能领取，是否前去阅读？";
                AlertDialog dialog = new AlertDialog(getActivity(), "去阅读", "放弃", msg , true);
                dialog.setSampleDialogListener(new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        ActivityUtils.startActivityWithModel(getContext(), ReadBroadCastActivity.class,list.get(i));
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
                ActivityUtils.startActivity(getContext(), HashrateActivity.class);
                break;
            case R.id.ll_wallet:
                ActivityUtils.startActivity(getContext(), MyPropetryActivity.class);
                break;
            case R.id.ll_invite_friend:
                ActivityUtils.startActivity(getContext(), InviteFriendActivity.class);
                break;
        }
    }
}
