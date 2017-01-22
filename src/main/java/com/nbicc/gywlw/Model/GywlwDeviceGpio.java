package com.nbicc.gywlw.Model;

public class GywlwDeviceGpio {
    private Integer id;

    private String fieldAddress;

    private String fieldName;

    private String fieldName2;

    private String fieldFunctioncode;

    private String fieldAttach;

    private Integer fieldRw;

    private String deviceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldAddress() {
        return fieldAddress;
    }

    public void setFieldAddress(String fieldAddress) {
        this.fieldAddress = fieldAddress == null ? null : fieldAddress.trim();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public String getFieldName2() {
        return fieldName2;
    }

    public void setFieldName2(String fieldName2) {
        this.fieldName2 = fieldName2 == null ? null : fieldName2.trim();
    }

    public String getFieldFunctioncode() {
        return fieldFunctioncode;
    }

    public void setFieldFunctioncode(String fieldFunctioncode) {
        this.fieldFunctioncode = fieldFunctioncode == null ? null : fieldFunctioncode.trim();
    }

    public String getFieldAttach() {
        return fieldAttach;
    }

    public void setFieldAttach(String fieldAttach) {
        this.fieldAttach = fieldAttach == null ? null : fieldAttach.trim();
    }

    public Integer getFieldRw() {
        return fieldRw;
    }

    public void setFieldRw(Integer fieldRw) {
        this.fieldRw = fieldRw;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }
}