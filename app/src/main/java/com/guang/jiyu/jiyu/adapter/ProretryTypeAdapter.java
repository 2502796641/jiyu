package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.ProretryTypeModel;

import java.util.List;

/**
 * Created by admin on 2018/8/20.
 */

public class ProretryTypeAdapter extends BaseAdapter{
    private List<ProretryTypeModel> list;
    private Context context;
    private LayoutInflater inflater;

    public ProretryTypeAdapter(Context context,List<ProretryTypeModel> list) {
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
            view = inflater.inflate(R.layout.item_proretry_type_list,null);
            holder.rl_details = view.findViewById(R.id.rl_proretry_detials);
            holder.tv_type = view.findViewById(R.id.tv_proretry_type);
            holder.tv_count = view.findViewById(R.id.tv_proretry_count);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.tv_count.setText(list.get(i).getCount());
        holder.tv_type.setText(list.get(i).getType());
        return view;
    }

    //添加数据
    public void addItem(ProretryTypeModel model){
        list.add(model);
    }

    static class Holder{
        RelativeLayout rl_details;
        TextView tv_type;
        TextView tv_count;
    }
}
