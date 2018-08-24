package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/8/20.
 */

public class ProretryTypeModel extends BaseModel{
    public String type;
    public String count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ProretryTypeModel{" +
                "type='" + type + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
