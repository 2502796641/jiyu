package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.HashrateRecordModel;
import com.guang.jiyu.jiyu.model.ProretryTypeModel;

import java.util.List;

/**
 * Created by admin on 2018/8/31.
 */

public class HashrateRecordAdapter extends BaseAdapter{

    private List<HashrateRecordModel> list;
    private Context context;
    private LayoutInflater inflater;

    public HashrateRecordAdapter(List<HashrateRecordModel> list, Context context) {
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
            view = inflater.inflate(R.layout.item_hashrate_record,null);
            holder.tv_content = view.findViewById(R.id.tv_content);
            holder.tv_updateTime = view.findViewById(R.id.tv_time);
            holder.tv_state = view.findViewById(R.id.tv_state);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.tv_content.setText(list.get(i).getFdcType());
        holder.tv_updateTime.setText(list.get(i).getCreatedAt());
        int state = list.get(i).getFdnQty();

        if(state > 0){
            holder.tv_state.setText("+" + state);
            holder.tv_state.setTextColor(context.getResources().getColor(R.color.red));
        }else{
            holder.tv_state.setText("" + state);
            holder.tv_state.setTextColor(context.getResources().getColor(R.color.unreadTextColor));
        }

        return view;
    }

    public void setList(List<HashrateRecordModel> list) {
        this.list = list;
    }


    static class Holder{
        TextView tv_content;
        TextView tv_updateTime;
        TextView tv_state;
    }
}
