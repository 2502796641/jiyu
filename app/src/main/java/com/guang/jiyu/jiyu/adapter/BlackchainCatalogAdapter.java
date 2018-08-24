package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.BlackchainCatalogModel;
import com.guang.jiyu.jiyu.model.PropetryRecordModel;

import java.util.List;

/**
 * Created by admin on 2018/8/23.
 */

public class BlackchainCatalogAdapter extends BaseAdapter{
    private List<BlackchainCatalogModel> list;
    private Context context;
    private LayoutInflater inflater;

    public BlackchainCatalogAdapter(Context context,List<BlackchainCatalogModel> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view == null){
            holder = new Holder();
            view = inflater.inflate(R.layout.item_blackchain_catalog,null);
            holder.tv_video = view.findViewById(R.id.tv_video);
            holder.tv_title = view.findViewById(R.id.tv_title);

            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        holder.tv_title = view.findViewById(R.id.tv_title);
        return view;
    }

    static class Holder{
        TextView tv_video;
        TextView tv_title;
    }
}
