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
package com.tcdng.jacklyn.file.controllers;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.jacklyn.common.annotation.CrudPanelList;
import com.tcdng.jacklyn.common.annotation.SessionLoading;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.file.entities.BatchFileDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileDefinitionQuery;
import com.tcdng.jacklyn.file.entities.BatchFileFieldDefinition;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing batch file definition records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/file/batchfiledefinition")
@UplBinding("web/file/upl/managebatchfiledefinition.upl")
@SessionLoading(crudPanelLists = { @CrudPanelList(panel = "frmBatchFileFieldDefPanel", property = "record.fieldDefList") })
public class BatchFileDefinitionController extends AbstractFileCrudController<BatchFileDefinition> {

    private String searchName;

    private String searchDescription;

    private RecordStatus searchStatus;

    public BatchFileDefinitionController() {
        super(BatchFileDefinition.class, "$m{file.batchfiledefinition.hint}",
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
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

    @Override
    protected List<BatchFileDefinition> find() throws UnifyException {
        BatchFileDefinitionQuery query = new BatchFileDefinitionQuery();
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
        return getFileService().findBatchFileDefinitions(query);
    }

    @Override
    protected BatchFileDefinition find(Long id) throws UnifyException {
        return getFileService().findBatchFileDefinition(id);
    }

    @Override
    protected BatchFileDefinition prepareCreate() throws UnifyException {
        BatchFileDefinition batchFileDefinition = new BatchFileDefinition();
        batchFileDefinition.setFieldDefList(new ArrayList<BatchFileFieldDefinition>());
        return batchFileDefinition;
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
}
