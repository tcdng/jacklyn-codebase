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
package com.tcdng.jacklyn.system.business;

import java.util.Map;

import com.tcdng.jacklyn.system.constants.SystemSchedTaskConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.task.AbstractTaskStatusLogger;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Logs the status of a scheduled task.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("scheduledtaskstatuslogger")
public class ScheduledTaskStatusLogger extends AbstractTaskStatusLogger {

    @Configurable
    private SystemService systemService;

    @Override
    public void logTaskStatus(TaskMonitor taskMonitor, Map<String, Object> parameters) {
        if (taskMonitor.isDone()) {
            Long scheduledTaskId = (Long) parameters.get(SystemSchedTaskConstants.SCHEDULEDTASK_ID);
            Long scheduledTaskHistId = (Long) parameters.get(SystemSchedTaskConstants.SCHEDULEDTASKHIST_ID);
            String errorMsgs = null;
            try {
                Exception[] exceptions = taskMonitor.getExceptions();
                if (exceptions != null && exceptions.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (Exception exception : exceptions) {
                        sb.append(getExceptionMessage(LocaleType.APPLICATION, exception));
                        sb.append(getLineSeparator());
                    }
                    errorMsgs = sb.toString();
                }
            } catch (UnifyException e) {
                logError(e);
            }

            try {
                systemService.releaseScheduledTask(scheduledTaskId, scheduledTaskHistId, taskMonitor.getTaskStatus(0),
                        errorMsgs);
            } catch (UnifyException e) {
                logError(e);
            }
        }
    }
}
