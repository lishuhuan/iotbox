package com.nbicc.gywlw.Model;

import java.util.Date;

public class GywlwProjectDeviceGroup {
    private String id;

    private String projectId;

    private String deviceId;

    private Date createTime;

    private Byte delMark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
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