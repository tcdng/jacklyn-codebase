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
package com.tcdng.jacklyn.common.web.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.tcdng.jacklyn.common.constants.CommonModuleNameConstants;
import com.tcdng.jacklyn.common.constants.JacklynApplicationAttributeConstants;
import com.tcdng.jacklyn.common.data.ManagedEntityPrivilegeNames;
import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.jacklyn.common.utils.PageControllerSessionUtils;
import com.tcdng.jacklyn.shared.organization.PrivilegeCategoryConstants;
import com.tcdng.unify.core.UnifyContainer;
import com.tcdng.unify.core.UnifyCorePropertyConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.Describable;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.ui.control.Table;
import com.tcdng.unify.web.ui.data.Hint.MODE;
import com.tcdng.unify.web.ui.panel.SwitchPanel;

/**
 * Convenient abstract base class for page controllers that manage prefetched
 * records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@UplBinding("web/common/upl/manageprefetch.upl")
@ResultMappings({
        @ResultMapping(
                name = BasePrefetchController.HIDEPOPUP_REFERESHMAIN,
                response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{manageRecordPanel}" }),
        @ResultMapping(name = "refreshmain", response = { "!refreshpanelresponse panels:$l{manageRecordPanel}" }),
        @ResultMapping(
                name = "refreshtable",
                response = { "!refreshpanelresponse panels:$l{detailsPanel tablePanel actionPanel}" }),
        @ResultMapping(
                name = "refreshitemviewer",
                response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{prefetchItemPanel}" }),
        @ResultMapping(
                name = "switchsearch",
                response = { "!hidepopupresponse", "!switchpanelresponse panels:$l{manageBodyPanel.searchBodyPanel}",
                        "!refreshpanelresponse panels:$l{detailsPanel}" }),
        @ResultMapping(
                name = "switchitemview",
                response = { "!hidepopupresponse", "!switchpanelresponse panels:$l{manageBodyPanel.prefetchItemPanel}",
                        "!refreshpanelresponse panels:$l{detailsPanel}" }),
        @ResultMapping(name = "documentView", response = { "!docviewresponse" }) })
public abstract class BasePrefetchController<T extends Entity, U> extends BasePageController {

    public static final String HIDEPOPUP_REFERESHMAIN = "hidepopuprefreshmain";

    private static final String SWITCH_MAPPING = "switch-mapping";

    @Configurable("$m{common.report.norecordintable}")
    private String noRecordMessage;

    private Class<T> entityClass;

    private int modifier;
    
    private int mode;

    private String itemCountLabel;

    private String modeDescription;

    private String modeStyle;

    private String reportType;

    private List<T> recordList;

    private T record;

    private T oldRecord;

    private Table table;

    private String recordHintName;

    private boolean describable;

    public BasePrefetchController(Class<T> entityClass, String hint, int modifier) {
        super(ManageRecordModifier.isSecure(modifier), false);
        this.entityClass = entityClass;
        this.modifier = modifier;
        recordHintName = hint;
        describable = Describable.class.isAssignableFrom(entityClass);
    }

    @Action
    public String openPrefetchPage() throws UnifyException {
        findRecords();
        switchToTableContentPanel();
        return openPage();
    }

    @Action
    public String prepareViewRecord() throws UnifyException {
        prepareView(ManageRecordModifier.VIEW);
        logUserEvent(EventType.VIEW, record, false);
        return getSwitchItemViewMapping();
    }

    @Action
    public String prepareUpdateRecord() throws UnifyException {
        prepareView(ManageRecordModifier.MODIFY);
        oldRecord = ReflectUtils.shallowBeanCopy(record);
        return getSwitchItemViewMapping();
    }

    @SuppressWarnings("unchecked")
    @Action
    public String updateAndNextRecord() throws UnifyException {
        doUpdate();

        record = find((U) record.getId());
        recordList.set(table.getViewIndex(), record);

        if (table.isViewIndexAtLast()) {
            return done();
        }

        return nextRecord();
    }

    @Action
    public String updateAndCloseRecord() throws UnifyException {
        doUpdate();
        return done();
    }

    @Action
    public String firstRecord() throws UnifyException {
        onLoseView(record);
        table.setViewIndex(0);
        return performModeAction();
    }

    @Action
    public String previousRecord() throws UnifyException {
        onLoseView(record);
        table.setViewIndex(table.getViewIndex() - 1);
        return performModeAction();
    }

    @Action
    public String nextRecord() throws UnifyException {
        onLoseView(record);
        table.setViewIndex(table.getViewIndex() + 1);
        return performModeAction();
    }

    @Action
    public String lastRecord() throws UnifyException {
        onLoseView(record);
        table.setViewIndex(recordList.size() - 1);
        return performModeAction();
    }

    @Action
    public String done() throws UnifyException {
        onLoseView(record);
        findRecords();
        return "switchsearch";
    }

    @Action
    public String cancel() throws UnifyException {
        return done();
    }

    @Action
    public String refreshItemViewer() throws UnifyException {
        onRefreshItemViewer();
        loadSessionOnRefresh();
        return "refreshitemviewer";
    }

    @Action
    public String prepareGenerateReport() throws UnifyException {
        if (recordList == null || recordList.isEmpty()) {
            return showMessageBox(noRecordMessage);
        }

        ReportOptions reportOptions =
                getCommonReportProvider().getDynamicReportOptions(entityClass.getName(), table.getColumnPropertyList());
        reportOptions.setReportResourcePath("/common/resource/report");
        reportOptions.setReportFormat(reportType);
        reportOptions.setContent(recordList);
        return showReportOptionsBox(reportOptions);
    }

    public List<T> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    }

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }

    public String getItemCountLabel() {
        return itemCountLabel;
    }

    public String getModeDescription() {
        return modeDescription;
    }

    public void setModeDescription(String modeDescription) {
        this.modeDescription = modeDescription;
    }

    public String getModeStyle() {
        return modeStyle;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    @Override
    protected void onInitialize() throws UnifyException {
        noRecordMessage = resolveSessionMessage(noRecordMessage);
        recordHintName = resolveSessionMessage(recordHintName);
    }

    @Override
    protected void onSetPage() throws UnifyException {
        // Get reference to UPL table
        table = getPageWidgetByShortName(Table.class, "tablePanel.contentTbl");

        // Set validation page attributes
        getPage().setAttribute("validationClass", entityClass);
        getPage().setAttribute("validationIdClass", Long.class);

        manageReportable();
    }

    @Override
    protected String getDocViewPanelName() {
        return "manageRecordPanel";
    }

    @Override
    protected void onIndexPage() throws UnifyException {
        super.onIndexPage();
        updateSearch();
    }

    @Override
    protected void onClosePage() throws UnifyException {
        table = null;
        recordList = null;
        oldRecord = null;
        record = null;
    }

    protected void setSwitchItemViewMapping(String mapping) throws UnifyException {
        setRequestAttribute(SWITCH_MAPPING, mapping);
    }

    protected void onLoseView(T record) throws UnifyException {
        onloadSessionOnLoseView();
    }

    protected void switchToTableContentPanel() throws UnifyException {
        SwitchPanel switchPanel = getPageWidgetByShortName(SwitchPanel.class, "manageBodyPanel");
        switchPanel.switchContent("searchBodyPanel");
    }

    protected void switchToItemViewerPanel() throws UnifyException {
        SwitchPanel switchPanel = getPageWidgetByShortName(SwitchPanel.class, "manageBodyPanel");
        switchPanel.switchContent("prefetchItemPanel");
    }

    protected String getCurrentSwitchShortName() throws UnifyException {
        SwitchPanel switchPanel = getPageWidgetByShortName(SwitchPanel.class, "manageBodyPanel");
        return switchPanel.getCurrentWidgetShortName();
    }
    
    protected PageControllerSessionUtils getPageControllerSessionUtils() throws UnifyException {
        return (PageControllerSessionUtils) getComponent(CommonModuleNameConstants.PAGECONTROLLERSESSIONUTILS);
    }

    protected abstract void prepareForPrefetch() throws UnifyException;

    protected abstract List<T> find() throws UnifyException;

    protected abstract T find(U id) throws UnifyException;

    protected abstract int update(T record) throws UnifyException;

    protected abstract void setItemViewerEditable(boolean editable) throws UnifyException;

    protected T getSelectedRecord() throws UnifyException {
        return recordList.get(table.getViewIndex());
    }

    protected List<Long> getSelectedIds() throws UnifyException {
        if (table.getSelectedRows() > 0) {
            Integer[] selectedIndexes = table.getSelectedRowIndexes();
            List<Long> selectedIds = new ArrayList<Long>();
            for (int i = 0; i < selectedIndexes.length; i++) {
                selectedIds.add((Long) recordList.get(selectedIndexes[i]).getId());
            }
            return selectedIds;
        }
        return Collections.emptyList();
    }

    protected String[] getSelectedDescription() throws UnifyException {
        String[] selectedDescriptions = DataUtils.ZEROLEN_STRING_ARRAY;
        if (table.getSelectedRows() > 0) {
            Integer[] selectedIndexes = table.getSelectedRowIndexes();
            selectedDescriptions = new String[selectedIndexes.length];
            if (describable) {
                for (int i = 0; i < selectedIndexes.length; i++) {
                    selectedDescriptions[i] = ((Describable) recordList.get(selectedIndexes[i])).getDescription();
                }
            }
        }
        return selectedDescriptions;
    }

    protected void updateSearch() throws UnifyException {
        setVisible("editTblBtn", ManageRecordModifier.isEditable(modifier));
        setVisible("viewTblBtn", ManageRecordModifier.isViewable(modifier));

        manageReportable();
    }

    protected void onPrepareView(T record) throws UnifyException {

    }

    protected void onPrepareItemViewer(T record) throws UnifyException {

    }

    protected void onRefreshSearch() throws UnifyException {

    }

    protected void onRefreshItemViewer() throws UnifyException {

    }

    protected Table getTable() {
        return table;
    }

    protected String getSwitchItemViewMapping() throws UnifyException {
        String mapping = (String) getRequestAttribute(SWITCH_MAPPING);
        if (!StringUtils.isBlank(mapping)) {
            return mapping;
        }
        
        return "switchitemview";
    }

    private String findRecords() throws UnifyException {
        prepareForPrefetch();
        record = null;
        recordList = find();
        table.reset();
        logUserEvent(EventType.SEARCH, entityClass);

        if (recordList != null && recordList.size() >= getContainerSetting(int.class,
                UnifyCorePropertyConstants.APPLICATION_QUERY_LIMIT, UnifyContainer.DEFAULT_APPLICATION_QUERY_LIMIT)) {
            hintUser(MODE.WARNING, "$m{managerecord.hint.applicationquerylimit.reached}");
        }

        updateSearch();
        return "refreshtable";
    }

    private void doUpdate() throws UnifyException {
        int result = update(record);
        if (result > 0) {
            logUserEvent(EventType.UPDATE, oldRecord, record);
            hintUser("$m{hint.record.update.success}", recordHintName);
        } else {
            hintUser("$m{hint.record.updatetoworkflow.success}", recordHintName);
        }
    }

    @SuppressWarnings("unchecked")
    private void manageReportable() throws UnifyException {
        boolean isReportable = !getCommonReportProvider().isReportable(entityClass.getName());
        if (isReportable) {
            // Hide report button base on role privilege if necessary
            ManagedEntityPrivilegeNames managedPrivilegeNames =
                    ((Map<Class<? extends Entity>, ManagedEntityPrivilegeNames>) getApplicationAttribute(
                            JacklynApplicationAttributeConstants.MANAGED_PRIVILEGES)).get(entityClass);
            if (managedPrivilegeNames != null) {
                isReportable =
                        isCurrentRolePrivilege(PrivilegeCategoryConstants.REPORTABLE,
                                managedPrivilegeNames.getReportableName());
            }
        }

        setVisible("reportBtn", isReportable);
    }

    private void loadSessionOnRefresh() throws UnifyException {
        getPageControllerSessionUtils().loadSession(this);
    }

    private void onloadSessionOnLoseView() throws UnifyException {
        getPageControllerSessionUtils().unloadSession(this);
    }

    @SuppressWarnings("unchecked")
    private void prepareView(int mode) throws UnifyException {
        this.mode = mode;
        int index = table.getViewIndex();
        record = find((U) recordList.get(index).getId());
        recordList.set(index, record);
        onPrepareView(record);
        loadSessionOnRefresh();
        
        // Navigation buttons
        int viewIndex = table.getViewIndex();
        setDisabled("firstFrmBtn", recordList == null || viewIndex <= 0);
        setDisabled("prevFrmBtn", recordList == null || viewIndex <= 0);
        setDisabled("nextFrmBtn", recordList == null || viewIndex >= (recordList.size() - 1));
        setDisabled("lastFrmBtn", recordList == null || viewIndex >= (recordList.size() - 1));

        // Index description
        if (recordList != null) {
            itemCountLabel = getSessionMessage("label.itemcount", viewIndex + 1, recordList.size());
        }

        // Action buttons
        setVisible("saveNextFrmBtn", false);
        setVisible("saveCloseFrmBtn", false);
        setVisible("cancelFrmBtn", false);
        setVisible("doneFrmBtn", false);
        setPageValidationEnabled(false);

        switch (this.mode) {
            case ManageRecordModifier.MODIFY:
                setVisible("saveNextFrmBtn", true);
                setVisible("saveCloseFrmBtn", true);
                setItemViewerEditable(true);
                setPageValidationEnabled(true);
                setVisible("cancelFrmBtn", true);
                
                modeDescription = getSessionMessage("managerecord.mode.modify", recordHintName);
                modeStyle = EventType.UPDATE.colorMode();
                break;
            case ManageRecordModifier.VIEW:
                setItemViewerEditable(false);
                setVisible("doneFrmBtn", true);
                
                modeDescription = getSessionMessage("managerecord.mode.view", recordHintName);
                modeStyle = EventType.VIEW.colorMode();
               break;
            default:
                break;
        }
        
        // Call on update item viewer
        onPrepareItemViewer(record);
    }
    

    private String performModeAction() throws UnifyException {
        String result = null;
        switch (this.mode) {
            case ManageRecordModifier.MODIFY:
                return prepareUpdateRecord();
            case ManageRecordModifier.VIEW:
                return prepareViewRecord();
            default:
                break;
        }
        return result;
    }

    
}
