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
package com.tcdng.jacklyn.common.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.business.ReportProvider;
import com.tcdng.jacklyn.common.constants.JacklynApplicationAttributeConstants;
import com.tcdng.jacklyn.common.constants.JacklynSessionAttributeConstants;
import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.AbstractPageController;
import com.tcdng.unify.web.DocViewController;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.ui.Panel;
import com.tcdng.unify.web.ui.data.MessageResult;
import com.tcdng.unify.web.ui.data.SearchBox;
import com.tcdng.unify.web.ui.panel.TableCrudPanel;

/**
 * Common base class for all page controllers.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@ResultMappings({ @ResultMapping(name = "documentView", response = { "!docviewresponse" }) })
public abstract class BasePageController extends AbstractPageController implements DocViewController {

    public BasePageController(boolean secured, boolean readOnly) {
        super(secured, readOnly);
    }

    @Action
    public String getRemoteDoc() throws UnifyException {
        openPage();
        return "documentView";
    }

    @Override
    public Panel getDocViewPanel() throws UnifyException {
        return getPanelByShortName(getDocViewPanelName());
    }
    
    /**
     * Shows a search box.
     * 
     * @param resultPath
     *            the result URL
     * @param mappings
     *            the result mappings
     * @return the show search mapping name
     * @throws UnifyException
     *             if an error occurs
     */
    protected String showSearchBox(String resultPath, SearchBox.Mapping... mappings) throws UnifyException {
        SearchBox searchBoxInfo = new SearchBox(this, resultPath);
        for (SearchBox.Mapping mapping : mappings) {
            searchBoxInfo.addMapping(mapping);
        }

        setSessionAttribute(JacklynSessionAttributeConstants.SEARCHBOX, searchBoxInfo);
        return "showapplicationsearch";
    }

    /**
     * Launches a task and shows a monitoring box.
     * 
     * @param taskSetup
     *            the task setup
     * @param caption
     *            the task monitor box caption
     * @param onSuccessPath
     *            optional on task success forward to path
     * @param onFailurePath
     *            optional on task faile forward to path
     * @return the show application monitor box result mapping name
     * @throws UnifyException
     *             if an error occurs
     */
    protected String launchTaskWithMonitorBox(TaskSetup taskSetup, String caption, String onSuccessPath,
            String onFailurePath) throws UnifyException {
        if (taskSetup.getEventCode() != null) {
            logUserEvent(taskSetup.getEventCode(), taskSetup.getEventDetails());
        }

        return super.launchTaskWithMonitorBox(taskSetup, caption, onSuccessPath, onFailurePath);
    }

    /**
     * Sets up a response that shows a report options box. The report options box is
     * backed by supplied {@link ReportOptions} value of the session attribute
     * {@link JacklynSessionAttributeConstants#REPORTOPTIONS}.
     * 
     * @param reportOptions
     *            report options data object
     * @return response to show application report options box
     * @throws UnifyException
     *             if an error occurs
     */
    protected String showReportOptionsBox(ReportOptions reportOptions) throws UnifyException {
        setSessionAttribute(JacklynSessionAttributeConstants.REPORTOPTIONS, reportOptions);
        return "showapplicationreportoptions";
    }

    /**
     * Logs a user event using event code with optional details.
     * 
     * @param eventName
     *            the event code
     * @param details
     *            the event details
     * @throws UnifyException
     *             if event code is unknown. if an error occurs.
     */
    protected void logUserEvent(String eventName, String... details) throws UnifyException {
        getRequestContextUtil().logUserEvent(eventName, details);
    }

    /**
     * Logs a user event using event code with details.
     * 
     * @param eventName
     *            the event code
     * @param details
     *            the event details
     * @throws UnifyException
     *             if event code is unknown. if an error occurs.
     */
    protected void logUserEvent(String eventName, List<String> details) throws UnifyException {
        getRequestContextUtil().logUserEvent(eventName, details);
    }

    /**
     * Logs a user event using supplied event and record type.
     * 
     * @param eventType
     *            the event type
     * @param entityClass
     *            the record type
     * @throws UnifyException
     *             If an error occurs.
     */
    protected void logUserEvent(EventType eventType, Class<? extends Entity> entityClass) throws UnifyException {
        getRequestContextUtil().logUserEvent(eventType, entityClass);
    }

    /**
     * Logs a user event with associated record.
     * 
     * @param eventType
     *            the event type
     * @param record
     *            the record object
     * @param isNewRecord
     *            indicates supplied record is new
     * @throws UnifyException
     *             if an error occurs.
     */
    protected void logUserEvent(EventType eventType, Entity record, boolean isNewRecord) throws UnifyException {
        getRequestContextUtil().logUserEvent(eventType, record, isNewRecord);
    }

    /**
     * Logs a user event with associated old and new record.
     * 
     * @param eventType
     *            the event type
     * @param oldRecord
     *            the old record
     * @param newRecord
     *            the new record. Can be null
     * @throws UnifyException
     *             if audit type with supplied action is unknown. If an error
     *             occurs.
     */
    protected <T extends Entity> void logUserEvent(EventType eventType, T oldRecord, T newRecord)
            throws UnifyException {
        getRequestContextUtil().logUserEvent(eventType, oldRecord, newRecord);
    }

    /**
     * Returns a table CRUD panel using supplied name.
     * 
     * @param shortName
     *            the short name
     * @throws UnifyException
     *             if an error occurs
     */
    @SuppressWarnings("unchecked")
    protected <T extends Entity> TableCrudPanel<T> getTableCrudPanel(String shortName) throws UnifyException {
        return (TableCrudPanel<T>) getPageWidgetByShortName(TableCrudPanel.class, shortName);
    }

    /**
     * Returns message result obtained from request.
     * 
     * @throws UnifyException
     *             if an error occurs
     */
    protected MessageResult getMessageResult() throws UnifyException {
        return getRequestTarget(MessageResult.class);
    }

    /**
     * Returns the common report provider.
     * 
     * @throws UnifyException
     *             if an error occurs
     */
    protected ReportProvider getCommonReportProvider() throws UnifyException {
        return (ReportProvider) getApplicationAttribute(JacklynApplicationAttributeConstants.COMMON_REPORT_PROVIDER);
    }

    protected abstract String getDocViewPanelName();
}
