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

import com.tcdng.jacklyn.file.constants.FileModuleAuditConstants;
import com.tcdng.jacklyn.file.constants.FileTransferTaskConstants;
import com.tcdng.jacklyn.file.entities.FileTransferConfig;
import com.tcdng.jacklyn.file.web.beans.FileTransferPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Controller for file transfer page used for initiating and monitoring a file
 * transfer.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/file/filetransfer")
@UplBinding("web/file/upl/filetransfer.upl")
public class FileTransferController extends AbstractFilePageController<FileTransferPageBean> {

    public FileTransferController() {
        super(FileTransferPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String startFileTransferTask() throws UnifyException {
        FileTransferPageBean pageBean = getPageBean();
        FileTransferConfig fileTransferConfig =
                getFileService().findFileTransferConfig(pageBean.getFileTransferConfigId());
        TaskSetup taskSetup =
                TaskSetup.newBuilder().addTask(FileTransferTaskConstants.FILETRANSFERTASK)
                        .setParam(FileTransferTaskConstants.FILETRANSFERCONFIGNAME, fileTransferConfig.getName())
                        .setParam(FileTransferTaskConstants.WORKINGDT, pageBean.getWorkingDt())
                        .setParam(FileTransferTaskConstants.UPDATEFILEBOX, true)
                        .logEvent(FileModuleAuditConstants.START_FILETRANSFERTASK, fileTransferConfig.getName(),
                                String.valueOf(pageBean.getWorkingDt()))
                        .logMessages().build();
        return launchTaskWithMonitorBox(taskSetup, "$m{file.filetransfer.execution}");
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        FileTransferPageBean pageBean = getPageBean();
        if (pageBean.getWorkingDt() == null) {
            pageBean.setWorkingDt(getFileService().getToday());
        }
    }
}
