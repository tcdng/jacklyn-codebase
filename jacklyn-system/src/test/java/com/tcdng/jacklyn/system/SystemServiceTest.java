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
package com.tcdng.jacklyn.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.tcdng.jacklyn.common.AbstractJacklynTest;
import com.tcdng.jacklyn.common.TestCustomerModuleNameConstants;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.jacklyn.system.data.ScheduledTaskLargeData;
import com.tcdng.jacklyn.system.data.SystemControlState;
import com.tcdng.jacklyn.system.entities.ApplicationMenu;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItem;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItemQuery;
import com.tcdng.jacklyn.system.entities.ApplicationMenuQuery;
import com.tcdng.jacklyn.system.entities.ShortcutTile;
import com.tcdng.jacklyn.system.entities.ShortcutTileQuery;
import com.tcdng.jacklyn.system.entities.Module;
import com.tcdng.jacklyn.system.entities.ModuleQuery;
import com.tcdng.jacklyn.system.entities.ScheduledTask;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHist;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHistQuery;
import com.tcdng.jacklyn.system.entities.ScheduledTaskQuery;
import com.tcdng.jacklyn.system.entities.SystemParameter;
import com.tcdng.jacklyn.system.entities.SystemParameterQuery;
import com.tcdng.jacklyn.system.entities.Theme;
import com.tcdng.jacklyn.system.entities.ThemeQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.data.Input;
import com.tcdng.unify.core.data.Inputs;
import com.tcdng.unify.core.system.entities.ParameterValues;
import com.tcdng.unify.core.task.TaskStatus;
import com.tcdng.unify.core.ui.Tile;

