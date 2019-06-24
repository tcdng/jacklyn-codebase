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

package com.tcdng.jacklyn.system.data;

import java.util.List;

import com.tcdng.unify.core.data.Input;

/**
 * Schedule task runtime info.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ScheduledTaskDef {

    private String lock;

    private String description;

    private String taskName;

    private long startOffset;

    private long endOffset;

    private long repeatMillSecs;

    private String[] weekdays;

    private String[] days;

    private String[] months;

    private List<Input> inputList;

    private long timestamp;

    public ScheduledTaskDef(String lock, String description, String taskName, long startOffset, long endOffset,
            long repeatMillSecs, String[] weekdays, String[] days, String[] months, List<Input> inputList,
            long timestamp) {
        this.lock = lock;
        this.description = description;
        this.taskName = taskName;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.repeatMillSecs = repeatMillSecs;
        this.weekdays = weekdays;
        this.days = days;
        this.months = months;
        this.inputList = inputList;
        this.timestamp = timestamp;
    }

    public String getLock() {
        return lock;
    }

    public String getDescription() {
        return description;
    }

    public String getTaskName() {
        return taskName;
    }

    public long getStartOffset() {
        return startOffset;
    }

    public long getEndOffset() {
        return endOffset;
    }

    public long getRepeatMillSecs() {
        return repeatMillSecs;
    }

    public String[] getWeekdays() {
        return weekdays;
    }

    public String[] getDays() {
        return days;
    }

    public String[] getMonths() {
        return months;
    }

    public List<Input> getInputList() {
        return inputList;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
