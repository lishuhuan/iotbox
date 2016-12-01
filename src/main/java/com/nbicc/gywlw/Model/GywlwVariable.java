package com.nbicc.gywlw.Model;

import java.util.Date;

public class GywlwVariable {
    private String variableId;

    private String projectId;

    private String variableName;

    private Date lastConnect;

    public Date getLastConnect() {
        return lastConnect;
    }

    public void setLastConnect(Date lastConnect) {
        this.lastConnect = lastConnect;
    }

    public String getVariableId() {
        return variableId;
    }

    public void setVariableId(String variableId) {
        this.variableId = variableId == null ? null : variableId.trim();
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
}