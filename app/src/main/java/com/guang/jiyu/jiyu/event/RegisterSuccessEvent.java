package com.guang.jiyu.jiyu.event;

import com.guang.jiyu.base.BaseEvent;

/**
 * Created by admin on 2018/9/6.
 */

public class RegisterSuccessEvent extends BaseEvent{

    private String account;
    private String pwd;

    public RegisterSuccessEvent(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
