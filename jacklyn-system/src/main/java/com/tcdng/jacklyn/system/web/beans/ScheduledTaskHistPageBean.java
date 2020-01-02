/*
 * Copyright 2018-2020 The Code Department
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

package com.tcdng.jacklyn.system.web.beans;

import java.util.Date;

import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHist;
import com.tcdng.unify.core.task.TaskStatus;

/**
 * Scheduled task history entity page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ScheduledTaskHistPageBean extends BaseEntityPageBean<ScheduledTaskHist> {

    private Date searchExecutionDt;

    private Long searchScheduledTaskId;

    private TaskStatus searchStatus;

    public ScheduledTaskHistPageBean() {
        super("$m{system.scheduledtaskhist.hint}");
    }

    public Date getSearchExecutionDt() {
        return searchExecutionDt;
    }

    public void setSearchExecutionDt(Date searchExecutionDt) {
        this.searchExecutionDt = searchExecutionDt;
    }

    public Long getSearchScheduledTaskId() {
        return searchScheduledTaskId;
    }

    public void setSearchScheduledTaskId(Long searchScheduledTaskId) {
        this.searchScheduledTaskId = searchScheduledTaskId;
    }

    public TaskStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(TaskStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

}
