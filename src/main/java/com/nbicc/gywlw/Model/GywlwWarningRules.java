package com.nbicc.gywlw.Model;

public class GywlwWarningRules {
    private Integer ruleId;

    private String ruleName;

    private String ruleDesc;

    private String regId;

    private String regName;

    private String deviceId;

    private Double operand;

    private String operator;

    private Integer severity;

    private Byte delMark;

    public String getRegName() {
        return regName;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

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

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc == null ? null : ruleDesc.trim();
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

    public Double getOperand() {
        return operand;
    }

    public void setOperand(Double operand) {
        this.operand = operand;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public Byte getDelMark() {
        return delMark;
    }

    public void setDelMark(Byte delMark) {
        this.delMark = delMark;
    }
}