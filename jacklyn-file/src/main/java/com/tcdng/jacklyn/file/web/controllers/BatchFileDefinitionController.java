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
package com.tcdng.jacklyn.file.web.controllers;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.jacklyn.common.annotation.CrudPanelList;
import com.tcdng.jacklyn.common.annotation.SessionLoading;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.file.entities.BatchFileDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileDefinitionQuery;
import com.tcdng.jacklyn.file.entities.BatchFileFieldDefinition;
import com.tcdng.jacklyn.file.web.beans.BatchFileDefinitionPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * Controller for managing batch file definition records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/file/batchfiledefinition")
@UplBinding("web/file/upl/managebatchfiledefinition.upl")
@SessionLoading(
        crudPanelLists = { @CrudPanelList(panel = "frmBatchFileFieldDefPanel", property = "record.fieldDefList") })
@ResultMappings({
        @ResultMapping(name = "selectbeantomap", response = { "!showpopupresponse popup:$s{selectBeanToMapPopup}" }) })
public class BatchFileDefinitionController
        extends AbstractFileFormController<BatchFileDefinitionPageBean, BatchFileDefinition> {

    public BatchFileDefinitionController() {
        super(BatchFileDefinitionPageBean.class, BatchFileDefinition.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Action
    public String prepareMapBean() throws UnifyException {
        return "selectbeantomap";
    }

    @Action
    public String performMapBean() throws UnifyException {
        BatchFileDefinitionPageBean pageBean = getPageBean();
        BatchFileDefinition batchFileDefinition = pageBean.getRecord();
        List<BatchFileFieldDefinition> fieldDefList =
                getFileService().mergeBatchFileFieldMapping(pageBean.getBeanType(),
                        batchFileDefinition.getFieldDefList());
        batchFileDefinition.setFieldDefList(fieldDefList);
        return refreshCrudViewer();
    }

    @Override
    protected List<BatchFileDefinition> find() throws UnifyException {
        BatchFileDefinitionPageBean pageBean = getPageBean();
        BatchFileDefinitionQuery query = new BatchFileDefinitionQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }

        query.ignoreEmptyCriteria(true);
        return getFileService().findBatchFileDefinitions(query);
    }

    @Override
    protected BatchFileDefinition find(Long id) throws UnifyException {
        return getFileService().findBatchFileDefinition(id);
    }

    @Override
    protected Object create(BatchFileDefinition record) throws UnifyException {
        return getFileService().createBatchFileDefinition(record);
    }

    @Override
    protected int update(BatchFileDefinition record) throws UnifyException {
        return getFileService().updateBatchFileDefinition(record);
    }

    @Override
    protected int delete(BatchFileDefinition record) throws UnifyException {
        return getFileService().deleteBatchFileDefinition(record.getId());
    }

    @Override
    protected void onPrepareCreate(BatchFileDefinition batchFileDefinition) throws UnifyException {
        batchFileDefinition.setFieldDefList(new ArrayList<BatchFileFieldDefinition>());
    }

    @Override
    protected void onPrepareCrudViewer(BatchFileDefinition record, int mode) throws UnifyException {
        boolean isMapBean = mode == ManageRecordModifier.ADD || mode == ManageRecordModifier.MODIFY;
        setPageWidgetVisible("mapBeanBtn", isMapBean);
    }
}
