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
import com.tcdng.jacklyn.security.web.beans.ChangeUserPasswordPageBean;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
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
public class ChangeUserPasswordController extends AbstractSecurityPageController<ChangeUserPasswordPageBean> {

    public ChangeUserPasswordController() {
        super(ChangeUserPasswordPageBean.class, true, false, false);
    }

    @Action
    public String changeUserPassword() throws UnifyException {
        ChangeUserPasswordPageBean pageBean = getPageBean();
        try {
            pageBean.setChangePasswordMessage(null);
            getSecurityService().changeUserPassword(pageBean.getOldPassword(), pageBean.getNewPassword());
            logUserEvent(SecurityModuleAuditConstants.CHANGE_PASSWORD);
            hintUser("$m{security.changepassword.hint.passwordchanged}");
        } catch (UnifyException e) {
            UnifyError err = e.getUnifyError();
            pageBean.setChangePasswordMessage(getSessionMessage(err.getErrorCode(), err.getErrorParams()));
        } finally {
            pageBean.setNewPassword(null);
            pageBean.setOldPassword(null);
            pageBean.setConfirmPassword(null);
        }
        return "refreshmain";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        ChangeUserPasswordPageBean pageBean = getPageBean();
        pageBean.setNewPassword(null);
        pageBean.setOldPassword(null);
        pageBean.setConfirmPassword(null);
    }

}
