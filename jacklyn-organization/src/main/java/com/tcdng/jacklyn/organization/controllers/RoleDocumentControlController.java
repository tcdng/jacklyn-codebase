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
package com.tcdng.jacklyn.organization.controllers;

import java.util.Collections;
import java.util.List;

import com.tcdng.jacklyn.organization.entities.RolePrivilege;
import com.tcdng.jacklyn.organization.entities.RolePrivilegeWidget;
import com.tcdng.jacklyn.organization.entities.RolePrivilegeWidgetQuery;
import com.tcdng.jacklyn.shared.organization.PrivilegeCategoryConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * Controller for managing role document control privileges.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/organization/roledocumentcontrol")
@UplBinding("web/organization/upl/manageroledocumentcontrol.upl")
@ResultMappings({ @ResultMapping(name = "refreshtable",
        response = { "!refreshpanelresponse panels:$l{tablePanel actionPanel}" }) })
public class RoleDocumentControlController extends AbstractOrganizationController {

    private Long searchRoleId;

    private Long searchModuleId;

    private List<RolePrivilegeWidget> rolePrivilegeWidgetList;

    public RoleDocumentControlController() {
        super(true, false);
    }

    @Action
    public String findPrivileges() throws UnifyException {
        doFindPrivileges();
        logUserEvent(EventType.SEARCH, RolePrivilege.class);
        return "refreshtable";
    }

    @Action
    public String savePrivileges() throws UnifyException {
        getOrganizationService().updateRoleDocumentControls(rolePrivilegeWidgetList);
        hintUser("$m{hint.organization.roleprivilege.saved}");
        doFindPrivileges();
        return "refreshtable";
    }

    public Long getSearchRoleId() {
        return searchRoleId;
    }

    public void setSearchRoleId(Long searchRoleId) {
        this.searchRoleId = searchRoleId;
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
    }

    public List<RolePrivilegeWidget> getRolePrivilegeWidgetList() {
        return rolePrivilegeWidgetList;
    }

    public void setRolePrivilegeWidgetList(List<RolePrivilegeWidget> rolePrivilegeWidgetList) {
        this.rolePrivilegeWidgetList = rolePrivilegeWidgetList;
    }

    @Override
    protected void onIndexPage() throws UnifyException {
        super.onIndexPage();
        searchRoleId = null;
        searchModuleId = null;
        rolePrivilegeWidgetList = Collections.emptyList();
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        doFindPrivileges();
    }

    @Override
    protected void onClosePage() throws UnifyException {
        searchRoleId = null;
        searchModuleId = null;
        rolePrivilegeWidgetList = null;
    }

    @Override
    protected String getDocViewPanelName() {
        return "searchBodyPanel";
    }

    protected void doFindPrivileges() throws UnifyException {
        if (QueryUtils.isValidLongCriteria(searchRoleId) && QueryUtils.isValidLongCriteria(searchModuleId)) {
            RolePrivilegeWidgetQuery query = new RolePrivilegeWidgetQuery();
            query.roleId(searchRoleId);
            query.moduleId(searchModuleId);
            query.categoryName(PrivilegeCategoryConstants.DOCUMENTCONTROL);
            rolePrivilegeWidgetList = getOrganizationService().findRoleDocumentControls(query);
        } else {
            rolePrivilegeWidgetList = Collections.emptyList();
        }
    }
}
