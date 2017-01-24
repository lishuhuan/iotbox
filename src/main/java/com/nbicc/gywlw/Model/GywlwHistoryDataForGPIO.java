package com.nbicc.gywlw.Model;

import java.util.Date;

public class GywlwHistoryDataForGPIO {
    private Integer id;

    private String gpioId;

    private Integer value;

    private Date time;

    private String deviceId;

    private String content;

    private String alarm;

    private String variableName;

    private String ruleName;

    private String ruleName2;

    private String ruleCondition;

    private String ruleField;

    private String ruleAlarmlevel;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleName2() {
        return ruleName2;
    }

    public void setRuleName2(String ruleName2) {
        this.ruleName2 = ruleName2;
    }

    public String getRuleCondition() {
        return ruleCondition;
    }

    public void setRuleCondition(String ruleCondition) {
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

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGpioId() {
        return gpioId;
    }

    public void setGpioId(String gpioId) {
        this.gpioId = gpioId == null ? null : gpioId.trim();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm == null ? null : alarm.trim();
    }
}