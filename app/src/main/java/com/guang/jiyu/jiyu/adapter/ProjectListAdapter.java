package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.ProjectDetailModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 2018/7/12.
 */

public class ProjectListAdapter extends BaseAdapter{
    private List<ProjectDetailModel> list;
    private Context context;
    private LayoutInflater inflater;

    public ProjectListAdapter(Context context) {
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

    public void setList(List<ProjectDetailModel> list) {
        this.list = list;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if(view == null){
            holder = new Holder();
            view = inflater.inflate(R.layout.item_project_list,null);
            holder.rl_bg = view.findViewById(R.id.rl_bg);
            holder.iv_icon = view.findViewById(R.id.iv_icon);
            holder.tv_intro = view.findViewById(R.id.tv_content);
            holder.tv_title = view.findViewById(R.id.tv_title);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        //Picasso.get().load(list.get(i).getIconUrl()).into(holder.iv_icon);
        if(i % 2 == 0){
            holder.rl_bg.setBackgroundResource(R.mipmap.icon_project_bg1);
        }else{
            holder.rl_bg.setBackgroundResource(R.mipmap.icon_project_bg2);
        }
        holder.tv_title.setText(list.get(i).getTitle());
        holder.tv_intro.setText(list.get(i).getProjectIntro());
        Picasso.get().load(list.get(i).getIconUrl()).into(holder.iv_icon);
        return view;
    }

    static class Holder{
        RelativeLayout rl_bg;
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_intro;
    }
}
