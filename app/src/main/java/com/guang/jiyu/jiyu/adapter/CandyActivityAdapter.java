package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.ProjectDetailModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 2018/7/16.
 */

public class CandyActivityAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    public static final int TYPE_OLDER = 0;
    public static final int TYPE_CURRENT = 1;

    private List<ProjectDetailModel> list;
    private int tag;//历史活动和当前活动的分辨标识
    private Context context;
    public CandyActivityAdapter(Context context,List<ProjectDetailModel> list,int tag) {
        this.list = list;
        this.tag = tag;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(viewType == TYPE_CURRENT){
            view = View.inflate(parent.getContext(), R.layout.item_candy_current_activity,null);
            return new CurrentHolder(view);
        }else if(viewType == TYPE_OLDER){
            view = View.inflate(parent.getContext(), R.layout.item_project_list,null);
            return new OlderHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CurrentHolder){
            if(position == (list.size() - tag - 1)){
                ((CurrentHolder) holder).ll_part.setVisibility(View.VISIBLE);
            }else{
                ((CurrentHolder) holder).ll_part.setVisibility(View.GONE);
            }

            if(position % 2 == 0){
                ((CurrentHolder) holder).rl_main.setBackground(context.getResources().getDrawable(R.mipmap.icon_project_bg1) );
            }else{
                ((CurrentHolder) holder).rl_main.setBackground(context.getResources().getDrawable(R.mipmap.icon_project_bg2) );
            }

            ((CurrentHolder) holder).tv_title.setText(list.get(position).title);
            ((CurrentHolder) holder).tv_content.setText(list.get(position).projectIntro);
            Picasso.get().load(list.get(position).getIconUrl()).into(((CurrentHolder) holder).iv_icon);
            ((CurrentHolder) holder).rl_main.setTag(position);
            ((CurrentHolder) holder).btn_get.setTag(position);
        }

        if(holder instanceof OlderHolder){
            if(position % 2 == 0){
                ((OlderHolder) holder).rl_main.setBackground(context.getResources().getDrawable(R.mipmap.icon_project_bg1) );
            }else{
                ((OlderHolder) holder).rl_main.setBackground(context.getResources().getDrawable(R.mipmap.icon_project_bg2) );
            }

            ((OlderHolder) holder).tv_title.setText(list.get(position).title);
            ((OlderHolder) holder).tv_content.setText(list.get(position).projectIntro);
            Picasso.get().load(list.get(position).getIconUrl()).into(((OlderHolder) holder).iv_icon);
            ((OlderHolder) holder).rl_main.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        if(list.size() != 0){
            return list.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(position < list.size() - tag){
            return TYPE_CURRENT;
        }else{
            return TYPE_OLDER;
        }
    }

    /** item里面有多个控件可以点击 */
    public enum ViewName {
        RL_MAIN,
        BTN_GET,
    }

    public interface OnRecyclerViewItemClickListener {
        void onClick(View view, ViewName viewName, int position);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        int tag = (int) view.getTag();
        if (mOnItemClickListener != null) {
            switch (view.getId()){
                case R.id.rl_bg:
                    mOnItemClickListener.onClick(view, ViewName.RL_MAIN, tag);
                    break;
                case R.id.btn_get:
                    mOnItemClickListener.onClick(view, ViewName.BTN_GET, tag);
                    break;
            }
        }
    }

    private class CurrentHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_main;
        LinearLayout ll_part;
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_content;
        SuperButton btn_get;
        public CurrentHolder(View itemView) {
            super(itemView);
            rl_main = itemView.findViewById(R.id.rl_bg);
            ll_part = itemView.findViewById(R.id.ll_part);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_title = itemView.findViewById(R.id.tv_title);
            btn_get = itemView.findViewById(R.id.btn_get);
            initListener();
        }

        private void initListener(){
            rl_main.setOnClickListener(CandyActivityAdapter.this);
            btn_get.setOnClickListener(CandyActivityAdapter.this);
        }
    }

    private class OlderHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_main;
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_content;
        public OlderHolder(View itemView) {
            super(itemView);
            rl_main = itemView.findViewById(R.id.rl_bg);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_title = itemView.findViewById(R.id.tv_title);
            initListener();
        }

        private void initListener(){
            rl_main.setOnClickListener(CandyActivityAdapter.this);

        }
    }

}
