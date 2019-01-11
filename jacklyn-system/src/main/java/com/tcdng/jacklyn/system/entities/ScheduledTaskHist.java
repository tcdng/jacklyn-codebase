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
package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.annotation.Format;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseTimestampedEntity;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.task.TaskStatus;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity for storing scheduled task history information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SystemModuleNameConstants.SYSTEM_MODULE, title = "Scheduled Task History", reportable = true,
        auditable = true)
@Table("SCHEDTASKHIST")
public class ScheduledTaskHist extends BaseTimestampedEntity {

    @ForeignKey(ScheduledTask.class)
    private Long scheduledTaskId;

    @Column(length = 256, nullable = true)
    private String errorMsg;

    @Column
    private TaskStatus taskStatus;

    @Format(description = "$m{system.scheduledtaskhist.scheduledtask}")
    @ListOnly(key = "scheduledTaskId", property = "description")
    private String scheduledTaskDesc;

    @ListOnly(key = "scheduledTaskId", property = "taskName")
    private String taskName;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(scheduledTaskDesc, " - timestamp:", getCreateDt(), "- status: ",
                this.taskStatus);
    }

    public Long getScheduledTaskId() {
        return scheduledTaskId;
    }

    public void setScheduledTaskId(Long scheduledTaskId) {
        this.scheduledTaskId = scheduledTaskId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getScheduledTaskDesc() {
        return scheduledTaskDesc;
    }

    public void setScheduledTaskDesc(String scheduledTaskDesc) {
        this.scheduledTaskDesc = scheduledTaskDesc;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
