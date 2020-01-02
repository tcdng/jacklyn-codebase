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
package com.tcdng.jacklyn.workflow.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.workflow.entities.WfCategory;
import com.tcdng.jacklyn.workflow.entities.WfCategoryQuery;
import com.tcdng.jacklyn.workflow.web.beans.WfCategoryPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing workflow categories.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/workflow/wfcategory")
@UplBinding("web/workflow/upl/managewfcategory.upl")
public class WfCategoryController extends AbstractWorkflowFormController<WfCategoryPageBean, WfCategory> {

    public WfCategoryController() {
        super(WfCategoryPageBean.class, WfCategory.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<WfCategory> find() throws UnifyException {
        WfCategoryPageBean pageBean = getPageBean();
        WfCategoryQuery query = new WfCategoryQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }

        query.addOrder("description").ignoreEmptyCriteria(true);
        return getWorkflowService().findWfCategories(query);
    }

    @Override
    protected WfCategory find(Long wfCategoryId) throws UnifyException {
        return getWorkflowService().findWfCategory(wfCategoryId);
    }

    @Override
    protected WfCategory prepareCreate() throws UnifyException {
        return new WfCategory();
    }

    @Override
    protected Object create(WfCategory wfCategory) throws UnifyException {
        return null;
    }

    @Override
    protected int update(WfCategory wfCategory) throws UnifyException {
        return getWorkflowService().updateWfCategory(wfCategory);
    }

    @Override
    protected int delete(WfCategory wfCategory) throws UnifyException {
        return 0;
    }

}
