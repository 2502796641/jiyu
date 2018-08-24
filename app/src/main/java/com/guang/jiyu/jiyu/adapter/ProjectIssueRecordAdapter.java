package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.ProjectIssueRecordModel;
import com.guang.jiyu.jiyu.model.PropetryRecordModel;

import java.util.List;

/**
 * Created by admin on 2018/8/24.
 */

public class ProjectIssueRecordAdapter extends BaseAdapter{

    private List<ProjectIssueRecordModel> list;
    private Context context;
    private LayoutInflater inflater;

    public ProjectIssueRecordAdapter(Context context,List<ProjectIssueRecordModel> list) {
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
        Holder holder;
        if(view == null){
            holder = new Holder();
            view = inflater.inflate(R.layout.item_project_issue_record,null);
            holder.tv_count = view.findViewById(R.id.tv_count);
            holder.tv_updateTime = view.findViewById(R.id.tv_updatetime);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        holder.tv_count.setText(list.get(i).getIssue_count());
        holder.tv_updateTime.setText(list.get(i).getUpdate_time());

        return view;
    }

    //添加数据
    public void addItem(ProjectIssueRecordModel model){
        list.add(model);
    }

    static class Holder{
        TextView tv_count;
        TextView tv_updateTime;
    }
}
