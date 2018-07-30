package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.AirCandyModel;

import java.util.List;

/**
 * Created by admin on 2018/7/18.
 */

public class AirCandyLoadAdapter extends BaseAdapter {

    private List<AirCandyModel> list;
    private LayoutInflater flater;
    private Context context;

    public AirCandyLoadAdapter(List<AirCandyModel> list, Context context) {
        this.list = list;
        this.context = context;
        flater = LayoutInflater.from(context);
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        CandyRecoedHolder candyRecoedHolder;
        if (convertView == null) {
            convertView = flater.inflate(R.layout.item_get_candy_record, null);
            candyRecoedHolder = new CandyRecoedHolder();
            candyRecoedHolder.tv_count = convertView.findViewById(R.id.tv_count);
            candyRecoedHolder.tv_currency = convertView.findViewById(R.id.tv_currency);
            candyRecoedHolder.tv_price = convertView.findViewById(R.id.tv_price);
            candyRecoedHolder.tv_time = convertView.findViewById(R.id.tv_time);
            convertView.setTag(candyRecoedHolder);
        } else {
            candyRecoedHolder = (CandyRecoedHolder) convertView.getTag();
        }

        candyRecoedHolder.tv_currency.setText(list.get(i).getCurrency());
        candyRecoedHolder.tv_count.setText(list.get(i).getAmount());
        candyRecoedHolder.tv_price.setText(list.get(i).getMoney());
        candyRecoedHolder.tv_time.setText(list.get(i).getUpdateTime());
        return convertView;
    }

    static class CandyRecoedHolder {
        private TextView tv_currency;
        private TextView tv_count;
        private TextView tv_price;
        private TextView tv_time;
    }
}



