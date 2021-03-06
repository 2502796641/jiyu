package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guang.jiyu.R;

import java.util.List;

/**
 * Created by admin on 2018/7/11.
 */

public class ListAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;

    public ListAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
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
        ViewHolder viewHolder ;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) view.findViewById(R.id.item_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv.setText(list.get(i));
        return view;
    }

    class ViewHolder {
        private TextView tv;
    }
}
