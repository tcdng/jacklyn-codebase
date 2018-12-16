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
package com.tcdng.jacklyn.system.business;

import java.util.Map;

import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.jacklyn.system.constants.SystemSchedTaskConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.task.AbstractTaskStatusLogger;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskStatus;

/**
 * Logs the status of a scheduled task.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("scheduledtaskstatuslogger")
public class ScheduledTaskStatusLogger extends AbstractTaskStatusLogger {

    @Configurable(SystemModuleNameConstants.SYSTEMBUSINESSMODULE)
    private SystemModule systemModule;

    @Override
    public void logTaskStatus(TaskMonitor taskMonitor, Map<String, Object> parameters) {
        createScheduledTaskHistory(taskMonitor.getCurrentTaskStatus(), parameters, taskMonitor.getExceptions());

        if (taskMonitor.isDone()) {
            parameters.remove(SystemSchedTaskConstants.SCHEDULEDTASK_ID);
        }
    }

    @Override
    public void logCriticalFailure(String taskName, Map<String, Object> parameters, Exception exception) {
        createScheduledTaskHistory(TaskStatus.CRITICAL, parameters, exception);
    }

    private void createScheduledTaskHistory(TaskStatus taskStatus, Map<String, Object> parameters,
            Exception... exceptions) {
        try {
            String errorMessages = null;
            if (exceptions != null && exceptions.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (Exception exception : exceptions) {
                    sb.append(getExceptionMessage(LocaleType.APPLICATION, exception));
                }
                errorMessages = sb.toString();
            }

            TaskStatus oldTaskStatus = (TaskStatus) parameters.get(SystemSchedTaskConstants.SCHEDULEDTASK_ID);
            if (!taskStatus.equals(oldTaskStatus)) {
                systemModule.createScheduledTaskHistory(
                        (Long) parameters.get(SystemSchedTaskConstants.SCHEDULEDTASK_ID), taskStatus, errorMessages);
                parameters.put(SystemSchedTaskConstants.SCHEDULEDTASK_ID, taskStatus);
            }
        } catch (UnifyException e) {
            logError(e);
        }
    }
}
