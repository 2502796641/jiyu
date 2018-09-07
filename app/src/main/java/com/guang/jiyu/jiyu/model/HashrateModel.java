package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/8/31.
 */

public class HashrateModel extends BaseModel{

    public int id;
    public int fdnQtyC;//基础算力
    public int fdnQtyD;//临时算力

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFdnQtyC() {
        return fdnQtyC;
    }

    public void setFdnQtyC(int fdnQtyC) {
        this.fdnQtyC = fdnQtyC;
    }

    public int getFdnQtyD() {
        return fdnQtyD;
    }

    public void setFdnQtyD(int fdnQtyD) {
        this.fdnQtyD = fdnQtyD;
    }
}
