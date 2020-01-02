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
package com.tcdng.jacklyn.system.business;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.constants.JacklynApplicationAttributeConstants;
import com.tcdng.jacklyn.common.constants.JacklynContainerPropertyConstants;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.shared.system.SystemAssetType;
import com.tcdng.jacklyn.shared.system.data.ToolingDocumentListItem;
import com.tcdng.jacklyn.shared.system.data.ToolingEntityFieldItem;
import com.tcdng.jacklyn.shared.system.data.ToolingEntityItem;
import com.tcdng.jacklyn.shared.system.data.ToolingListTypeItem;
import com.tcdng.jacklyn.shared.system.data.ToolingTransformerTypeItem;
import com.tcdng.jacklyn.shared.xml.config.module.InputControlConfig;
import com.tcdng.jacklyn.shared.xml.config.module.MenuConfig;
import com.tcdng.jacklyn.shared.xml.config.module.MenuItemConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ShortcutTileConfig;
import com.tcdng.jacklyn.shared.xml.config.module.SysParamConfig;
import com.tcdng.jacklyn.system.constants.SystemDataSourceTaskConstants;
import com.tcdng.jacklyn.system.constants.SystemDataSourceTaskParamConstants;
import com.tcdng.jacklyn.system.constants.SystemModuleErrorConstants;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.jacklyn.system.constants.SystemReservedUserConstants;
import com.tcdng.jacklyn.system.constants.SystemSchedTaskConstants;
import com.tcdng.jacklyn.system.data.AuthenticationLargeData;
import com.tcdng.jacklyn.system.data.DashboardDef;
import com.tcdng.jacklyn.system.data.DashboardLargeData;
import com.tcdng.jacklyn.system.data.DashboardLayerDef;
import com.tcdng.jacklyn.system.data.DashboardPortletDef;
import com.tcdng.jacklyn.system.data.ScheduledTaskDef;
import com.tcdng.jacklyn.system.data.ScheduledTaskLargeData;
import com.tcdng.jacklyn.system.data.SystemControlState;
import com.tcdng.jacklyn.system.entities.ApplicationMenu;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItem;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItemQuery;
import com.tcdng.jacklyn.system.entities.ApplicationMenuQuery;
import com.tcdng.jacklyn.system.entities.Authentication;
import com.tcdng.jacklyn.system.entities.AuthenticationQuery;
import com.tcdng.jacklyn.system.entities.Dashboard;
import com.tcdng.jacklyn.system.entities.DashboardLayer;
import com.tcdng.jacklyn.system.entities.DashboardPortlet;
import com.tcdng.jacklyn.system.entities.DashboardQuery;
import com.tcdng.jacklyn.system.entities.DataSource;
import com.tcdng.jacklyn.system.entities.DataSourceDriver;
import com.tcdng.jacklyn.system.entities.DataSourceDriverQuery;
import com.tcdng.jacklyn.system.entities.DataSourceQuery;
import com.tcdng.jacklyn.system.entities.InputCtrlDef;
import com.tcdng.jacklyn.system.entities.InputCtrlDefQuery;
import com.tcdng.jacklyn.system.entities.Module;
import com.tcdng.jacklyn.system.entities.ModuleQuery;
import com.tcdng.jacklyn.system.entities.ScheduledTask;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHist;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHistQuery;
import com.tcdng.jacklyn.system.entities.ScheduledTaskQuery;
import com.tcdng.jacklyn.system.entities.ShortcutTile;
import com.tcdng.jacklyn.system.entities.ShortcutTileQuery;
import com.tcdng.jacklyn.system.entities.SupportedLocale;
import com.tcdng.jacklyn.system.entities.SupportedLocaleQuery;
import com.tcdng.jacklyn.system.entities.SystemAsset;
import com.tcdng.jacklyn.system.entities.SystemAssetQuery;
import com.tcdng.jacklyn.system.entities.SystemParameter;
import com.tcdng.jacklyn.system.entities.SystemParameterQuery;
import com.tcdng.jacklyn.system.entities.Theme;
import com.tcdng.jacklyn.system.entities.ThemeQuery;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyCorePropertyConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Broadcast;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Id;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.Tooling;
import com.tcdng.unify.core.annotation.TransactionAttribute;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.ApplicationAttributeConstants;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.AggregateType;
import com.tcdng.unify.core.data.Document;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.Input;
import com.tcdng.unify.core.data.Inputs;
import com.tcdng.unify.core.database.AbstractEntity;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.database.sql.DynamicSqlDataSourceConfig;
import com.tcdng.unify.core.database.sql.DynamicSqlDataSourceManager;
import com.tcdng.unify.core.database.sql.SqlDialectNameConstants;
import com.tcdng.unify.core.list.ListCommand;
import com.tcdng.unify.core.list.ListManager;
import com.tcdng.unify.core.security.TwoWayStringCryptograph;
import com.tcdng.unify.core.system.entities.AbstractSequencedEntity;
import com.tcdng.unify.core.system.entities.ParameterDef;
import com.tcdng.unify.core.system.entities.UserSessionTrackingQuery;
import com.tcdng.unify.core.task.Task;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskManager;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskStatus;
import com.tcdng.unify.core.task.TaskStatusLogger;
import com.tcdng.unify.core.task.TaskableMethodConfig;
import com.tcdng.unify.core.transform.Transformer;
import com.tcdng.unify.core.ui.Menu;
import com.tcdng.unify.core.ui.MenuItem;
import com.tcdng.unify.core.ui.MenuItemSet;
import com.tcdng.unify.core.ui.MenuLoader;
import com.tcdng.unify.core.ui.MenuSet;
import com.tcdng.unify.core.ui.Tile;
import com.tcdng.unify.core.upl.UplUtils;
import com.tcdng.unify.core.util.AnnotationUtils;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.RemoteCallController;
import com.tcdng.unify.web.annotation.RemoteAction;

