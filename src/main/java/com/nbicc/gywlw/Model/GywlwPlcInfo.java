package com.nbicc.gywlw.Model;

public class GywlwPlcInfo {
    private String id;

    private String subdeviceId;

    private String deviceId;

    private String plcType;

    private String plcBrand;

    private String content;

    private String plcName;

    private String plcStatus;

    public String getPlcStatus() {
        return plcStatus;
    }

    public void setPlcStatus(String plcStatus) {
        this.plcStatus = plcStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
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

    public String getPlcType() {
        return plcType;
    }

    public void setPlcType(String plcType) {
        this.plcType = plcType == null ? null : plcType.trim();
    }

    public String getPlcBrand() {
        return plcBrand;
    }

    public void setPlcBrand(String plcBrand) {
        this.plcBrand = plcBrand == null ? null : plcBrand.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName == null ? null : plcName.trim();
    }
}