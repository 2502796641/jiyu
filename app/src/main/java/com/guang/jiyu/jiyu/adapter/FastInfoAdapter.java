package com.guang.jiyu.jiyu.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.jiyu.model.FastInformationModel;

import java.util.List;

/**
 * Created by admin on 2018/7/13.
 */

public class FastInfoAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    public static final int TYPE_DATE = 0;
    public static final int TYPE_NORMAL = 1;

    private List<FastInformationModel> list;
    private List<FastInformationModel> fastinfoList;

    public FastInfoAdapter(List<FastInformationModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
/*        View view = View.inflate(parent.getContext(),R.layout.item_information_normal,null);
        return new NormalHolder(view);*/
        if (viewType == TYPE_DATE) {
            view =View.inflate(parent.getContext(), R.layout.item_information_with_date,null);
            return new WithDateHolder(view);
        } else if (viewType == TYPE_NORMAL) {
            view =View.inflate(parent.getContext(),R.layout.item_information_normal,null);
            return new NormalHolder(view);
        }
        return null;
    }

    public void setList(List<FastInformationModel> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("fastinfo--------",list.get(position).toString());
        if(holder instanceof WithDateHolder){
            ((WithDateHolder) holder).tv_date.setText(list.get(position).mouths + " " + list.get(position).dayForWeek);
            ((WithDateHolder) holder).tv_title.setText(list.get(position).title);
            //((WithDateHolder) holder).web.loadUrl("https://www.chainnews.com/articles/058524523565.htm");
            ((WithDateHolder) holder).tv_content.setText(list.get(position).content);
            ((WithDateHolder) holder).tv_bulish.setText(list.get(position).bulish);
            ((WithDateHolder) holder).tv_bearish.setText(list.get(position).bearish);
            ((WithDateHolder) holder).tv_share.setText(list.get(position).share_times);
            ((WithDateHolder) holder).tv_time.setText(list.get(position).time);
            ((WithDateHolder) holder).ll_main.setTag(position);
            ((WithDateHolder) holder).ll_bearish.setTag(position);
            ((WithDateHolder) holder).ll_bulish.setTag(position);
            ((WithDateHolder) holder).ll_share_time.setTag(position);
            ((WithDateHolder) holder).tv_content.setTag(position);
        }

        if(holder instanceof NormalHolder){
            ((NormalHolder) holder).tv_title.setText(list.get(position).title);
            //((NormalHolder) holder).webView.loadUrl("https://www.chainnews.com/articles/058524523565.htm");
            ((NormalHolder) holder).tv_content.setText(list.get(position).content);
            ((NormalHolder) holder).tv_bulish.setText(list.get(position).bulish);
            ((NormalHolder) holder).tv_bearish.setText(list.get(position).bearish);
            ((NormalHolder) holder).tv_share.setText(list.get(position).share_times);
            ((NormalHolder) holder).tv_time.setText(list.get(position).time);
            ((NormalHolder) holder).ll_main.setTag(position);
            ((NormalHolder) holder).ll_bearish.setTag(position);
            ((NormalHolder) holder).ll_bulish.setTag(position);
            ((NormalHolder) holder).ll_share_time.setTag(position);
            ((NormalHolder) holder).tv_content.setTag(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
            if(list.get(position).tag == 0){
                return TYPE_DATE;
            }else{
                return TYPE_NORMAL;
            }

    }

    @Override
    public int getItemCount() {
        if(list.size() != 0){
            return list.size();
        }
        return 0;
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
                case R.id.ll_main:
                    mOnItemClickListener.onClick(view, ViewName.LL_MAIN, tag);
                    break;
                case R.id.ll_bulish:
                    mOnItemClickListener.onClick(view, ViewName.BULISH, tag);
                    break;
                case R.id.ll_bearish:
                    mOnItemClickListener.onClick(view, ViewName.BEARISH, tag);
                    break;
                case R.id.ll_share_times:
                    mOnItemClickListener.onClick(view, ViewName.SHARE, tag);
                    break;
                case R.id.tv_content:
                    mOnItemClickListener.onClick(view, ViewName.CONTENT, tag);
                    break;
            }
        }
    }


    /** item里面有多个控件可以点击 */
    public enum ViewName {
        LL_MAIN,
        BULISH,
        BEARISH,
        SHARE,
        CONTENT,
    }

    public interface OnRecyclerViewItemClickListener {
        void onClick(View view, ViewName viewName, int position);
    }


    private class WithDateHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_main;
        LinearLayout ll_bulish;
        LinearLayout ll_bearish;
        LinearLayout ll_share_time;
        ImageView iv_good_news;
        TextView tv_date;
        TextView tv_title;
        TextView tv_content;
        TextView tv_bulish;
        TextView tv_bearish;
        TextView tv_share;
        TextView tv_time;
        WebView web;
        public WithDateHolder(View itemView) {
            super(itemView);
            ll_main = itemView.findViewById(R.id.ll_main);
            ll_bulish = itemView.findViewById(R.id.ll_bulish);
            ll_bearish = itemView.findViewById(R.id.ll_bearish);
            ll_share_time = itemView.findViewById(R.id.ll_share_times);
            iv_good_news = itemView.findViewById(R.id.iv_good);
            tv_date = itemView.findViewById(R.id.tv_date);
            //web = itemView.findViewById(R.id.web);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_bulish = itemView.findViewById(R.id.tv_bulish);
            tv_bearish = itemView.findViewById(R.id.tv_bearish);
            tv_share = itemView.findViewById(R.id.tv_share_times);
            tv_time = itemView.findViewById(R.id.tv_time);
            initListener();
        }

        private void initListener(){
            ll_main.setOnClickListener(FastInfoAdapter.this);
            ll_bulish.setOnClickListener(FastInfoAdapter.this);
            ll_bearish.setOnClickListener(FastInfoAdapter.this);
            ll_share_time.setOnClickListener(FastInfoAdapter.this);
            tv_content.setOnClickListener(FastInfoAdapter.this);
        }
    }

    private class NormalHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_main;
        LinearLayout ll_bulish;
        LinearLayout ll_bearish;
        LinearLayout ll_share_time;
        ImageView iv_good_news;
        TextView tv_title;
        TextView tv_content;
        TextView tv_bulish;
        TextView tv_bearish;
        TextView tv_share;
        TextView tv_time;
        WebView webView;
        public NormalHolder(View itemView) {
            super(itemView);
            ll_main = itemView.findViewById(R.id.ll_main);
            ll_bulish = itemView.findViewById(R.id.ll_bulish);
            ll_bearish = itemView.findViewById(R.id.ll_bearish);
            ll_share_time = itemView.findViewById(R.id.ll_share_times);
            iv_good_news = itemView.findViewById(R.id.iv_good);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_bulish = itemView.findViewById(R.id.tv_bulish);
            tv_bearish = itemView.findViewById(R.id.tv_bearish);
            tv_share = itemView.findViewById(R.id.tv_share_times);
            tv_time = itemView.findViewById(R.id.tv_time);
            //webView = itemView.findViewById(R.id.web);
            initListener();
        }

        private void initListener(){
            ll_main.setOnClickListener(FastInfoAdapter.this);
            ll_bulish.setOnClickListener(FastInfoAdapter.this);
            ll_bearish.setOnClickListener(FastInfoAdapter.this);
            ll_share_time.setOnClickListener(FastInfoAdapter.this);
            tv_content.setOnClickListener(FastInfoAdapter.this);
        }
    }
}
