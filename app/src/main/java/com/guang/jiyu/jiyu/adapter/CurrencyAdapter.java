package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.CurrencyModel;
import com.guang.jiyu.jiyu.model.ProretryTypeModel;

import java.util.List;

/**
 * Created by admin on 2018/9/5.
 */

public class CurrencyAdapter extends BaseAdapter{
    private List<CurrencyModel>list;
    private Context context;
    private LayoutInflater inflater;
    private int selectorPosition = -1;

    public CurrencyAdapter(Context context,List<CurrencyModel> list) {
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
        view = inflater.inflate(R.layout.item_currency,null);

        TextView tv = view.findViewById(R.id.tv_currency);
        tv.setText(list.get(i).currency);
        if (selectorPosition == i){
            tv.setBackgroundResource(R.drawable.shape_currency_select);
            tv.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            tv.setBackgroundResource(R.drawable.shape_currency_normal);
            tv.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }
        return view;
    }

    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();
    }
}
