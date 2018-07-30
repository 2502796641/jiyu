package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/7/13.
 */

public class FastInformationModel extends BaseModel {

    public String infoId;
    public String mouths;
    public String dayForWeek;
    public String date;
    public String title;
    public String content;
    public String bulish;//看好
    public String bearish;//看空
    public String share_times;
    public String time;
    public int tag;//是否为该列表中的第一条数据

    public String getMouths() {
        return mouths;
    }

    public void setMouths(String mouths) {
        this.mouths = mouths;
    }

    public String getDayForWeek() {
        return dayForWeek;
    }

    public void setDayForWeek(String dayForWeek) {
        this.dayForWeek = dayForWeek;
    }

    public String getInfoId() {

        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBulish() {
        return bulish;
    }

    public void setBulish(String bulish) {
        this.bulish = bulish;
    }

    public String getBearish() {
        return bearish;
    }

    public void setBearish(String bearish) {
        this.bearish = bearish;
    }

    public String getShare_times() {
        return share_times;
    }

    public void setShare_times(String share_times) {
        this.share_times = share_times;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "FastInformationModel{" +
                "mouths='" + mouths + '\'' +
                ", dayForWeek='" + dayForWeek + '\'' +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", bulish='" + bulish + '\'' +
                ", bearish='" + bearish + '\'' +
                ", share_times='" + share_times + '\'' +
                ", time='" + time + '\'' +
                ", tag=" + tag +
                '}';
    }
}
