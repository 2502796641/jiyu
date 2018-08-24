package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/8/23.
 */

public class PropetryRecordModel extends BaseModel{

    public String state;
    public String updateTime;
    public String balance;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "PropetryRecordModel{" +
                "state='" + state + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}
