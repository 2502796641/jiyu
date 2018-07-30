package com.guang.jiyu.jiyu.event;

import com.guang.jiyu.base.BaseEvent;

/**
 * Created by admin on 2018/7/19.
 */

public class NewsFlashRefreshEvent extends BaseEvent{

    private boolean isRefresh;
    private int type;//新闻类型，1为资讯，2为快讯

    public NewsFlashRefreshEvent(int type) {
        this.type = type;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
