/*
 * Copyright 2018-2019 The Code Department.
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
package com.tcdng.jacklyn.security.web.controllers;

import com.tcdng.jacklyn.security.constants.SecurityModuleAuditConstants;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * Controller for changing user password.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/security/changepassword")
@UplBinding("web/security/upl/changeuserpassword.upl")
@ResultMappings({
        @ResultMapping(name = "refreshmain", response = { "!refreshpanelresponse panels:$l{changePasswordPanel}" }) })
public class ChangeUserPasswordController extends AbstractSecurityPageController {

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

    private String changePasswordMessage;

    public ChangeUserPasswordController() {
        super(true, false);
    }

    @Action
    public String changeUserPassword() throws UnifyException {
        try {
            setDisplayMessage(null);
            getSecurityService().changeUserPassword(oldPassword, newPassword);
            logUserEvent(SecurityModuleAuditConstants.CHANGE_PASSWORD);
            hintUser("$m{security.changepassword.hint.passwordchanged}");
        } catch (UnifyException e) {
            UnifyError err = e.getUnifyError();
            setDisplayMessage(getSessionMessage(err.getErrorCode(), err.getErrorParams()));
        } finally {
            oldPassword = null;
            newPassword = null;
            confirmPassword = null;
        }
        return "refreshmain";
    }

    public String getModeStyle() {
        return EventType.UPDATE.colorMode();
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

    @Override
    protected void onOpenPage() throws UnifyException {
        oldPassword = null;
        newPassword = null;
        confirmPassword = null;
        setDisplayMessage(changePasswordMessage);
    }

    @Override
    protected String getDocViewPanelName() {
        return "manageChangePasswordPanel";
    }

    private void setDisplayMessage(String message) throws UnifyException {
        changePasswordMessage = message;
    }

}
