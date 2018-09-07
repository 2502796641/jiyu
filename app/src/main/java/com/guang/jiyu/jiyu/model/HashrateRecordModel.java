package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/8/31.
 */

public class HashrateRecordModel extends BaseModel{
    public int id;
    public int fdnQty;//算力加减值
    public String fdcType;//算力加减详情
    public String createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFdnQty() {
        return fdnQty;
    }

    public void setFdnQty(int fdnQty) {
        this.fdnQty = fdnQty;
    }

    public String getFdcType() {
        return fdcType;
    }

    public void setFdcType(String fdcType) {
        this.fdcType = fdcType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
