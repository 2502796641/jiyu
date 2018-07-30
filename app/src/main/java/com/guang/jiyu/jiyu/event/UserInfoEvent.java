package com.guang.jiyu.jiyu.event;

import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.jiyu.model.UserInfoModel;

/**
 * Created by admin on 2018/7/25.
 */

public class UserInfoEvent extends BaseEvent{

    private UserInfoModel model;

    public UserInfoEvent(UserInfoModel model) {
        this.model = model;
    }

    public UserInfoModel getModel() {
        return model;
    }

    public void setModel(UserInfoModel model) {
        this.model = model;
    }
}