/**
 * System business service test.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class SystemServiceTest extends AbstractJacklynTest {

    @Test
    public void testFindModuleByName() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Module module = systemService.findModule(SystemModuleNameConstants.SYSTEM_MODULE);
        assertNotNull(module);
        assertEquals(SystemModuleNameConstants.SYSTEM_MODULE, module.getName());
        assertFalse("$m{system.module}".equals(module.getDescription()));
    }

    @Test
    public void testGetModuleId() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Module module = systemService.findModule(SystemModuleNameConstants.SYSTEM_MODULE);
        Long moduleId = systemService.getModuleId(SystemModuleNameConstants.SYSTEM_MODULE);
        assertNotNull(module);
        assertEquals(module.getId(), moduleId);
    }

    @Test
    public void testFindModuleById() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Module module = systemService.findModule(SystemModuleNameConstants.SYSTEM_MODULE);
        assertNotNull(module);
        Module fetchedModule = systemService.findModule(module.getId());
        assertNotNull(fetchedModule);
        assertEquals(module, fetchedModule);
    }

    @Test
    public void testFindModules() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Module module = systemService.findModule(SystemModuleNameConstants.SYSTEM_MODULE);

        List<Module> moduleList = systemService
                .findModules(new ModuleQuery().name(SystemModuleNameConstants.SYSTEM_MODULE));
        assertNotNull(moduleList);
        assertEquals(1, moduleList.size());
        assertEquals(module, moduleList.get(0));
    }

    @Test
    public void testFindModulesWithMappedResult() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Module module = systemService.findModule(SystemModuleNameConstants.SYSTEM_MODULE);

        Map<Long, Module> moduleMap = systemService.findModules(Long.class, "id",
                (ModuleQuery) new ModuleQuery().name(SystemModuleNameConstants.SYSTEM_MODULE));
        assertNotNull(moduleMap);
        assertEquals(1, moduleMap.size());

        Module fetchedModule = moduleMap.get(module.getId());
        assertEquals(module, fetchedModule);
    }

    @Test
    public void testDeactivateDeactivatableModule() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Module module = systemService.findModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
        assertEquals(RecordStatus.ACTIVE, module.getStatus());

        systemService.deactivateModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
        module = systemService.findModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
        assertEquals(RecordStatus.INACTIVE, module.getStatus());
    }

    @Test(expected = UnifyException.class)
    public void testDeactivateNonDeactivatableModule() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Module module = systemService.findModule(SystemModuleNameConstants.SYSTEM_MODULE);
        assertEquals(RecordStatus.ACTIVE, module.getStatus());

        systemService.deactivateModule(SystemModuleNameConstants.SYSTEM_MODULE);
    }

    @Test
    public void testActivateModule() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        systemService.deactivateModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
        Module module = systemService.findModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
        assertEquals(RecordStatus.INACTIVE, module.getStatus());

        systemService.activateModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);

        module = systemService.findModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
        assertEquals(RecordStatus.ACTIVE, module.getStatus());
    }

    @Test
    public void testFindMenus() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ApplicationMenuQuery query = new ApplicationMenuQuery();
        query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
        List<ApplicationMenu> menuList = systemService.findMenus(query);
        assertNotNull(menuList);
        assertEquals(2, menuList.size());
    }

    @Test
    public void testFindMenuById() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ApplicationMenuQuery query = new ApplicationMenuQuery();
        query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
        List<ApplicationMenu> menuList = systemService.findMenus(query);

        ApplicationMenu menu = systemService.findMenu(menuList.get(0).getId());
        assertEquals(menuList.get(0), menu);
    }

    @Test
    public void testFindMenuItems() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ApplicationMenuItemQuery query = new ApplicationMenuItemQuery();
        query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
        List<ApplicationMenuItem> menuItemList = systemService.findMenuItems(query);
        assertNotNull(menuItemList);
        assertFalse(menuItemList.isEmpty());
    }

    @Test
    public void testFindMenuItemById() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ApplicationMenuItemQuery query = new ApplicationMenuItemQuery();
        query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
        List<ApplicationMenuItem> menuItemList = systemService.findMenuItems(query);
        assertFalse(menuItemList.isEmpty());

        ApplicationMenuItem menuItem = systemService.findMenuItem(menuItemList.get(0).getId());
        assertEquals(menuItemList.get(0), menuItem);
    }

    @Test
    public void testSaveMenuOrder() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ApplicationMenuQuery query = new ApplicationMenuQuery();
        query.orderById().ignoreEmptyCriteria(true);
        List<ApplicationMenu> menuList = systemService.findMenus(query);
        assertEquals(0, menuList.get(0).getDisplayOrder());
        assertEquals(0, menuList.get(1).getDisplayOrder());

        systemService.saveMenuOrder(menuList);
        menuList = systemService.findMenus(query);
        assertEquals(0, menuList.get(0).getDisplayOrder());
        assertEquals(1, menuList.get(1).getDisplayOrder());
    }

    @Test
    public void testSaveMenuItemOrder() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ApplicationMenuItemQuery query = new ApplicationMenuItemQuery();
        query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
        query.orderById();
        List<ApplicationMenuItem> menuItemList = systemService.findMenuItems(query);
        assertEquals(0, menuItemList.get(0).getDisplayOrder());
        assertEquals(0, menuItemList.get(1).getDisplayOrder());
        assertEquals(0, menuItemList.get(2).getDisplayOrder());
        assertEquals(0, menuItemList.get(3).getDisplayOrder());

        systemService.saveMenuItemOrder(menuItemList);
        menuItemList = systemService.findMenuItems(query);
        assertEquals(0, menuItemList.get(0).getDisplayOrder());
        assertEquals(1, menuItemList.get(1).getDisplayOrder());
        assertEquals(2, menuItemList.get(2).getDisplayOrder());
        assertEquals(3, menuItemList.get(3).getDisplayOrder());
    }

    @Test
    public void testFindSysParameterByName() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        SystemParameter sysParameter = systemService
                .findSysParameter(SystemModuleSysParamConstants.SYSPARAM_APPLICATION_VERSION);
        assertNotNull(sysParameter);
        assertEquals(SystemModuleSysParamConstants.SYSPARAM_APPLICATION_VERSION, sysParameter.getName());
        assertFalse("$m{system.sysparam.applicationversion}".equals(sysParameter.getDescription()));
        assertEquals("!ui-text size:24", sysParameter.getEditor());
        assertEquals("1.0.0", sysParameter.getValue());
        assertEquals(Boolean.FALSE, sysParameter.getControl());
        assertEquals(Boolean.FALSE, sysParameter.getEditable());
    }

    @Test
    public void testFindSysParameterById() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        SystemParameter sysParameter = systemService
                .findSysParameter(SystemModuleSysParamConstants.SYSPARAM_APPLICATION_VERSION);
        SystemParameter fetchedSysParameter = systemService.findSysParameter(sysParameter.getId());
        assertEquals(sysParameter, fetchedSysParameter);
    }

    @Test
    public void testFindSysParameters() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        SystemParameterQuery query = new SystemParameterQuery();
        query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
        query.addOrder("name");
        List<SystemParameter> sysParameterList = systemService.findSysParameters(query);
        assertNotNull(sysParameterList);
        assertEquals(SystemModuleSysParamConstants.SYSPARAM_APPLICATION_VERSION, sysParameterList.get(0).getName());
        assertEquals(SystemModuleSysParamConstants.SYSPARAM_CLIENT_TITLE, sysParameterList.get(1).getName());
    }

    @Test
    public void testFindSystemControlStates() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        SystemParameterQuery query = new SystemParameterQuery();
        query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
        List<SystemControlState> systemControlList = systemService.findSystemControlStates(query);
        assertNotNull(systemControlList);
        assertEquals(1, systemControlList.size());

        SystemControlState systemControlState = systemControlList.get(0);
        assertEquals(SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_ENABLED, systemControlState.getName());
        assertFalse("$m{system.sysparam.enablesystemscheduler}".equals(systemControlState.getDescription()));
        assertEquals(Boolean.FALSE, systemControlState.isEnabled());
    }

    @Test
    public void testGetSysParameterValue() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        int maxTrigger = systemService.getSysParameterValue(int.class,
                SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_MAX_TRIGGER);
        assertTrue(maxTrigger > 0);
    }

    @Test
    public void testSetSysParameterValue() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        int count = systemService
                .setSysParameterValue(SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_MAX_TRIGGER, "20");
        assertEquals(1, count);
        int maxTrigger = systemService.getSysParameterValue(int.class,
                SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_MAX_TRIGGER);
        assertEquals(20, maxTrigger);
    }

    @Test
    public void testFindNormalizedScheduledTaskParameters() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        Inputs inputs = document.getScheduledTaskParams();
        assertNotNull(inputs);
        assertEquals(1, inputs.size());
        Input<?> input = inputs.getInput("batchSize");
        assertEquals("batchSize", input.getName());
        assertEquals("Batch Size", input.getDescription());
        assertEquals("!ui-integer", input.getEditor());
        assertEquals(Integer.class, input.getType());
    }

    @Test
    public void testCreateScheduledTask() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        Inputs inputs = document.getScheduledTaskParams();
        inputs.setInputValue("batchSize", "400");
        Long scheduledTaskId = systemService.createScheduledTask(document);
        assertNotNull(scheduledTaskId);
    }

    @Test(expected = UnifyException.class)
    public void testCreateScheduledTaskWithMandatoryParamNotSet() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        systemService.createScheduledTask(document);
    }

    @Test
    public void testFindScheduledTasks() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        Inputs inputs = document.getScheduledTaskParams();
        inputs.setInputValue("batchSize", "400");
        systemService.createScheduledTask(document);
        List<ScheduledTask> scheduledTaskList = systemService
                .findScheduledTasks(new ScheduledTaskQuery().taskName("testschedulabletask"));
        assertNotNull(scheduledTaskList);
        assertEquals(1, scheduledTaskList.size());
        assertEquals(scheduledTask.getDescription(), scheduledTaskList.get(0).getDescription());
        assertEquals(scheduledTask.getStartTime(), scheduledTaskList.get(0).getStartTime());
        assertEquals(scheduledTask.getTaskName(), scheduledTaskList.get(0).getTaskName());
    }

    @Test
    public void testFindScheduledTaskById() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        Inputs inputs = document.getScheduledTaskParams();
        inputs.setInputValue("batchSize", "400");
        Long scheduledTaskId = systemService.createScheduledTask(document);

        ScheduledTask fetchedScheduledTask = systemService.findScheduledTask(scheduledTaskId);
        assertNotNull(fetchedScheduledTask);
        assertEquals(scheduledTask, fetchedScheduledTask);
    }

    @Test
    public void testUpdateScheduledTask() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        Inputs inputs = document.getScheduledTaskParams();
        inputs.setInputValue("batchSize", "400");
        Long scheduledTaskId = systemService.createScheduledTask(document);

        ScheduledTaskLargeData fetchDocument = systemService.findScheduledTaskDocument(scheduledTaskId);
        ScheduledTask fetchedScheduledTask = fetchDocument.getData();
        fetchedScheduledTask.setFrequency(Integer.valueOf(2));
        fetchedScheduledTask.setFrequencyUnit(FrequencyUnit.HOUR);
        fetchedScheduledTask.setStartTime(systemService.getNow());
        fetchDocument.getScheduledTaskParams().setInputValue("batchSize", "1000");

        systemService.updateScheduledTask(fetchDocument);

        ScheduledTaskLargeData updateDocument = systemService.findScheduledTaskDocument(scheduledTaskId);
        ScheduledTask updatedScheduledTask = updateDocument.getData();
        assertFalse(scheduledTask.equals(updatedScheduledTask));
        assertEquals(fetchedScheduledTask, updatedScheduledTask);
        assertEquals("1000", updateDocument.getScheduledTaskParams().getInputValue("batchSize"));
    }

    @Test
    public void testDeleteScheduledTask() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        document.getScheduledTaskParams().setInputValue("batchSize", "400");
        Long scheduledTaskId = systemService.createScheduledTask(document);

        int count = systemService.deleteScheduledTask(scheduledTaskId);
        assertEquals(1, count);
    }

    @Test
    public void testDeactivateScheduledTask() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        document.getScheduledTaskParams().setInputValue("batchSize", "400");
        Long scheduledTaskId = systemService.createScheduledTask(document);

        ScheduledTask fetchedScheduledTask = systemService.findScheduledTask(scheduledTaskId);
        assertEquals(RecordStatus.ACTIVE, fetchedScheduledTask.getStatus());

        systemService.deactivateScheduledTask(scheduledTaskId);

        fetchedScheduledTask = systemService.findScheduledTask(scheduledTaskId);
        assertEquals(RecordStatus.INACTIVE, fetchedScheduledTask.getStatus());
    }

    @Test
    public void testActivateScheduledTask() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        document.getScheduledTaskParams().setInputValue("batchSize", "400");
        Long scheduledTaskId = systemService.createScheduledTask(document);

        ScheduledTask fetchedScheduledTask = systemService.findScheduledTask(scheduledTaskId);
        assertEquals(RecordStatus.ACTIVE, fetchedScheduledTask.getStatus());

        systemService.deactivateScheduledTask(scheduledTaskId);
        systemService.activateScheduledTask(scheduledTaskId);

        fetchedScheduledTask = systemService.findScheduledTask(scheduledTaskId);
        assertEquals(RecordStatus.ACTIVE, fetchedScheduledTask.getStatus());
    }

    @Test
    public void testCreateScheduledTaskHistory() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        document.getScheduledTaskParams().setInputValue("batchSize", "400");
        Long scheduledTaskId = systemService.createScheduledTask(document);

        Long scheduledTaskHistId = systemService.createScheduledTaskHistory(scheduledTaskId, TaskStatus.INITIALIZED,
                null);
        assertNotNull(scheduledTaskHistId);
    }

    @Test
    public void testFindScheduledTaskHistory() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        document.getScheduledTaskParams().setInputValue("batchSize", "400");
        Long scheduledTaskId = systemService.createScheduledTask(document);

        systemService.createScheduledTaskHistory(scheduledTaskId, TaskStatus.INITIALIZED, null);
        systemService.createScheduledTaskHistory(scheduledTaskId, TaskStatus.CANCELED, "Some message!");

        List<ScheduledTaskHist> scheduledTaskHistList = systemService.findScheduledTaskHistory(
                (ScheduledTaskHistQuery) new ScheduledTaskHistQuery().scheduledTaskId(scheduledTaskId).orderById());
        assertNotNull(scheduledTaskHistList);
        assertEquals(2, scheduledTaskHistList.size());

        ScheduledTaskHist scheduledTaskHist = scheduledTaskHistList.get(0);
        assertEquals(scheduledTaskId, scheduledTaskHist.getScheduledTaskId());
        assertEquals("testschedulabletask", scheduledTaskHist.getTaskName());
        assertNull(scheduledTaskHist.getErrorMsg());
        assertEquals(TaskStatus.INITIALIZED, scheduledTaskHist.getTaskStatus());

        scheduledTaskHist = scheduledTaskHistList.get(1);
        assertEquals(scheduledTaskId, scheduledTaskHist.getScheduledTaskId());
        assertEquals("testschedulabletask", scheduledTaskHist.getTaskName());
        assertEquals("Some message!", scheduledTaskHist.getErrorMsg());
        assertEquals(TaskStatus.CANCELED, scheduledTaskHist.getTaskStatus());
    }

    @Test
    public void testFindScheduledTaskHistoryById() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ScheduledTask scheduledTask = getScheduledTask(systemService.getNow());
        ScheduledTaskLargeData document = systemService
                .loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTask));
        document.getScheduledTaskParams().setInputValue("batchSize", "400");
        Long scheduledTaskId = systemService.createScheduledTask(document);

        Long scheduledTaskHistId = systemService.createScheduledTaskHistory(scheduledTaskId, TaskStatus.INITIALIZED,
                "Started!");
        ScheduledTaskHist scheduledTaskHist = systemService.findScheduledTaskHist(scheduledTaskHistId);
        assertEquals(scheduledTaskId, scheduledTaskHist.getScheduledTaskId());
        assertEquals("testschedulabletask", scheduledTaskHist.getTaskName());
        assertEquals("Started!", scheduledTaskHist.getErrorMsg());
        assertEquals(TaskStatus.INITIALIZED, scheduledTaskHist.getTaskStatus());
    }

    @Test
    public void testCreateTheme() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Theme theme = getTheme();
        Long themeId = systemService.createTheme(theme);
        assertNotNull(themeId);
    }

    @Test
    public void testFindThemeById() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Theme theme = getTheme();
        Long themeId = systemService.createTheme(theme);

        Theme fetchedTheme = systemService.findTheme(themeId);
        assertNotNull(fetchedTheme);
        assertEquals(theme, fetchedTheme);
    }

    @Test
    public void testFindThemes() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Theme theme = getTheme();
        systemService.createTheme(theme);

        List<Theme> themeList = systemService.findThemes(new ThemeQuery().name("thm001"));
        assertNotNull(themeList);
        assertEquals(1, themeList.size());
        assertEquals(theme.getName(), themeList.get(0).getName());
        assertEquals(theme.getDescription(), themeList.get(0).getDescription());
    }

    @Test
    public void testUpdateTheme() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Theme theme = getTheme();
        Long themeId = systemService.createTheme(theme);

        Theme fetchedTheme = systemService.findTheme(themeId);
        fetchedTheme.setDescription("Red Boat");
        fetchedTheme.setResourcePath("/redboat");
        int count = systemService.updateTheme(fetchedTheme);
        assertEquals(1, count);

        Theme updatedTheme = systemService.findTheme(themeId);
        assertEquals(fetchedTheme, updatedTheme);
        assertFalse(theme.equals(updatedTheme));
    }

    @Test
    public void testDeleteTheme() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        Theme theme = getTheme();
        Long themeId = systemService.createTheme(theme);

        int count = systemService.deleteTheme(themeId);
        assertEquals(1, count);
    }

    @Test
    public void testCreateShortcutTile() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ShortcutTile shortcutTile = getShortcutTile();
        Long shortcutTileId = systemService.createShortcutTile(shortcutTile);
        assertNotNull(shortcutTileId);
    }

    @Test
    public void testFindShortcutTileById() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ShortcutTile shortcutTile = getShortcutTile();
        Long shortcutTileId = systemService.createShortcutTile(shortcutTile);

        ShortcutTile fetchedShortcutTile = systemService.findShortcutTile(shortcutTileId);
        assertNotNull(fetchedShortcutTile);
        assertEquals(shortcutTile.getName(), fetchedShortcutTile.getName());
        assertEquals(shortcutTile.getCaption(), fetchedShortcutTile.getCaption());
        assertEquals(shortcutTile.getDescription(), fetchedShortcutTile.getDescription());
        assertEquals(shortcutTile.getGenerator(), fetchedShortcutTile.getGenerator());
        assertEquals(shortcutTile.getImageSrc(), fetchedShortcutTile.getImageSrc());
        assertEquals(shortcutTile.getStatus(), fetchedShortcutTile.getStatus());
    }

    @Test
    public void testFindShortcutTiles() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ShortcutTile shortcutTile = getShortcutTile();
        systemService.createShortcutTile(shortcutTile);

        List<ShortcutTile> shortcutTileList = systemService
                .findShortcutTiles(new ShortcutTileQuery().name("tile001"));
        assertNotNull(shortcutTileList);
        assertEquals(1, shortcutTileList.size());
        ShortcutTile fetchedShortcutTile = shortcutTileList.get(0);
        assertEquals(shortcutTile.getName(), fetchedShortcutTile.getName());
        assertEquals(shortcutTile.getCaption(), fetchedShortcutTile.getCaption());
        assertEquals(shortcutTile.getDescription(), fetchedShortcutTile.getDescription());
        assertEquals(shortcutTile.getGenerator(), fetchedShortcutTile.getGenerator());
        assertEquals(shortcutTile.getImageSrc(), fetchedShortcutTile.getImageSrc());
        assertEquals(shortcutTile.getStatus(), fetchedShortcutTile.getStatus());
    }

    @Test
    public void testUpdateShortcutTile() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ShortcutTile shortcutTile = getShortcutTile();
        Long shortcutTileId = systemService.createShortcutTile(shortcutTile);

        ShortcutTile fetchedShortcutTile = systemService.findShortcutTile(shortcutTileId);
        fetchedShortcutTile.setDescription("New Currencies");
        int count = systemService.updateShortcutTile(fetchedShortcutTile);
        assertEquals(1, count);

        ShortcutTile updatedShortcutTile = systemService.findShortcutTile(shortcutTileId);
        assertEquals(fetchedShortcutTile, updatedShortcutTile);
        assertFalse(shortcutTile.equals(updatedShortcutTile));
    }

    @Test
    public void testDeleteShortcutTile() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ShortcutTile shortcutTile = getShortcutTile();
        Long shortcutTileId = systemService.createShortcutTile(shortcutTile);

        int count = systemService.deleteShortcutTile(shortcutTileId);
        assertEquals(1, count);
    }

    @Test
    public void testSaveShortcutTileOrder() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ShortcutTile shortcutTile = getShortcutTile();
        systemService.createShortcutTile(shortcutTile);

        List<ShortcutTile> shortcutTileList = systemService
                .findShortcutTiles(new ShortcutTileQuery().name("tile001"));
        assertEquals(0, shortcutTileList.get(0).getDisplayOrder());

        systemService.saveShortcutTileOrder(shortcutTileList);
        shortcutTileList = systemService.findShortcutTiles(new ShortcutTileQuery().name("tile001"));
        assertEquals(0, shortcutTileList.get(0).getDisplayOrder());
    }

    @Test
    public void testGenerateTiles() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);

        ShortcutTile shortcutTile = getShortcutTile();
        systemService.createShortcutTile(shortcutTile);

        List<ShortcutTile> shortcutTileList = systemService
                .findShortcutTiles(new ShortcutTileQuery().name("tile001"));

        List<Tile> tileList = systemService.generateTiles(shortcutTileList);
        assertNotNull(tileList);
        assertEquals(1, tileList.size());

        Tile tile = tileList.get(0);
        assertEquals(shortcutTile.getCaption(), tile.getCaption());
        assertEquals(shortcutTile.getPath(), tile.getActionPath());
        assertEquals(shortcutTile.getImageSrc(), tile.getImageSrc());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onSetup() throws Exception {
        deleteAll(ShortcutTile.class, Theme.class, ScheduledTaskHist.class, ParameterValues.class,
                ScheduledTask.class);
    }

    @Override
    protected void onTearDown() throws Exception {

    }

    private ScheduledTask getScheduledTask(Date now) {
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.setDescription("Test Scheduled Taskable");
        scheduledTask.setStartTime(now);
        scheduledTask.setTaskName("testschedulabletask");
        return scheduledTask;
    }

    private Theme getTheme() {
        Theme theme = new Theme();
        theme.setName("thm001");
        theme.setDescription("Blue Boat");
        theme.setResourcePath("/blueboat");
        theme.setStatus(RecordStatus.ACTIVE);
        return theme;
    }

    private ShortcutTile getShortcutTile() throws Exception {
        SystemService systemService = (SystemService) getComponent(SystemModuleNameConstants.SYSTEMSERVICE);
        Long moduleId = systemService.getModuleId(SystemModuleNameConstants.SYSTEM_MODULE);
        ShortcutTile shortcutTile = new ShortcutTile();
        shortcutTile.setModuleId(moduleId);
        shortcutTile.setName("tile001");
        shortcutTile.setDescription("Currencies");
        shortcutTile.setImageSrc("/currencies.png");
        shortcutTile.setCaption("Manage Currencies");
        shortcutTile.setPath("/accounts/currencies");
        shortcutTile.setStatus(RecordStatus.ACTIVE);
        return shortcutTile;
    }
}
