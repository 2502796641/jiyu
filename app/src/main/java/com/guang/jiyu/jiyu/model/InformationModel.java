package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/7/11.
 */

public class InformationModel extends BaseModel{

    public String title;
    public String infoId;
    public String content;
    public String resource;
    public String reading_count;
    public String img_url;
    public String data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getReading_count() {
        return reading_count;
    }

    public void setReading_count(String reading_count) {
        this.reading_count = reading_count;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "InformationModel{" +
                "content='" + content + '\'' +
                ", resource='" + resource + '\'' +
                ", reading_count='" + reading_count + '\'' +
                ", img_url='" + img_url + '\'' +
                '}';
    }
}
