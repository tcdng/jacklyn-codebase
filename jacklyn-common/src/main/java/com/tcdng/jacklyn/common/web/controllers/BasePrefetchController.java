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
import com.tcdng.unify.web.ui.widget.control.Table;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;
import com.tcdng.unify.web.ui.widget.panel.SwitchPanel;

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
public abstract class BasePrefetchController<T extends BaseEntityPageBean<V>, U, V extends Entity>
        extends BasePageController<T> {

    public static final String HIDEPOPUP_REFERESHMAIN = "hidepopuprefreshmain";

    private static final String SWITCH_MAPPING = "switch-mapping";

    private Class<V> entityClass;

    private int modifier;

    public BasePrefetchController(Class<T> pageBeanClass, Class<V> entityClass, int modifier) {
        super(pageBeanClass, ManageRecordModifier.isSecure(modifier), ReadOnly.FALSE, ResetOnWrite.FALSE);
        this.entityClass = entityClass;
        this.modifier = modifier;
    }

    @Action
    public String prepareViewRecord() throws UnifyException {
        prepareView(ManageRecordModifier.VIEW);
        BaseEntityPageBean<V> pageBean = getPageBean();
        logUserEvent(EventType.VIEW, pageBean.getRecord(), false);
        return getSwitchItemViewMapping();
    }

    @Action
    public String prepareUpdateRecord() throws UnifyException {
        prepareView(ManageRecordModifier.MODIFY);
        BaseEntityPageBean<V> pageBean = getPageBean();
        V oldRecord = ReflectUtils.shallowBeanCopy(pageBean.getRecord());
        pageBean.setOldRecord(oldRecord);
        return getSwitchItemViewMapping();
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
    public String refreshItemViewer() throws UnifyException {
        onRefreshItemViewer();
        loadSessionOnRefresh();
        return "refreshitemviewer";
    }

    @Action
    public String prepareGenerateReport() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        List<V> recordList = pageBean.getRecordList();
        if (recordList == null || recordList.isEmpty()) {
            return showMessageBox(pageBean.getNoRecordMessage());
        }

        ReportOptions reportOptions =
                getCommonReportProvider().getDynamicReportOptions(entityClass.getName(),
                        getTable().getColumnPropertyList());
        reportOptions.setReportResourcePath("/common/resource/report");
        reportOptions.setReportFormat(pageBean.getReportType());
        reportOptions.setContent(recordList);
        return showReportOptionsBox(reportOptions);
    }

    @Override
    protected void onInitPage() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
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
        if (pageBean.isPrefetch()) {
            findRecords();
            switchToTableContentPanel();
            pageBean.setPrefetch(false);
        }

        super.onOpenPage();
    }

    protected void setSwitchItemViewMapping(String mapping) throws UnifyException {
        setRequestAttribute(SWITCH_MAPPING, mapping);
    }

    protected void onLoseView(V record) throws UnifyException {
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

    protected abstract List<V> find() throws UnifyException;

    protected abstract V find(U id) throws UnifyException;

    protected abstract int update(V record) throws UnifyException;

    protected abstract void setItemViewerEditable(boolean editable) throws UnifyException;

    protected Table getTable() throws UnifyException {
        return getPageWidgetByShortName(Table.class, "tablePanel.contentTbl");
    }

    protected V getSelectedRecord() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        return pageBean.getRecordList().get(getTable().getViewIndex());
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
        setPageWidgetVisible("editTblBtn", ManageRecordModifier.isEditable(modifier));
        setPageWidgetVisible("viewTblBtn", ManageRecordModifier.isViewable(modifier));

        manageReportable();
    }

    protected void onPrepareView(V record) throws UnifyException {

    }

    protected void onPrepareItemViewer(V record) throws UnifyException {

    }

    protected void onRefreshSearch() throws UnifyException {

    }

    protected void onRefreshItemViewer() throws UnifyException {

    }

    protected String getSwitchItemViewMapping() throws UnifyException {
        String mapping = (String) getRequestAttribute(SWITCH_MAPPING);
        if (StringUtils.isNotBlank(mapping)) {
            return mapping;
        }

        return "switchitemview";
    }

    protected void setPrefetchOnly() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        pageBean.setPrefetch(true);
    }

    private void loseView() throws UnifyException {
        onLoseView((V) getPageBean().getRecord());
    }

    private String findRecords() throws UnifyException {
        prepareForPrefetch();

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

        setPageWidgetVisible("reportBtn", isReportable);
    }

    private void loadSessionOnRefresh() throws UnifyException {
        getPageControllerSessionUtils().loadSession(this);
    }

    private void onloadSessionOnLoseView() throws UnifyException {
        getPageControllerSessionUtils().unloadSession(this);
    }

    @SuppressWarnings("unchecked")
    private void prepareView(int mode) throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        pageBean.setMode(mode);

        Table table = getTable();
        int index = table.getViewIndex();
        List<V> recordList = pageBean.getRecordList();
        V record = find((U) recordList.get(index).getId());
        pageBean.setRecord(record);
        recordList.set(index, record);
        onPrepareView(record);
        loadSessionOnRefresh();

        // Navigation buttons
        int viewIndex = table.getViewIndex();
        setPageWidgetDisabled("firstFrmBtn", recordList == null || viewIndex <= 0);
        setPageWidgetDisabled("prevFrmBtn", recordList == null || viewIndex <= 0);
        setPageWidgetDisabled("nextFrmBtn", recordList == null || viewIndex >= (recordList.size() - 1));
        setPageWidgetDisabled("lastFrmBtn", recordList == null || viewIndex >= (recordList.size() - 1));

        // Index description
        if (recordList != null) {
            String itemCountLabel = getSessionMessage("label.itemcount", viewIndex + 1, recordList.size());
            pageBean.setItemCountLabel(itemCountLabel);
        }

        // Action buttons
        setPageWidgetVisible("saveNextFrmBtn", false);
        setPageWidgetVisible("saveCloseFrmBtn", false);
        setPageWidgetVisible("cancelFrmBtn", false);
        setPageWidgetVisible("doneFrmBtn", false);
        setPageValidationEnabled(false);

        String modeDescription = null;
        String modeStyle = null;
        switch (mode) {
            case ManageRecordModifier.MODIFY:
                setPageWidgetVisible("saveNextFrmBtn", true);
                setPageWidgetVisible("saveCloseFrmBtn", true);
                setItemViewerEditable(true);
                setPageValidationEnabled(true);
                setPageWidgetVisible("cancelFrmBtn", true);

                modeDescription = getSessionMessage("managerecord.mode.modify", pageBean.getRecordHintName());
                modeStyle = EventType.UPDATE.colorMode();
                break;
            case ManageRecordModifier.VIEW:
                setItemViewerEditable(false);
                setPageWidgetVisible("doneFrmBtn", true);

                modeDescription = getSessionMessage("managerecord.mode.view", pageBean.getRecordHintName());
                modeStyle = EventType.VIEW.colorMode();
                break;
            default:
                break;
        }

        pageBean.setModeDescription(modeDescription);
        pageBean.setModeStyle(modeStyle);

        // Call on update item viewer
        onPrepareItemViewer(record);
    }

    private String performModeAction() throws UnifyException {
        BaseEntityPageBean<V> pageBean = getPageBean();
        String result = null;
        switch (pageBean.getMode()) {
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
