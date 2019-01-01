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
package com.tcdng.jacklyn.organization.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.organization.entities.Department;
import com.tcdng.jacklyn.organization.entities.DepartmentQuery;
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
public class DepartmentController extends AbstractOrganizationRecordController<Department> {

    private String searchName;

    private String searchDescription;

    public DepartmentController() {
        super(Department.class, "organization.department.hint", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
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

    @Override
    protected List<Department> find() throws UnifyException {
        DepartmentQuery query = new DepartmentQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.name(searchName);
        }

        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }

        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.order("description").ignoreEmptyCriteria(true);
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
