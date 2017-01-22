package com.nbicc.gywlw.Model;

public class GywlwWarningRules {
    private Integer ruleId;

    private String ruleName;

    private String ruleName2;

    private String plcId;

    private String deviceId;

    private Byte delMark;

    private Integer rulesPass;

    private Integer rule2;

    private Integer rule1;

    private Integer ruleCondition;

    private String ruleField;

    private Integer ruleAlarmlevel;

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName == null ? null : ruleName.trim();
    }

    public String getRuleName2() {
        return ruleName2;
    }

    public void setRuleName2(String ruleName2) {
        this.ruleName2 = ruleName2 == null ? null : ruleName2.trim();
    }

    public String getPlcId() {
        return plcId;
    }

    public void setPlcId(String plcId) {
        this.plcId = plcId == null ? null : plcId.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Byte getDelMark() {
        return delMark;
    }

    public void setDelMark(Byte delMark) {
        this.delMark = delMark;
    }

    public Integer getRulesPass() {
        return rulesPass;
    }

    public void setRulesPass(Integer rulesPass) {
        this.rulesPass = rulesPass;
    }

    public Integer getRule2() {
        return rule2;
    }

    public void setRule2(Integer rule2) {
        this.rule2 = rule2;
    }

    public Integer getRule1() {
        return rule1;
    }

    public void setRule1(Integer rule1) {
        this.rule1 = rule1;
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
        this.ruleField = ruleField == null ? null : ruleField.trim();
    }

    public Integer getRuleAlarmlevel() {
        return ruleAlarmlevel;
    }

    public void setRuleAlarmlevel(Integer ruleAlarmlevel) {
        this.ruleAlarmlevel = ruleAlarmlevel;
    }
}