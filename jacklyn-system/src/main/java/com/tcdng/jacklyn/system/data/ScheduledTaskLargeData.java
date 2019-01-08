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
package com.tcdng.jacklyn.system.data;

import com.tcdng.jacklyn.common.entities.BaseLargeData;
import com.tcdng.jacklyn.system.entities.ScheduledTask;
import com.tcdng.unify.core.data.Inputs;

/**
 * Scheduled task large data.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ScheduledTaskLargeData extends BaseLargeData<ScheduledTask> {

    private Inputs scheduledTaskParams;

    public ScheduledTaskLargeData() {
        super(new ScheduledTask());
    }

    public ScheduledTaskLargeData(ScheduledTask scheduledTaskData) {
        super(scheduledTaskData);
    }

    public ScheduledTaskLargeData(ScheduledTask scheduledTaskData, Inputs scheduledTaskParams) {
        super(scheduledTaskData);
        this.scheduledTaskParams = scheduledTaskParams;
    }

    public Inputs getScheduledTaskParams() {
        return scheduledTaskParams;
    }

    public void setScheduledTaskParams(Inputs scheduledTaskParams) {
        this.scheduledTaskParams = scheduledTaskParams;
    }
}
