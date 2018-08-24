package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.guang.jiyu.R;

/**
 * Created by admin on 2018/8/23.
 */

public class IconAdapter extends BaseAdapter{
    private int[] arr;
    private Context context;

    public IconAdapter(int[] arr, Context context) {
        this.arr = arr;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arr.length;
    }

    @Override
    public Object getItem(int i) {
        return arr[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view == null){
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_gv_icon, null);
            holder.iv_icon = view.findViewById(R.id.img_portrait);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        holder.iv_icon.setImageResource(arr[i]);
        return view;
    }

    static class Holder{
        ImageView iv_icon;
    }
}
