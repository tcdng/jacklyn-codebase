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

import java.util.List;
import java.util.Map;

import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.jacklyn.shared.system.data.ToolingListTypeItem;
import com.tcdng.jacklyn.shared.system.data.ToolingTransformerTypeItem;
import com.tcdng.jacklyn.shared.system.data.ToolingDocumentListItem;
import com.tcdng.jacklyn.shared.system.data.ToolingEntityItem;
import com.tcdng.jacklyn.system.data.AuthenticationLargeData;
import com.tcdng.jacklyn.system.data.DashboardDef;
import com.tcdng.jacklyn.system.data.DashboardLargeData;
import com.tcdng.jacklyn.system.data.ScheduledTaskLargeData;
import com.tcdng.jacklyn.system.data.SystemControlState;
import com.tcdng.jacklyn.system.entities.ApplicationMenu;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItem;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItemQuery;
import com.tcdng.jacklyn.system.entities.ApplicationMenuQuery;
import com.tcdng.jacklyn.system.entities.Authentication;
import com.tcdng.jacklyn.system.entities.AuthenticationQuery;
import com.tcdng.jacklyn.system.entities.Dashboard;
import com.tcdng.jacklyn.system.entities.DashboardQuery;
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
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.application.StartupShutdownHook;
import com.tcdng.unify.core.task.TaskStatus;
import com.tcdng.unify.core.ui.Tile;

