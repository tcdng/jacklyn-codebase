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
package com.tcdng.jacklyn.workflow.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.annotation.CrudPanelList;
import com.tcdng.jacklyn.common.annotation.SessionLoading;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.workflow.entities.WfDoc;
import com.tcdng.jacklyn.workflow.entities.WfDocQuery;
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
        @CrudPanelList(panel = "frmWfDocFieldListPanel", field = "record.fieldList"),
        @CrudPanelList(panel = "frmWfDocClassifierListPanel", field = "record.classifierList"),
        @CrudPanelList(panel = "frmWfDocAttachmentListPanel", field = "record.attachmentList"),
        @CrudPanelList(panel = "frmWfDocBeanMappingListPanel", field = "record.beanMappingList")})
public class WfDocController extends AbstractWorkflowRecordController<WfDoc> {

    private Long searchWfCategoryId;

    private String searchName;

    private String searchDescription;

    public WfDocController() {
        super(WfDoc.class, "workflow.wfdoc.hint",
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    public Long getSearchWfCategoryId() {
        return searchWfCategoryId;
    }

    public void setSearchWfCategoryId(Long searchWfCategoryId) {
        this.searchWfCategoryId = searchWfCategoryId;
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
    protected List<WfDoc> find() throws UnifyException {
        WfDocQuery query = new WfDocQuery();
        if (QueryUtils.isValidLongCriteria(searchWfCategoryId)) {
            query.wfCategoryId(searchWfCategoryId);
        }

        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.nameLike(searchName);
        }

        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }

        query.order("description").ignoreEmptyCriteria(true);
        return getWorkflowModule().findWfDocs(query);
    }

    @Override
    protected WfDoc find(Long wfDocId) throws UnifyException {
        return getWorkflowModule().findWfDoc(wfDocId);
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
