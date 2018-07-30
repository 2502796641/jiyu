package com.guang.jiyu.jiyu.listener;

import android.view.View;
import android.widget.AdapterView;

/**
 * 单击ListView中某一项触发的事件
 * Created by admin on 2018/6/25.
 */

public class OnItemListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long id) {
        System.out.println("123");
    }
}
