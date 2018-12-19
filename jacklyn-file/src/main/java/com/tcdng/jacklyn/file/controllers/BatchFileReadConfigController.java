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
package com.tcdng.jacklyn.file.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.file.data.BatchFileReadConfigLargeData;
import com.tcdng.jacklyn.file.entities.BatchFileReadConfig;
import com.tcdng.jacklyn.file.entities.BatchFileReadConfigQuery;
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
@Component("/file/batchfilereadconfig")
@UplBinding("web/file/upl/managebatchfilereadconfig.upl")
public class BatchFileReadConfigController extends AbstractFileRecordController<BatchFileReadConfig> {

    private String searchName;

    private String searchDescription;

    private RecordStatus searchStatus;

    private BatchFileReadConfigLargeData formBean;

    private BatchFileReadConfigLargeData clipboardFormBean;

    public BatchFileReadConfigController() {
        super(BatchFileReadConfig.class, "file.batchfilereadconfig.hint",
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Action
    @Override
    public String copyRecord() throws UnifyException {
        clipboardFormBean = ReflectUtils.shallowBeanCopy(formBean);
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

    public BatchFileReadConfigLargeData getFormBean() {
        return formBean;
    }

    public void setFormBean(BatchFileReadConfigLargeData formBean) {
        this.formBean = formBean;
    }

    @Override
    protected List<BatchFileReadConfig> find() throws UnifyException {
        BatchFileReadConfigQuery query = new BatchFileReadConfigQuery();
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
        return getFileService().findBatchFileReadConfigs(query);
    }

    @Override
    protected BatchFileReadConfig find(Long id) throws UnifyException {
        formBean = getFileService().findBatchFileReadConfigDocument(id);
        return formBean.getData();
    }

    @Override
    protected BatchFileReadConfig prepareCreate() throws UnifyException {
        formBean = new BatchFileReadConfigLargeData();
        return formBean.getData();
    }

    @Override
    protected Object create(BatchFileReadConfig batchUploadConfig) throws UnifyException {
        return getFileService().createBatchFileReadConfig(formBean);
    }

    @Override
    protected int update(BatchFileReadConfig batchUploadConfig) throws UnifyException {
        return getFileService().updateBatchFileReadConfig(formBean);
    }

    @Override
    protected int delete(BatchFileReadConfig batchUploadConfig) throws UnifyException {
        return getFileService().deleteBatchFileReadConfig(batchUploadConfig.getId());
    }

    @Override
    protected void onPrepareView(BatchFileReadConfig batchUploadConfig, boolean onPaste) throws UnifyException {
        if (onPaste) {
            formBean.setFileReaderParams(clipboardFormBean.getFileReaderParams());
            ;
        } else {
            formBean = getFileService().loadBatchFileReadConfigDocumentValues(formBean);
        }
    }

    @Override
    protected void onLoseView(BatchFileReadConfig batchUploadConfig) throws UnifyException {
        formBean = new BatchFileReadConfigLargeData();
        clipboardFormBean = null;
    }
}
