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

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.file.data.BatchFileReadDefinitionLargeData;
import com.tcdng.jacklyn.file.entities.BatchFileReadDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileReadDefinitionQuery;
import com.tcdng.jacklyn.file.web.beans.BatchFileReadDefinitionPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for managing batch file read configuration record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/file/batchfilereaddefinition")
@UplBinding("web/file/upl/managebatchfilereaddefinition.upl")
public class BatchFileReadDefinitionController
        extends AbstractFileFormController<BatchFileReadDefinitionPageBean, BatchFileReadDefinition> {

    public BatchFileReadDefinitionController() {
        super(BatchFileReadDefinitionPageBean.class, BatchFileReadDefinition.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Action
    @Override
    public String copyRecord() throws UnifyException {
        BatchFileReadDefinitionPageBean pageBean = getPageBean();
        BatchFileReadDefinitionLargeData clipboardLargeData =
                ReflectUtils.shallowBeanCopy(pageBean.getClipboardLargeData());
        pageBean.setClipboardLargeData(clipboardLargeData);
        return super.copyRecord();
    }

    @Action
    public String refreshParameters() throws UnifyException {
        BatchFileReadDefinitionPageBean pageBean = getPageBean();
        onPrepareView(pageBean.getRecord(), false);
        return "refreshmain";
    }

    @Override
    protected List<BatchFileReadDefinition> find() throws UnifyException {
        BatchFileReadDefinitionPageBean pageBean = getPageBean();
        BatchFileReadDefinitionQuery query = new BatchFileReadDefinitionQuery();
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
        return getFileService().findBatchFileReadDefinitions(query);
    }

    @Override
    protected BatchFileReadDefinition find(Long id) throws UnifyException {
        BatchFileReadDefinitionPageBean pageBean = getPageBean();
        BatchFileReadDefinitionLargeData largeData = getFileService().findBatchFileReadDefinitionDocument(id);
        pageBean.setLargeData(largeData);
        return largeData.getData();
    }

    @Override
    protected Object create(BatchFileReadDefinition batchUploadConfig) throws UnifyException {
        BatchFileReadDefinitionPageBean pageBean = getPageBean();
        return getFileService().createBatchFileReadDefinition(pageBean.getLargeData());
    }

    @Override
    protected int update(BatchFileReadDefinition batchUploadConfig) throws UnifyException {
        BatchFileReadDefinitionPageBean pageBean = getPageBean();
        return getFileService().updateBatchFileReadDefinition(pageBean.getLargeData());
    }

    @Override
    protected int delete(BatchFileReadDefinition batchUploadConfig) throws UnifyException {
        return getFileService().deleteBatchFileReadDefinition(batchUploadConfig.getId());
    }

    @Override
    protected void onPrepareCreate(BatchFileReadDefinition batchFileReadDefinition) throws UnifyException {
        BatchFileReadDefinitionPageBean pageBean = getPageBean();
        pageBean.setLargeData(new BatchFileReadDefinitionLargeData(batchFileReadDefinition));
    }

    @Override
    protected void onPrepareView(BatchFileReadDefinition batchUploadConfig, boolean onPaste) throws UnifyException {
        BatchFileReadDefinitionPageBean pageBean = getPageBean();
        BatchFileReadDefinitionLargeData largeData = pageBean.getLargeData();
        if (onPaste) {
            largeData.setFileReaderParams(pageBean.getClipboardLargeData().getFileReaderParams());
        } else {
            largeData = getFileService().loadBatchFileReadConfigDocumentValues(largeData);
        }
    }

    @Override
    protected void onLoseView(BatchFileReadDefinition batchUploadConfig) throws UnifyException {
        BatchFileReadDefinitionPageBean pageBean = getPageBean();
        pageBean.setLargeData(new BatchFileReadDefinitionLargeData());
        pageBean.setClipboardLargeData(null);
    }
}
