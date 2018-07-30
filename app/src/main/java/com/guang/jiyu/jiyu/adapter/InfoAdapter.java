package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.InformationModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 2018/7/11.
 */

public class InfoAdapter extends BaseAdapter{
    private List<InformationModel> list;
    private Context context;
    private LayoutInflater inflater;

    public InfoAdapter(List<InformationModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Log.d("fastinfor-----",list.get(position).toString());
        if(convertView == null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_fast_information,null);
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            holder.tv_url = convertView.findViewById(R.id.tv_url);
            holder.tv_reading_count = convertView.findViewById(R.id.tv_reading_count);
            holder.iv_img = convertView.findViewById(R.id.iv_content_img);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        holder.tv_content.setText(list.get(position).getTitle());
        holder.tv_url.setText(list.get(position).getResource());
        holder.tv_reading_count.setText(list.get(position).getReading_count());
        Picasso.get().load(list.get(position).getImg_url()).into(holder.iv_img);

        return convertView;
    }

    public void setList(List<InformationModel> list) {
        this.list = list;
    }

    static class Holder{
        TextView tv_content;
        TextView tv_url;
        TextView tv_reading_count;
        ImageView iv_img;
    }
}
