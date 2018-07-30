package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/7/27.
 */

public class WalletModel extends BaseModel{
    public int walletId;
    public int mbtc;
    public int status;

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getMbtc() {
        return mbtc;
    }

    public void setMbtc(int mbtc) {
        this.mbtc = mbtc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
