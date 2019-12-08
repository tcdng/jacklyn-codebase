/*
 * Copyright 2018-2019 The Code Department.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * UnaddLessThan required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.jacklyn.system.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseVersionedTimestampedStatusEntityQuery;

/**
 * Query class for scheduled tasks.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ScheduledTaskQuery extends BaseVersionedTimestampedStatusEntityQuery<ScheduledTask> {

    public ScheduledTaskQuery() {
        super(ScheduledTask.class);
    }

    public ScheduledTaskQuery descriptionLike(String description) {
        return (ScheduledTaskQuery) addLike("description", description);
    }

    public ScheduledTaskQuery taskName(String taskName) {
        return (ScheduledTaskQuery) addEquals("taskName", taskName);
    }

    public ScheduledTaskQuery readyToRunOn(Date date) {
        return (ScheduledTaskQuery) addLessThanEqual("nextExecutionOn", date).addEquals("status", RecordStatus.ACTIVE);
    }
}
