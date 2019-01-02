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
import com.tcdng.jacklyn.organization.entities.Branch;
import com.tcdng.jacklyn.organization.entities.BranchQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing branches.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/organization/branch")
@UplBinding("web/organization/upl/managebranch.upl")
public class BranchController extends AbstractOrganizationCrudController<Branch> {

    private String searchName;

    private String searchDescription;

    public BranchController() {
        super(Branch.class, "$m{organization.branch.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
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
    protected List<Branch> find() throws UnifyException {
        BranchQuery query = new BranchQuery();
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
        return getOrganizationService().findBranches(query);
    }

    @Override
    protected Branch find(Long id) throws UnifyException {
        return getOrganizationService().findBranch(id);
    }

    @Override
    protected Branch prepareCreate() throws UnifyException {
        return new Branch();
    }

    @Override
    protected Object create(Branch branchData) throws UnifyException {
        return getOrganizationService().createBranch(branchData);
    }

    @Override
    protected int update(Branch branchData) throws UnifyException {
        return getOrganizationService().updateBranch(branchData);
    }

    @Override
    protected int delete(Branch branchData) throws UnifyException {
        return getOrganizationService().deleteBranch(branchData.getId());
    }

}
