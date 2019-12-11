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

import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.unify.core.logging.EventType;

/**
 * Change user password page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ChangeUserPasswordPageBean extends BasePageBean {

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

    private String changePasswordMessage;

    public ChangeUserPasswordPageBean() {
        super("manageChangePasswordPanel");
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

    public String getChangePasswordMessage() {
        return changePasswordMessage;
    }

    public void setChangePasswordMessage(String changePasswordMessage) {
        this.changePasswordMessage = changePasswordMessage;
    }

    public String getModeStyle() {
        return EventType.UPDATE.colorMode();
    }

}
