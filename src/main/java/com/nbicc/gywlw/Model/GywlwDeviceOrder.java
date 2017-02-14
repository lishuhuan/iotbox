package com.nbicc.gywlw.Model;

import java.util.Date;

public class GywlwDeviceOrder {
    private Integer id;

    private String deviceId;

    private String deviceSn;

    private String orderCode;

    private Integer orderTotal;

    private Integer orderFinished;

    private Date createdTime;

    private String recentWorkTime;

    private String finishTime;

    private String totalTime;

    private String passageway;

    private String productionId;

    private String productionName;

    private String operateMark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn == null ? null : deviceSn.trim();
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    public Integer getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Integer orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Integer getOrderFinished() {
        return orderFinished;
    }

    public void setOrderFinished(Integer orderFinished) {
        this.orderFinished = orderFinished;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getRecentWorkTime() {
        return recentWorkTime;
    }

    public void setRecentWorkTime(String recentWorkTime) {
        this.recentWorkTime = recentWorkTime == null ? null : recentWorkTime.trim();
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime == null ? null : finishTime.trim();
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime == null ? null : totalTime.trim();
    }

    public String getPassageway() {
        return passageway;
    }

    public void setPassageway(String passageway) {
        this.passageway = passageway == null ? null : passageway.trim();
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId == null ? null : productionId.trim();
    }

    public String getProductionName() {
        return productionName;
    }

    public void setProductionName(String productionName) {
        this.productionName = productionName == null ? null : productionName.trim();
    }

    public String getOperateMark() {
        return operateMark;
    }

    public void setOperateMark(String operateMark) {
        this.operateMark = operateMark == null ? null : operateMark.trim();
    }
}