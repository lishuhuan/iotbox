package com.nbicc.gywlw.Model;

import java.util.Date;

public class GywlwDevice {
    private String deviceId;

    private String deviceSn;

    private String adminId;

    private String adminName;

    private String factoryId;

    private String factoryName;

    private Date createTime;

    private String content;

    private Byte deviceOnline;      //0为在线，1为离线

    private String deviceTypeId;

    private String deviceName;

    private Date lastConnected;

    private String gateway;

    private Date expired;

    private Byte delMark;

    private Byte expiredRight;

    private String gpioId;

    private String hardwareEdition;

    private String softwareEdition;

    private Integer threeGMode; //0：否，默认为以太网模式 ,1：是

    private String deviceStatus;

    private String orderCode;

    private Integer orderTotal;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Integer orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getHardwareEdition() {
        return hardwareEdition;
    }

    public void setHardwareEdition(String hardwareEdition) {
        this.hardwareEdition = hardwareEdition;
    }

    public String getSoftwareEdition() {
        return softwareEdition;
    }

    public void setSoftwareEdition(String softwareEdition) {
        this.softwareEdition = softwareEdition;
    }

    public Integer getThreeGMode() {
        return threeGMode;
    }

    public void setThreeGMode(Integer threeGMode) {
        this.threeGMode = threeGMode;
    }

    public String getGpioId() {
        return gpioId;
    }

    public void setGpioId(String gpioId) {
        this.gpioId = gpioId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public Byte getDeviceOnline() {
        return deviceOnline;
    }

    public void setDeviceOnline(Byte deviceOnline) {
        this.deviceOnline = deviceOnline;
    }

    public Byte getExpiredRight() {
        return expiredRight;
    }

    public void setExpiredRight(Byte expiredRight) {
        this.expiredRight = expiredRight;
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

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId == null ? null : adminId.trim();
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId == null ? null : factoryId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }



    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId == null ? null : deviceTypeId.trim();
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    public Date getLastConnected() {
        return lastConnected;
    }

    public void setLastConnected(Date lastConnected) {
        this.lastConnected = lastConnected;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway == null ? null : gateway.trim();
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public Byte getDelMark() {
        return delMark;
    }

    public void setDelMark(Byte delMark) {
        this.delMark = delMark;
    }
}