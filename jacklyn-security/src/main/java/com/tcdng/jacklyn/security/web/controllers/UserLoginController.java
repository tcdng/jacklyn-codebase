/*
 * Copyright 2018-2020 The Code Department.
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

import java.util.List;
import java.util.Locale;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.security.constants.SecurityModuleAuditConstants;
import com.tcdng.jacklyn.security.constants.SecurityModuleErrorConstants;
import com.tcdng.jacklyn.security.constants.SecurityModuleSysParamConstants;
import com.tcdng.jacklyn.security.entities.User;
import com.tcdng.jacklyn.security.entities.UserRole;
import com.tcdng.jacklyn.security.entities.UserRoleQuery;
import com.tcdng.jacklyn.security.web.beans.UserLoginPageBean;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.security.TwoFactorAutenticationService;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.ui.control.Table;
import com.tcdng.unify.web.ui.panel.SwitchPanel;

/**
 * Controller for user login.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/security/loginpage")
@UplBinding("web/security/upl/login.upl")
@ResultMappings({
        @ResultMapping(name = "refreshlogin", response = { "!refreshpanelresponse panels:$l{loginSequencePanel}" }),
        @ResultMapping(
                name = "switchlogin",
                response = { "!hidepopupresponse",
                        "!switchpanelresponse panels:$l{loginSequencePanel.loginBodyPanel}" }),
        @ResultMapping(
                name = "switchchangepassword",
                response = { "!switchpanelresponse panels:$l{loginSequencePanel.changePasswordBodyPanel}" }),
        @ResultMapping(name = "switchrolepanel", response = { "!showpopupresponse popup:$s{selectRolePanel}" }) })
public class UserLoginController extends AbstractApplicationForwarderController<UserLoginPageBean> {

    public UserLoginController() {
        super(UserLoginPageBean.class, false, false, false);
    }

    @Action
    public String login() throws UnifyException {
        UserLoginPageBean pageBean = getPageBean();
        try {
            Locale loginLocale = null;
            if (pageBean.isLanguage() && StringUtils.isNotBlank(pageBean.getLanguageTag())) {
                loginLocale = Locale.forLanguageTag(pageBean.getLanguageTag());
            }

            User user = getSecurityService().loginUser(pageBean.getUserName(), pageBean.getPassword(), loginLocale);
            pageBean.setUserName(null);
            pageBean.setPassword(null);

            if (!user.isReserved() && pageBean.isIs2FA()) {
                TwoFactorAutenticationService twoFactorAuthService =
                        (TwoFactorAutenticationService) this
                                .getComponent(ApplicationComponents.APPLICATION_TWOFACTORAUTHENTICATIONSERVICE);
                if (!twoFactorAuthService.authenticate(pageBean.getUserName(), pageBean.getToken())) {
                    throw new UnifyException(SecurityModuleErrorConstants.INVALID_ONETIME_PASSWORD);
                }
            }

            logUserEvent(SecurityModuleAuditConstants.LOGIN);
            pageBean.setLoginMessage(null);

            if (user.isChangeUserPassword() && !user.isReserved()) {
                pageBean.setOldPassword(null);
                pageBean.setNewPassword(null);
                pageBean.setConfirmPassword(null);
                return "switchchangepassword";
            }

            return selectRole();
        } catch (UnifyException e) {
            logError(e);
            UnifyError err = e.getUnifyError();
            pageBean.setLoginMessage(getSessionMessage(err.getErrorCode(), err.getErrorParams()));
        }
        return "refreshlogin";
    }

    @Action
    public String changeUserPassword() throws UnifyException {
        UserLoginPageBean pageBean = getPageBean();
        try {
            pageBean.setLoginMessage(null);
            getSecurityService().changeUserPassword(pageBean.getOldPassword(), pageBean.getNewPassword());
            logUserEvent(SecurityModuleAuditConstants.CHANGE_PASSWORD);
            return selectRole();
        } catch (UnifyException e) {
            UnifyError err = e.getUnifyError();
            pageBean.setLoginMessage(getSessionMessage(err.getErrorCode(), err.getErrorParams()));
            if (SecurityModuleErrorConstants.USER_ROLE_NOT_ACTIVE_AT_CURRENTTIME.equals(err.getErrorCode())) {
                return "switchlogin";
            }
        }
        return "switchchangepassword";
    }

    @Action
    public String cancelChangeUserPassword() throws UnifyException {
        UserLoginPageBean pageBean = getPageBean();
        pageBean.setUserName(null);
        pageBean.setPassword(null);
        pageBean.setLoginMessage(null);
        getSecurityService().logoutUser(false);
        return "switchlogin";
    }

    @Action
    public String selectUserRole() throws UnifyException {
        UserLoginPageBean pageBean = getPageBean();
        UserRole userRole = pageBean.getUserRoleList().get(getRoleTable().getViewIndex());
        pageBean.setUserRoleList(null);
        return forwardToApplication(userRole);
    }

    @Action
    public String cancelSelectUserRole() throws UnifyException {
        return cancelChangeUserPassword();
    }

    @Action
    public String changeLanguage() throws UnifyException {
        UserLoginPageBean pageBean = getPageBean();
        if (StringUtils.isNotBlank(pageBean.getLanguageTag())) {
            getSessionContext().setLocale(Locale.forLanguageTag(pageBean.getLanguageTag()));
        } else {
            getSessionContext().setLocale(pageBean.getOrigLocale());
        }

        return "refreshlogin";
    }

    @Override
    protected void onInitPage() throws UnifyException {
        UserLoginPageBean pageBean = getPageBean();
        // Show/hide language field based on system parameter
        boolean isLanguage =
                getSystemService().getSysParameterValue(boolean.class,
                        SystemModuleSysParamConstants.SYSPARAM_USE_LOGIN_LOCALE);
        setPageWidgetVisible("loginPanel.languageField", isLanguage);
        if (isLanguage) {
            pageBean.setOrigLocale(getSessionLocale());
        }

        pageBean.setLanguage(isLanguage);

        // Show/hide token field based on system parameter
        boolean is2FA =
                getSystemService().getSysParameterValue(boolean.class,
                        SecurityModuleSysParamConstants.ENABLE_TWOFACTOR_AUTHENTICATION);
        setPageWidgetVisible("loginPanel.tokenField", is2FA);
        pageBean.setIs2FA(is2FA);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        UserLoginPageBean pageBean = getPageBean();
        pageBean.setUserName(null);
        pageBean.setPassword(null);
        pageBean.setToken(null);
        pageBean.setNewPassword(null);
        pageBean.setOldPassword(null);
        pageBean.setConfirmPassword(null);
        SwitchPanel switchPanel = (SwitchPanel) getPage().getPanelByShortName("loginSequencePanel");
        switchPanel.switchContent("loginBodyPanel");
    }

    private String selectRole() throws UnifyException {
        UserLoginPageBean pageBean = getPageBean();
        pageBean.setLoginMessage(null);

        // Get user roles that are active based on current time
        UserToken userToken = getUserToken();
        UserRole userRole = null;

        UserRoleQuery query = new UserRoleQuery();
        query.userLoginId(userToken.getUserLoginId());
        query.roleStatus(RecordStatus.ACTIVE);
        query.roleActiveTime(getSecurityService().getNow());
        List<UserRole> userRoleList = getSecurityService().findUserRoles(query);

        if (userRoleList.isEmpty()) {
            if (!userToken.isReservedUser()) {
                throw new UnifyException(SecurityModuleErrorConstants.USER_ROLE_NOT_ACTIVE_AT_CURRENTTIME);
            }
        } else {
            if (userRoleList.size() > 1) {
                pageBean.setUserRoleList(userRoleList);
                getRoleTable().reset();
                return "switchrolepanel";
            }
            userRole = userRoleList.get(0);
        }

        return forwardToApplication(userRole);
    }

    private Table getRoleTable() throws UnifyException {
        return getPageWidgetByShortName(Table.class, "selectRolePanel.roleTablePanel.contentTbl");
    }
}
