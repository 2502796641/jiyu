package com.guang.jiyu.jiyu.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.jiyu.activity.HashrateActivity;
import com.guang.jiyu.jiyu.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by admin on 2018/6/25.
 */

public class HowToGetHashrateFragment extends BaseFragment {
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.btn_get)
    SuperButton btnGet;
    Unbinder unbinder;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_how_to_get_hashrate;
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        unbinder = ButterKnife.bind(this, rootView);
        changeTxtColor();
        return rootView;
    }

    /**
     * 改变文本某个字段颜色
     */
    private void changeTxtColor() {
        SpannableStringBuilder ssb = new SpannableStringBuilder(tv1.getText().toString());
        ssb.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.yellow_2)), 6, 7,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv1.setText(ssb);
        tv2.setText(ssb);
        tv3.setText(ssb);
        tv4.setText(ssb);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_get)
    public void onViewClicked() {
        //ActivityUtils.startActivity(getContext(), HashrateActivity.class);
    }
}
