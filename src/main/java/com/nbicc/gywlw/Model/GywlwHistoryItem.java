package com.nbicc.gywlw.Model;

import java.util.Date;

public class GywlwHistoryItem implements Cloneable{
    private Integer itemId;

    private String regId;

    private String plcId;

    private String plcName;

    private String deviceId;

    private String projectId;

    private String variableName;

    private String deviceName;

    private String itemName;

    private String itemAlias;

    private String itemAddress;

    private Integer itemType;

    private Double itemValue;

    private Date itemTime;

    private Integer severity;

    private String itemContent;

    private Byte delMark;

    private Integer ruleId;
    private String ruleName;
    private String ruleName2;
    private Integer rule1;
    private Integer rule2;
    private Integer ruleCondition;
    private String ruleField;
    private String ruleAlarmlevel;


    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    public String getPlcId() {
        return plcId;
    }

    public void setPlcId(String plcId) {
        this.plcId = plcId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName2() {
        return ruleName2;
    }

    public void setRuleName2(String ruleName2) {
        this.ruleName2 = ruleName2;
    }

    public Integer getRule1() {
        return rule1;
    }

    public void setRule1(Integer rule1) {
        this.rule1 = rule1;
    }

    public Integer getRule2() {
        return rule2;
    }

    public void setRule2(Integer rule2) {
        this.rule2 = rule2;
    }

    public Integer getRuleCondition() {
        return ruleCondition;
    }

    public void setRuleCondition(Integer ruleCondition) {
        this.ruleCondition = ruleCondition;
    }

    public String getRuleField() {
        return ruleField;
    }

    public void setRuleField(String ruleField) {
        this.ruleField = ruleField;
    }

    public String getRuleAlarmlevel() {
        return ruleAlarmlevel;
    }

    public void setRuleAlarmlevel(String ruleAlarmlevel) {
        this.ruleAlarmlevel = ruleAlarmlevel;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId == null ? null : regId.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName == null ? null : variableName.trim();
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public String getItemAlias() {
        return itemAlias;
    }

    public void setItemAlias(String itemAlias) {
        this.itemAlias = itemAlias == null ? null : itemAlias.trim();
    }

    public String getItemAddress() {
        return itemAddress;
    }

    public void setItemAddress(String itemAddress) {
        this.itemAddress = itemAddress == null ? null : itemAddress.trim();
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Double getItemValue() {
        return itemValue;
    }

    public void setItemValue(Double itemValue) {
        this.itemValue = itemValue;
    }

    public Date getItemTime() {
        return itemTime;
    }

    public void setItemTime(Date itemTime) {
        this.itemTime = itemTime;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent == null ? null : itemContent.trim();
    }

    public Byte getDelMark() {
        return delMark;
    }

    public void setDelMark(Byte delMark) {
        this.delMark = delMark;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}