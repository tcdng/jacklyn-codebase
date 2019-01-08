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

import java.util.List;

import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.system.data.ScheduledTaskLargeData;
import com.tcdng.jacklyn.system.entities.ScheduledTask;
import com.tcdng.jacklyn.system.entities.ScheduledTaskQuery;
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
public class ScheduledTaskController extends AbstractSystemCrudController<ScheduledTask> {

    private String searchTaskName;

    private String searchDescription;

    private ScheduledTaskLargeData largeData;

    private ScheduledTaskLargeData clipboardLargeData;

    public ScheduledTaskController() {
        super(ScheduledTask.class, "$m{system.scheduledtask.hint}", ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
                | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.REPORTABLE);
    }

    @Action
    @Override
    public String copyRecord() throws UnifyException {
        clipboardLargeData = ReflectUtils.shallowBeanCopy(largeData);
        return super.copyRecord();
    }

    @Action
    public String refreshParameters() throws UnifyException {
        onPrepareView(getRecord(), false);
        return "refreshmain";
    }

    public String getSearchTaskName() {
        return searchTaskName;
    }

    public void setSearchTaskName(String searchTaskName) {
        this.searchTaskName = searchTaskName;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public ScheduledTaskLargeData getLargeData() {
        return largeData;
    }

    public void setLargeData(ScheduledTaskLargeData largeData) {
        this.largeData = largeData;
    }

    @Override
    protected List<ScheduledTask> find() throws UnifyException {
        ScheduledTaskQuery query = new ScheduledTaskQuery();
        if (QueryUtils.isValidStringCriteria(searchTaskName)) {
            query.taskName(searchTaskName);
        }

        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }

        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
        }
        query.ignoreEmptyCriteria(true);

        return getSystemService().findScheduledTasks(query);
    }

    @Override
    protected ScheduledTask find(Long id) throws UnifyException {
        largeData = getSystemService().findScheduledTaskDocument(id);
        return largeData.getData();
    }

    @Override
    protected ScheduledTask prepareCreate() throws UnifyException {
        largeData = new ScheduledTaskLargeData();
        return largeData.getData();
    }

    @Override
    protected Object create(ScheduledTask scheduledTaskData) throws UnifyException {
        return getSystemService().createScheduledTask(largeData);
    }

    @Override
    protected int update(ScheduledTask scheduledTaskData) throws UnifyException {
        return getSystemService().updateScheduledTask(largeData);
    }

    @Override
    protected int delete(ScheduledTask scheduledTaskData) throws UnifyException {
        return 0;
    }

    @Override
    protected void onPrepareView(ScheduledTask scheduledTaskData, boolean onPaste) throws UnifyException {
        if (onPaste) {
            largeData.setScheduledTaskParams(clipboardLargeData.getScheduledTaskParams());
        } else {
            largeData = getSystemService().loadScheduledTaskDocumentValues(largeData);
        }

        boolean isDisabled = ManageRecordModifier.VIEW == getMode() || ManageRecordModifier.DELETE == getMode();
        setDisabled("frmParamsTbl", isDisabled);
    }

    @Override
    protected void onLoseView(ScheduledTask scheduledTaskData) throws UnifyException {
        largeData = new ScheduledTaskLargeData();
        clipboardLargeData = null;
    }
}