/**
 * System business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface SystemService extends JacklynBusinessService, StartupShutdownHook {

    /**
     * Creates a new dashboard.
     * 
     * @param dashboard
     *            the dashboard data
     * @return the created dashboard ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createDashboard(Dashboard dashboard) throws UnifyException;

    /**
     * Finds a dashboard by ID.
     * 
     * @param id
     *            the dashboard ID
     * @return the dashboard data
     * @throws UnifyException
     *             if dashboard with ID is not found
     */
    DashboardLargeData findDashboard(Long id) throws UnifyException;

    /**
     * Finds a dashboard definition by name.
     * @param name the name to find with
     * @return dashboard with corresponding name otherwise null
     * @throws UnifyException if an error occurs
     */
    Dashboard findDashboard(String name) throws UnifyException;
    
    /**
     * Finds dashboards by query.
     * 
     * @param query
     *            the dashboard query
     * @return the list of applications found
     * @throws UnifyException
     *             if an error occurs
     */
    List<Dashboard> findDashboards(DashboardQuery query) throws UnifyException;

    /**
     * Updates an dashboard.
     * 
     * @param dashboardLargeData
     *            the dashboard data
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateDashboard(DashboardLargeData dashboardLargeData) throws UnifyException;

    /**
     * Deletes an dashboard.
     * 
     * @param id
     *            the dashboard ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteDashboard(Long id) throws UnifyException;

    /**
     * Gets runtime dashboard definition.
     * 
     * @param name
     *            the dashboard name
     * @return the runtime object
     * @throws UnifyException
     *             if dashboard with name is unknown.
     */
    DashboardDef getRuntimeDashboardDef(String name) throws UnifyException;

    /**
     * Creates a new authentication.
     * 
     * @param authenticationLargeData
     *            the authentication large data
     * @return the created authentication ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createAuthentication(AuthenticationLargeData authenticationLargeData) throws UnifyException;

    /**
     * Gets large data for specified authentication.
     * 
     * @param authenticationId
     *            the authentication ID
     * @return the authentication form
     * @throws UnifyException
     *             if an error occurs
     */
    AuthenticationLargeData findAuthentication(Long authenticationId) throws UnifyException;

    /**
     * Gets large data for specified authentication.
     * 
     * @param name
     *            the authentication name
     * @return the authentication form
     * @throws UnifyException
     *             if an error occurs
     */
    AuthenticationLargeData findAuthentication(String name) throws UnifyException;

    /**
     * Finds authentications by query.
     * 
     * @param query
     *            the authentication query
     * @return the list of authentications found
     * @throws UnifyException
     *             if an error occurs
     */
    List<Authentication> findAuthentications(AuthenticationQuery query) throws UnifyException;

    /**
     * Updates a authentication.
     * 
     * @param authenticationFormData
     *            the authentication document
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateAuthentication(AuthenticationLargeData authenticationFormData) throws UnifyException;

    /**
     * Deletes a authentication.
     * 
     * @param id
     *            the authentication ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteAuthentication(Long id) throws UnifyException;

    /**
     * Creates a remote module.
     * 
     * @param module
     *            the module
     * @return the created module ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createRemoteModule(Module module) throws UnifyException;

    /**
     * Finds module by name.
     * 
     * @param name
     *            the module name
     * @return the module data otherwise null value if not found
     * @throws UnifyException
     *             if an error occurs
     */
    Module findModule(String name) throws UnifyException;

    /**
     * Returns the module ID of module with supplied module name.
     * 
     * @param moduleName
     *            the module name
     * @return the module ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long getModuleId(String moduleName) throws UnifyException;

    /**
     * Finds module by id.
     * 
     * @param id
     *            the module ID
     * @return the module data
     * @throws UnifyException
     *             if module record with ID does not exist. If an error occurs
     */
    Module findModule(Long id) throws UnifyException;

    /**
     * Finds modules by query.
     * 
     * @param query
     *            the query
     * @return the module list
     * @throws UnifyException
     *             if an error occurs
     */
    List<Module> findModules(ModuleQuery query) throws UnifyException;

    /**
     * Finds module by query and stores result in a map.
     * 
     * @param keyClass
     *            the map key class
     * @param keyName
     *            the key name
     * @param query
     *            the query with key field name set.
     * @return Map&lt;T, Module&gt; the result map
     * @throws UnifyException
     *             if an error occurs
     */
    <T> Map<T, Module> findModules(Class<T> keyClass, String keyName, ModuleQuery query) throws UnifyException;

    /**
     * Activates a module by name.
     * 
     * @param name
     *            the module name
     * @throws UnifyException
     *             if an error occurs
     */
    void activateModule(String name) throws UnifyException;

    /**
     * Deactivates a module by name.
     * 
     * @param name
     *            the module name
     * @throws UnifyException
     *             if an error occurs
     */
    void deactivateModule(String name) throws UnifyException;

    /**
     * Finds menus by query.
     * 
     * @param query
     *            the query
     * @return the menu list
     * @throws UnifyException
     *             if an error occurs
     */
    List<ApplicationMenu> findMenus(ApplicationMenuQuery query) throws UnifyException;

    /**
     * Finds a menu record by ID.
     * 
     * @param id
     *            the menu ID
     * @return the fetched menu record
     * @throws UnifyException
     *             if menu with ID is not found
     */
    ApplicationMenu findMenu(Long id) throws UnifyException;

    /**
     * Finds menu items by query.
     * 
     * @param query
     *            the query
     * @return the menu item list
     * @throws UnifyException
     *             if an error occurs
     */
    List<ApplicationMenuItem> findMenuItems(ApplicationMenuItemQuery query) throws UnifyException;

    /**
     * Finds a menu item record by ID.
     * 
     * @param id
     *            the menu item ID
     * @return the fetched menu item record
     * @throws UnifyException
     *             if menu item with ID is not found
     */
    ApplicationMenuItem findMenuItem(Long id) throws UnifyException;

    /**
     * Save the display order of menu list.
     * 
     * @param menuList
     *            the menu list
     * @throws UnifyException
     *             if an error occurs
     */
    void saveMenuOrder(List<ApplicationMenu> menuList) throws UnifyException;

    /**
     * Save the display order of menu item list.
     * 
     * @param menuItemList
     *            the menu item list
     * @throws UnifyException
     *             if an error occurs
     */
    void saveMenuItemOrder(List<ApplicationMenuItem> menuItemList) throws UnifyException;

    /**
     * Loads application menu.
     * 
     * @param params
     *            parameters to other nodes.
     * @throws UnifyException
     *             if an error occurs
     */
    void loadApplicationMenu(String... params) throws UnifyException;

    /**
     * Finds a system parameter by name.
     * 
     * @param name
     *            the system parameter name
     * @return the system parameter
     * @throws UnifyException
     *             if an error occurs
     */
    SystemParameter findSysParameter(String name) throws UnifyException;

    /**
     * Finds a system parameter by ID.
     * 
     * @param id
     *            the system parameter ID
     * @return the system parameter
     * @throws UnifyException
     *             if system parameter with record is not found
     */
    SystemParameter findSysParameter(Long id) throws UnifyException;

    /**
     * Finds system parameters by query.
     * 
     * @param query
     *            the query
     * @return the system parameter list
     * @throws UnifyException
     *             if an error occurs
     */
    List<SystemParameter> findSysParameters(SystemParameterQuery query) throws UnifyException;

    /**
     * Finds the current states of system parameters that are for system control.
     * 
     * @param query
     *            the search query
     * @return list of system control states
     * @throws UnifyException
     *             if an error occurs
     */
    List<SystemControlState> findSystemControlStates(SystemParameterQuery query) throws UnifyException;

    /**
     * Gets system parameter value and converts to the specified type.
     * 
     * @param clazz
     *            the type to convert to
     * @param name
     *            the system parameter name
     * @return the resulting value
     * @throws UnifyException
     *             if parameter with name is unknown. if a value data conversion
     *             error occurs
     */
    <T> T getSysParameterValue(Class<T> clazz, String name) throws UnifyException;

    /**
     * Updates the value field of a system parameter.
     * 
     * @param name
     *            the system parameter name
     * @param value
     *            the value to set
     * @return the number of records updated
     * @throws UnifyException
     *             if an error occurs
     */
    int setSysParameterValue(String name, Object value) throws UnifyException;

    /**
     * Creates a scheduled task.
     * 
     * @param scheduledTask
     *            the scheduled task to create
     * @return the Id of the created record
     * @throws UnifyException
     *             if scheduled task creation failed
     */
    Long createScheduledTask(ScheduledTask scheduledTask) throws UnifyException;

    /**
     * Creates a scheduled task.
     * 
     * @param scheduledTaskFormData
     *            the scheduled task document
     * @return the Id of the created record
     * @throws UnifyException
     *             if scheduled task creation failed
     */
    Long createScheduledTask(ScheduledTaskLargeData scheduledTaskFormData) throws UnifyException;

    /**
     * Finds scheduled tasks by query.
     * 
     * @param query
     *            the search query
     * @return list of scheduled tasks
     * @throws UnifyException
     *             if an error occurs
     */
    List<ScheduledTask> findScheduledTasks(ScheduledTaskQuery query) throws UnifyException;

    /**
     * Finds scheduled task by Id.
     * 
     * @param id
     *            the scheduled task Id
     * @return the scheduled task record
     * @throws UnifyException
     *             if scheduled task with id not found
     */
    ScheduledTask findScheduledTask(Long id) throws UnifyException;

    /**
     * Finds scheduled task by Id.
     * 
     * @param id
     *            the scheduled task Id
     * @return the scheduled task document
     * @throws UnifyException
     *             if scheduled task with id not found
     */
    ScheduledTaskLargeData findScheduledTaskDocument(Long id) throws UnifyException;

    /**
     * Updates a scheduled task record by Id and version number.
     * 
     * @param scheduledTask
     *            the scheduled task
     * @return the update count
     * @throws UnifyException
     *             if scheduled task record does not exist
     */
    int updateScheduledTask(ScheduledTask scheduledTask) throws UnifyException;

    /**
     * Updates a scheduled task record by Id and version number.
     * 
     * @param scheduledTaskFormData
     *            the scheduled task document
     * @return the update count
     * @throws UnifyException
     *             if scheduled task record does not exist
     */
    int updateScheduledTask(ScheduledTaskLargeData scheduledTaskFormData) throws UnifyException;

    /**
     * Deletes scheduled task record by id.
     * 
     * @param scheduledTaskId
     *            the scheduled task id
     * @return the delete count
     * @throws UnifyException
     *             if scheduled task with Id does not exist
     */
    int deleteScheduledTask(Long scheduledTaskId) throws UnifyException;

    /**
     * Reloads parameter values for a scheduled task
     * 
     * @param scheduledTaskFormData
     *            the scheduled task document
     * @return the task document
     * @throws UnifyException
     *             if an error occurs
     */
    ScheduledTaskLargeData loadScheduledTaskDocumentValues(ScheduledTaskLargeData scheduledTaskFormData)
            throws UnifyException;

    /**
     * Activates a scheduled task by ID.
     * 
     * @param schduledTaskId
     *            the scheduled task ID
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int activateScheduledTask(Long schduledTaskId) throws UnifyException;

    /**
     * Deactivates a scheduled task by ID.
     * 
     * @param schduledTaskId
     *            the scheduled task ID
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int deactivateScheduledTask(Long schduledTaskId) throws UnifyException;

    /**
     * Creates a scheduled task history for a scheduled task.
     * 
     * @param scheduledTaskId
     *            the scheduled task ID
     * @param taskStatus
     *            the task status
     * @param errorMessages
     *            the error messages
     * @return the scheduled task history ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createScheduledTaskHistory(Long scheduledTaskId, TaskStatus taskStatus, String errorMessages)
            throws UnifyException;

    /**
     * Finds scheduled task history by query.
     * 
     * @param query
     *            the search query
     * @return list of scheduled task history
     * @throws UnifyException
     *             if an error occurs
     */
    List<ScheduledTaskHist> findScheduledTaskHistory(ScheduledTaskHistQuery query) throws UnifyException;

    /**
     * Finds a scheduled task history record by ID.
     * 
     * @param id
     *            the search by ID
     * @return the scheduled task history record
     * @throws UnifyException
     *             if record with supplied id is not found
     */
    ScheduledTaskHist findScheduledTaskHist(Long id) throws UnifyException;

    /**
     * Creates a new theme.
     * 
     * @param theme
     *            the theme data
     * @return the created theme ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createTheme(Theme theme) throws UnifyException;

    /**
     * Finds theme by ID.
     * 
     * @param themeId
     *            the theme ID
     * @return the theme data
     * @throws UnifyException
     *             if theme with ID is not found
     */
    Theme findTheme(Long themeId) throws UnifyException;

    /**
     * Finds themes by query.
     * 
     * @param query
     *            the theme query
     * @return the list of themes found
     * @throws UnifyException
     *             if an error occurs
     */
    List<Theme> findThemes(ThemeQuery query) throws UnifyException;

    /**
     * Updates a theme.
     * 
     * @param theme
     *            the theme
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateTheme(Theme theme) throws UnifyException;

    /**
     * Deletes a theme.
     * 
     * @param id
     *            the theme ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteTheme(Long id) throws UnifyException;

    /**
     * Creates a new supported locale.
     * 
     * @param supportedLocale
     *            the supported locale data
     * @return the created supported locale ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createSupportedLocale(SupportedLocale supportedLocale) throws UnifyException;

    /**
     * Finds supported locale by ID.
     * 
     * @param supportedLocaleId
     *            the supported locale ID
     * @return the supported locale data
     * @throws UnifyException
     *             if supported locale with ID is not found
     */
    SupportedLocale findSupportedLocale(Long supportedLocaleId) throws UnifyException;

    /**
     * Finds supported locales by query.
     * 
     * @param query
     *            the supported locale query
     * @return the list of supported locales found
     * @throws UnifyException
     *             if an error occurs
     */
    List<SupportedLocale> findSupportedLocales(SupportedLocaleQuery query) throws UnifyException;

    /**
     * Updates a supported locale.
     * 
     * @param supportedLocale
     *            the supported locale
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateSupportedLocale(SupportedLocale supportedLocale) throws UnifyException;

    /**
     * Deletes a supported locale.
     * 
     * @param id
     *            the supported locale ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteSupportedLocale(Long id) throws UnifyException;

    /**
     * Creates a new shortcut tile.
     * 
     * @param shortcutTile
     *            the shortcut tile data
     * @return the created shortcut tile ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createShortcutTile(ShortcutTile shortcutTile) throws UnifyException;

    /**
     * Finds a shortcut tile by ID.
     * 
     * @param shortcutTileId
     *            the shortcut tile ID
     * @return the shortcut tile data
     * @throws UnifyException
     *             if shortcut tile with ID is not found
     */
    ShortcutTile findShortcutTile(Long shortcutTileId) throws UnifyException;

    /**
     * Finds shortcut tiles by query.
     * 
     * @param query
     *            the shortcut tile query
     * @return the list of shortcut tiles found
     * @throws UnifyException
     *             if an error occurs
     */
    List<ShortcutTile> findShortcutTiles(ShortcutTileQuery query) throws UnifyException;

    /**
     * Updates a shortcut tile.
     * 
     * @param shortcutTile
     *            the shortcut tile
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateShortcutTile(ShortcutTile shortcutTile) throws UnifyException;

    /**
     * Deletes a shortcut tile.
     * 
     * @param id
     *            the shortcut tile ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteShortcutTile(Long id) throws UnifyException;

    /**
     * Save the display order of shortcut tile list.
     * 
     * @param shortcutTileList
     *            the shortcut tile list
     * @throws UnifyException
     *             if an error occurs
     */
    void saveShortcutTileOrder(List<ShortcutTile> shortcutTileList) throws UnifyException;

    /**
     * Generates user interface tiles for shortcut tiles that match query.
     * 
     * @param query
     *            the shortcut query
     * @return a list of tiles
     * @throws UnifyException
     *             if an error occurs
     */
    List<Tile> generateTiles(ShortcutTileQuery query) throws UnifyException;

    /**
     * Generates user interface tiles using supplied shortcut tile list.
     * 
     * @param shortcutTileList
     *            the shortcut tile list
     * @return a list of tiles
     * @throws UnifyException
     *             if an error occurs
     */
    List<Tile> generateTiles(List<ShortcutTile> shortcutTileList) throws UnifyException;

    /**
     * Creates a new input control definition.
     * 
     * @param inputCtrlDef
     *            the input control definition data
     * @return the created input control definition ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createInputCtrlDef(InputCtrlDef inputCtrlDef) throws UnifyException;

    /**
     * Finds input control definition by ID.
     * 
     * @param inputCtrlDefId
     *            the input control definition ID
     * @return the input control definition data
     * @throws UnifyException
     *             if input control definition with ID is not found
     */
    InputCtrlDef findInputCtrlDef(Long inputCtrlDefId) throws UnifyException;

    /**
     * Finds input control definitions by query.
     * 
     * @param query
     *            the input control definition query
     * @return the list of input control definitions found
     * @throws UnifyException
     *             if an error occurs
     */
    List<InputCtrlDef> findInputCtrlDefs(InputCtrlDefQuery query) throws UnifyException;

    /**
     * Updates a input control definition.
     * 
     * @param inputCtrlDef
     *            the input control definition
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateInputCtrlDef(InputCtrlDef inputCtrlDef) throws UnifyException;

    /**
     * Deletes a input control definition.
     * 
     * @param id
     *            the input control definition ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteInputCtrlDef(Long id) throws UnifyException;

    /**
     * Finds system asset by ID.
     * 
     * @param systemAssetId
     *            the system asset ID
     * @return the system asset data
     * @throws UnifyException
     *             if system asset with ID is not found
     */
    SystemAsset findSystemAsset(Long systemAssetId) throws UnifyException;

    /**
     * Finds system assets by query.
     * 
     * @param query
     *            the system asset query
     * @return the list of system assets found
     * @throws UnifyException
     *             if an error occurs
     */
    List<SystemAsset> findSystemAssets(SystemAssetQuery query) throws UnifyException;

    /**
     * Finds system asset IDs by query.
     * 
     * @param query
     *            the system asset query
     * @return the list of system asset IDs found
     * @throws UnifyException
     *             if an error occurs
     */
    List<Long> findSystemAssetIds(SystemAssetQuery query) throws UnifyException;

    /**
     * Gets the number of unique active user sessions.
     * 
     * @return the number of unique sessions
     * @throws UnifyException
     *             if an error occurs
     */
    int getUniqueActiveUserSessions() throws UnifyException;

    /**
     * Finds all tooling base types.
     * 
     * @return list of base types
     * @throws UnifyException
     *             if an error occurs
     */
    List<ToolingEntityItem> findToolingBaseTypes() throws UnifyException;

    /**
     * Finds all tooling document types.
     * 
     * @return list of document types
     * @throws UnifyException
     *             if an error occurs
     */
    List<ToolingEntityItem> findToolingDocumentTypes() throws UnifyException;

    /**
     * Finds all tooling enumeration types.
     * 
     * @return list of enumeration types
     * @throws UnifyException
     *             if an error occurs
     */
    List<ToolingEntityItem> findToolingEnumTypes() throws UnifyException;

    /**
     * Finds all tooling document list items.
     * 
     * @return list of document list items
     * @throws UnifyException
     *             if an error occurs
     */
    List<ToolingDocumentListItem> findToolingDocumentListItems() throws UnifyException;
    
    /**
     * Finds all tooling transformer types.
     * 
     * @return list of transformer types
     * @throws UnifyException
     *             if an error occurs
     */
    List<ToolingTransformerTypeItem> findToolingTransformerTypes() throws UnifyException;

    /**
     * Finds all tooling list types.
     * 
     * @return list of list types
     * @throws UnifyException
     *             if an error occurs
     */
    List<ToolingListTypeItem> findToolingListTypes() throws UnifyException;
}
