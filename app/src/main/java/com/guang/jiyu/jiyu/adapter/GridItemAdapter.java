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

    private int[] check_icon = new int[]{R.mipmap.b3_01, R.mipmap.b3_02, R.mipmap.b3_03, R.mipmap.b3_04,
            R.mipmap.b3_05, R.mipmap.b3_06, R.mipmap.b3_07, R.mipmap.b3_08};

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
        if(list.size() != 0){
            ProretryTypeModel model =  list.get(position);
            viewHolder.iv_icon.setImageResource(check_icon[Integer.parseInt(model.getIcon())]);
            viewHolder.tv_count.setText(model.getAverage());
            viewHolder.tv_type.setText(model.getCurrency());
        }

        return convertView;
    }

    public void setList(List<ProretryTypeModel> list) {
        this.list = list;
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_count;
        TextView tv_type;
    }
}
