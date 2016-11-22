package com.nbicc.gywlw.Model;

import java.util.Date;

public class GywlwUser {
    private String userId;

    private String userPsd;

    private String userPhone;

    private String userName;

    private Date createTime;

    private Integer userLevel;

    private Integer duserLevel;

    private String parentId;

    private Boolean userSex;

    private String userEmail;

    private String userFixedphone;

    private String companyId;

    private String companyName;

    private Boolean delMark;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserPsd() {
        return userPsd;
    }

    public void setUserPsd(String userPsd) {
        this.userPsd = userPsd == null ? null : userPsd.trim();
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getDuserLevel() {
        return duserLevel;
    }

    public void setDuserLevel(Integer duserLevel) {
        this.duserLevel = duserLevel;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public Boolean getUserSex() {
        return userSex;
    }

    public void setUserSex(Boolean userSex) {
        this.userSex = userSex;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail == null ? null : userEmail.trim();
    }

    public String getUserFixedphone() {
        return userFixedphone;
    }

    public void setUserFixedphone(String userFixedphone) {
        this.userFixedphone = userFixedphone == null ? null : userFixedphone.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public Boolean getDelMark() {
        return delMark;
    }

    public void setDelMark(Boolean delMark) {
        this.delMark = delMark;
    }
}