/*
 * Copyright 2018 The Code Department
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.jacklyn.security.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedTimestampedStatusEntity;
import com.tcdng.jacklyn.organization.entities.Branch;
import com.tcdng.jacklyn.security.constants.SecurityModuleNameConstants;
import com.tcdng.jacklyn.system.entities.Theme;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity for storing user information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Policy("userpolicy")
@Managed(module = SecurityModuleNameConstants.SECURITY_MODULE, title = "User", reportable = true, auditable = true)
@Table(name = "USER", uniqueConstraints = { @UniqueConstraint({ "loginId" }) })
public class User extends BaseVersionedTimestampedStatusEntity {

    @ForeignKey(type = Theme.class, nullable = true)
    private Long themeId;

    @ForeignKey(type = Branch.class, nullable = true)
    private Long branchId;

    @Column(length = 64)
    private String fullName;

    @Column(transformer = "uppercase-transformer")
    private String loginId;

    @Column(length = 256)
    private String password;

    @Column(length = 64)
    private String email;

    @Column
    private Integer loginAttempts;

    @Column(name = "LOGIN_LOCKED_FG")
    private Boolean loginLocked;

    @Column(name = "CHANGE_PASSWORD_FG")
    private Boolean changePassword;

    @Column(name = "PASSWORD_EXPIRES_FG")
    private Boolean passwordExpires;

    @Column(name = "ALLOW_MULTI_LOGIN_FG")
    private Boolean allowMultipleLogin;

    @Column(nullable = true)
    private Date passwordExpiryDt;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date lastLoginDt;

    @Column(name = "SUPERVISOR_FG")
    private Boolean supervisor;

    @ListOnly(key = "branchId", property = "description")
    private String branchDesc;

    @ListOnly(key = "themeId", property = "description")
    private String themeDesc;

    public User(Long id, String fullName, String loginId, String email, Boolean passwordExpires) {
        this.setId(id);
        this.fullName = fullName;
        this.loginId = loginId;
        this.email = email;
        this.passwordExpires = passwordExpires;
        this.supervisor = Boolean.FALSE;
    }

    public User() {

    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    @Override
    public String getDescription() {
        return fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getLoginLocked() {
        return loginLocked;
    }

    public void setLoginLocked(Boolean loginLocked) {
        this.loginLocked = loginLocked;
    }

    public Boolean getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(Boolean changePassword) {
        this.changePassword = changePassword;
    }

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public Boolean getPasswordExpires() {
        return passwordExpires;
    }

    public void setPasswordExpires(Boolean passwordExpires) {
        this.passwordExpires = passwordExpires;
    }

    public Date getPasswordExpiryDt() {
        return passwordExpiryDt;
    }

    public void setPasswordExpiryDt(Date passwordExpiryDt) {
        this.passwordExpiryDt = passwordExpiryDt;
    }

    public Date getLastLoginDt() {
        return lastLoginDt;
    }

    public void setLastLoginDt(Date lastLoginDt) {
        this.lastLoginDt = lastLoginDt;
    }

    public String getBranchDesc() {
        return branchDesc;
    }

    public void setBranchDesc(String branchDesc) {
        this.branchDesc = branchDesc;
    }

    public String getThemeDesc() {
        return themeDesc;
    }

    public void setThemeDesc(String themeDesc) {
        this.themeDesc = themeDesc;
    }

    public boolean isChangeUserPassword() {
        return Boolean.TRUE.equals(this.changePassword);
    }

    public Boolean getAllowMultipleLogin() {
        return allowMultipleLogin;
    }

    public void setAllowMultipleLogin(Boolean allowMultipleLogin) {
        this.allowMultipleLogin = allowMultipleLogin;
    }

    public Boolean getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Boolean supervisor) {
        this.supervisor = supervisor;
    }
}
