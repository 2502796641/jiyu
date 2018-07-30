package com.guang.jiyu.jiyu.event;

import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.jiyu.model.AirCandyModel;

/**
 * Created by admin on 2018/7/19.
 */

public class GetCandySuccessEvent extends BaseEvent{
    private AirCandyModel airCandyModel;

    public GetCandySuccessEvent(AirCandyModel airCandyModel) {
        this.airCandyModel = airCandyModel;
    }

    public AirCandyModel getAirCandyModel() {
        return airCandyModel;
    }

    public void setAirCandyModel(AirCandyModel airCandyModel) {
        this.airCandyModel = airCandyModel;
    }
}
