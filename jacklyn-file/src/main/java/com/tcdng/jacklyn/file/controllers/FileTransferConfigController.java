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
import com.tcdng.jacklyn.file.constants.FileModuleAuditConstants;
import com.tcdng.jacklyn.file.constants.FileTransferTaskConstants;
import com.tcdng.jacklyn.file.entities.FileTransferConfig;
import com.tcdng.jacklyn.file.entities.FileTransferConfigQuery;
import com.tcdng.jacklyn.shared.file.FileTransferDirection;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for managing file transfer configuration record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/file/filetransferconfig")
@UplBinding("web/file/upl/managefiletransferconfig.upl")
public class FileTransferConfigController extends AbstractFileCrudController<FileTransferConfig> {

    private String searchName;

    private FileTransferDirection searchDirection;

    private RecordStatus searchStatus;

    public FileTransferConfigController() {
        super(FileTransferConfig.class, "$m{file.filetransferconfig.hint}",
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Action
    public String testFileTransferConfig() throws UnifyException {
        TaskSetup taskSetup = TaskSetup.newBuilder().addTask(FileTransferTaskConstants.FILETRANSFERCONFIGTESTTASK)
                .setParam(FileTransferTaskConstants.FILETRANSFERCONFIGDATA, getRecord())
                .logEvent(FileModuleAuditConstants.TEST_FILETRANSFERCONFIG, getRecord().getName()).logMessages()
                .build();
        return launchTaskWithMonitorBox(taskSetup, "$m{file.filetransferconfig.test}");
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public FileTransferDirection getSearchDirection() {
        return searchDirection;
    }

    public void setSearchDirection(FileTransferDirection searchDirection) {
        this.searchDirection = searchDirection;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    @Override
    protected List<FileTransferConfig> find() throws UnifyException {
        FileTransferConfigQuery query = new FileTransferConfigQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.nameLike(searchName);
        }
        if (getSearchDirection() != null) {
            query.direction(searchDirection);
        }
        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.ignoreEmptyCriteria(true);
        return getFileService().findFileTransferConfigs(query);
    }

    @Override
    protected FileTransferConfig find(Long id) throws UnifyException {
        return getFileService().findFileTransferConfig(id);
    }

    @Override
    protected FileTransferConfig prepareCreate() throws UnifyException {
        return new FileTransferConfig();
    }

    @Override
    protected Object create(FileTransferConfig fileTransferConfigData) throws UnifyException {
        return getFileService().createFileTransferConfig(fileTransferConfigData);
    }

    @Override
    protected int update(FileTransferConfig fileTransferConfigData) throws UnifyException {
        return getFileService().updateFileTransferConfig(fileTransferConfigData);
    }

    @Override
    protected int delete(FileTransferConfig fileTransferConfigData) throws UnifyException {
        return getFileService().deleteFileTransferConfig(fileTransferConfigData.getId());
    }
}
