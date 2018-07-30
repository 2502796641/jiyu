package com.guang.jiyu.jiyu.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.jiyu.activity.InformationSearchActivity;
import com.guang.jiyu.jiyu.activity.LoginActivity;
import com.guang.jiyu.jiyu.activity.MessageRecordActivity;
import com.guang.jiyu.jiyu.adapter.FragmentAdapter;
import com.guang.jiyu.jiyu.event.NewsFlashRefreshEvent;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.NoScrollViewPager;
import com.guang.jiyu.jiyu.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by admin on 2018/7/19.
 */

public class InformationFragment extends BaseFragment {
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.btn_fast_information)
    SuperButton btnFastInformation;
    @BindView(R.id.btn_information)
    SuperButton btnInformation;
    Unbinder unbinder;
    @BindView(R.id.vp)
    NoScrollViewPager vp;

    private List<BaseFragment> list = new ArrayList<>();
    private FragmentAdapter fragmentAdapter;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment mCurrentFragment;
    private final static String TAG_fastInfo = "fastInfo";
    private final static String TAG_completeInfo = "completeInfo";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_information;
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
        initFragment();
        switchTabState(0);
       // switchFragment(TAG_fastInfo, TAG_completeInfo);
        return rootView;
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithSpecialImg(getActivity(), titlebar, "资讯", R.mipmap.icon_search);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.startActivity(getContext(), InformationSearchActivity.class);
            }
        });
        titlebar.addAction(new TitleBar.ImageAction(R.mipmap.icon_msg_remind) {
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

    private void initFragment() {
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();
        InfoFragment fastInformationFragment = new InfoFragment();
        FastInfoFragment completeInformationFragment = new FastInfoFragment();
        list.add(completeInformationFragment);
        list.add(fastInformationFragment);
        FragmentManager fm= getActivity().getSupportFragmentManager();

        fragmentAdapter =new FragmentAdapter(fm, list); //new myFragmentPagerAdater记得带上两个参数
        vp.setAdapter(fragmentAdapter);
        vp.setCurrentItem(0);
/*        ft.add(R.id.ll_main, completeInformationFragment, TAG_completeInfo)
                .add(R.id.ll_main, fastInformationFragment, TAG_fastInfo)
                .hide(completeInformationFragment)
                .commit();*/
    }

    public void switchFragment(String fromTag, String toTag) {
        Fragment from = fm.findFragmentByTag(fromTag);
        Fragment to = fm.findFragmentByTag(toTag);
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = fm.beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.ll_main, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private void switchTabState(int i) {
        switch (i) {
            case 0:
                setBtnAttr(btnFastInformation, btnInformation);
                break;
            case 1:
                setBtnAttr(btnInformation, btnFastInformation);
                break;
        }
    }

    private void setBtnAttr(SuperButton btn_1, SuperButton btn_2) {
        btn_1.setShapeType(SuperButton.RECTANGLE)
                .setShapeCornersRadius(8)
                .setShapeSolidColor(setColor(R.color.yellow_2))
                .setUseShape();
        btn_1.setTextColor(setColor(R.color.white));
        btn_2.setShapeType(SuperButton.RECTANGLE)
                .setShapeCornersRadius(8)
                .setShapeSolidColor(setColor(R.color.white))
                .setUseShape();
        btn_2.setTextColor(setColor(R.color.colorBlack));
    }


    private int setColor(int resId) {
        return getResources().getColor(resId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_fast_information, R.id.btn_information})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_fast_information:
                switchTabState(0);
                vp.setCurrentItem(0);
                //switchFragment(TAG_fastInfo, TAG_completeInfo);
                break;
            case R.id.btn_information:
                switchTabState(1);
                vp.setCurrentItem(1);
                EventBus.getDefault().post(new NewsFlashRefreshEvent(1));
                //switchFragment(TAG_completeInfo, TAG_fastInfo);
                break;
        }
    }
}
