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

import com.tcdng.jacklyn.common.annotation.CrudPanelList;
import com.tcdng.jacklyn.common.annotation.SessionLoading;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.workflow.entities.WfDoc;
import com.tcdng.jacklyn.workflow.entities.WfDocQuery;
import com.tcdng.jacklyn.workflow.web.beans.WfDocPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing workflow document definitions.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/workflow/wfdoc")
@UplBinding("web/workflow/upl/managewfdoc.upl")
@SessionLoading(crudPanelLists = {
        @CrudPanelList(panel = "frmWfDocFieldListPanel", property = "record.fieldList"),
        @CrudPanelList(panel = "frmWfDocClassifierListPanel", property = "record.classifierList"),
        @CrudPanelList(panel = "frmWfDocAttachmentListPanel", property = "record.attachmentList"),
        @CrudPanelList(panel = "frmWfDocBeanMappingListPanel", property = "record.beanMappingList")})
public class WfDocController extends AbstractWorkflowFormController<WfDocPageBean, WfDoc> {

    public WfDocController() {
        super(WfDocPageBean.class, WfDoc.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<WfDoc> find() throws UnifyException {
        WfDocPageBean pageBean = getPageBean();
        WfDocQuery query = new WfDocQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchWfCategoryId())) {
            query.wfCategoryId(pageBean.getSearchWfCategoryId());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.wfCategoryStatus(pageBean.getSearchStatus());
        }

        query.addOrder("description").ignoreEmptyCriteria(true);
        return getWorkflowService().findWfDocs(query);
    }

    @Override
    protected WfDoc find(Long wfDocId) throws UnifyException {
        return getWorkflowService().findWfDoc(wfDocId);
    }

    @Override
    protected WfDoc prepareCreate() throws UnifyException {
        return new WfDoc();
    }

    @Override
    protected void onPrepareView(WfDoc wfDocData, boolean onPaste) throws UnifyException {

    }

    @Override
    protected Object create(WfDoc wfDocData) throws UnifyException {
        return null;
    }

    @Override
    protected int update(WfDoc wfDocData) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(WfDoc record) throws UnifyException {
        return 0;
    }
}