/**
 * Default implementation of system business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(SystemModuleNameConstants.SYSTEMSERVICE)
public class SystemServiceImpl extends AbstractJacklynBusinessService implements SystemService {

    private static final String SCHEDULED_TASK = "scheduledTask";

    @Configurable
    private TaskManager taskManager;

    @Configurable
    private ListManager listManager;

    @Configurable("scheduledtaskstatuslogger")
    private TaskStatusLogger taskStatusLogger;

    @Configurable
    private DynamicSqlDataSourceManager dataSourceManager;

    private FactoryMap<Long, ScheduledTaskDef> scheduledTaskDefs;

    private FactoryMap<String, DashboardDef> dashboardDefs;

    public SystemServiceImpl() {
        scheduledTaskDefs = new FactoryMap<Long, ScheduledTaskDef>(true) {

            @Override
            protected boolean stale(Long scheduledTaskId, ScheduledTaskDef scheduledTaskDef) throws Exception {
                boolean stale = false;
                try {
                    Date updateDt = db().value(Date.class, "updateDt", new ScheduledTaskQuery().id(scheduledTaskId));
                    stale = resolveUTC(updateDt) != scheduledTaskDef.getTimestamp();
                } catch (Exception e) {
                    logError(e);
                }

                return stale;
            }

            @Override
            protected ScheduledTaskDef create(Long scheduledTaskId, Object... params) throws Exception {
                ScheduledTask scheduledTask = db().find(ScheduledTask.class, scheduledTaskId);

                String lock = "scheduledtask-lock" + scheduledTaskId;
                long startTimeOffset = CalendarUtils.getTimeOfDayOffset(scheduledTask.getStartTime());
                long endTimeOffset = 0;
                if (scheduledTask.getEndTime() != null) {
                    endTimeOffset = CalendarUtils.getTimeOfDayOffset(scheduledTask.getEndTime());
                } else {
                    endTimeOffset = CalendarUtils.getTimeOfDayOffset(CalendarUtils.getLastSecondDate(getNow()));
                }

                long repeatMillSecs = 0;
                if (scheduledTask.getFrequency() != null && scheduledTask.getFrequencyUnit() != null) {
                    repeatMillSecs =
                            CalendarUtils.getMilliSecondsByFrequency(scheduledTask.getFrequencyUnit(),
                                    scheduledTask.getFrequency());
                }

                List<Input<?>> inputList =
                        getParameterService()
                                .fetchNormalizedInputs(scheduledTask.getTaskName(), SCHEDULED_TASK, scheduledTaskId)
                                .getInputList();
                return new ScheduledTaskDef(lock, scheduledTask.getDescription(), scheduledTask.getTaskName(),
                        startTimeOffset, endTimeOffset, repeatMillSecs, scheduledTask.getWeekdays(),
                        scheduledTask.getDays(), scheduledTask.getMonths(), inputList,
                        resolveUTC(scheduledTask.getUpdateDt()));
            }
        };

        dashboardDefs = new FactoryMap<String, DashboardDef>(true) {

            @Override
            protected boolean stale(String name, DashboardDef dashboardDef) throws Exception {
                boolean stale = false;
                try {
                    Date updateDt = db().value(Date.class, "updateDt", new DashboardQuery().name(name));
                    stale = resolveUTC(updateDt) != dashboardDef.getTimestamp();
                } catch (Exception e) {
                    logError(e);
                }

                return stale;
            }

            @Override
            protected DashboardDef create(String name, Object... params) throws Exception {
                Dashboard dashboard = db().find(new DashboardQuery().name(name));
                if (dashboard == null) {
                    throw new UnifyException(SystemModuleErrorConstants.DASHBOARD_NAME_UNKNOWN, name);
                }

                Map<String, List<DashboardPortletDef>> portletDefs = new HashMap<String, List<DashboardPortletDef>>();
                for (DashboardPortlet dashboardPortlet : dashboard.getPortletList()) {
                    List<DashboardPortletDef> portletList = portletDefs.get(dashboardPortlet.getLayerName());
                    if (portletList == null) {
                        portletList = new ArrayList<DashboardPortletDef>();
                        portletDefs.put(dashboardPortlet.getLayerName(), portletList);
                    }

                    long refreshMillSec =
                            dashboardPortlet.getRefreshPeriod() != null ? CalendarUtils.getMilliSecondsByFrequency(
                                    FrequencyUnit.SECOND, dashboardPortlet.getRefreshPeriod()) : 0L;
                    portletList.add(new DashboardPortletDef(dashboardPortlet.getName(), dashboardPortlet.getPanelName(),
                            dashboardPortlet.getNumberOfSections(), refreshMillSec));
                }

                List<DashboardLayerDef> layerList = new ArrayList<DashboardLayerDef>();
                for (DashboardLayer dashboardLayer : dashboard.getLayerList()) {
                    layerList.add(new DashboardLayerDef(dashboardLayer.getName(), dashboardLayer.getNumberOfSections(),
                            DataUtils.unmodifiableList(portletDefs.get(dashboardLayer.getName()))));
                }

                String dashboardViewer =
                        UplUtils.generateUplGeneratorTargetName("dashboard-generator", dashboard.getName());
                return new DashboardDef(dashboard.getName(), dashboard.getOrientationType(),
                        resolveUTC(dashboard.getUpdateDt()), DataUtils.unmodifiableList(layerList), dashboardViewer);
            }
        };
    }

    @Override
    public Long createDashboard(Dashboard dashboard) throws UnifyException {
        return (Long) db().create(dashboard);
    }

    @Override
    public DashboardLargeData findDashboard(Long id) throws UnifyException {
        return new DashboardLargeData(db().list(Dashboard.class, id));
    }

    @Override
    public Dashboard findDashboard(String name) throws UnifyException {
        return db().find(new DashboardQuery().name(name));
    }

    @Override
    public List<Dashboard> findDashboards(DashboardQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateDashboard(DashboardLargeData dashboardLargeData) throws UnifyException {
        return db().updateByIdVersion(dashboardLargeData.getData());
    }

    @Override
    public int deleteDashboard(Long id) throws UnifyException {
        return db().delete(Dashboard.class, id);
    }

    @Override
    public Long createAuthentication(AuthenticationLargeData authenticationFormData) throws UnifyException {
        Authentication authentication = getEncryptedAuthentication(authenticationFormData);
        return (Long) db().create(authentication);
    }

    @Override
    public DashboardDef getRuntimeDashboardDef(String name) throws UnifyException {
        return dashboardDefs.get(name);
    }

    @Override
    public AuthenticationLargeData findAuthentication(Long authenticationId) throws UnifyException {
        Authentication authentication = db().find(Authentication.class, authenticationId);
        return internalFindAuthentication(authentication);
    }

    @Override
    public AuthenticationLargeData findAuthentication(String name) throws UnifyException {
        Authentication authentication = db().find(new AuthenticationQuery().name(name));
        return internalFindAuthentication(authentication);
    }

    @Override
    public List<Authentication> findAuthentications(AuthenticationQuery query) throws UnifyException {
        Query<Authentication> cloneQuery = query.copy();
        cloneQuery.addSelect("id", "name", "description", "cryptograph", "status", "statusDesc");
        return db().listAll(cloneQuery);
    }

    @Override
    public int updateAuthentication(AuthenticationLargeData authenticationFormData) throws UnifyException {
        Authentication authentication = getEncryptedAuthentication(authenticationFormData);
        return db().updateByIdVersion(authentication);
    }

    @Override
    public int deleteAuthentication(Long id) throws UnifyException {
        return db().delete(Authentication.class, id);
    }

    @Override
    public Long createRemoteModule(Module module) throws UnifyException {
        module.setRemote(Boolean.TRUE);
        return (Long) db().create(module);
    }

    @Override
    public Module findModule(String name) throws UnifyException {
        return db().list(new ModuleQuery().name(name));
    }

    @Override
    public Module findModule(Long id) throws UnifyException {
        return db().list(Module.class, id);
    }

    @Override
    public List<Module> findModules(ModuleQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public Long getModuleId(String moduleName) throws UnifyException {
        return db().value(Long.class, "id", new ModuleQuery().name(moduleName));
    }

    @Override
    public <T> Map<T, Module> findModules(Class<T> keyClass, String keyName, ModuleQuery query) throws UnifyException {
        return db().listAllMap(keyClass, keyName, query);
    }

    @Override
    public void activateModule(String name) throws UnifyException {
        updateModuleStatus(name, RecordStatus.ACTIVE);
    }

    @Override
    public void deactivateModule(String name) throws UnifyException {
        updateModuleStatus(name, RecordStatus.INACTIVE);
    }

    @Override
    public List<ApplicationMenuItem> findMenuItems(ApplicationMenuItemQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public ApplicationMenuItem findMenuItem(Long id) throws UnifyException {
        return db().list(ApplicationMenuItem.class, id);
    }

    @Override
    public List<ApplicationMenu> findMenus(ApplicationMenuQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public ApplicationMenu findMenu(Long id) throws UnifyException {
        return db().list(ApplicationMenu.class, id);
    }

    @Override
    public SystemParameter findSysParameter(String name) throws UnifyException {
        return db().list(new SystemParameterQuery().name(name));
    }

    @Override
    public SystemParameter findSysParameter(Long id) throws UnifyException {
        return db().list(SystemParameter.class, id);
    }

    @Override
    public List<SystemParameter> findSysParameters(SystemParameterQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public List<SystemControlState> findSystemControlStates(SystemParameterQuery query) throws UnifyException {
        List<SystemControlState> systemControlStateList = new ArrayList<SystemControlState>();
        Restriction criteria = query.getRestrictions();
        Query<SystemParameter> innerQuery =
                query.copyNoCriteria().addRestriction(criteria).addEquals("control", Boolean.TRUE).addOrder("name");
        int index = 0;
        List<SystemParameter> list = db().findAll(innerQuery);
        for (SystemParameter sysParameter : list) {
            systemControlStateList.add(new SystemControlState(index++, sysParameter.getName(),
                    sysParameter.getDescription(), convert(boolean.class, sysParameter.getValue(), null)));
        }
        return systemControlStateList;
    }

    @Override
    public int setSysParameterValue(String name, Object value) throws UnifyException {
        return db().updateAll(new SystemParameterQuery().name(name),
                new Update().add("value", convert(String.class, value, null)));
    }

    @Override
    public <T> T getSysParameterValue(Class<T> clazz, String name) throws UnifyException {
        SystemParameter sysParameter = findSysParameter(name);
        if (sysParameter == null) {
            throw new UnifyException(SystemModuleErrorConstants.SYSPARAM_WITH_NAME_UNKNOWN, name);
        }
        return convert(clazz, sysParameter.getValue(), null);
    }

    @Override
    public Long createScheduledTask(ScheduledTask scheduledTask) throws UnifyException {
        return (Long) db().create(scheduledTask);
    }

    @Override
    public Long createScheduledTask(ScheduledTaskLargeData scheduledTaskFormData) throws UnifyException {
        ScheduledTask scheduledTask = scheduledTaskFormData.getData();
        Long scheduledTaskId = (Long) db().create(scheduledTask);
        Inputs inputs = scheduledTaskFormData.getScheduledTaskParams();
        if (inputs.size() > 0) {
            getParameterService().updateParameterValues(scheduledTask.getTaskName(), SCHEDULED_TASK, scheduledTaskId,
                    inputs);
        }

        return scheduledTaskId;
    }

    @Override
    public List<ScheduledTask> findScheduledTasks(ScheduledTaskQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public ScheduledTask findScheduledTask(Long id) throws UnifyException {
        return db().find(ScheduledTask.class, id);
    }

    @Override
    public ScheduledTaskLargeData findScheduledTaskDocument(Long id) throws UnifyException {
        ScheduledTask scheduledTask = db().find(ScheduledTask.class, id);
        Inputs parameterValues =
                getParameterService().fetchNormalizedInputs(scheduledTask.getTaskName(), SCHEDULED_TASK, id);
        return new ScheduledTaskLargeData(scheduledTask, parameterValues);
    }

    @Override
    public int updateScheduledTask(ScheduledTask scheduledTask) throws UnifyException {
        return db().updateByIdVersion(scheduledTask);
    }

    @Override
    public int updateScheduledTask(ScheduledTaskLargeData scheduledTaskFormData) throws UnifyException {
        ScheduledTask scheduledTask = scheduledTaskFormData.getData();
        int updateCount = db().updateByIdVersion(scheduledTask);
        Inputs inputs = scheduledTaskFormData.getScheduledTaskParams();
        if (inputs.size() > 0) {
            getParameterService().updateParameterValues(scheduledTask.getTaskName(), SCHEDULED_TASK,
                    scheduledTask.getId(), inputs);
        }

        return updateCount;
    }

    @Override
    public int deleteScheduledTask(Long scheduledTaskId) throws UnifyException {
        String taskName = db().value(String.class, "taskName", new ScheduledTaskQuery().id(scheduledTaskId));
        getParameterService().deleteParameterValues(taskName, SCHEDULED_TASK, scheduledTaskId);
        return db().delete(ScheduledTask.class, scheduledTaskId);
    }

    @Override
    public ScheduledTaskLargeData loadScheduledTaskDocumentValues(ScheduledTaskLargeData scheduledTaskFormData)
            throws UnifyException {
        ScheduledTask scheduledTask = scheduledTaskFormData.getData();
        Inputs parameterValues =
                getParameterService().fetchNormalizedInputs(scheduledTask.getTaskName(), SCHEDULED_TASK,
                        scheduledTask.getId());
        return new ScheduledTaskLargeData(scheduledTask, parameterValues);
    }

    @Override
    public int activateScheduledTask(Long schduledTaskId) throws UnifyException {
        return db().updateAll(new ScheduledTaskQuery().id(schduledTaskId),
                new Update().add("status", RecordStatus.ACTIVE));
    }

    @Override
    public int deactivateScheduledTask(Long schduledTaskId) throws UnifyException {
        return db().updateAll(new ScheduledTaskQuery().id(schduledTaskId),
                new Update().add("status", RecordStatus.INACTIVE));
    }

    @Override
    public Long createScheduledTaskHistory(Long scheduledTaskId, TaskStatus taskStatus, String errorMessages)
            throws UnifyException {
        ScheduledTaskHist scheduledTaskHist = new ScheduledTaskHist();
        scheduledTaskHist.setScheduledTaskId(scheduledTaskId);
        scheduledTaskHist.setStartedOn(db().getNow());
        scheduledTaskHist.setErrorMsg(errorMessages);
        scheduledTaskHist.setTaskStatus(taskStatus);
        return (Long) db().create(scheduledTaskHist);
    }

    @Override
    public void releaseScheduledTask(Long scheduledTaskId, Long scheduledTaskHistId, TaskStatus completionTaskStatus,
            String errorMsg) throws UnifyException {
        // Release lock on scheduled task
        releaseClusterLock(scheduledTaskDefs.get(scheduledTaskId).getLock());

        // Update history
        ScheduledTaskHist scheduledTaskHist = db().find(ScheduledTaskHist.class, scheduledTaskHistId);
        scheduledTaskHist.setFinishedOn(db().getNow());
        scheduledTaskHist.setTaskStatus(completionTaskStatus);
        scheduledTaskHist.setErrorMsg(errorMsg);
        db().updateByIdVersion(scheduledTaskHist);
    }

    @Override
    public List<ScheduledTaskHist> findScheduledTaskHistory(ScheduledTaskHistQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public ScheduledTaskHist findScheduledTaskHist(Long id) throws UnifyException {
        return db().list(ScheduledTaskHist.class, id);
    }

    @Override
    public Long createTheme(Theme theme) throws UnifyException {
        return (Long) db().create(theme);
    }

    @Override
    public Theme findTheme(Long themeId) throws UnifyException {
        return db().find(Theme.class, themeId);
    }

    @Override
    public List<Theme> findThemes(ThemeQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateTheme(Theme theme) throws UnifyException {
        return db().updateByIdVersion(theme);
    }

    @Override
    public int deleteTheme(Long id) throws UnifyException {
        return db().delete(Theme.class, id);
    }

    @Override
    public Long createSupportedLocale(SupportedLocale supportedLocale) throws UnifyException {
        return (Long) db().create(supportedLocale);
    }

    @Override
    public SupportedLocale findSupportedLocale(Long supportedLocaleId) throws UnifyException {
        return db().find(SupportedLocale.class, supportedLocaleId);
    }

    @Override
    public List<SupportedLocale> findSupportedLocales(SupportedLocaleQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateSupportedLocale(SupportedLocale supportedLocale) throws UnifyException {
        return db().updateByIdVersion(supportedLocale);
    }

    @Override
    public int deleteSupportedLocale(Long id) throws UnifyException {
        return db().delete(SupportedLocale.class, id);
    }

    @Override
    public Long createInputCtrlDef(InputCtrlDef inputCtrlDef) throws UnifyException {
        return (Long) db().create(inputCtrlDef);
    }

    @Override
    public InputCtrlDef findInputCtrlDef(Long inputCtrlDefId) throws UnifyException {
        return db().find(InputCtrlDef.class, inputCtrlDefId);
    }

    @Override
    public List<InputCtrlDef> findInputCtrlDefs(InputCtrlDefQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateInputCtrlDef(InputCtrlDef inputCtrlDef) throws UnifyException {
        return db().updateByIdVersion(inputCtrlDef);
    }

    @Override
    public int deleteInputCtrlDef(Long id) throws UnifyException {
        return db().delete(InputCtrlDef.class, id);
    }

    @Override
    public SystemAsset findSystemAsset(Long systemAssetId) throws UnifyException {
        return db().list(SystemAsset.class, systemAssetId);
    }

    @Override
    public List<SystemAsset> findSystemAssets(SystemAssetQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public List<Long> findSystemAssetIds(SystemAssetQuery query) throws UnifyException {
        return db().valueList(Long.class, "id", query);
    }

    @Override
    public int getUniqueActiveUserSessions() throws UnifyException {
        return Integer.valueOf((String) db()
                .aggregate(AggregateType.COUNT,
                        new UserSessionTrackingQuery().loggedIn().addSelect("userLoginId").setDistinct(true))
                .getValue());
    }

    @Override
    public Long createDataSourceDriver(DataSourceDriver datasourceDriver) throws UnifyException {
        return (Long) db().create(datasourceDriver);
    }

    @Override
    public DataSourceDriver findDataSourceDriver(Long datasourceDriverId) throws UnifyException {
        return db().find(DataSourceDriver.class, datasourceDriverId);
    }

    @Override
    public List<DataSourceDriver> findDataSourceDrivers(DataSourceDriverQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateDataSourceDriver(DataSourceDriver datasourceDriver) throws UnifyException {
        return db().updateByIdVersion(datasourceDriver);
    }

    @Override
    public int deleteDataSourceDriver(Long id) throws UnifyException {
        return db().delete(DataSourceDriver.class, id);
    }

    @Override
    public Long createDataSource(DataSource dataSourceName) throws UnifyException {
        return (Long) db().create(dataSourceName);
    }

    @Override
    public DataSource findDataSource(Long dataSourceId) throws UnifyException {
        return db().list(DataSource.class, dataSourceId);
    }

    @Override
    public DataSource findDataSource(String dataSourceName) throws UnifyException {
        return db().list(new DataSourceQuery().name(dataSourceName));
    }

    @Override
    public List<DataSource> findDataSources(DataSourceQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateDataSource(DataSource dataSource) throws UnifyException {
        int updateCount = db().updateByIdVersion(dataSource);
        if (dataSourceManager.isConfigured(dataSource.getName())) {
            dataSourceManager.reconfigure(getDynamicSqlDataSourceConfig(dataSource));
        }

        return updateCount;
    }

    @Override
    public int deleteDataSource(Long id) throws UnifyException {
        DataSource dataSource = db().list(new DataSourceQuery().id(id));
        int updateCount = db().delete(DataSource.class, id);
        if (dataSourceManager.isConfigured(dataSource.getName())) {
            dataSourceManager.terminateConfiguration(dataSource.getName());
        }

        return updateCount;
    }

    @Override
    public boolean activateDataSource(String dataSourceName) throws UnifyException {
        if (!dataSourceManager.isConfigured(dataSourceName)) {
            DataSource dataSource = db().list(new DataSourceQuery().name(dataSourceName));
            dataSourceManager.configure(getDynamicSqlDataSourceConfig(dataSource));
            return true;
        }

        return false;
    }

    @Override
    public String activateDataSource(Long dataSourceId) throws UnifyException {
        DataSource dataSource = db().list(new DataSourceQuery().id(dataSourceId));
        if (!dataSourceManager.isConfigured(dataSource.getName())) {
            dataSourceManager.configure(getDynamicSqlDataSourceConfig(dataSource));
        }

        return dataSource.getName();
    }

    @Taskable(
            name = SystemDataSourceTaskConstants.DATASOURCETESTTASK, description = "DataSource Test Task",
            parameters = { @Parameter(
                    name = SystemDataSourceTaskParamConstants.DATASOURCE, type = DataSource.class, mandatory = true) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    public boolean executeTestDataSourceTask(TaskMonitor taskMonitor, DataSource dataSource) throws UnifyException {
        boolean result = false;

        addTaskMessage(taskMonitor, "$m{system.datasource.taskmonitor.performing}");
        addTaskMessage(taskMonitor, "$m{system.datasource.taskmonitor.connecting}", dataSource.getConnectionUrl());
        DataSourceDriver driver = findDataSourceDriver(dataSource.getDataSourceDriverId());
        result =
                dataSourceManager.testConfiguration(new DynamicSqlDataSourceConfig(taskMonitor.getTaskId(0),
                        driver.getDialect(), driver.getDriverType(), dataSource.getConnectionUrl(),
                        dataSource.getUserName(), dataSource.getPassword(), 1, false));
        addTaskMessage(taskMonitor, "$m{system.datasource.taskmonitor.completed}", result);
        return result;
    }

    @Override
    public List<ToolingEntityItem> findToolingBaseTypes() throws UnifyException {
        List<ToolingEntityItem> resultList = new ArrayList<ToolingEntityItem>();
        for (Class<? extends Entity> entityClass : getAnnotatedClasses(BaseEntity.class, Tooling.class,
                "com.tcdng.jacklyn.common.entities")) {
            resultList.add(createToolingEntityItem(entityClass));
        }

        resultList.add(createToolingEntityItem(AbstractEntity.class));
        resultList.add(createToolingEntityItem(AbstractSequencedEntity.class));
        return resultList;
    }

    @Override
    public List<ToolingEntityItem> findToolingDocumentTypes() throws UnifyException {
        List<ToolingEntityItem> resultList = new ArrayList<ToolingEntityItem>();
        for (Class<? extends Document> entityClass : getAnnotatedClassesExcluded(Document.class, Tooling.class,
                "com.tcdng.jacklyn.common.entities")) {
            resultList.add(createToolingEntityItem(entityClass));
        }
        return resultList;
    }

    @Override
    public List<ToolingEntityItem> findToolingEnumTypes() throws UnifyException {
        List<ToolingEntityItem> resultList = new ArrayList<ToolingEntityItem>();
        List<ToolingEntityFieldItem> fieldList = new ArrayList<ToolingEntityFieldItem>();
        fieldList.add(new ToolingEntityFieldItem("code", String.class.getCanonicalName()));
        fieldList.add(new ToolingEntityFieldItem("description", String.class.getCanonicalName()));
        for (Class<? extends EnumConst> enumClass : getAnnotatedClassesExcluded(EnumConst.class, Tooling.class,
                "com.tcdng.jacklyn.common.entities")) {
            Tooling ta = enumClass.getAnnotation(Tooling.class);
            StaticList sla = enumClass.getAnnotation(StaticList.class);
            if (sla != null) {
                resultList.add(new ToolingEntityItem(sla.value(), resolveApplicationMessage(ta.description()),
                        enumClass.getName(), "code", ta.guarded(), fieldList));
            }
        }
        return resultList;
    }

    @Override
    public List<ToolingDocumentListItem> findToolingDocumentListItems() throws UnifyException {
        List<ToolingDocumentListItem> resultList = new ArrayList<ToolingDocumentListItem>();
        for (Class<? extends Document> documentClass : getAnnotatedClassesExcluded(Document.class, Tooling.class,
                "com.tcdng.jacklyn.common.entities")) {
            Tooling ta = documentClass.getAnnotation(Tooling.class);
            resultList.add(
                    new ToolingDocumentListItem(documentClass.getName(), resolveApplicationMessage(ta.description())));
        }
        return resultList;
    }

    @Override
    public List<ToolingTransformerTypeItem> findToolingTransformerTypes() throws UnifyException {
        return getToolingTypes(ToolingTransformerTypeItem.class, Transformer.class);
    }

    @Override
    public List<ToolingListTypeItem> findToolingListTypes() throws UnifyException {
        List<ToolingListTypeItem> resultList = new ArrayList<ToolingListTypeItem>();
        for (Class<? extends EnumConst> enumClass : getAnnotatedClasses(EnumConst.class, Tooling.class)) {
            StaticList sla = enumClass.getAnnotation(StaticList.class);
            if (sla != null) {
                Tooling ta = enumClass.getAnnotation(Tooling.class);
                resultList.add(new ToolingListTypeItem(sla.value(), resolveApplicationMessage(ta.description())));
            }
        }

        resultList.addAll(getToolingTypes(ToolingListTypeItem.class, ListCommand.class));
        return resultList;
    }

    @Periodic(PeriodicType.SLOW)
    @Transactional(TransactionAttribute.REQUIRES_NEW)
    public void triggerScheduledTasksForExecution(TaskMonitor taskMonitor) throws UnifyException {
        // If periodic task is canceled or scheduler is disabled cancel all
        // scheduled tasks
        if (taskMonitor.isCanceled() || !getSysParameterValue(Boolean.class,
                SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_ENABLED)) {
            return;
        }

        // Working dates
        Date now = db().getNow();
        final Date workingDt = CalendarUtils.getMidnightDate(now);

        // Expiration allowance
        int expirationAllowanceMins =
                getSysParameterValue(int.class,
                        SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_TRIGGER_EXPIRATION);
        long expirationAllowanceMilliSec =
                CalendarUtils.getMilliSecondsByFrequency(FrequencyUnit.MINUTE, expirationAllowanceMins);

        int maxScheduledTaskTrigger =
                getSysParameterValue(int.class, SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_MAX_TRIGGER);

        // Fetch tasks ready to run
        logDebug("Fetching ready tasks...");
        List<Long> readyScheduledTaskIdList =
                db().valueList(Long.class, "id", new ScheduledTaskQuery().readyToRunOn(now));

        // Schedule tasks that are active only today
        logDebug("[{0}] potential scheduled task(s) to run...", readyScheduledTaskIdList.size());
        int triggered = 0;
        for (Long scheduledTaskId : readyScheduledTaskIdList) {
            ScheduledTaskDef scheduledTaskDef = scheduledTaskDefs.get(scheduledTaskId);
            String taskLock = scheduledTaskDef.getLock();
            logDebug("Attempting to grab scheduled task lock [{0}] ...", taskLock);

            if (!isWithClusterLock(taskLock) && grabClusterLock(taskLock)) {
                logDebug("Grabbed scheduled task lock [{0}] ...", taskLock);

                boolean lockHeldForTask = false;
                try {
                    logDebug("Setting up scheduled task [{0}] ...", scheduledTaskDef.getDescription());
                    Map<String, Object> taskParameters = new HashMap<String, Object>();
                    taskParameters.put(SystemSchedTaskConstants.SCHEDULEDTASK_ID, scheduledTaskId);

                    Date nextExecutionOn =
                            db().value(Date.class, "nextExecutionOn", new ScheduledTaskQuery().id(scheduledTaskId));
                    Date expiryOn = CalendarUtils.getDateWithOffset(nextExecutionOn, expirationAllowanceMilliSec);
                    if (now.before(expiryOn)) {
                        // Task execution has not expired. Start task
                        // Load settings
                        for (Input<?> input : scheduledTaskDef.getInputList()) {
                            taskParameters.put(input.getName(), input.getTypeValue());
                        }

                        // Create history
                        ScheduledTaskHist scheduledTaskHist = new ScheduledTaskHist();
                        scheduledTaskHist.setScheduledTaskId(scheduledTaskId);
                        scheduledTaskHist.setStartedOn(now);
                        scheduledTaskHist.setTaskStatus(TaskStatus.INITIALIZED);
                        Long scheduledTaskHistId = (Long) db().create(scheduledTaskHist);
                        taskParameters.put(SystemSchedTaskConstants.SCHEDULEDTASKHIST_ID, scheduledTaskHistId);

                        // Fire task
                        taskManager.startTask(scheduledTaskDef.getTaskName(), taskParameters, true,
                                taskStatusLogger.getName());
                        logDebug("Task [{0}] is setup to run...", scheduledTaskDef.getDescription());

                        lockHeldForTask = true;
                        triggered++;
                    }

                    // Calculate and set next execution
                    Date calcNextExecutionOn = null;
                    long repeatMillSecs = scheduledTaskDef.getRepeatMillSecs();
                    if (repeatMillSecs > 0) {
                        Date limit = CalendarUtils.getDateWithOffset(workingDt, scheduledTaskDef.getEndOffset());
                        long factor = ((now.getTime() - nextExecutionOn.getTime()) / repeatMillSecs) + 1;
                        long actNextOffsetMillSecs = factor * repeatMillSecs;
                        calcNextExecutionOn = CalendarUtils.getDateWithOffset(nextExecutionOn, actNextOffsetMillSecs);
                        if (calcNextExecutionOn.compareTo(limit) >= 0) {
                            calcNextExecutionOn = null;
                        }
                    }

                    if (calcNextExecutionOn == null) {
                        // Use next eligible date start time
                        calcNextExecutionOn =
                                CalendarUtils.getDateWithOffset(
                                        CalendarUtils.getNextEligibleDate(scheduledTaskDef.getWeekdays(),
                                                scheduledTaskDef.getDays(), scheduledTaskDef.getMonths(), workingDt),
                                        scheduledTaskDef.getStartOffset());
                    }

                    db().updateById(ScheduledTask.class, scheduledTaskId,
                            new Update().add("nextExecutionOn", calcNextExecutionOn).add("lastExecutionOn", now));
                    logDebug("Task [{0}] is scheduled to run next on [{1,date,dd/MM/yy HH:mm:ss}]...",
                            scheduledTaskDef.getDescription(), calcNextExecutionOn);

                } catch (UnifyException e) {
                    try {
                        releaseClusterLock(taskLock);
                    } catch (Exception e1) {
                    }
                } finally {
                    if (!lockHeldForTask) {
                        releaseClusterLock(taskLock);
                    }
                }
            }

            if (triggered >= maxScheduledTaskTrigger) {
                break;
            }
        }
    }

    @Override
    public void saveMenuOrder(List<ApplicationMenu> menuList) throws UnifyException {
        ApplicationMenuQuery query = new ApplicationMenuQuery();
        Update update = new Update();
        for (int i = 0; i < menuList.size(); i++) {
            ApplicationMenu applicationMenu = menuList.get(i);
            query.clear();
            update.clear();
            query.id(applicationMenu.getId());
            update.add("displayOrder", i);
            db().updateAll(query, update);
        }
        loadApplicationMenu();
    }

    @Override
    public void saveMenuItemOrder(List<ApplicationMenuItem> menuItemList) throws UnifyException {
        ApplicationMenuItemQuery query = new ApplicationMenuItemQuery();
        Update update = new Update();
        for (int i = 0; i < menuItemList.size(); i++) {
            ApplicationMenuItem applicationMenuItem = menuItemList.get(i);
            query.clear();
            update.clear();
            query.id(applicationMenuItem.getId());
            update.add("displayOrder", i);
            db().updateAll(query, update);
        }

        loadApplicationMenu();
    }

    @Broadcast
    @Override
    public void loadApplicationMenu(String... params) throws UnifyException {
        logInfo("Loading application menu...");
        List<MenuItemSet> menuItemSetList = new ArrayList<MenuItemSet>();
        List<ApplicationMenu> applicationMenuList =
                findMenus((ApplicationMenuQuery) new ApplicationMenuQuery().orderByDisplayOrder().orderByModuleDesc()
                        .installed(Boolean.TRUE).status(RecordStatus.ACTIVE));
        for (ApplicationMenu applicationMenu : applicationMenuList) {
            List<ApplicationMenuItem> applicationMenuItemList =
                    findMenuItems(
                            (ApplicationMenuItemQuery) new ApplicationMenuItemQuery().menuId(applicationMenu.getId())
                                    .orderByDisplayOrder().hidden(Boolean.FALSE).installed(Boolean.TRUE));
            List<MenuItem> menuItemList = new ArrayList<MenuItem>();
            for (ApplicationMenuItem applicationMenuItem : applicationMenuItemList) {
                MenuItem menuItem =
                        new MenuItem(applicationMenuItem.getCaption(), applicationMenuItem.getName(),
                                applicationMenuItem.getPath(), null);
                menuItemList.add(menuItem);
            }

            menuItemSetList.add(new MenuItemSet(applicationMenu.getCaption(), applicationMenu.getName(),
                    applicationMenu.getPath(), menuItemList));
        }

        MenuSet menuSet = new MenuSet();
        menuSet.add(new Menu(getApplicationName(), getApplicationName(), null,
                Collections.unmodifiableList(menuItemSetList)));
        if (getContainerSetting(boolean.class, UnifyCorePropertyConstants.APPLICATION_OSMODE, false)) {
            // Load application menus
            menuSet.setAlwaysSelect(true);
            MenuLoader menuLoader = (MenuLoader) getComponent(ApplicationComponents.APPLICATION_MENULOADER);
            for (Menu menu : menuLoader.loadMenus()) {
                menuSet.add(menu);
            }
        }

        setApplicationAttribute(ApplicationAttributeConstants.APPLICATION_MENUSET, menuSet);

        broadcastRefreshMenu();

        logInfo("Application menu loaded.");
    }

    @Override
    public Long createShortcutTile(ShortcutTile shortcutTile) throws UnifyException {
        return (Long) db().create(shortcutTile);
    }

    @Override
    public ShortcutTile findShortcutTile(Long shortcutTileId) throws UnifyException {
        return db().list(ShortcutTile.class, shortcutTileId);
    }

    @Override
    public List<ShortcutTile> findShortcutTiles(ShortcutTileQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateShortcutTile(ShortcutTile shortcutTile) throws UnifyException {
        return db().updateById(shortcutTile);
    }

    @Override
    public int deleteShortcutTile(Long id) throws UnifyException {
        return db().delete(ShortcutTile.class, id);
    }

    @Override
    public List<Tile> generateTiles(ShortcutTileQuery query) throws UnifyException {
        return generateTiles(db().findAll(query));
    }

    @Override
    public List<Tile> generateTiles(List<ShortcutTile> shortcutTileList) throws UnifyException {
        List<Tile> tileList = new ArrayList<Tile>();
        for (ShortcutTile shortcutTile : shortcutTileList) {
            if (shortcutTile.getGenerator() != null) {
                ShortcutTileGenerator shortcutTileGenerator =
                        (ShortcutTileGenerator) getComponent(shortcutTile.getGenerator());
                tileList.add(shortcutTileGenerator.generate(shortcutTile));
            } else {
                String caption = resolveSessionMessage(shortcutTile.getCaption());
                tileList.add(new Tile(shortcutTile.getImageSrc(), caption, shortcutTile.getPath(), null,
                        shortcutTile.isLandscape()));
            }
        }
        return tileList;
    }

    @Override
    public void saveShortcutTileOrder(List<ShortcutTile> shortcutTileList) throws UnifyException {
        ShortcutTileQuery query = new ShortcutTileQuery();
        Update update = new Update();
        for (int i = 0; i < shortcutTileList.size(); i++) {
            ShortcutTile shortcutTile = shortcutTileList.get(i);
            query.clear();
            update.clear();
            query.id(shortcutTile.getId());
            update.add("displayOrder", i);
            db().updateAll(query, update);
        }
    }

    @Override
    public void onApplicationStartup() throws UnifyException {
        // Default system user tokens
        setApplicationAttribute(JacklynApplicationAttributeConstants.DEFAULT_SYSTEM_USERTOKEN, createReservedUserToken(
                SystemReservedUserConstants.SYSTEM_LOGINID, SystemReservedUserConstants.SYSTEM_ID));
        setApplicationAttribute(JacklynApplicationAttributeConstants.DEFAULT_ANONYMOUS_USERTOKEN,
                createReservedUserToken(SystemReservedUserConstants.ANONYMOUS_LOGINID,
                        SystemReservedUserConstants.ANONYMOUS_ID));

        loadApplicationMenu();
    }

    @Override
    public void onApplicationShutdown() throws UnifyException {

    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
        logInfo("Managing system module definitions...");
        // Uninstall old records
        Update update = new Update().add("installed", Boolean.FALSE);
        db().updateAll(new ModuleQuery().installed(Boolean.TRUE), update);
        db().updateAll(new ApplicationMenuQuery().installed(Boolean.TRUE), update);
        db().updateAll(new ApplicationMenuItemQuery().installed(Boolean.TRUE), update);
        db().updateAll(new SystemAssetQuery().installed(Boolean.TRUE), update);

        ShortcutTileQuery dtQuery = (ShortcutTileQuery) new ShortcutTileQuery().clear().ignoreEmptyCriteria(true);
        db().updateAll(dtQuery, update);

        // Install new and update old
        Module module = new Module();
        ApplicationMenu applicationMenu = new ApplicationMenu();
        ApplicationMenuItem applicationMenuItem = new ApplicationMenuItem();
        ShortcutTile shortcutTile = new ShortcutTile();

        // Detect and register modules
        logDebug("Detecting and installing modules...");
        Set<String> nonExtModules = new HashSet<String>();
        for (ModuleConfig moduleConfig : moduleConfigList) {
            if (!moduleConfig.isExtension()) {
                String moduleName = moduleConfig.getName();
                if (nonExtModules.contains(moduleName)) {
                    throw new UnifyException(SystemModuleErrorConstants.MODULE_MULTIPLE_CONFIGURATIONS_WITH_NAME,
                            moduleName);
                }

                Module oldModule = findModule(moduleName);
                if (oldModule == null) {
                    // Create new module
                    module.setName(moduleName);
                    module.setDescription(resolveApplicationMessage(moduleConfig.getDescription()));
                    module.setDeactivatable(moduleConfig.isDeactivatable());
                    module.setRemote(Boolean.FALSE);
                    module.setInstalled(Boolean.TRUE);
                    db().create(module);
                } else {
                    // Otherwise perform update
                    oldModule.setDescription(resolveApplicationMessage(moduleConfig.getDescription()));
                    oldModule.setDeactivatable(moduleConfig.isDeactivatable());
                    oldModule.setRemote(Boolean.FALSE);
                    oldModule.setInstalled(Boolean.TRUE);
                    db().updateByIdVersion(oldModule);
                }
                nonExtModules.add(moduleName);
            }
        }

        // Register module system parameters and menus
        Map<String, Module> moduleMap =
                db().findAllMap(String.class, "name", new ModuleQuery().ignoreEmptyCriteria(true));
        DataUtils.sort(moduleConfigList, ModuleConfig.class, "description", true);
        for (ModuleConfig moduleConfig : moduleConfigList) {
            String moduleName = moduleConfig.getName();
            module = moduleMap.get(moduleName);
            if (module == null) {
                throw new UnifyException(SystemModuleErrorConstants.MODULE_UNKNOWN_MODULE_NAME_REFERENCED, moduleName);
            }

            Long moduleId = module.getId();
            if (moduleConfig.getSysParams() != null
                    && DataUtils.isNotBlank(moduleConfig.getSysParams().getSysParamList())) {
                logDebug("Updating system parameter definitions for module [{0}]...", module.getDescription());
                boolean updateVersion = true;
                for (SysParamConfig sysParamConfig : moduleConfig.getSysParams().getSysParamList()) {
                    SystemParameter sysParameter = findSysParameter(sysParamConfig.getName());
                    String description = resolveApplicationMessage(sysParamConfig.getDescription());
                    if (sysParameter == null) {
                        logDebug("Creating system parameter [{0}]...", sysParamConfig.getName());
                        sysParameter = new SystemParameter();
                        sysParameter.setModuleId(module.getId());
                        sysParameter.setName(sysParamConfig.getName());
                        sysParameter.setDescription(description);
                        String defaultVal = sysParamConfig.getDefaultVal();
                        if (SystemModuleSysParamConstants.SYSPARAM_SYSTEM_EMAIL.equals(sysParamConfig.getName())) {
                            defaultVal =
                                    getContainerSetting(String.class,
                                            JacklynContainerPropertyConstants.JACKLYN_SYSTEM_DEFAULT_EMAIL, defaultVal);
                        }

                        sysParameter.setValue(defaultVal);
                        sysParameter.setEditor(sysParamConfig.getEditor());
                        sysParameter.setType(sysParamConfig.getType());
                        sysParameter.setControl(sysParamConfig.isControl());
                        sysParameter.setEditable(sysParamConfig.isEditable());
                        db().create(sysParameter);
                    } else {
                        logDebug("Updating system parameter [{0}]...", sysParamConfig.getName());
                        sysParameter.setModuleId(module.getId());
                        sysParameter.setDescription(description);
                        sysParameter.setEditor(sysParamConfig.getEditor());
                        sysParameter.setType(sysParamConfig.getType());
                        sysParameter.setControl(sysParamConfig.isControl());
                        sysParameter.setEditable(sysParamConfig.isEditable());
                        // Check for application version
                        if (updateVersion && SystemModuleSysParamConstants.SYSPARAM_APPLICATION_VERSION
                                .equals(sysParamConfig.getName())) {
                            sysParameter.setValue(getDeploymentVersion());
                            updateVersion = false;
                        }
                        db().updateByIdVersion(sysParameter);
                    }
                }
            }

            if (moduleConfig.getMenus() != null && DataUtils.isNotBlank(moduleConfig.getMenus().getMenuList())) {
                logDebug("Updating menu definitions for module [{0}]...", module.getDescription());
                List<MenuConfig> menuConfigList = moduleConfig.getMenus().getMenuList();

                DataUtils.sort(menuConfigList, MenuConfig.class, "caption", true);
                applicationMenu.setModuleId(module.getId());
                // Deal with parent menus first
                ApplicationMenuQuery mQuery = new ApplicationMenuQuery();
                for (MenuConfig menuConfig : menuConfigList) {
                    mQuery.clear();
                    Long menuId = null;
                    String menuName = menuConfig.getName();
                    ApplicationMenu oldApplicationMenu = db().find(mQuery.name(menuName));
                    if (oldApplicationMenu == null) {
                        logDebug("Registering module menu definition [{0}]...", applicationMenu.getName());
                        applicationMenu.setName(menuName);
                        applicationMenu.setDescription(menuConfig.getDescription());
                        applicationMenu.setPageCaption(menuConfig.getPageCaption());
                        applicationMenu.setCaption(menuConfig.getCaption());
                        applicationMenu.setPath(menuConfig.getPath());
                        applicationMenu.setInstalled(Boolean.TRUE);
                        menuId = (Long) db().create(applicationMenu);
                    } else {
                        logDebug("Updating module menu definition [{0}]...", oldApplicationMenu.getName());
                        oldApplicationMenu.setDescription(menuConfig.getDescription());
                        oldApplicationMenu.setPageCaption(menuConfig.getPageCaption());
                        oldApplicationMenu.setCaption(menuConfig.getCaption());
                        oldApplicationMenu.setPath(menuConfig.getPath());
                        oldApplicationMenu.setInstalled(Boolean.TRUE);
                        db().updateByIdVersion(oldApplicationMenu);
                        menuId = oldApplicationMenu.getId();
                    }

                    List<MenuItemConfig> menuItemList = menuConfig.getMenuItemList();
                    if (menuItemList != null) {
                        ApplicationMenuItemQuery miQuery = new ApplicationMenuItemQuery();
                        applicationMenuItem.setMenuId(menuId);
                        for (MenuItemConfig menuItemConfig : menuItemList) {
                            miQuery.clear();
                            String menuItemName = menuItemConfig.getName();
                            ApplicationMenuItem oldApplicationMenuItem = db().find(miQuery.name(menuItemName));
                            if (oldApplicationMenuItem == null) {
                                applicationMenuItem.setName(menuItemName);
                                applicationMenuItem.setDescription(menuItemConfig.getDescription());
                                applicationMenuItem.setPageCaption(menuItemConfig.getPageCaption());
                                applicationMenuItem.setCaption(menuItemConfig.getCaption());
                                applicationMenuItem.setPath(menuItemConfig.getPath());
                                applicationMenuItem.setHidden(menuItemConfig.isHidden());
                                applicationMenuItem.setInstalled(Boolean.TRUE);
                                db().create(applicationMenuItem);
                            } else {
                                oldApplicationMenuItem.setMenuId(menuId);
                                oldApplicationMenuItem.setDescription(menuItemConfig.getDescription());
                                oldApplicationMenuItem.setPageCaption(menuItemConfig.getPageCaption());
                                oldApplicationMenuItem.setCaption(menuItemConfig.getCaption());
                                oldApplicationMenuItem.setPath(menuItemConfig.getPath());
                                oldApplicationMenuItem.setHidden(menuItemConfig.isHidden());
                                oldApplicationMenuItem.setInstalled(Boolean.TRUE);
                                db().updateByIdVersion(oldApplicationMenuItem);
                            }
                        }
                    }
                }
            }

            if (moduleConfig.getShortcutTiles() != null
                    && DataUtils.isNotBlank(moduleConfig.getShortcutTiles().getShortcutTileList())) {
                logDebug("Reading shortcut tile definitions for module [{0}]...",
                        resolveApplicationMessage(moduleConfig.getDescription()));

                shortcutTile.setModuleId(moduleId);
                for (ShortcutTileConfig shortcutTileConfig : moduleConfig.getShortcutTiles().getShortcutTileList()) {
                    dtQuery.clear();
                    ShortcutTile oldShortcutTile = db().find(dtQuery.name(shortcutTileConfig.getName()));

                    if (oldShortcutTile == null) {
                        shortcutTile.setName(shortcutTileConfig.getName());
                        shortcutTile.setDescription(resolveApplicationMessage(shortcutTileConfig.getDescription()));
                        shortcutTile.setImageSrc(AnnotationUtils.getAnnotationString(shortcutTileConfig.getImage()));
                        shortcutTile.setCaption(AnnotationUtils.getAnnotationString(shortcutTileConfig.getCaption()));
                        shortcutTile
                                .setGenerator(AnnotationUtils.getAnnotationString(shortcutTileConfig.getGenerator()));
                        shortcutTile.setPath(shortcutTileConfig.getPath());
                        shortcutTile.setLandscape(shortcutTileConfig.isLandscape());
                        shortcutTile.setInstalled(Boolean.TRUE);
                        db().create(shortcutTile);
                    } else {
                        oldShortcutTile.setDescription(resolveApplicationMessage(shortcutTileConfig.getDescription()));
                        oldShortcutTile.setImageSrc(AnnotationUtils.getAnnotationString(shortcutTileConfig.getImage()));
                        oldShortcutTile
                                .setCaption(AnnotationUtils.getAnnotationString(shortcutTileConfig.getCaption()));
                        oldShortcutTile
                                .setGenerator(AnnotationUtils.getAnnotationString(shortcutTileConfig.getGenerator()));
                        oldShortcutTile.setPath(shortcutTileConfig.getPath());
                        oldShortcutTile.setLandscape(shortcutTileConfig.isLandscape());
                        oldShortcutTile.setInstalled(Boolean.TRUE);
                        db().updateById(oldShortcutTile);
                    }
                }
            }

            if (moduleConfig.getInputControls() != null
                    && DataUtils.isNotBlank(moduleConfig.getInputControls().getInputControlList())) {
                logDebug("Reading input control defintions for module [{0}]...",
                        resolveApplicationMessage(moduleConfig.getDescription()));

                InputCtrlDef inputCtrlDef = new InputCtrlDef();
                inputCtrlDef.setModuleId(moduleId);
                inputCtrlDef.setStatus(RecordStatus.ACTIVE);
                for (InputControlConfig icc : moduleConfig.getInputControls().getInputControlList()) {
                    InputCtrlDef oldInputCtrlDef = db().find(new InputCtrlDefQuery().name(icc.getName()));
                    if (oldInputCtrlDef == null) {
                        inputCtrlDef.setName(icc.getName());
                        inputCtrlDef.setDescription(icc.getDescription());
                        inputCtrlDef.setControl(icc.getEditor());
                        inputCtrlDef.setStatus(RecordStatus.ACTIVE);
                        db().create(inputCtrlDef);
                    } else {
                        oldInputCtrlDef.setDescription(icc.getDescription());
                        oldInputCtrlDef.setControl(icc.getEditor());
                        oldInputCtrlDef.setStatus(RecordStatus.ACTIVE);
                        db().updateById(oldInputCtrlDef);
                    }
                }
            }
        }

        // Define parameters for all schedulable tasks and business service tasks
        for (UnifyComponentConfig ucc : getComponentConfigs(Task.class)) {
            getParameterService().defineParameters(ucc.getName(), ucc.getType());
        }

        for (TaskableMethodConfig bmtc : taskManager.getAllTaskableMethodConfigs()) {
            if (bmtc.isSchedulable()) {
                List<ParameterDef> parameterList = new ArrayList<ParameterDef>();
                for (TaskableMethodConfig.ParamConfig pc : bmtc.getParamConfigList()) {
                    String editor = AnnotationUtils.getAnnotationString(pc.getEditor());
                    if (editor != null) {
                        ParameterDef parameterDef = new ParameterDef();
                        parameterDef.setDescription(pc.getParamDesc());
                        parameterDef.setEditor(editor);
                        parameterDef.setMandatory(pc.isMandatory());
                        parameterDef.setName(pc.getParamName());
                        parameterDef.setType(pc.getType().getName());
                        parameterList.add(parameterDef);
                    }
                }

                getParameterService().defineParameters(bmtc.getName(), parameterList);
            }
        }

        // Install application assets
        logDebug("Installing application assets...");
        SystemAssetQuery sysAssetQuery = new SystemAssetQuery();
        SystemAsset systemAsset = new SystemAsset();
        systemAsset.setStatus(RecordStatus.ACTIVE);
        systemAsset.setInstalled(Boolean.TRUE);
        for (Class<? extends RemoteCallController> remoteCallClass : this
                .getAnnotatedClasses(RemoteCallController.class, Managed.class)) {
            Managed ma = remoteCallClass.getAnnotation(Managed.class);
            module = moduleMap.get(ma.module());
            if (module == null) {
                throw new UnifyException(SystemModuleErrorConstants.MODULE_UNKNOWN_MODULE_NAME_REFERENCED, ma.module());
            }

            Long moduleId = module.getId();
            Method[] methods = remoteCallClass.getMethods();
            for (Method method : methods) {
                RemoteAction goa = method.getAnnotation(RemoteAction.class);
                if (goa != null) {
                    sysAssetQuery.clear();
                    SystemAsset oldSystemAsset =
                            db().find(sysAssetQuery.type(SystemAssetType.REMOTECALL_METHOD).name(goa.name()));
                    String description = resolveApplicationMessage(goa.description());
                    if (oldSystemAsset == null) {
                        systemAsset.setModuleId(moduleId);
                        systemAsset.setName(goa.name());
                        systemAsset.setDescription(description);
                        systemAsset.setType(SystemAssetType.REMOTECALL_METHOD);
                        db().create(systemAsset);
                    } else {
                        oldSystemAsset.setModuleId(moduleId);
                        oldSystemAsset.setDescription(description);
                        oldSystemAsset.setInstalled(Boolean.TRUE);
                        db().updateById(oldSystemAsset);
                    }
                }
            }

        }

        // Configure default data source drivers
        ensureDataSourceDriver("HSQLDB_DRV", "Hyper SQL JDBC Driver", SqlDialectNameConstants.HSQLDB,
                "org.hsqldb.jdbcDriver");
        ensureDataSourceDriver("MSSQL_DRV", "Microsoft SQL JDBC Driver", SqlDialectNameConstants.MSSQL,
                "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        ensureDataSourceDriver("MSSQL_2012_DRV", "Microsoft SQL 2012 JDBC Driver", SqlDialectNameConstants.MSSQL_2012,
                "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        ensureDataSourceDriver("MYSQL_DRV", "MySQL JDBC Driver", SqlDialectNameConstants.MYSQL,
                "com.mysql.jdbc.Driver");
        ensureDataSourceDriver("ORACLE_DRV", "Oracle JDBC Driver", SqlDialectNameConstants.ORACLE,
                "oracle.jdbc.OracleDriver");
        ensureDataSourceDriver("ORACLE12C_DRV", "Oracle (12c) JDBC Driver", SqlDialectNameConstants.ORACLE_12C,
                "oracle.jdbc.OracleDriver");
        ensureDataSourceDriver("POSTGRESQL_DRV", "PostgreSQL JDBC Driver", SqlDialectNameConstants.POSTGRESQL,
                "org.postgresql.Driver");
    }

    private UserToken createReservedUserToken(String loginId, Long id) throws UnifyException {
        return new UserToken(loginId, "System", getSessionContext().getRemoteAddress(), null, null, null, null, true,
                true, true, false);
    }

    private ToolingEntityItem createToolingEntityItem(Class<? extends Entity> entityClass) throws UnifyException {
        Tooling ta = entityClass.getAnnotation(Tooling.class);
        List<ToolingEntityFieldItem> fieldList = new ArrayList<ToolingEntityFieldItem>();
        String id = null;

        for (Field field : ReflectUtils.getAnnotatedFields(entityClass, Id.class)) {
            id = field.getName();
            fieldList.add(new ToolingEntityFieldItem(field.getName(), field.getType().getCanonicalName()));
            break;
        }

        for (Field field : ReflectUtils.getAnnotatedFields(entityClass, ForeignKey.class)) {
            fieldList.add(new ToolingEntityFieldItem(field.getName(), field.getType().getCanonicalName()));
        }

        for (Field field : ReflectUtils.getAnnotatedFields(entityClass, Column.class)) {
            fieldList.add(new ToolingEntityFieldItem(field.getName(), field.getType().getCanonicalName()));
        }

        // Add list-only fields 11/7/19
        for (Field field : ReflectUtils.getAnnotatedFields(entityClass, ListOnly.class)) {
            fieldList.add(new ToolingEntityFieldItem(field.getName(), field.getType().getCanonicalName()));
        }

        String entityToolingName = AnnotationUtils.getAnnotationString(ta.name());
        if (StringUtils.isBlank(entityToolingName)) {
            throw new UnifyException(SystemModuleErrorConstants.TOOLING_ENTITY_NAME_REQUIRED, entityClass);
        }

        return new ToolingEntityItem(entityToolingName, resolveApplicationMessage(ta.description()),
                entityClass.getName(), id, ta.guarded(), fieldList);
    }

    private AuthenticationLargeData internalFindAuthentication(Authentication authentication) throws UnifyException {
        TwoWayStringCryptograph cryptograph = (TwoWayStringCryptograph) getComponent(authentication.getCryptograph());
        String decPassword = cryptograph.decrypt(authentication.getPassword());
        authentication.setPassword(decPassword);
        return new AuthenticationLargeData(authentication);
    }

    private Authentication getEncryptedAuthentication(AuthenticationLargeData authenticationFormData)
            throws UnifyException {
        Authentication authentication = authenticationFormData.getData();
        TwoWayStringCryptograph cryptograph = (TwoWayStringCryptograph) getComponent(authentication.getCryptograph());
        String encPassword = cryptograph.encrypt(authenticationFormData.getPassword());
        authentication.setPassword(encPassword);
        return authentication;
    }

    private void updateModuleStatus(String name, RecordStatus status) throws UnifyException {
        ModuleQuery query = new ModuleQuery().name(name);
        if (!db().value(boolean.class, "deactivatable", query)) {
            throw new UnifyException(SystemModuleErrorConstants.MODULE_DATA_NOT_DEACTIVATABLE, name);
        }
        db().updateAll(query, new Update().add("status", status));
    }

    private DynamicSqlDataSourceConfig getDynamicSqlDataSourceConfig(DataSource dataSource) throws UnifyException {
        return new DynamicSqlDataSourceConfig(dataSource.getName(), dataSource.getDialect(), dataSource.getDriverType(),
                dataSource.getConnectionUrl(), dataSource.getUserName(), dataSource.getPassword(),
                dataSource.getMaxConnections(), false);
    }

    private void ensureDataSourceDriver(String name, String description, String dialect, String driverType)
            throws UnifyException {
        if (db().countAll(new DataSourceDriverQuery().name(name)) == 0) {
            db().create(new DataSourceDriver(name, description, dialect, driverType));
        }
    }
}
