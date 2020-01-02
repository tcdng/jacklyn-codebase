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
package com.tcdng.jacklyn.organization.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.organization.data.RoleLargeData;
import com.tcdng.jacklyn.organization.entities.Role;
import com.tcdng.jacklyn.organization.entities.RoleQuery;
import com.tcdng.jacklyn.organization.web.beans.RolePageBean;
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
public class RoleController extends AbstractOrganizationFormController<RolePageBean, Role> {

    public RoleController() {
        super(RolePageBean.class, Role.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    @Action
    public String copyRecord() throws UnifyException {
        RolePageBean pageBean = getPageBean();
        RoleLargeData clipboardLargeData = ReflectUtils.shallowBeanCopy(pageBean.getLargeData());
        pageBean.setClipboardLargeData(clipboardLargeData);
        return super.copyRecord();
    }

    @Override
    protected List<Role> find() throws UnifyException {
        RolePageBean pageBean = getPageBean();
        RoleQuery query = new RoleQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchDepartmentId())) {
            query.departmentId(pageBean.getSearchDepartmentId());
        }
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getOrganizationService().findRoles(query);
    }

    @Override
    protected Role find(Long id) throws UnifyException {
        RolePageBean pageBean = getPageBean();
        RoleLargeData largeData = getOrganizationService().findRoleForm(id);
        pageBean.setLargeData(largeData);
        return largeData.getData();
    }

    @Override
    protected Role prepareCreate() throws UnifyException {
        RolePageBean pageBean = getPageBean();
        RoleLargeData largeData = new RoleLargeData();
        pageBean.setLargeData(largeData);
        return largeData.getData();
    }

    @Override
    protected void onPrepareView(Role roleData, boolean onPaste) throws UnifyException {
        RolePageBean pageBean = getPageBean();
        RoleLargeData largeData = pageBean.getLargeData();
        if (onPaste) {
            RoleLargeData clipboardLargeData = pageBean.getClipboardLargeData();
            largeData.setPrivilegeIdList(clipboardLargeData.getPrivilegeIdList());
            largeData.setWfStepIdList(clipboardLargeData.getWfStepIdList());
        }
    }

    @Override
    protected void onLoseView(Role roleData) throws UnifyException {
        RolePageBean pageBean = getPageBean();
        pageBean.setLargeData(new RoleLargeData());
        pageBean.setClipboardLargeData(null);
    }

    @Override
    protected Object create(Role roleData) throws UnifyException {
        RolePageBean pageBean = getPageBean();
        return getOrganizationService().createRole(pageBean.getLargeData());
    }

    @Override
    protected int update(Role roleData) throws UnifyException {
        RolePageBean pageBean = getPageBean();
        return getOrganizationService().updateRole(pageBean.getLargeData());
    }

    @Override
    protected int delete(Role roleData) throws UnifyException {
        return getOrganizationService().deleteRole(roleData.getId());
    }
}
