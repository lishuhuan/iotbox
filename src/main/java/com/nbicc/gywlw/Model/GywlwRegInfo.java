package com.nbicc.gywlw.Model;

public class GywlwRegInfo {
    private String regId;

    private String subdeviceId;

    private String deviceId;

    private String regName;

    private String regAlias;

    private String regAddress;

    private String gatewayId;

    private String plcId;

    private String content;

    private Byte permission;

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId == null ? null : regId.trim();
    }

    public String getSubdeviceId() {
        return subdeviceId;
    }

    public void setSubdeviceId(String subdeviceId) {
        this.subdeviceId = subdeviceId == null ? null : subdeviceId.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getRegName() {
        return regName;
    }

    public void setRegName(String regName) {
        this.regName = regName == null ? null : regName.trim();
    }

    public String getRegAlias() {
        return regAlias;
    }

    public void setRegAlias(String regAlias) {
        this.regAlias = regAlias == null ? null : regAlias.trim();
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress == null ? null : regAddress.trim();
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId == null ? null : gatewayId.trim();
    }

    public String getPlcId() {
        return plcId;
    }

    public void setPlcId(String plcId) {
        this.plcId = plcId == null ? null : plcId.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Byte getPermission() {
        return permission;
    }

    public void setPermission(Byte permission) {
        this.permission = permission;
    }
}