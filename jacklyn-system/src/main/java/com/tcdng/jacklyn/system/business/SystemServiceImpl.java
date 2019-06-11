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
import java.util.concurrent.ConcurrentHashMap;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.constants.JacklynApplicationAttributeConstants;
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
import com.tcdng.unify.core.list.ListCommand;
import com.tcdng.unify.core.list.ListManager;
import com.tcdng.unify.core.operation.Criteria;
import com.tcdng.unify.core.operation.Update;
import com.tcdng.unify.core.security.TwoWayStringCryptograph;
import com.tcdng.unify.core.system.entities.AbstractSequencedEntity;
import com.tcdng.unify.core.system.entities.ParameterDef;
import com.tcdng.unify.core.system.entities.UserSessionTrackingQuery;
import com.tcdng.unify.core.task.Task;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskManager;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskParameterConstants;
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
import com.tcdng.unify.web.RemoteCallController;
import com.tcdng.unify.web.annotation.GatewayAction;

/**
 * Default implementation of system business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(SystemModuleNameConstants.SYSTEMSERVICE)
public class SystemServiceImpl extends AbstractJacklynBusinessService implements SystemService {

    @Configurable
    private TaskManager taskManager;

    @Configurable
    private ListManager listManager;

    @Configurable("scheduledtaskstatuslogger")
    private TaskStatusLogger taskStatusLogger;

    @Configurable
    private DynamicSqlDataSourceManager dataSourceManager;

    private static final String SCHEDULED_TASK = "scheduledTask";

    private Map<Long, TaskInfo> triggeredTaskInfoMap;

    private Date workingDt;

    private FactoryMap<String, DashboardDef> dashboards;

    public SystemServiceImpl() {
        triggeredTaskInfoMap = new ConcurrentHashMap<Long, TaskInfo>();

        dashboards = new FactoryMap<String, DashboardDef>(true) {

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
        return dashboards.get(name);
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
        cloneQuery.select("id", "name", "description", "cryptograph", "status", "statusDesc");
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
        Criteria criteria = query.getCriteria();
        Query<SystemParameter> innerQuery =
                query.copyNoCriteria().add(criteria).equals("control", Boolean.TRUE).order("name");
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
        getParameterService().updateParameterValues(scheduledTask.getTaskName(), SCHEDULED_TASK, scheduledTaskId,
                scheduledTaskFormData.getScheduledTaskParams());
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
        scheduledTask.setUpdated(Boolean.TRUE);
        return db().updateByIdVersion(scheduledTask);
    }

    @Override
    public int updateScheduledTask(ScheduledTaskLargeData scheduledTaskFormData) throws UnifyException {
        ScheduledTask scheduledTask = scheduledTaskFormData.getData();
        scheduledTask.setUpdated(Boolean.TRUE);
        int updateCount = db().updateByIdVersion(scheduledTask);
        getParameterService().updateParameterValues(scheduledTask.getTaskName(), SCHEDULED_TASK, scheduledTask.getId(),
                scheduledTaskFormData.getScheduledTaskParams());
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
        if (errorMessages != null) {
            if (errorMessages.length() > 250) {
                errorMessages = errorMessages.substring(0, 250);
            }
        }
        scheduledTaskHist.setErrorMsg(errorMessages);
        scheduledTaskHist.setTaskStatus(taskStatus);
        return (Long) db().create(scheduledTaskHist);
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
                        new UserSessionTrackingQuery().loggedIn().select("userLoginId").distinct(true))
                .get(0).getValue());
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
        return db().find(DataSource.class, dataSourceId);
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
            if (!triggeredTaskInfoMap.isEmpty()) {
                logDebug("Stopping all running scheduled tasks...");
                for (TaskInfo taskInfo : triggeredTaskInfoMap.values()) {
                    if (!taskInfo.isDummy()) {
                        taskInfo.getTaskMonitor().cancel();
                    }
                }
                triggeredTaskInfoMap.clear();
            }
            return;
        }

        // Check working date. If not current day, stop all scheduled tasks,
        // then clear task information map
        Date now = db().getNow();
        Date currentDt = CalendarUtils.getMidnightDate(now);
        if (!currentDt.equals(workingDt)) {
            for (TaskInfo taskInfo : triggeredTaskInfoMap.values()) {
                if (!taskInfo.isDummy()) {
                    logDebug("Stopping scheduled task [{0}] from running...", taskInfo.getDescription());
                    taskInfo.getTaskMonitor().cancel();
                }
            }
            triggeredTaskInfoMap.clear();
            workingDt = currentDt;
        }

        long scheduledTaskExpirationAllowance =
                getSysParameterValue(long.class,
                        SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_TRIGGER_EXPIRATION);
        // Convert to milliseconds
        scheduledTaskExpirationAllowance = scheduledTaskExpirationAllowance * 60 * 1000;

        int maxScheduledTaskTrigger =
                getSysParameterValue(int.class, SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_MAX_TRIGGER);

        // Fetch new scheduled tasks with start time less or equal current
        // time
        logDebug("Fetching new scheduled tasks...");
        now = CalendarUtils.getTimeOfDay(now);
        List<ScheduledTask> scheduledTaskList =
                listNewScheduledTasks(now, new ArrayList<Long>(triggeredTaskInfoMap.keySet()));

        // Schedule tasks that are active only today
        logDebug("[{0}] new scheduled task(s) fetched...", scheduledTaskList.size());
        int triggered = 0;
        for (ScheduledTask scheduledTask : scheduledTaskList) {
            Long scheduledTaskId = scheduledTask.getId();
            // Cancel any task that has been updated
            TaskInfo taskInfo = triggeredTaskInfoMap.remove(scheduledTaskId);
            if (taskInfo != null && !taskInfo.isDummy()) {
                taskInfo.getTaskMonitor().cancel();
            }

            Date scheduledTaskTime = CalendarUtils.getTimeOfDay(scheduledTask.getStartTime());
            if (now.after(scheduledTaskTime) && isOkToRunOnWorkingDate(scheduledTask)) {
                String taskLock = "scheduledtask-lock" + scheduledTaskId;
                if (grabClusterLock(taskLock)) {
                    Map<String, Object> schdParameters = new HashMap<String, Object>();
                    try {
                        schdParameters.put(TaskParameterConstants.LOCK_TO_RELEASE, taskLock);
                        schdParameters.put(SystemSchedTaskConstants.SCHEDULEDTASK_ID, scheduledTaskId);
                        for (Input parameterValue : getParameterService().fetchNormalizedInputs(
                                scheduledTask.getTaskName(), SCHEDULED_TASK, scheduledTask.getId()).getInputList()) {
                            schdParameters.put(parameterValue.getName(), parameterValue.getTypeValue());
                        }

                        TaskMonitor schdTaskMonitor = null;
                        if (scheduledTask.getFrequency() != null && scheduledTask.getFrequencyUnit() != null) {
                            long periodInMillSec =
                                    CalendarUtils.getMilliSecondsByFrequency(scheduledTask.getFrequencyUnit(),
                                            scheduledTask.getFrequency());

                            int numberOfTimes = 0;
                            if (scheduledTask.getNumberOfTimes() != null) {
                                numberOfTimes = scheduledTask.getNumberOfTimes();
                            }

                            // Check for expiry
                            boolean isExpired = false;
                            if (scheduledTask.getExpires()) {
                                long windowToRunMillSec =
                                        numberOfTimes * periodInMillSec + scheduledTaskExpirationAllowance;
                                long actualWindowToRunMillSec =
                                        windowToRunMillSec - (now.getTime() - scheduledTaskTime.getTime());
                                if (actualWindowToRunMillSec > 0) {
                                    // Recalculate number of times to repeat
                                    if (periodInMillSec > 0) {
                                        int reCalcNumberOfTimes = (int) (actualWindowToRunMillSec / periodInMillSec);
                                        if (reCalcNumberOfTimes < numberOfTimes) {
                                            numberOfTimes = reCalcNumberOfTimes;
                                        }
                                    }
                                } else {
                                    isExpired = true;
                                }
                            }

                            // Schedule periodic if not expired
                            if (!isExpired) {
                                logDebug("Scheduling task [{0}] to run every [{1}ms] with a [{2}ms] delay...",
                                        scheduledTask.getDescription(), periodInMillSec, 0);
                                schdTaskMonitor =
                                        taskManager.scheduleTaskToRunPeriodically(scheduledTask.getTaskName(),
                                                schdParameters, false, 0, periodInMillSec, numberOfTimes,
                                                taskStatusLogger.getName());
                            }
                        } else {
                            // Check for expiry
                            boolean isExpired = false;
                            if (scheduledTask.getExpires()) {
                                long actualWindowToRunMillSec =
                                        scheduledTaskExpirationAllowance
                                                - (now.getTime() - scheduledTask.getStartTime().getTime());
                                isExpired = actualWindowToRunMillSec <= 0;
                            }

                            if (!isExpired) {
                                // Schedule one-shot
                                logDebug("Scheduling task [{0}] to run one time...", scheduledTask.getDescription());
                                schdTaskMonitor =
                                        taskManager.startTask(scheduledTask.getTaskName(), schdParameters, false,
                                                taskStatusLogger.getName());
                            }
                        }

                        if (schdTaskMonitor != null) {
                            triggered++;
                            triggeredTaskInfoMap.put(scheduledTaskId,
                                    new TaskInfo(schdTaskMonitor, scheduledTask.getDescription()));
                        }
                    } catch (UnifyException e) {
                        try {
                            releaseClusterLock(taskLock);
                        } catch (Exception e1) {
                        }

                        taskStatusLogger.logCriticalFailure(scheduledTask.getTaskName(), schdParameters, e);
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
                    findMenuItems((ApplicationMenuItemQuery) new ApplicationMenuItemQuery()
                            .menuId(applicationMenu.getId()).orderByDisplayOrder().installed(Boolean.TRUE));
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
        menuSet.add(
                new Menu(getApplicationName(), getApplicationName(), Collections.unmodifiableList(menuItemSetList)));
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
                    && !DataUtils.isBlank(moduleConfig.getSysParams().getSysParamList())) {
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
                        sysParameter.setValue(sysParamConfig.getDefaultValue());
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

            if (moduleConfig.getMenus() != null && !DataUtils.isBlank(moduleConfig.getMenus().getMenuList())) {
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
                        applicationMenu.setRemotePath(menuConfig.getRemotePath());
                        applicationMenu.setInstalled(Boolean.TRUE);
                        menuId = (Long) db().create(applicationMenu);
                    } else {
                        logDebug("Updating module menu definition [{0}]...", oldApplicationMenu.getName());
                        oldApplicationMenu.setDescription(menuConfig.getDescription());
                        oldApplicationMenu.setPageCaption(menuConfig.getPageCaption());
                        oldApplicationMenu.setCaption(menuConfig.getCaption());
                        oldApplicationMenu.setPath(menuConfig.getPath());
                        oldApplicationMenu.setRemotePath(menuConfig.getRemotePath());
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
                                applicationMenuItem.setRemotePath(menuItemConfig.getRemotePath());
                                applicationMenuItem.setInstalled(Boolean.TRUE);
                                db().create(applicationMenuItem);
                            } else {
                                oldApplicationMenuItem.setMenuId(menuId);
                                oldApplicationMenuItem.setDescription(menuItemConfig.getDescription());
                                oldApplicationMenuItem.setPageCaption(menuItemConfig.getPageCaption());
                                oldApplicationMenuItem.setCaption(menuItemConfig.getCaption());
                                oldApplicationMenuItem.setPath(menuItemConfig.getPath());
                                oldApplicationMenuItem.setRemotePath(menuItemConfig.getRemotePath());
                                oldApplicationMenuItem.setInstalled(Boolean.TRUE);
                                db().updateByIdVersion(oldApplicationMenuItem);
                            }
                        }
                    }
                }
            }

            if (moduleConfig.getShortcutTiles() != null
                    && !DataUtils.isBlank(moduleConfig.getShortcutTiles().getShortcutTileList())) {
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
                    && !DataUtils.isBlank(moduleConfig.getInputControls().getInputControlList())) {
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
                GatewayAction goa = method.getAnnotation(GatewayAction.class);
                if (goa != null) {
                    sysAssetQuery.clear();
                    SystemAsset oldSystemAsset =
                            db().find(sysAssetQuery.type(SystemAssetType.REMOTECALLMETHOD).name(goa.name()));
                    String description = resolveApplicationMessage(goa.description());
                    if (oldSystemAsset == null) {
                        systemAsset.setModuleId(moduleId);
                        systemAsset.setName(goa.name());
                        systemAsset.setDescription(description);
                        systemAsset.setType(SystemAssetType.REMOTECALLMETHOD);
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

    }

    private UserToken createReservedUserToken(String loginId, Long id) throws UnifyException {
        return new UserToken(loginId, "System", getSessionContext().getRemoteAddress(), null, null, true, true, true,
                false);
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

        return new ToolingEntityItem(ta.name(), resolveApplicationMessage(ta.description()), entityClass.getName(), id,
                ta.guarded(), fieldList);
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

    private boolean isOkToRunOnWorkingDate(ScheduledTask scheduledTask) throws UnifyException {
        // Add scheduled task ID to triggeredTaskInfoMap, marking the scheduled
        // task as treated using a dummy task
        triggeredTaskInfoMap.put(scheduledTask.getId(), new TaskInfo());

        return RecordStatus.ACTIVE.equals(scheduledTask.getStatus()) && CalendarUtils.isWithinCalendar(
                scheduledTask.getWeekdays(), scheduledTask.getDays(), scheduledTask.getMonths(), workingDt);
    }

    private DynamicSqlDataSourceConfig getDynamicSqlDataSourceConfig(DataSource dataSource) throws UnifyException {
        return new DynamicSqlDataSourceConfig(dataSource.getName(), dataSource.getDialect(), dataSource.getDriverType(),
                dataSource.getConnectionUrl(), dataSource.getUserName(), dataSource.getPassword(),
                dataSource.getMaxConnections(), false);
    }

    private List<ScheduledTask> listNewScheduledTasks(Date time, List<Long> oldScheduledTaskIds) throws UnifyException {
        // Fetch truly new tasks
        ScheduledTaskQuery query = new ScheduledTaskQuery();
        if (!oldScheduledTaskIds.isEmpty()) {
            query.idNotIn(oldScheduledTaskIds);
        }
        query.startTimeBeforeOrOn(time);
        query.status(RecordStatus.ACTIVE);
        List<ScheduledTask> list = db().findAll(query);

        // Fetch old tasks that may have been modified
        if (!oldScheduledTaskIds.isEmpty()) {
            query.clear();
            query.idIn(oldScheduledTaskIds);
            query.updated(Boolean.TRUE);
            List<ScheduledTask> oldList = db().findAll(query);
            list.addAll(oldList);
        }

        // Clear update flags
        if (!list.isEmpty()) {
            List<Long> idList = new ArrayList<Long>();
            for (ScheduledTask scheduledTask : list) {
                idList.add(scheduledTask.getId());
            }

            query.clear();
            query.idIn(idList);
            db().updateAll(query, new Update().add("updated", Boolean.FALSE));
        }
        return list;
    }

    private class TaskInfo {

        private TaskMonitor taskMonitor;

        private String description;

        public TaskInfo() {

        }

        public TaskInfo(TaskMonitor taskMonitor, String description) {
            this.taskMonitor = taskMonitor;
            this.description = description;
        }

        public TaskMonitor getTaskMonitor() {
            return taskMonitor;
        }

        public String getDescription() {
            return description;
        }

        public boolean isDummy() {
            return taskMonitor == null;
        }
    }
}
