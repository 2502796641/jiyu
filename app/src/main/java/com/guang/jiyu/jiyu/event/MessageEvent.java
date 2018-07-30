package com.guang.jiyu.jiyu.event;

import com.guang.jiyu.base.BaseEvent;

/**
 * Created by admin on 2018/7/9.
 */

public class MessageEvent extends BaseEvent{

    private String message;
    public  MessageEvent(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
