package com.guang.jiyu.jiyu.event;

import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.jiyu.model.InformationModel;

/**
 * Created by admin on 2018/7/13.
 */

public class FastInformationEvent extends BaseEvent{

    private InformationModel fastInformationModel;

    public FastInformationEvent(InformationModel fastInformationModel) {
        this.fastInformationModel = fastInformationModel;
    }

    public InformationModel getFastInformationModel() {
        return fastInformationModel;
    }

    public void setFastInformationModel(InformationModel fastInformationModel) {
        this.fastInformationModel = fastInformationModel;
    }
}
