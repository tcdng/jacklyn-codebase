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
import com.tcdng.jacklyn.common.web.beans.BaseEntityPageBean;
import com.tcdng.jacklyn.shared.organization.PrivilegeCategoryConstants;
import com.tcdng.unify.core.UnifyContainer;
import com.tcdng.unify.core.UnifyCorePropertyConstants;
import com.tcdng.unify.core.UnifyException;
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
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.ui.control.Table;
import com.tcdng.unify.web.ui.data.Hint.MODE;
import com.tcdng.unify.web.ui.panel.SearchCriteriaPanel;
import com.tcdng.unify.web.ui.panel.SwitchPanel;

/**
 * Convenient abstract base class for page controllers that manage entity CRUD
 * actions on records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@UplBinding("web/common/upl/managerecord.upl")
@ResultMappings({
        @ResultMapping(
                name = BaseEntityController.HIDEPOPUP_REFERESHMAIN,
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
public abstract class BaseEntityController<T extends BaseEntityPageBean<V>, U, V extends Entity>
        extends BasePageController<T> {

    public static final String HIDEPOPUP_REFERESHMAIN = "hidepopuprefreshmain";

    private static final String SWITCH_MAPPING = "switch-mapping";

    private Class<V> entityClass;

    private int modifier;

    public BaseEntityController(Class<T> pageBeanClass, Class<V> entityClass, int modifier) {
        super(pageBeanClass, ManageRecordModifier.isSecure(modifier), ReadOnly.FALSE, ResetOnWrite.FALSE);
        this.entityClass = entityClass;
        this.modifier = modifier;
    }

    @Action
    public String findRecords() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        List<V> recordList = find();
        pageBean.setRecordList(recordList);
        pageBean.setRecord(null);
        getTable().reset();
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
        V record = prepareCreate();
        BaseEntityPageBean<V> pageBean = getPageBean();
        pageBean.setRecord(record);
        loadSessionOnCreate();
        updateCrudViewer(ManageRecordModifier.ADD);
        return getSwitchCrudMapping();
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

        BaseEntityPageBean<V> pageBean = getPageBean();
        V record = pageBean.getRecord();
        logUserEvent(EventType.VIEW, record, false);
        if (ManageRecordModifier.isActivatable(modifier)) {
            V oldRecord = ReflectUtils.shallowBeanCopy(record);
            pageBean.setOldRecord(oldRecord);
        }
        updateCrudViewer(ManageRecordModifier.VIEW);
        return getSwitchCrudMapping();
    }

    @Action
    public String prepareUpdateRecord() throws UnifyException {
        prepareView();
        BaseEntityPageBean<V> pageBean = getPageBean();
        V record = pageBean.getRecord();
        V oldRecord = ReflectUtils.shallowBeanCopy(record);
        pageBean.setOldRecord(oldRecord);
        updateCrudViewer(ManageRecordModifier.MODIFY);
        return getSwitchCrudMapping();
    }

    @SuppressWarnings("unchecked")
    @Action
    public String updateAndNextRecord() throws UnifyException {
        doUpdate();

        Table table = getTable();
        BaseEntityPageBean<V> pageBean = getPageBean();
        V record = pageBean.getRecord();
        record = find((U) record.getId());
        pageBean.setRecord(record);
        pageBean.getRecordList().set(table.getViewIndex(), record);

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
        return getSwitchCrudMapping();
    }

    @Action
    public String deleteRecord() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        V record = pageBean.getRecord();
        delete(record);
        logUserEvent(EventType.DELETE, record, false);
        hintUser("$m{hint.record.delete.success}", pageBean.getRecordHintName());

        Table table = getTable();
        List<V> recordList = pageBean.getRecordList();
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
        BaseEntityPageBean<V> pageBean = getPageBean();
        V record = pageBean.getRecord();
        activate(record);
        logUserEvent(EventType.UPDATE, pageBean.getOldRecord(), record);
        return prepareViewRecord();
    }

    @Action
    public String deactivateRecord() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        V record = pageBean.getRecord();
        deactivate(record);
        logUserEvent(EventType.UPDATE, pageBean.getOldRecord(), record);
        return prepareViewRecord();
    }

    @Action
    public String copyRecord() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        V clipRecord = ReflectUtils.shallowBeanCopy(pageBean.getRecord());
        pageBean.setClipRecord(clipRecord);
        onCopy(clipRecord);
        if (ManageRecordModifier.isCopyToAdd(modifier)) {
            return prepareCreateRecord();
        }

        setPageAttribute("copyresponse", DataUtils.ZEROLEN_STRING_ARRAY);
        return noResult();
    }

    @Action
    public String pasteRecord() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        V record = pageBean.getRecord();
        ReflectUtils.shallowBeanCopy(record, pageBean.getClipRecord());
        if (record instanceof BaseEntity) {
            ((BaseEntity) record).setId(null);
        }

        onPrepareView(record, true);
        loadSessionOnRefresh();
        return getSwitchCrudMapping();
    }

    @Action
    public String firstRecord() throws UnifyException {
        loseView();
        getTable().setViewIndex(0);
        return performModeAction();
    }

    @Action
    public String previousRecord() throws UnifyException {
        loseView();
        Table table = getTable();
        table.setViewIndex(table.getViewIndex() - 1);
        return performModeAction();
    }

    @Action
    public String nextRecord() throws UnifyException {
        loseView();
        Table table = getTable();
        table.setViewIndex(table.getViewIndex() + 1);
        return performModeAction();
    }

    @Action
    public String lastRecord() throws UnifyException {
        loseView();
        BaseEntityPageBean<V> pageBean = getPageBean();
        getTable().setViewIndex(pageBean.getRecordList().size() - 1);
        return performModeAction();
    }

    @Action
    public String done() throws UnifyException {
        loseView();
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
        List<V> contentList = null;
        BaseEntityPageBean<V> pageBean = getPageBean();
        if (pageBean.getMode() == ManageRecordModifier.SEARCH) {
            List<V> recordList = pageBean.getRecordList();
            if (recordList == null || recordList.isEmpty()) {
                return showMessageBox(pageBean.getNoRecordMessage());
            }

            contentList = recordList;
        } else {
            V record = pageBean.getRecord();
            if (record == null) {
                return showMessageBox(pageBean.getNoRecordMessage());
            }

            contentList = new ArrayList<V>();
            contentList.add(record);
        }

        ReportOptions reportOptions =
                getCommonReportProvider().getDynamicReportOptions(entityClass.getName(),
                        getTable().getColumnPropertyList());
        reportOptions.setReportResourcePath("/common/resource/report");
        reportOptions.setReportFormat(pageBean.getReportType());
        // TODO Set report layout based on mode
        reportOptions.setContent(contentList);
        return showReportOptionsBox(reportOptions);
    }

    protected int getModifier() {
        return modifier;
    }

    @Override
    protected void onInitPage() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        pageBean.setMode(ManageRecordModifier.SEARCH);
        pageBean.setNoRecordMessage(resolveSessionMessage(pageBean.getNoRecordMessage()));
        pageBean.setRecordHintName(resolveSessionMessage(pageBean.getRecordHintName()));

        // Set validation page attributes
        getPage().setAttribute("validationClass", entityClass);
        getPage().setAttribute("validationIdClass", Long.class);

        manageReportable();
    }

    @Override
    protected void onIndexPage() throws UnifyException {
        super.onIndexPage();
        updateSearch();
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        if (pageBean.getMode() == ManageRecordModifier.SEARCH) {
            if ((ManageRecordModifier.SEARCH_ON_OPEN & modifier) > 0) {
                findRecords();
            }
            updateSearch();
        }
    }

    protected Table getTable() throws UnifyException {
        return getPageWidgetByShortName(Table.class, "tablePanel.contentTbl");
    }

    protected V getSelectedRecord() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        List<V> recordList = pageBean.getRecordList();
        return recordList.get(getTable().getViewIndex());
    }

    protected List<Long> getSelectedIds() throws UnifyException {
        Table table = getTable();
        if (table.getSelectedRows() > 0) {
            Integer[] selectedIndexes = table.getSelectedRowIndexes();
            List<Long> selectedIds = new ArrayList<Long>();
            BaseEntityPageBean<V> pageBean = getPageBean();
            List<V> recordList = pageBean.getRecordList();
            for (int i = 0; i < selectedIndexes.length; i++) {
                selectedIds.add((Long) recordList.get(selectedIndexes[i]).getId());
            }
            return selectedIds;
        }
        return Collections.emptyList();
    }

    protected String[] getSelectedDescription() throws UnifyException {
        String[] selectedDescriptions = DataUtils.ZEROLEN_STRING_ARRAY;
        Table table = getTable();
        if (table.getSelectedRows() > 0) {
            Integer[] selectedIndexes = table.getSelectedRowIndexes();
            selectedDescriptions = new String[selectedIndexes.length];
            if (Describable.class.isAssignableFrom(entityClass)) {
                BaseEntityPageBean<V> pageBean = getPageBean();
                List<V> recordList = pageBean.getRecordList();
                for (int i = 0; i < selectedIndexes.length; i++) {
                    selectedDescriptions[i] = ((Describable) recordList.get(selectedIndexes[i])).getDescription();
                }
            }
        }
        return selectedDescriptions;
    }

    protected void updateSearch() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        pageBean.setMode(ManageRecordModifier.SEARCH);

        // Search
        setPageWidgetVisible("addTblBtn", ManageRecordModifier.isAddable(modifier));
        setPageWidgetVisible("editTblBtn", ManageRecordModifier.isEditable(modifier));
        setPageWidgetVisible("viewTblBtn", ManageRecordModifier.isViewable(modifier));
        setPageWidgetVisible("deleteTblBtn", ManageRecordModifier.isDeletable(modifier));

        manageReportable();

        setPageWidgetDisabled("searchPanel", false);
        updateModeDescription();
    }

    protected void setSwitchCrudMapping(String mapping) throws UnifyException {
        setRequestAttribute(SWITCH_MAPPING, mapping);
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

    protected void onLoseView(V record) throws UnifyException {
        onloadSessionOnLoseView();
    }

    protected boolean isEditable(V record) throws UnifyException {
        return true;
    }

    protected boolean isActivatable(V record) throws UnifyException {
        return false;
    }

    protected boolean isDeactivatable(V record) throws UnifyException {
        return false;
    }

    protected int activate(V record) throws UnifyException {
        return 0;
    }

    protected int deactivate(V record) throws UnifyException {
        return 0;
    }

    protected PageControllerSessionUtils getPageControllerSessionUtils() throws UnifyException {
        return (PageControllerSessionUtils) getComponent(CommonModuleNameConstants.PAGECONTROLLERSESSIONUTILS);
    }

    protected abstract List<V> find() throws UnifyException;

    protected abstract V find(U id) throws UnifyException;

    protected abstract V prepareCreate() throws UnifyException;

    protected abstract Object create(V record) throws UnifyException;

    protected abstract int update(V record) throws UnifyException;

    protected abstract int delete(V record) throws UnifyException;

    protected abstract void setCrudViewerEditable(boolean editable) throws UnifyException;

    protected void onCopy(V recordCopy) throws UnifyException {

    }

    protected void onPrepareView(V record, boolean onPaste) throws UnifyException {

    }

    protected void onPrepareCrudViewer(V record, int mode) throws UnifyException {

    }

    protected void onRefreshSearch() throws UnifyException {

    }

    protected void onRefreshCrudViewer() throws UnifyException {

    }

    protected int getMode() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        return pageBean.getMode();
    }

    protected boolean isEditableMode() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        int mode = pageBean.getMode();
        return mode == ManageRecordModifier.ADD || mode == ManageRecordModifier.MODIFY;
    }

    protected String getSwitchCrudMapping() throws UnifyException {
        String mapping = (String) getRequestAttribute(SWITCH_MAPPING);
        if (StringUtils.isNotBlank(mapping)) {
            return mapping;
        }

        return "switchcrud";
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
                        isCurrentRolePrivilege(PrivilegeCategoryConstants.REPORTABLE,
                                managedPrivilegeNames.getReportableName());
            }
        }

        setPageWidgetVisible("reportBtn", isReportable);
    }

    private void loseView() throws UnifyException {
        onLoseView((V) getPageBean().getRecord());
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
        BaseEntityPageBean<V> pageBean = getPageBean();
        V record = pageBean.getRecord();
        Object id = create(record);
        if (id != null) {
            logUserEvent(EventType.CREATE, record, true);
            hintUser("$m{hint.record.create.success}", pageBean.getRecordHintName());
        } else {
            hintUser("$m{hint.record.createtoworkflow.success}", pageBean.getRecordHintName());
        }
    }

    private void doUpdate() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        V record = pageBean.getRecord();
        int result = update(record);
        if (result > 0) {
            logUserEvent(EventType.UPDATE, pageBean.getOldRecord(), record);
            hintUser("$m{hint.record.update.success}", pageBean.getRecordHintName());
        } else {
            hintUser("$m{hint.record.updatetoworkflow.success}", pageBean.getRecordHintName());
        }
    }

    @SuppressWarnings("unchecked")
    private void prepareView() throws UnifyException {
        int index = getTable().getViewIndex();
        BaseEntityPageBean<V> pageBean = getPageBean();
        List<V> recordList = pageBean.getRecordList();
        V record = find((U) recordList.get(index).getId());
        pageBean.setRecord(record);
        recordList.set(index, record);
        onPrepareView(record, false);
        loadSessionOnRefresh();
    }

    private void updateCrudViewer(int mode) throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        pageBean.setMode(mode);

        V record = pageBean.getRecord();
        List<V> recordList = pageBean.getRecordList();
        // Navigation buttons
        int viewIndex = getTable().getViewIndex();
        boolean isCreateMode = mode == ManageRecordModifier.ADD;
        setPageWidgetVisible("pasteFrmBtn", isCreateMode);
        setPageWidgetVisible("firstFrmBtn", !isCreateMode);
        setPageWidgetVisible("prevFrmBtn", !isCreateMode);
        setPageWidgetVisible("nextFrmBtn", !isCreateMode);
        setPageWidgetVisible("lastFrmBtn", !isCreateMode);
        setPageWidgetVisible("copyFrmBtn", !isCreateMode);
        setPageWidgetVisible("itemOfLabel", !isCreateMode);
        if (isCreateMode) {
            // Enable/disable paste button
            setPageWidgetDisabled("pasteFrmBtn", pageBean.getClipRecord() == null);
            setPageWidgetVisible("pasteFrmBtn", ManageRecordModifier.isPastable(modifier));
        } else {
            setPageWidgetDisabled("firstFrmBtn", recordList == null || viewIndex <= 0);
            setPageWidgetDisabled("prevFrmBtn", recordList == null || viewIndex <= 0);
            setPageWidgetDisabled("nextFrmBtn", recordList == null || viewIndex >= (recordList.size() - 1));
            setPageWidgetDisabled("lastFrmBtn", recordList == null || viewIndex >= (recordList.size() - 1));

            // For clipboard buttons
            setPageWidgetVisible("copyFrmBtn", ManageRecordModifier.isCopyable(modifier));
        }

        // Other buttons
        setPageWidgetVisible("createNextFrmBtn", false);
        setPageWidgetVisible("createCloseFrmBtn", false);
        setPageWidgetVisible("saveNextFrmBtn", false);
        setPageWidgetVisible("saveCloseFrmBtn", false);
        setPageWidgetVisible("deleteFrmBtn", false);
        setPageWidgetVisible("activateFrmBtn", false);
        setPageWidgetVisible("deactivateFrmBtn", false);
        setPageWidgetVisible("cancelFrmBtn", false);
        setPageWidgetVisible("doneFrmBtn", false);
        setPageValidationEnabled(false);

        setPageWidgetDisabled("searchPanel", true);

        switch (mode) {
            case ManageRecordModifier.ADD:
                setCrudViewerEditable(true);
                setPageWidgetVisible("createNextFrmBtn", true);
                setPageWidgetVisible("createCloseFrmBtn", true);
                setPageWidgetVisible("cancelFrmBtn", true);
                setPageValidationEnabled(true);
                break;
            case ManageRecordModifier.MODIFY:
                if (ManageRecordModifier.isEditable(modifier) && record != null && !record.isReserved()
                        && isEditable(record)) {
                    if (!ManageRecordModifier.isAlternateSave(modifier)) {
                        setPageWidgetVisible("saveNextFrmBtn", true);
                        setPageWidgetVisible("saveCloseFrmBtn", true);
                    }
                    setCrudViewerEditable(true);
                    setPageValidationEnabled(true);
                } else {
                    setCrudViewerEditable(false);
                }
                setPageWidgetVisible("cancelFrmBtn", true);
                break;
            case ManageRecordModifier.VIEW:
                setCrudViewerEditable(false);
                setPageWidgetVisible("doneFrmBtn", true);
                if (ManageRecordModifier.isActivatable(modifier)) {
                    setPageWidgetVisible("activateFrmBtn", isActivatable(record));
                    setPageWidgetVisible("deactivateFrmBtn", isDeactivatable(record));
                }
                break;

            case ManageRecordModifier.DELETE:
                setCrudViewerEditable(false);
                setPageWidgetVisible("deleteFrmBtn", true);
                setPageWidgetVisible("cancelFrmBtn", true);
                break;

            default:
                setPageWidgetDisabled("searchPanel", false);
                break;
        }

        // Index description
        if (recordList != null) {
            String itemCountLabel = getSessionMessage("label.itemcount", viewIndex + 1, recordList.size());
            pageBean.setItemCountLabel(itemCountLabel);
        }

        updateModeDescription();
        onPrepareCrudViewer(record, mode);
    }

    private void updateModeDescription() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        String modeDescription = null;
        String modeStyle = null;
        switch (pageBean.getMode()) {
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

        modeDescription = getSessionMessage(modeDescription, pageBean.getRecordHintName());
        pageBean.setModeDescription(modeDescription);
        pageBean.setModeStyle(modeStyle);
    }

    private String performModeAction() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        String result = null;
        switch (pageBean.getMode()) {
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
