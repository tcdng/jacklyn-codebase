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
package com.tcdng.jacklyn.security.controllers;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.tcdng.jacklyn.common.business.SystemNotificationProvider;
import com.tcdng.jacklyn.common.constants.JacklynSessionAttributeConstants;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.security.constants.SecurityModuleAuditConstants;
import com.tcdng.jacklyn.security.data.UserRoleOptions;
import com.tcdng.jacklyn.security.entities.UserRole;
import com.tcdng.jacklyn.security.entities.UserRoleQuery;
import com.tcdng.jacklyn.shared.security.PrivilegeCategoryConstants;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.entities.ShortcutTileQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.resource.ImageGenerator;
import com.tcdng.unify.core.ui.Tile;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.annotation.WebApplication;
import com.tcdng.unify.web.ui.control.Table;

/**
 * Page controller for application document.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@WebApplication("$m{default.application}")
@Component("/application")
@UplBinding("web/security/upl/application.upl")
@ResultMappings({
        @ResultMapping(name = "forwardtohome", response = { "!forwardresponse path:$x{application.web.home}" }),
        @ResultMapping(
                name = "showuserroleoptions", response = { "!showpopupresponse popup:$s{userRoleOptionsPopup}" }),
        @ResultMapping(name = "showuserdetails", response = { "!showpopupresponse popup:$s{userDetailsPopup}" }) })
public class ApplicationController extends AbstractApplicationForwarderController {

    @Configurable
    private SystemNotificationProvider systemNotificationProvider;

    @Configurable
    private SystemService systemService;

    @Configurable("userphoto-generator")
    private ImageGenerator userPhotoGenerator;

    private Table selectRoleTableState;

    public ApplicationController() {
        super(true, false);
    }

    public int getAlertCount() throws UnifyException {
        return systemNotificationProvider.countSystemNotifications(getUserToken().getUserLoginId());
    }

    public ImageGenerator getUserPhotoGenerator() {
        return userPhotoGenerator;
    }

    @Action
    public String logOut() throws UnifyException {
        logUserEvent(SecurityModuleAuditConstants.LOGOUT);
        getSecurityService().logout(true);
        return "forwardtohome";
    }

    @Action
    public String prepareUserRoleOptions() throws UnifyException {
        // Get user roles that are active based on current time
        UserToken userToken = getUserToken();

        UserRoleQuery query = new UserRoleQuery();
        String roleName = (String) userToken.getRoleCode();
        if (QueryUtils.isValidStringCriteria(roleName)) {
            query.roleNameNot(roleName);
        }
        query.userLoginId(userToken.getUserLoginId());
        query.roleStatus(RecordStatus.ACTIVE);
        query.roleActiveTime(new Date());
        List<UserRole> userRoleList = getSecurityService().findUserRoles(query);
        UserRoleOptions userRoleOptions = new UserRoleOptions();
        userRoleOptions.setUserRoleList(userRoleList);
        setSessionAttribute(JacklynSessionAttributeConstants.USERROLEOPTIONS, userRoleOptions);
        return "showuserroleoptions";
    }

    @Action
    public String showUserDetails() throws UnifyException {
        return "showuserdetails";
    }

    @Action
    public String switchUserRole() throws UnifyException {
        UserRoleOptions userRoleOptions =
                (UserRoleOptions) getSessionAttribute(JacklynSessionAttributeConstants.USERROLEOPTIONS);
        UserRole userRoleData = userRoleOptions.getUserRoleList().get(selectRoleTableState.getViewIndex());
        return forwardToApplication(userRoleData);
    }

    @Override
    protected void onIndexPage() throws UnifyException {
        super.onIndexPage();

        List<Tile> tileList = Collections.emptyList();
        ShortcutTileQuery query = new ShortcutTileQuery().orderByDisplayOrder();
        if (getUserToken().isReservedUser()) {
            query.ignoreEmptyCriteria(true);
            tileList = systemService.generateTiles(query);
        } else {
            Set<String> shortcutNames = getPrivilegeCodes(PrivilegeCategoryConstants.SHORTCUT);
            if (!shortcutNames.isEmpty()) {
                query.nameIn(shortcutNames);
                tileList = systemService.generateTiles(query);
            }
        }

        setSessionAttribute(JacklynSessionAttributeConstants.SHORTCUTDECK, tileList);
    }

    @Override
    protected void onSetPage() throws UnifyException {
        selectRoleTableState = getPageWidgetByShortName(Table.class, "userRoleOptionsPopup.roleTablePanel.contentTbl");
    }
}
