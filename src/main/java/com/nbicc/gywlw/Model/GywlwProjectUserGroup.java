package com.nbicc.gywlw.Model;

import java.util.Date;

public class GywlwProjectUserGroup {

    private String userName;

    private Integer id;

    private String projectId;

    private String userId;

    private Byte writePermission;

    private Date createTime;

    private Byte delMark;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Byte getWritePermission() {
        return writePermission;
    }

    public void setWritePermission(Byte writePermission) {
        this.writePermission = writePermission;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getDelMark() {
        return delMark;
    }

    public void setDelMark(Byte delMark) {
        this.delMark = delMark;
    }
}