package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.PropetryRecordModel;
import com.guang.jiyu.jiyu.model.ProretryTypeModel;

import java.util.List;

/**
 * Created by admin on 2018/8/23.
 */

public class MyPropetryRecordAdapter extends BaseAdapter{
    private List<PropetryRecordModel> list;
    private Context context;
    private LayoutInflater inflater;

    public MyPropetryRecordAdapter(Context context,List<PropetryRecordModel> list) {
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
            view = inflater.inflate(R.layout.item_proretry_record,null);
            holder.tv_state = view.findViewById(R.id.tv_state);
            holder.tv_updateTime = view.findViewById(R.id.tv_updatetime);
            holder.tv_balance = view.findViewById(R.id.tv_balance);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.tv_state.setText(list.get(i).getState());
        holder.tv_updateTime.setText(list.get(i).getUpdateTime());
        holder.tv_balance.setText(list.get(i).getBalance());
        return view;
    }

    //添加数据
    public void addItem(PropetryRecordModel model){
        list.add(model);
    }

    static class Holder{
        TextView tv_state;
        TextView tv_updateTime;
        TextView tv_balance;
    }
}
