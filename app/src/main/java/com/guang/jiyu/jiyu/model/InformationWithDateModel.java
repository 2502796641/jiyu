package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

import java.util.List;

/**
 * Created by admin on 2018/7/23.
 */

public class InformationWithDateModel extends BaseModel{
    public String mouths;
    public String dayForWeek;
    public List<FastInformationModel> list;


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

    public List<FastInformationModel> getList() {
        return list;
    }

    public void setList(List<FastInformationModel> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "InformationWithDateModel{" +
                "mouths='" + mouths + '\'' +
                ", dayForWeek='" + dayForWeek + '\'' +
                ", list=" + list +
                '}';
    }
}
