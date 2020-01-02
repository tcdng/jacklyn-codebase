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
package com.tcdng.jacklyn.system.web.controllers;

import java.util.Collections;
import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHist;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHistQuery;
import com.tcdng.jacklyn.system.web.beans.ScheduledTaskHistPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing scheduled task history.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/scheduledtaskhist")
@UplBinding("web/system/upl/managescheduledtaskhist.upl")
public class ScheduledTaskHistController extends AbstractSystemFormController<ScheduledTaskHistPageBean, ScheduledTaskHist> {

    @Configurable
    private SystemService systemService;

    public ScheduledTaskHistController() {
        super(ScheduledTaskHistPageBean.class, ScheduledTaskHist.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        
        ScheduledTaskHistPageBean pageBean = getPageBean();
        if (pageBean.getSearchExecutionDt() == null) {
            pageBean.setSearchExecutionDt(systemService.getToday());
        }
    }

    @Override
    protected List<ScheduledTaskHist> find() throws UnifyException {
        ScheduledTaskHistPageBean pageBean = getPageBean();
        ScheduledTaskHistQuery query = new ScheduledTaskHistQuery();
        if (pageBean.getSearchExecutionDt() != null && QueryUtils.isValidLongCriteria(pageBean.getSearchScheduledTaskId())) {
            query.scheduledTaskId(pageBean.getSearchScheduledTaskId());
            
            if (pageBean.getSearchStatus() != null) {
                query.taskStatus(pageBean.getSearchStatus());
            }

            query.startedOn(pageBean.getSearchExecutionDt());
            return systemService.findScheduledTaskHistory(query);
        }

        return Collections.emptyList();
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
    protected Object create(ScheduledTaskHist scheduledTaskHist) throws UnifyException {
        return null;
    }

    @Override
    protected int update(ScheduledTaskHist scheduledTaskHist) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(ScheduledTaskHist scheduledTaskHist) throws UnifyException {
        return 0;
    }
}
