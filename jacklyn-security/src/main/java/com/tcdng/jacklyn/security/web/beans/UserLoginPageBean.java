/*
 * Copyright 2018-2019 The Code Department
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

package com.tcdng.jacklyn.security.web.beans;

import java.util.List;
import java.util.Locale;

import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.jacklyn.security.entities.UserRole;

/**
 * User login page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class UserLoginPageBean extends BasePageBean {

    private String userName;

    private String password;

    private String token;

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

    private String loginMessage;

    private String languageTag;
    
    private List<UserRole> userRoleList;

    private Locale origLocale;
    
    private boolean isLanguage;

    private boolean is2FA;

    public UserLoginPageBean() {
        super(null);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    public void setLoginMessage(String loginMessage) {
        this.loginMessage = loginMessage;
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }

    public List<UserRole> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<UserRole> userRoleList) {
        this.userRoleList = userRoleList;
    }

    public Locale getOrigLocale() {
        return origLocale;
    }

    public void setOrigLocale(Locale origLocale) {
        this.origLocale = origLocale;
    }

    public boolean isLanguage() {
        return isLanguage;
    }

    public void setLanguage(boolean isLanguage) {
        this.isLanguage = isLanguage;
    }

    public boolean isIs2FA() {
        return is2FA;
    }

    public void setIs2FA(boolean is2fa) {
        is2FA = is2fa;
    }

}
