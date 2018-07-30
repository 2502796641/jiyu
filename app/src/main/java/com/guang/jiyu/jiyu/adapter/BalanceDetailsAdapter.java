package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.BalanceDetailsModel;
import com.guang.jiyu.jiyu.model.InformationModel;

import java.util.List;

/**
 * Created by admin on 2018/7/24.
 */

public class BalanceDetailsAdapter extends BaseAdapter{
    private List<BalanceDetailsModel> list;

    private Context context;
    private LayoutInflater inflater;

    public BalanceDetailsAdapter(List<BalanceDetailsModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
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
            view = inflater.inflate(R.layout.item_balance_details,null);
            holder.tv_price = view.findViewById(R.id.tv_price);
            holder.tv_state = view.findViewById(R.id.tv_state);
            holder.tv_updateTime = view.findViewById(R.id.tv_time);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
/*        holder.tv_state.setText(list.get(i).getState());
        holder.tv_updateTime.setText(list.get(i).getUpdateTimes());
        holder.tv_price.setText(list.get(i).getPrice());*/
        return view;
    }

    static class Holder{
        TextView tv_state;
        TextView tv_updateTime;
        TextView tv_price;
    }
}
