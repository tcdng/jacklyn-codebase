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
import com.tcdng.jacklyn.organization.entities.Branch;
import com.tcdng.jacklyn.organization.entities.BranchQuery;
import com.tcdng.jacklyn.organization.web.beans.BranchPageBean;
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
public class BranchController extends AbstractOrganizationFormController<BranchPageBean, Branch> {

    public BranchController() {
        super(BranchPageBean.class, Branch.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<Branch> find() throws UnifyException {
        BranchPageBean pageBean = getPageBean();
        BranchQuery query = new BranchQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchZoneId())) {
            query.zoneId(pageBean.getSearchZoneId());
        }

        if (QueryUtils.isValidLongCriteria(pageBean.getSearchStateId())) {
            query.stateId(pageBean.getSearchStateId());
        }

        if (QueryUtils.isValidLongCriteria(pageBean.getSearchHubId())) {
            query.hubId(pageBean.getSearchHubId());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchCode())) {
            query.code(pageBean.getSearchCode());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }

        query.excludeSysRecords();
        query.addOrder("description").ignoreEmptyCriteria(true);
        return getOrganizationService().findBranches(query);
    }

    @Override
    protected Branch find(Long id) throws UnifyException {
        return getOrganizationService().findBranch(id);
    }

    @Override
    protected Object create(Branch branch) throws UnifyException {
        return getOrganizationService().createBranch(branch);
    }

    @Override
    protected int update(Branch branch) throws UnifyException {
        return getOrganizationService().updateBranch(branch);
    }

    @Override
    protected int delete(Branch branch) throws UnifyException {
        return getOrganizationService().deleteBranch(branch.getId());
    }

}
