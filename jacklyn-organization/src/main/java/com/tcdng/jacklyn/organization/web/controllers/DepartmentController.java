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
import com.tcdng.jacklyn.organization.entities.Department;
import com.tcdng.jacklyn.organization.entities.DepartmentQuery;
import com.tcdng.jacklyn.organization.web.beans.DepartmentPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing departments.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/organization/department")
@UplBinding("web/organization/upl/managedepartment.upl")
public class DepartmentController extends AbstractOrganizationFormController<DepartmentPageBean, Department> {

    public DepartmentController() {
        super(DepartmentPageBean.class, Department.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<Department> find() throws UnifyException {
        DepartmentPageBean pageBean = getPageBean();
        DepartmentQuery query = new DepartmentQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.name(pageBean.getSearchName());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getOrganizationService().findDepartments(query);
    }

    @Override
    protected Department find(Long id) throws UnifyException {
        return getOrganizationService().findDepartment(id);
    }

    @Override
    protected Department prepareCreate() throws UnifyException {
        return new Department();
    }

    @Override
    protected Object create(Department departmentData) throws UnifyException {
        return getOrganizationService().createDepartment(departmentData);
    }

    @Override
    protected int update(Department departmentData) throws UnifyException {
        return getOrganizationService().updateDepartment(departmentData);
    }

    @Override
    protected int delete(Department departmentData) throws UnifyException {
        return getOrganizationService().deleteDepartment(departmentData.getId());
    }

}
