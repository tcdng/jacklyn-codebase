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
package com.tcdng.jacklyn.system.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.entities.BaseEntityQuery;
import com.tcdng.unify.core.task.TaskStatus;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Query class for scheduled task history.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ScheduledTaskHistQuery extends BaseEntityQuery<ScheduledTaskHist> {

    public ScheduledTaskHistQuery() {
        super(ScheduledTaskHist.class);
    }

    public ScheduledTaskHistQuery taskStatus(TaskStatus taskStatus) {
        return (ScheduledTaskHistQuery) addEquals("taskStatus", taskStatus);
    }

    public ScheduledTaskHistQuery taskName(String taskName) {
        return (ScheduledTaskHistQuery) addEquals("taskName", taskName);
    }

    public ScheduledTaskHistQuery scheduledTaskId(Long scheduledTaskId) {
        return (ScheduledTaskHistQuery) addEquals("scheduledTaskId", scheduledTaskId);
    }

    public ScheduledTaskHistQuery startedOn(Date date) {
        return (ScheduledTaskHistQuery) addBetween("startedOn", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }
}
