package com.guang.jiyu.jiyu.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guang.jiyu.R;
import com.guang.jiyu.base.Contants;

import java.util.List;

/**
 * Created by admin on 2018/6/25.
 */

public class LoadMoreAdapter extends BaseAdapter {

    private List<Integer> datas;
    private LayoutInflater flater;
    private String tag;
    private Context context;

    //构造函数
    public LoadMoreAdapter(List<Integer> datas,Context context,String tag){
        this.datas=datas;
        this.tag = tag;
        this.context = context;
        flater=LayoutInflater.from(context);
    }
    //得到数据总数
    @Override
    public int getCount() {
        return datas.size();
    }

    //得到每一条数据
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    //得到项目的位置
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(Contants.TAG_COMEPLETE_RANK.equals(tag)){
            RankHolder rankHolder;
            if(convertView==null){
                convertView=flater.inflate(R.layout.item_complete_rank, null);
                rankHolder=new RankHolder();
                rankHolder.tv_num = convertView.findViewById(R.id.tv_num);
                rankHolder.tv_name = convertView.findViewById(R.id.tv_name);
                rankHolder.tv_hashrate = convertView.findViewById(R.id.tv_hashrate);
                convertView.setTag(rankHolder);
            }else{
                rankHolder=(RankHolder)convertView.getTag();
            }
            if(datas.get(position) == 1){
                rankHolder.tv_num.setBackgroundResource(R.mipmap.icon_first);
            }else if(datas.get(position) == 2){
                rankHolder.tv_num.setBackgroundResource(R.mipmap.icon_second);
            }else if(datas.get(position) == 3){
                rankHolder.tv_num.setBackgroundResource(R.mipmap.icon_thrid);
            }
            rankHolder.tv_num.setText("选项"+datas.get(position));
        }

        if(Contants.TAG_LOADMORE_STATE.equals(tag)){
            StateHolder stateHolder;
            if(convertView==null){
                convertView=flater.inflate(R.layout.item_more_state, null);
                stateHolder=new StateHolder();
                stateHolder.tv_name = convertView.findViewById(R.id.tv_name);
                stateHolder.tv_getHashrate = convertView.findViewById(R.id.tv_state);
                stateHolder.tv_time = convertView.findViewById(R.id.tv_time);
                convertView.setTag(stateHolder);
            }else{
                stateHolder=(StateHolder)convertView.getTag();
            }
            int len = stateHolder.tv_getHashrate.getText().toString().length();
            SpannableStringBuilder ssb = new SpannableStringBuilder(stateHolder.tv_getHashrate.getText().toString());
            ssb.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.theme_color)), 3, len,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            stateHolder.tv_getHashrate.setText(ssb);
            stateHolder.tv_name.setText("选项"+datas.get(position));
        }



        if(Contants.TAG_INVITE_REMARK.equals(tag)){
            InviteRemarkHolder inviteRemarkHolder;
            if(convertView == null){
                convertView = flater.inflate(R.layout.item_invite_remark,null);
                inviteRemarkHolder = new InviteRemarkHolder();
                inviteRemarkHolder.tv_account = convertView.findViewById(R.id.tv_account);
                inviteRemarkHolder.tv_hashrate_state = convertView.findViewById(R.id.tv_hashrate_state);
                inviteRemarkHolder.tv_state = convertView.findViewById(R.id.tv_state);
                inviteRemarkHolder.tv_time = convertView.findViewById(R.id.tv_time);
                convertView.setTag(inviteRemarkHolder);
            }else{
                inviteRemarkHolder = (InviteRemarkHolder) convertView.getTag();
            }

            inviteRemarkHolder.tv_time.setText("选项"+datas.get(position));

        }

        if(Contants.TAG_GETTING_CANDY_RECORD.equals(tag)){
            CandyRecoedHolder candyRecoedHolder;
            if(convertView == null){
                convertView = flater.inflate(R.layout.item_get_candy_record,null);
                candyRecoedHolder = new CandyRecoedHolder();
                candyRecoedHolder.tv_count = convertView.findViewById(R.id.tv_count);
                candyRecoedHolder.tv_currency = convertView.findViewById(R.id.tv_currency);
                candyRecoedHolder.tv_price = convertView.findViewById(R.id.tv_price);
                candyRecoedHolder.tv_time = convertView.findViewById(R.id.tv_time);
                convertView.setTag(candyRecoedHolder);
            }else{
                candyRecoedHolder = (CandyRecoedHolder) convertView.getTag();
            }

            candyRecoedHolder.tv_currency.setText("选项"+datas.get(position));
        }
        return convertView;
    }

    static class RankHolder{
        private TextView tv_num;
        private TextView tv_name;
        private TextView tv_hashrate;
    }

    static class StateHolder{
        private TextView tv_name;
        private TextView tv_getHashrate;
        private TextView tv_time;
    }

    static class HashrateHolder{
        private TextView tv_time;
        private TextView tv_score;
    }

    static class InviteRemarkHolder{
        private TextView tv_state;
        private TextView tv_account;
        private TextView tv_hashrate_state;
        private TextView tv_time;
    }

    static class CandyRecoedHolder{
        private TextView tv_currency;
        private TextView tv_count;
        private TextView tv_price;
        private TextView tv_time;
    }


    //添加数据
    public void addItem(Integer i){
        datas.add(i);
    }
}
