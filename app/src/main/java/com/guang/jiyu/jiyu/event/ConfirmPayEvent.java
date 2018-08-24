package com.guang.jiyu.jiyu.event;

import com.guang.jiyu.base.BaseEvent;

/**
 * Created by admin on 2018/8/24.
 */

public class ConfirmPayEvent extends BaseEvent{
    private String account;

    public ConfirmPayEvent(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
