package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.ProretryTypeModel;

import java.util.List;

/**
 * Created by admin on 2018/8/21.
 */

public class GridItemAdapter extends BaseAdapter {

    private List<ProretryTypeModel> list;    //定义数据类
    private LayoutInflater mInflater;       //定义Inflater
    private Context context;
    public GridItemAdapter(Context context, List<ProretryTypeModel> list){
        this.context = context;
        this.list = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        if(convertView==null){
            convertView = mInflater.inflate(R.layout.grid_item,null);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置holder
        ProretryTypeModel model =  list.get(position);
        viewHolder.iv_icon.setImageResource(R.mipmap.icon_fish3);
        viewHolder.tv_count.setText(model.getCount());
        viewHolder.tv_type.setText(model.getType());
        return convertView;
    }


    private class ViewHolder{
        ImageView iv_icon;
        TextView tv_count;
        TextView tv_type;
    }
}
