package com.guang.jiyu.jiyu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.jiyu.adapter.BlackchainCatalogAdapter;
import com.guang.jiyu.jiyu.model.BlackchainCatalogModel;
import com.guang.jiyu.jiyu.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 区块链简介目录
 * Created by admin on 2018/8/23.
 */

public class BlackchainCatalogFragment extends BaseFragment {
    @BindView(R.id.lv)
    ListView lv;
    Unbinder unbinder;
    private BlackchainCatalogAdapter adapter;
    private List<BlackchainCatalogModel> list;
    @Override
    public int getLayoutID() {
        return R.layout.fragment_blackchain_catalog;
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
        for(int i = 0;i < 6;i++){
            BlackchainCatalogModel model = new BlackchainCatalogModel();
            model.title = i + "、阿莎断货对哈素海";
            list.add(model);
        }
        adapter = new BlackchainCatalogAdapter(getContext(),list);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToastUtils.showToast("" + i);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
