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
import com.tcdng.jacklyn.common.entities.BaseEntity;
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
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.ui.control.Table;
import com.tcdng.unify.web.ui.data.Hint.MODE;
import com.tcdng.unify.web.ui.panel.SearchCriteriaPanel;
import com.tcdng.unify.web.ui.panel.SwitchPanel;

/**
 * Convenient abstract base class for page controllers that manage CRUD actions
 * on records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@UplBinding("web/common/upl/managerecord.upl")
@ResultMappings({
        @ResultMapping(
                name = BaseCrudController.HIDEPOPUP_REFERESHMAIN,
                response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{manageRecordPanel}" }),
        @ResultMapping(name = "refreshmain", response = { "!refreshpanelresponse panels:$l{manageRecordPanel}" }),
        @ResultMapping(
                name = "refreshtable",
                response = { "!refreshpanelresponse panels:$l{searchPanel tablePanel actionPanel}" }),
        @ResultMapping(
                name = "refreshcrudviewer",
                response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{crudPanel}" }),
        @ResultMapping(
                name = "switchsearch",
                response = { "!hidepopupresponse", "!switchpanelresponse panels:$l{manageBodyPanel.searchBodyPanel}",
                        "!refreshpanelresponse panels:$l{searchPanel}" }),
        @ResultMapping(
                name = "switchcrud",
                response = { "!hidepopupresponse", "!switchpanelresponse panels:$l{manageBodyPanel.crudPanel}",
                        "!refreshpanelresponse panels:$l{searchPanel}" }),
        @ResultMapping(
                name = "switchsearch_io",
                response = { "!hidepopupresponse", "!switchpanelresponse panels:$l{mainResultPanel.searchResultPanel}",
                        "!refreshpanelresponse panels:$l{searchPanel}" }),
        @ResultMapping(
                name = "switchcrud_io",
                response = { "!hidepopupresponse", "!switchpanelresponse panels:$l{mainResultPanel.crudPanel}",
                        "!refreshpanelresponse panels:$l{searchPanel}" }),
        @ResultMapping(name = "documentView", response = { "!docviewresponse" }) })
public abstract class BaseCrudController<T extends Entity, U> extends BasePageController {

    public static final String HIDEPOPUP_REFERESHMAIN = "hidepopuprefreshmain";

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

    private T clipRecord;

    private T oldRecord;

    private Table table;

    private String recordHintName;

    private boolean describable;

    public BaseCrudController(Class<T> entityClass, String hint, int modifier) {
        super(ManageRecordModifier.isSecure(modifier), false);
        this.entityClass = entityClass;
        this.modifier = modifier;
        recordHintName = hint;
        describable = Describable.class.isAssignableFrom(entityClass);
    }

    @Action
    public String findRecords() throws UnifyException {
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

    @Action
    public String prepareCreateRecord() throws UnifyException {
        record = prepareCreate();
        loadSessionOnCreate();
        updateCrudViewer(ManageRecordModifier.ADD);
        return "switchcrud";
    }

    @Action
    public String createAndNextRecord() throws UnifyException {
        doCreate();
        return prepareCreateRecord();
    }

    @Action
    public String createAndCloseRecord() throws UnifyException {
        doCreate();
        return done();
    }

    @Action
    public String prepareViewRecord() throws UnifyException {
        prepareView();
        logUserEvent(EventType.VIEW, record, false);
        if (ManageRecordModifier.isActivatable(modifier)) {
            oldRecord = ReflectUtils.shallowBeanCopy(record);
        }
        updateCrudViewer(ManageRecordModifier.VIEW);
        return "switchcrud";
    }

    @Action
    public String prepareUpdateRecord() throws UnifyException {
        prepareView();
        oldRecord = ReflectUtils.shallowBeanCopy(record);
        updateCrudViewer(ManageRecordModifier.MODIFY);
        return "switchcrud";
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
    public String prepareDeleteRecord() throws UnifyException {
        prepareView();
        updateCrudViewer(ManageRecordModifier.DELETE);
        return "switchcrud";
    }

    @Action
    public String deleteRecord() throws UnifyException {
        delete(record);
        logUserEvent(EventType.DELETE, record, false);
        hintUser("$m{hint.record.delete.success}", recordHintName);
        recordList.remove(table.getViewIndex());

        if (recordList.isEmpty()) {
            return done();
        }

        int size = recordList.size();
        if (table.getViewIndex() >= size) {
            table.setViewIndex(size - 1);
        }
        return done();
    }

    @Action
    public String activateRecord() throws UnifyException {
        activate(record);
        logUserEvent(EventType.UPDATE, oldRecord, record);
        return prepareViewRecord();
    }

    @Action
    public String deactivateRecord() throws UnifyException {
        deactivate(record);
        logUserEvent(EventType.UPDATE, oldRecord, record);
        return prepareViewRecord();
    }

    @Action
    public String copyRecord() throws UnifyException {
        clipRecord = ReflectUtils.shallowBeanCopy(record);
        if (ManageRecordModifier.isCopyToAdd(modifier)) {
            return prepareCreateRecord();
        }

        setPageAttribute("copyresponse", DataUtils.ZEROLEN_STRING_ARRAY);
        return noResult();
    }

    @Action
    public String pasteRecord() throws UnifyException {
        ReflectUtils.shallowBeanCopy(record, clipRecord);
        if (record instanceof BaseEntity) {
            ((BaseEntity) record).setId(null);
        }

        onPrepareView(record, true);
        loadSessionOnRefresh();
        return "switchcrud";
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
    public String refreshSearch() throws UnifyException {
        onRefreshSearch();
        return findRecords();
    }

    @Action
    public String refreshCrudViewer() throws UnifyException {
        onRefreshCrudViewer();
        loadSessionOnRefresh();
        return "refreshcrudviewer";
    }

    @Action
    public String prepareGenerateReport() throws UnifyException {
        List<T> contentList = null;
        if (mode == ManageRecordModifier.SEARCH) {
            if (recordList == null || recordList.isEmpty()) {
                return showMessageBox(noRecordMessage);
            }
            contentList = recordList;
        } else {
            if (record == null) {
                return showMessageBox(noRecordMessage);
            }
            contentList = new ArrayList<T>();
            contentList.add(record);
        }

        ReportOptions reportOptions =
                getCommonReportProvider().getDynamicReportOptions(entityClass.getName(), table.getColumnPropertyList());
        reportOptions.setReportResourcePath("/common/resource/report");
        reportOptions.setReportFormat(reportType);
        reportOptions.setColumnarLayout(mode != ManageRecordModifier.SEARCH);
        reportOptions.setContent(contentList);
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

    public int getModifier() {
        return modifier;
    }

    @Override
    protected void onInitialize() throws UnifyException {
        mode = ManageRecordModifier.SEARCH;
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
    protected void onOpenPage() throws UnifyException {
        if (mode == ManageRecordModifier.SEARCH) {
            if ((ManageRecordModifier.SEARCH_ON_OPEN & modifier) > 0) {
                findRecords();
            }
            updateSearch();
        }
    }

    @Override
    protected void onClosePage() throws UnifyException {
        table = null;
        recordList = null;
        oldRecord = null;
        record = null;
        clipRecord = null;
    }

    protected void switchToTableContentPanel() throws UnifyException {
        SwitchPanel switchPanel = getPageWidgetByShortName(SwitchPanel.class, "manageBodyPanel");
        switchPanel.switchContent("searchBodyPanel");
    }

    protected void switchToCrudViewerPanel() throws UnifyException {
        SwitchPanel switchPanel = getPageWidgetByShortName(SwitchPanel.class, "manageBodyPanel");
        switchPanel.switchContent("crudPanel");
    }

    protected String getCurrentSwitchShortName() throws UnifyException {
        SwitchPanel switchPanel = getPageWidgetByShortName(SwitchPanel.class, "manageBodyPanel");
        return switchPanel.getCurrentWidgetShortName();
    }

    protected void showSearchActionButtons() throws UnifyException {
        getPageWidgetByShortName(SearchCriteriaPanel.class, "searchPanel").showActionButtons();
    }

    protected void hideSearchActionButtons() throws UnifyException {
        getPageWidgetByShortName(SearchCriteriaPanel.class, "searchPanel").hideActionButtons();
    }

    protected void onLoseView(T record) throws UnifyException {
        onloadSessionOnLoseView();
    }

    protected boolean isEditable(T record) throws UnifyException {
        return true;
    }

    protected boolean isActivatable(T record) throws UnifyException {
        return false;
    }

    protected boolean isDeactivatable(T record) throws UnifyException {
        return false;
    }

    protected int activate(T record) throws UnifyException {
        return 0;
    }

    protected int deactivate(T record) throws UnifyException {
        return 0;
    }

    protected PageControllerSessionUtils getPageControllerSessionUtils() throws UnifyException {
        return (PageControllerSessionUtils) getComponent(CommonModuleNameConstants.PAGECONTROLLERSESSIONUTILS);
    }

    protected abstract List<T> find() throws UnifyException;

    protected abstract T find(U id) throws UnifyException;

    protected abstract T prepareCreate() throws UnifyException;

    protected abstract Object create(T record) throws UnifyException;

    protected abstract int update(T record) throws UnifyException;

    protected abstract int delete(T record) throws UnifyException;

    protected abstract void setCrudViewerEditable(boolean editable) throws UnifyException;

    /**
     * Returns the currently selected record from table list.
     * 
     * @throws UnifyException
     *             if an error occurs
     */
    protected T getSelectedRecord() throws UnifyException {
        return recordList.get(table.getViewIndex());
    }

    /**
     * Returns the ids of selected items in search result table.
     * 
     * @throws UnifyException
     *             if an error occurs
     */
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

    /**
     * Returns the descriptions of selected items in search result table.
     * 
     * @throws UnifyException
     *             if an error occurs
     */
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
        mode = ManageRecordModifier.SEARCH;

        // Search
        setVisible("addTblBtn", ManageRecordModifier.isAddable(modifier));
        setVisible("editTblBtn", ManageRecordModifier.isEditable(modifier));
        setVisible("viewTblBtn", ManageRecordModifier.isViewable(modifier));
        setVisible("deleteTblBtn", ManageRecordModifier.isDeletable(modifier));

        manageReportable();

        setDisabled("searchPanel", false);
        updateModeDescription();
    }

    protected void onPrepareView(T record, boolean onPaste) throws UnifyException {

    }

    protected void onPrepareCrudViewer(T record, int mode) throws UnifyException {

    }

    protected void onRefreshSearch() throws UnifyException {

    }

    protected void onRefreshCrudViewer() throws UnifyException {

    }

    protected Table getTable() {
        return table;
    }

    protected int getMode() {
        return mode;
    }

    protected boolean isEditableMode() {
        return mode == ManageRecordModifier.ADD || mode == ManageRecordModifier.MODIFY;
    }

    @SuppressWarnings("unchecked")
    private void manageReportable() throws UnifyException {
        boolean isReportable =
                ManageRecordModifier.isReportable(modifier)
                        && !getCommonReportProvider().isReportable(entityClass.getName());
        if (isReportable) {
            // Hide report button base on role privilege if necessary
            ManagedEntityPrivilegeNames managedPrivilegeNames =
                    ((Map<Class<? extends Entity>, ManagedEntityPrivilegeNames>) getApplicationAttribute(
                            JacklynApplicationAttributeConstants.MANAGED_PRIVILEGES)).get(entityClass);
            if (managedPrivilegeNames != null) {
                isReportable =
                        isRolePrivilege(PrivilegeCategoryConstants.REPORTABLE,
                                managedPrivilegeNames.getReportableName());
            }
        }

        setVisible("reportBtn", isReportable);
    }

    private void loadSessionOnCreate() throws UnifyException {
        getPageControllerSessionUtils().loadSession(this);
    }

    private void loadSessionOnRefresh() throws UnifyException {
        getPageControllerSessionUtils().loadSession(this);
    }

    private void onloadSessionOnLoseView() throws UnifyException {
        getPageControllerSessionUtils().unloadSession(this);
    }

    private void doCreate() throws UnifyException {
        Object id = create(record);
        if (id != null) {
            logUserEvent(EventType.CREATE, record, true);
            hintUser("$m{hint.record.create.success}", recordHintName);
        } else {
            hintUser("$m{hint.record.createtoworkflow.success}", recordHintName);
        }
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
    private void prepareView() throws UnifyException {
        int index = table.getViewIndex();
        record = find((U) recordList.get(index).getId());
        recordList.set(index, record);
        onPrepareView(record, false);
        loadSessionOnRefresh();
    }

    private void updateCrudViewer(int mode) throws UnifyException {
        this.mode = mode;

        // Navigation buttons
        int viewIndex = table.getViewIndex();
        boolean isCreateMode = mode == ManageRecordModifier.ADD;
        setVisible("pasteFrmBtn", isCreateMode);
        setVisible("firstFrmBtn", !isCreateMode);
        setVisible("prevFrmBtn", !isCreateMode);
        setVisible("nextFrmBtn", !isCreateMode);
        setVisible("lastFrmBtn", !isCreateMode);
        setVisible("copyFrmBtn", !isCreateMode);
        setVisible("itemOfLabel", !isCreateMode);
        if (isCreateMode) {
            // Enable/disable paste button
            setDisabled("pasteFrmBtn", clipRecord == null);
            setVisible("pasteFrmBtn", ManageRecordModifier.isPastable(modifier));
        } else {
            setDisabled("firstFrmBtn", recordList == null || viewIndex <= 0);
            setDisabled("prevFrmBtn", recordList == null || viewIndex <= 0);
            setDisabled("nextFrmBtn", recordList == null || viewIndex >= (recordList.size() - 1));
            setDisabled("lastFrmBtn", recordList == null || viewIndex >= (recordList.size() - 1));

            // For clipboard buttons
            setVisible("copyFrmBtn", ManageRecordModifier.isCopyable(modifier));
        }

        // Other buttons
        setVisible("createNextFrmBtn", false);
        setVisible("createCloseFrmBtn", false);
        setVisible("saveNextFrmBtn", false);
        setVisible("saveCloseFrmBtn", false);
        setVisible("deleteFrmBtn", false);
        setVisible("activateFrmBtn", false);
        setVisible("deactivateFrmBtn", false);
        setVisible("cancelFrmBtn", false);
        setVisible("doneFrmBtn", false);
        setPageValidationEnabled(false);

        setDisabled("searchPanel", true);

        switch (mode) {
            case ManageRecordModifier.ADD:
                setCrudViewerEditable(true);
                setVisible("createNextFrmBtn", true);
                setVisible("createCloseFrmBtn", true);
                setVisible("cancelFrmBtn", true);
                setPageValidationEnabled(true);
                break;
            case ManageRecordModifier.MODIFY:
                if (ManageRecordModifier.isEditable(modifier) && record != null && !record.isReserved()
                        && isEditable(record)) {
                    if (!ManageRecordModifier.isAlternateSave(modifier)) {
                        setVisible("saveNextFrmBtn", true);
                        setVisible("saveCloseFrmBtn", true);
                    }
                    setCrudViewerEditable(true);
                    setPageValidationEnabled(true);
                } else {
                    setCrudViewerEditable(false);
                }
                setVisible("cancelFrmBtn", true);
                break;
            case ManageRecordModifier.VIEW:
                setCrudViewerEditable(false);
                setVisible("doneFrmBtn", true);
                if (ManageRecordModifier.isActivatable(modifier)) {
                    setVisible("activateFrmBtn", isActivatable(record));
                    setVisible("deactivateFrmBtn", isDeactivatable(record));
                }
                break;

            case ManageRecordModifier.DELETE:
                setCrudViewerEditable(false);
                setVisible("deleteFrmBtn", true);
                setVisible("cancelFrmBtn", true);
                break;

            default:
                setDisabled("searchPanel", false);
                break;
        }

        // Index description
        if (recordList != null) {
            itemCountLabel = getSessionMessage("label.itemcount", viewIndex + 1, recordList.size());
        }

        updateModeDescription();
        onPrepareCrudViewer(record, mode);
    }

    private void updateModeDescription() throws UnifyException {
        modeDescription = null;
        switch (mode) {
            case ManageRecordModifier.ADD:
                modeDescription = "managerecord.mode.add";
                modeStyle = EventType.CREATE.colorMode();
                break;
            case ManageRecordModifier.DELETE:
                modeDescription = "managerecord.mode.delete";
                modeStyle = EventType.DELETE.colorMode();
                break;
            case ManageRecordModifier.MODIFY:
                modeDescription = "managerecord.mode.modify";
                modeStyle = EventType.UPDATE.colorMode();
                break;
            case ManageRecordModifier.VIEW:
                modeDescription = "managerecord.mode.view";
                modeStyle = EventType.VIEW.colorMode();
                break;
            case ManageRecordModifier.SEARCH:
            default:
                modeDescription = "managerecord.mode.search";
                modeStyle = EventType.SEARCH.colorMode();
                break;
        }

        modeDescription = getSessionMessage(modeDescription, recordHintName);
    }

    private String performModeAction() throws UnifyException {
        String result = null;
        switch (mode) {
            case ManageRecordModifier.ADD:
                return prepareCreateRecord();
            case ManageRecordModifier.DELETE:
                return prepareDeleteRecord();
            case ManageRecordModifier.MODIFY:
                return prepareUpdateRecord();
            case ManageRecordModifier.VIEW:
                return prepareViewRecord();
            case ManageRecordModifier.SEARCH:
                break;
            default:
                break;
        }
        return result;
    }
}
