package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/7/12.
 */

public class ProjectDetailModel extends BaseModel {
    public String title;//标题
    public int itemId;//项目唯一标识
    public String projectIntro;//项目简介
    public String issueDate;//发行时间
    public String officialWebrite;//官网
    public String whitePaper;//白皮书
    public String issueCount;//发行量
    public String circulateCount;//流通量
    public String iconUrl;//图片url
    public int statu;//判断是否为历史活动，0不是历史项目，1历史项目

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProjectIntro() {
        return projectIntro;
    }

    public void setProjectIntro(String projectIntro) {
        this.projectIntro = projectIntro;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getOfficialWebrite() {
        return officialWebrite;
    }

    public void setOfficialWebrite(String officialWebrite) {
        this.officialWebrite = officialWebrite;
    }

    public String getWhitePaper() {
        return whitePaper;
    }

    public void setWhitePaper(String whitePaper) {
        this.whitePaper = whitePaper;
    }

    public String getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(String issueCount) {
        this.issueCount = issueCount;
    }

    public String getCirculateCount() {
        return circulateCount;
    }

    public void setCirculateCount(String circulateCount) {
        this.circulateCount = circulateCount;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    @Override
    public String toString() {
        return "ProjectDetailModel{" +
                "title='" + title + '\'' +
                ", projectIntro='" + projectIntro + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", officialWebrite='" + officialWebrite + '\'' +
                ", whitePaper='" + whitePaper + '\'' +
                ", issueCount='" + issueCount + '\'' +
                ", circulateCount='" + circulateCount + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}
