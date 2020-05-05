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

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.data.ScheduledTaskLargeData;
import com.tcdng.jacklyn.system.entities.ScheduledTask;
import com.tcdng.jacklyn.system.entities.ScheduledTaskQuery;
import com.tcdng.jacklyn.system.web.beans.ScheduledTaskPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for managing scheduled tasks.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/scheduledtask")
@UplBinding("web/system/upl/managescheduledtask.upl")
public class ScheduledTaskController extends AbstractSystemFormController<ScheduledTaskPageBean, ScheduledTask> {

    public ScheduledTaskController() {
        super(ScheduledTaskPageBean.class, ScheduledTask.class, ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Action
    @Override
    public String copyRecord() throws UnifyException {
        ScheduledTaskPageBean pageBean = getPageBean();
        ScheduledTaskLargeData clipboardLargeData = ReflectUtils.shallowBeanCopy(pageBean.getLargeData());
        pageBean.setClipboardLargeData(clipboardLargeData);
        return super.copyRecord();
    }

    @Action
    public String refreshParameters() throws UnifyException {
        ScheduledTaskPageBean pageBean = getPageBean();
        onPrepareView(pageBean.getRecord(), false);
        return "refreshmain";
    }

    @Override
    protected List<ScheduledTask> find() throws UnifyException {
        ScheduledTaskPageBean pageBean = getPageBean();
        ScheduledTaskQuery query = new ScheduledTaskQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchTaskName())) {
            query.taskName(pageBean.getSearchTaskName());
        }

        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.ignoreEmptyCriteria(true);

        return getSystemService().findScheduledTasks(query);
    }

    @Override
    protected ScheduledTask find(Long id) throws UnifyException {
        ScheduledTaskPageBean pageBean = getPageBean();
        ScheduledTaskLargeData largeData = getSystemService().findScheduledTaskDocument(id);
        pageBean.setLargeData(largeData);
        return largeData.getData();
    }

    @Override
    protected Object create(ScheduledTask scheduledTask) throws UnifyException {
        ScheduledTaskPageBean pageBean = getPageBean();
        return getSystemService().createScheduledTask(pageBean.getLargeData());
    }

    @Override
    protected int update(ScheduledTask scheduledTask) throws UnifyException {
        ScheduledTaskPageBean pageBean = getPageBean();
        return getSystemService().updateScheduledTask(pageBean.getLargeData());
    }

    @Override
    protected int delete(ScheduledTask scheduledTask) throws UnifyException {
        return 0;
    }

    @Override
    protected void onCopy(ScheduledTask scheduledTaskCopy) throws UnifyException {
        scheduledTaskCopy.setNextExecutionOn(null);
        scheduledTaskCopy.setLastExecutionOn(null);
    }

    @Override
    protected void onPrepareCreate(ScheduledTask scheduledTask) throws UnifyException {
        ScheduledTaskPageBean pageBean = getPageBean();
        pageBean.setLargeData(new ScheduledTaskLargeData(scheduledTask));
    }

    @Override
    protected void onPrepareView(ScheduledTask scheduledTask, boolean onPaste) throws UnifyException {
        ScheduledTaskPageBean pageBean = getPageBean();
        ScheduledTaskLargeData largeData = pageBean.getLargeData();
        if (onPaste) {
            largeData.setScheduledTaskParams(pageBean.getClipboardLargeData().getScheduledTaskParams());
        } else {
            largeData = getSystemService().loadScheduledTaskDocumentValues(largeData);
            pageBean.setLargeData(largeData);
        }

        boolean isDisabled = ManageRecordModifier.VIEW == getMode() || ManageRecordModifier.DELETE == getMode();
        setPageWidgetDisabled("frmParamsTbl", isDisabled);
    }

    @Override
    protected void onLoseView(ScheduledTask scheduledTask) throws UnifyException {
        ScheduledTaskPageBean pageBean = getPageBean();
        pageBean.setLargeData(new ScheduledTaskLargeData());
        pageBean.setClipboardLargeData(null);
        ;
    }
}
