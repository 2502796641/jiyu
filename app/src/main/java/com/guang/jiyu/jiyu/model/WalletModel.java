package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/7/27.
 */

public class WalletModel extends BaseModel{
    public int walletId;
    public Double mbtc;
    public int status;
    public String currency;

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public Double getMbtc() {
        return mbtc;
    }

    public void setMbtc(Double mbtc) {
        this.mbtc = mbtc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
