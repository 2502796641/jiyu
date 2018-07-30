package com.guang.jiyu.jiyu.event;

import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.jiyu.model.FastInformationModel;

/**
 * Created by admin on 2018/7/13.
 */

public class ShareInformationEvent extends BaseEvent {
    private FastInformationModel informationModel;

    public ShareInformationEvent(FastInformationModel informationModel) {
        this.informationModel = informationModel;
    }

    public FastInformationModel getInformationModel() {
        return informationModel;
    }

    public void setInformationModel(FastInformationModel informationModel) {
        this.informationModel = informationModel;
    }
}
