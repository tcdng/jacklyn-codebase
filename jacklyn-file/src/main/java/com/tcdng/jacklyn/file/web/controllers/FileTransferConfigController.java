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

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.file.constants.FileModuleAuditConstants;
import com.tcdng.jacklyn.file.constants.FileTransferTaskConstants;
import com.tcdng.jacklyn.file.entities.FileTransferConfig;
import com.tcdng.jacklyn.file.entities.FileTransferConfigQuery;
import com.tcdng.jacklyn.file.web.beans.FileTransferConfigPageBean;
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
public class FileTransferConfigController
        extends AbstractFileFormController<FileTransferConfigPageBean, FileTransferConfig> {

    public FileTransferConfigController() {
        super(FileTransferConfigPageBean.class, FileTransferConfig.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.CLIPBOARD
                        | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Action
    public String testFileTransferConfig() throws UnifyException {
        FileTransferConfigPageBean pageBean = getPageBean();
        TaskSetup taskSetup =
                TaskSetup.newBuilder().addTask(FileTransferTaskConstants.FILETRANSFERCONFIGTESTTASK)
                        .setParam(FileTransferTaskConstants.FILETRANSFERCONFIGDATA, pageBean.getRecord())
                        .logEvent(FileModuleAuditConstants.TEST_FILETRANSFERCONFIG, pageBean.getRecord().getName())
                        .logMessages().build();
        return launchTaskWithMonitorBox(taskSetup, "$m{file.filetransferconfig.test}");
    }

    @Override
    protected List<FileTransferConfig> find() throws UnifyException {
        FileTransferConfigPageBean pageBean = getPageBean();
        FileTransferConfigQuery query = new FileTransferConfigQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }
        if (pageBean.getSearchDirection() != null) {
            query.direction(pageBean.getSearchDirection());
        }
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
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
