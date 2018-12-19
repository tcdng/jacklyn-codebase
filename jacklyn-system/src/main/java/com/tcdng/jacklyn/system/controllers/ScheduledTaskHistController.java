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
package com.tcdng.jacklyn.system.controllers;

import java.util.Date;
import java.util.List;

import com.tcdng.jacklyn.common.controllers.ManageRecordController;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHist;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHistQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskStatus;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing scheduled task history.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/scheduledtaskhist")
@UplBinding("web/system/upl/managescheduledtaskhist.upl")
public class ScheduledTaskHistController extends ManageRecordController<ScheduledTaskHist, Long> {

    @Configurable
    private SystemService systemService;

    private Date searchExecutionDt;

    private Long searchScheduledTaskId;

    private TaskStatus searchStatus;

    public ScheduledTaskHistController() {
        super(ScheduledTaskHist.class, "system.scheduledtaskhist.hint",
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
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

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        if (searchExecutionDt == null) {
            searchExecutionDt = CalendarUtils.getCurrentMidnightDate();
        }
    }

    @Override
    protected List<ScheduledTaskHist> find() throws UnifyException {
        ScheduledTaskHistQuery query = new ScheduledTaskHistQuery();
        if (QueryUtils.isValidLongCriteria(searchScheduledTaskId)) {
            query.scheduledTaskId(searchScheduledTaskId);
        }
        if (getSearchStatus() != null) {
            query.taskStatus(getSearchStatus());
        }
        query.createdOn(searchExecutionDt);
        return systemService.findScheduledTaskHistory(query);
    }

    @Override
    protected ScheduledTaskHist find(Long id) throws UnifyException {
        return systemService.findScheduledTaskHist(id);
    }

    @Override
    protected ScheduledTaskHist prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(ScheduledTaskHist scheduledTaskHistData) throws UnifyException {
        return null;
    }

    @Override
    protected int update(ScheduledTaskHist scheduledTaskHistData) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(ScheduledTaskHist scheduledTaskHistData) throws UnifyException {
        return 0;
    }
}
