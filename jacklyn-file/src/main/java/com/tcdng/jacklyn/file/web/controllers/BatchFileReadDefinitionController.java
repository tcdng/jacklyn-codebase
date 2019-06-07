/*
 * Copyright 2018-2019 The Code Department.
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

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.file.data.BatchFileReadDefinitionLargeData;
import com.tcdng.jacklyn.file.entities.BatchFileReadDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileReadDefinitionQuery;
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
public class BatchFileReadDefinitionController extends AbstractFileCrudController<BatchFileReadDefinition> {

    private String searchName;

    private String searchDescription;

    private RecordStatus searchStatus;

    private BatchFileReadDefinitionLargeData largeData;

    private BatchFileReadDefinitionLargeData clipboardLargeData;

    public BatchFileReadDefinitionController() {
        super(BatchFileReadDefinition.class, "$m{file.batchfilereaddefinition.hint}",
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Action
    @Override
    public String copyRecord() throws UnifyException {
        clipboardLargeData = ReflectUtils.shallowBeanCopy(largeData);
        return super.copyRecord();
    }

    @Action
    public String refreshParameters() throws UnifyException {
        onPrepareView(getRecord(), false);
        return "refreshmain";
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

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public BatchFileReadDefinitionLargeData getLargeData() {
        return largeData;
    }

    public void setLargeData(BatchFileReadDefinitionLargeData largeData) {
        this.largeData = largeData;
    }

    @Override
    protected List<BatchFileReadDefinition> find() throws UnifyException {
        BatchFileReadDefinitionQuery query = new BatchFileReadDefinitionQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.nameLike(searchName);
        }
        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }
        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.ignoreEmptyCriteria(true);
        return getFileService().findBatchFileReadDefinitions(query);
    }

    @Override
    protected BatchFileReadDefinition find(Long id) throws UnifyException {
        largeData = getFileService().findBatchFileReadDefinitionDocument(id);
        return largeData.getData();
    }

    @Override
    protected BatchFileReadDefinition prepareCreate() throws UnifyException {
        largeData = new BatchFileReadDefinitionLargeData();
        return largeData.getData();
    }

    @Override
    protected Object create(BatchFileReadDefinition batchUploadConfig) throws UnifyException {
        return getFileService().createBatchFileReadDefinition(largeData);
    }

    @Override
    protected int update(BatchFileReadDefinition batchUploadConfig) throws UnifyException {
        return getFileService().updateBatchFileReadDefinition(largeData);
    }

    @Override
    protected int delete(BatchFileReadDefinition batchUploadConfig) throws UnifyException {
        return getFileService().deleteBatchFileReadDefinition(batchUploadConfig.getId());
    }

    @Override
    protected void onPrepareView(BatchFileReadDefinition batchUploadConfig, boolean onPaste) throws UnifyException {
        if (onPaste) {
            largeData.setFileReaderParams(clipboardLargeData.getFileReaderParams());
        } else {
            largeData = getFileService().loadBatchFileReadConfigDocumentValues(largeData);
        }
    }

    @Override
    protected void onLoseView(BatchFileReadDefinition batchUploadConfig) throws UnifyException {
        largeData = new BatchFileReadDefinitionLargeData();
        clipboardLargeData = null;
    }
}
