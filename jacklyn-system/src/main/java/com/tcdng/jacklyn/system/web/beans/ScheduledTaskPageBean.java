/*
 * Copyright 2018-2019 The Code Department
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

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.system.data.ScheduledTaskLargeData;
import com.tcdng.jacklyn.system.entities.ScheduledTask;

/**
 * Scheduled task entity page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ScheduledTaskPageBean extends BaseEntityPageBean<ScheduledTask> {

    private String searchTaskName;

    private String searchDescription;

    private RecordStatus searchStatus;
    
    private ScheduledTaskLargeData largeData;

    private ScheduledTaskLargeData clipboardLargeData;

    public ScheduledTaskPageBean() {
        super("$m{system.scheduledtask.hint}");
    }

    public String getSearchTaskName() {
        return searchTaskName;
    }

    public void setSearchTaskName(String searchTaskName) {
        this.searchTaskName = searchTaskName;
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

    public ScheduledTaskLargeData getLargeData() {
        return largeData;
    }

    public void setLargeData(ScheduledTaskLargeData largeData) {
        this.largeData = largeData;
    }

    public ScheduledTaskLargeData getClipboardLargeData() {
        return clipboardLargeData;
    }

    public void setClipboardLargeData(ScheduledTaskLargeData clipboardLargeData) {
        this.clipboardLargeData = clipboardLargeData;
    }

}