package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/8/20.
 */

public class ProretryTypeModel extends BaseModel{
    public int iconTag;
    public int itemId;
    public int sendTheTotal;
    public String type= "";
    public String count= "";
    public String theIssuingParty= "";
    public String broadcastContent= "";
    public String broadcastImages1 = "";
    public String broadcastImages2= "";
    public String broadcastImages3= "";
    public String broadcastStartTime= "";
    public String sendingLimit= "";
    public String sendObject= "";
    public String average= "";
    public String peopleNumber= "";
    public String icon= "";
    public String currency= "";
    public String actuallyPay= "";

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSendTheTotal() {
        return sendTheTotal;
    }

    public void setSendTheTotal(int sendTheTotal) {
        this.sendTheTotal = sendTheTotal;
    }

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

    public int getIconTag() {
        return iconTag;
    }

    public void setIconTag(int iconTag) {
        this.iconTag = iconTag;
    }

    public String getTheIssuingParty() {
        return theIssuingParty;
    }

    public void setTheIssuingParty(String theIssuingParty) {
        this.theIssuingParty = theIssuingParty;
    }

    public String getBroadcastContent() {
        return broadcastContent;
    }

    public void setBroadcastContent(String broadcastContent) {
        this.broadcastContent = broadcastContent;
    }

    public String getBroadcastImages1() {
        return broadcastImages1;
    }

    public void setBroadcastImages1(String broadcastImages1) {
        this.broadcastImages1 = broadcastImages1;
    }

    public String getBroadcastImages2() {
        return broadcastImages2;
    }

    public void setBroadcastImages2(String broadcastImages2) {
        this.broadcastImages2 = broadcastImages2;
    }

    public String getBroadcastImages3() {
        return broadcastImages3;
    }

    public void setBroadcastImages3(String broadcastImages3) {
        this.broadcastImages3 = broadcastImages3;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getBroadcastStartTime() {
        return broadcastStartTime;
    }

    public void setBroadcastStartTime(String broadcastStartTime) {
        this.broadcastStartTime = broadcastStartTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(String peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getActuallyPay() {
        return actuallyPay;
    }

    public void setActuallyPay(String actuallyPay) {
        this.actuallyPay = actuallyPay;
    }

    public String getSendingLimit() {
        return sendingLimit;
    }

    public void setSendingLimit(String sendingLimit) {
        this.sendingLimit = sendingLimit;
    }

    public String getSendObject() {
        return sendObject;
    }

    public void setSendObject(String sendObject) {
        this.sendObject = sendObject;
    }

    @Override
    public String toString() {
        return "ProretryTypeModel{" +
                "iconTag=" + iconTag +
                ", itemId=" + itemId +
                ", sendTheTotal=" + sendTheTotal +
                ", type='" + type + '\'' +
                ", count='" + count + '\'' +
                ", theIssuingParty='" + theIssuingParty + '\'' +
                ", broadcastContent='" + broadcastContent + '\'' +
                ", broadcastImages1='" + broadcastImages1 + '\'' +
                ", broadcastImages2='" + broadcastImages2 + '\'' +
                ", broadcastImages3='" + broadcastImages3 + '\'' +
                ", broadcastStartTime='" + broadcastStartTime + '\'' +
                ", sendingLimit='" + sendingLimit + '\'' +
                ", sendObject='" + sendObject + '\'' +
                ", average='" + average + '\'' +
                ", peopleNumber='" + peopleNumber + '\'' +
                ", icon='" + icon + '\'' +
                ", currency='" + currency + '\'' +
                ", actuallyPay='" + actuallyPay + '\'' +
                '}';
    }
}
