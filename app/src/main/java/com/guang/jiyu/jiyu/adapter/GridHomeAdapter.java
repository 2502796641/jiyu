package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.ProretryTypeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/8/27.
 */

public class GridHomeAdapter extends BaseAdapter {
    private ImageView layoutImage;
    private TextView layoutText;
    private List<ProretryTypeModel> list;
    private LayoutInflater mInflater;       //定义Inflater
    private int resource;
    private Context context;

    private int[] check_icon = new int[]{R.mipmap.b1_01, R.mipmap.b1_02, R.mipmap.b1_03, R.mipmap.b1_04,
            R.mipmap.b1_05, R.mipmap.b1_06, R.mipmap.b1_07, R.mipmap.b1_08, R.mipmap.b1_08, R.mipmap.b1_08, R.mipmap.b1_08, R.mipmap.b1_08};
    public GridHomeAdapter(@NonNull Context context,List<ProretryTypeModel> list) {
        this.context = context;
        this.list = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ViewHolder viewHolder;
            if(convertView==null){
                viewHolder = new ViewHolder();
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
            if(position % 2 == 1){
                convertView.setVisibility(View.VISIBLE);
            }else{
                convertView.setVisibility(View.GONE);
            }
            /*layoutImage = (ImageView) inflate.findViewById(R.id.layout_image);
            layoutText = (TextView) inflate.findViewById(R.id.layout_text);
           layoutText.setText(list.get(position).getType());
            layoutImage.setImageResource(check_icon[Integer.valueOf(list.get(position).getCount())]);*/
        return convertView;
    }


    @Override
    public boolean isEnabled(int position) {
        if (position % 2 == 0) {
            return false;
        }
        return true;
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_count;
        TextView tv_type;
    }
}
