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
package com.tcdng.jacklyn.organization.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.organization.data.RoleLargeData;
import com.tcdng.jacklyn.organization.entities.Role;
import com.tcdng.jacklyn.organization.entities.RoleQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for managing roles.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/organization/role")
@UplBinding("web/organization/upl/managerole.upl")
public class RoleController extends AbstractOrganizationCrudController<Role> {

    private String searchName;

    private String searchDescription;

    private Long searchDepartmentId;
    
    private RecordStatus searchStatus;

    private RoleLargeData largeData;

    private RoleLargeData clipboardLargeData;

    public RoleController() {
        super(Role.class, "$m{organization.role.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
        largeData = new RoleLargeData();
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public Long getSearchDepartmentId() {
        return searchDepartmentId;
    }

    public void setSearchDepartmentId(Long searchDepartmentId) {
        this.searchDepartmentId = searchDepartmentId;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public RoleLargeData getLargeData() {
        return largeData;
    }

    public void setLargeData(RoleLargeData largeData) {
        this.largeData = largeData;
    }

    @Override
    protected List<Role> find() throws UnifyException {
        RoleQuery query = new RoleQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.nameLike(searchName);
        }
        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }
        if (QueryUtils.isValidLongCriteria(searchDepartmentId)) {
            query.departmentId(searchDepartmentId);
        }
        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getOrganizationService().findRoles(query);
    }

    @Override
    protected Role find(Long id) throws UnifyException {
        largeData = getOrganizationService().findRoleForm(id);
        return largeData.getData();
    }

    @Override
    protected Role prepareCreate() throws UnifyException {
        largeData = new RoleLargeData();
        return largeData.getData();
    }

    @Override
    protected void onPrepareView(Role roleData, boolean onPaste) throws UnifyException {
        if (onPaste) {
            largeData.setPrivilegeIdList(clipboardLargeData.getPrivilegeIdList());
            largeData.setWfStepIdList(clipboardLargeData.getWfStepIdList());
        }
    }

    @Override
    @Action
    public String copyRecord() throws UnifyException {
        clipboardLargeData = ReflectUtils.shallowBeanCopy(largeData);
        return super.copyRecord();
    }

    @Override
    protected void onLoseView(Role roleData) throws UnifyException {
        largeData = new RoleLargeData();
        clipboardLargeData = null;
    }

    @Override
    protected Object create(Role roleData) throws UnifyException {
        return getOrganizationService().createRole(largeData);
    }

    @Override
    protected int update(Role roleData) throws UnifyException {
        return getOrganizationService().updateRole(largeData);
    }

    @Override
    protected int delete(Role roleData) throws UnifyException {
        return getOrganizationService().deleteRole(roleData.getId());
    }
}
