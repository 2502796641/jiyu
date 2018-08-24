package com.guang.jiyu.jiyu.model;

import com.guang.jiyu.base.BaseModel;

/**
 * Created by admin on 2018/8/24.
 */

public class ProjectIssueRecordModel extends BaseModel{

    public String issue_count;
    public String update_time;

    public String getIssue_count() {
        return issue_count;
    }

    public void setIssue_count(String issue_count) {
        this.issue_count = issue_count;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
