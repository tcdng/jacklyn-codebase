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

package com.tcdng.jacklyn.system.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.entities.BaseVersionedTimestampedStatusEntityPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Scheduled task policy.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("scheduledtask-policy")
public class ScheduledTaskPolicy extends BaseVersionedTimestampedStatusEntityPolicy {

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        ScheduledTask scheduledTask = (ScheduledTask) record;
        scheduledTask.setNextExecutionOn(getNextExecutionOn(scheduledTask, now));
        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        ScheduledTask scheduledTask = (ScheduledTask) record;
        scheduledTask.setNextExecutionOn(getNextExecutionOn(scheduledTask, now));
        super.preUpdate(record, now);
    }

    private Date getNextExecutionOn(ScheduledTask scheduledTask, Date now) throws UnifyException {
        Date nextExecutionOn = CalendarUtils.getMidnightDate(now);
        if (!CalendarUtils.isWithinCalendar(scheduledTask.getWeekdays(), scheduledTask.getDays(),
                scheduledTask.getMonths(), nextExecutionOn)) {
            nextExecutionOn =
                    CalendarUtils.getNextEligibleDate(scheduledTask.getWeekdays(), scheduledTask.getDays(),
                            scheduledTask.getMonths(), nextExecutionOn);
        }

        return CalendarUtils.getDateWithOffset(nextExecutionOn,
                CalendarUtils.getTimeOfDayOffset(scheduledTask.getStartTime()));
    }
}
