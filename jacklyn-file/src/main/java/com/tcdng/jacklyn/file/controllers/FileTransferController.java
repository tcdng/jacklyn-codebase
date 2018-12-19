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

import java.util.Date;

import com.tcdng.jacklyn.file.constants.FileModuleAuditConstants;
import com.tcdng.jacklyn.file.constants.FileTransferTaskConstants;
import com.tcdng.jacklyn.file.entities.FileTransferConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for file transfer page used for initiating and monitoring a file
 * transfer.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/file/filetransfer")
@UplBinding("web/file/upl/filetransfer.upl")
public class FileTransferController extends AbstractFileController {

    private Long fileTransferConfigId;

    private Date workingDt;

    public FileTransferController() {
        super(true, false);
    }

    @Action
    public String startFileTransferTask() throws UnifyException {
        FileTransferConfig fileTransferConfigData = getFileService().findFileTransferConfig(fileTransferConfigId);
        TaskSetup taskSetup = TaskSetup.newBuilder().addTask(FileTransferTaskConstants.FILETRANSFERTASK)
                .setParam(FileTransferTaskConstants.FILETRANSFERCONFIGNAME, fileTransferConfigData.getName())
                .setParam(FileTransferTaskConstants.WORKINGDT, workingDt)
                .setParam(FileTransferTaskConstants.UPDATEFILEBOX, true)
                .logEvent(FileModuleAuditConstants.START_FILETRANSFERTASK, fileTransferConfigData.getName(),
                        String.valueOf(workingDt))
                .logMessages().build();
        return launchTaskWithMonitorBox(taskSetup, "file.filetransfer.execution");
    }

    public String getModeStyle() {
        return EventType.CREATE.colorMode();
    }

    public Long getFileTransferConfigId() {
        return fileTransferConfigId;
    }

    public void setFileTransferConfigId(Long fileTransferConfigId) {
        this.fileTransferConfigId = fileTransferConfigId;
    }

    public Date getWorkingDt() {
        return workingDt;
    }

    public void setWorkingDt(Date workingDt) {
        this.workingDt = workingDt;
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        if (workingDt == null) {
            workingDt = CalendarUtils.getCurrentMidnightDate();
        }
    }
}
