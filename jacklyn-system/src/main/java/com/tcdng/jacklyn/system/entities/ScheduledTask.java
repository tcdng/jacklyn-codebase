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

import java.util.Date;

import com.tcdng.jacklyn.common.annotation.Format;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedTimestampedStatusEntity;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.constant.FrequencyUnit;

/**
 * Entity for storing scheduled task information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Policy("scheduledtask-policy")
@Managed(module = SystemModuleNameConstants.SYSTEM_MODULE, title = "Scheduled Task", reportable = true,
        auditable = true)
@Table(name = "JKSCHEDTASK", uniqueConstraints = { @UniqueConstraint({ "description" }) })
public class ScheduledTask extends BaseVersionedTimestampedStatusEntity {

    @Column(name = "SCHEDTASK_DESC", length = 64)
    private String description;

    @Column(name = "TASK_NM")
    private String taskName;

    @Column(type = ColumnType.TIMESTAMP)
    private Date startTime;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date endTime;

    @Column(type = ColumnType.TIMESTAMP)
    private Date nextExecutionOn;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date lastExecutionOn;

    @Column(nullable = true)
    private Integer frequency;

    @Format(description = "$m{system.scheduledtask.frequencyunit}")
    @Column(nullable = true)
    private FrequencyUnit frequencyUnit;

    @Column(nullable = true)
    private String[] weekdays;

    @Column(nullable = true)
    private String[] days;

    @Column(nullable = true)
    private String[] months;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getNextExecutionOn() {
        return nextExecutionOn;
    }

    public void setNextExecutionOn(Date nextExecutionOn) {
        this.nextExecutionOn = nextExecutionOn;
    }

    public Date getLastExecutionOn() {
        return lastExecutionOn;
    }

    public void setLastExecutionOn(Date lastExecutionOn) {
        this.lastExecutionOn = lastExecutionOn;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public FrequencyUnit getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(FrequencyUnit frequencyUnit) {
        this.frequencyUnit = frequencyUnit;
    }

    public String[] getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(String[] weekdays) {
        this.weekdays = weekdays;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public String[] getMonths() {
        return months;
    }

    public void setMonths(String[] months) {
        this.months = months;
    }
}
