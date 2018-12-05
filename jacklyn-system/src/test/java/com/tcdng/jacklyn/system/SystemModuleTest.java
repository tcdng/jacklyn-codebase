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
import com.tcdng.jacklyn.system.business.SystemModule;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.jacklyn.system.data.SystemControlState;
import com.tcdng.jacklyn.system.entities.ApplicationMenu;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItem;
import com.tcdng.jacklyn.system.entities.ApplicationMenuItemQuery;
import com.tcdng.jacklyn.system.entities.ApplicationMenuQuery;
import com.tcdng.jacklyn.system.entities.DashboardTile;
import com.tcdng.jacklyn.system.entities.DashboardTileQuery;
import com.tcdng.jacklyn.system.entities.Module;
import com.tcdng.jacklyn.system.entities.ModuleQuery;
import com.tcdng.jacklyn.system.entities.ScheduledTask;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHist;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHistQuery;
import com.tcdng.jacklyn.system.entities.ScheduledTaskLargeData;
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
 * System business module test.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class SystemModuleTest extends AbstractJacklynTest {

	@Test
	public void testFindModuleByName() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Module moduleData = systemModule.findModule(SystemModuleNameConstants.SYSTEM_MODULE);
		assertNotNull(moduleData);
		assertEquals(SystemModuleNameConstants.SYSTEM_MODULE, moduleData.getName());
		assertFalse("$m{system.module}".equals(moduleData.getDescription()));
	}

	@Test
	public void testGetModuleId() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Module moduleData = systemModule.findModule(SystemModuleNameConstants.SYSTEM_MODULE);
		Long moduleId = systemModule.getModuleId(SystemModuleNameConstants.SYSTEM_MODULE);
		assertNotNull(moduleData);
		assertEquals(moduleData.getId(), moduleId);
	}

	@Test
	public void testFindModuleById() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Module moduleData = systemModule.findModule(SystemModuleNameConstants.SYSTEM_MODULE);
		assertNotNull(moduleData);
		Module fetchedModuleData = systemModule.findModule(moduleData.getId());
		assertNotNull(fetchedModuleData);
		assertEquals(moduleData, fetchedModuleData);
	}

	@Test
	public void testFindModules() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Module moduleData = systemModule.findModule(SystemModuleNameConstants.SYSTEM_MODULE);

		List<Module> moduleList = systemModule
				.findModules(new ModuleQuery().name(SystemModuleNameConstants.SYSTEM_MODULE));
		assertNotNull(moduleList);
		assertEquals(1, moduleList.size());
		assertEquals(moduleData, moduleList.get(0));
	}

	@Test
	public void testFindModulesWithMappedResult() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Module moduleData = systemModule.findModule(SystemModuleNameConstants.SYSTEM_MODULE);

		Map<Long, Module> moduleMap = systemModule.findModules(Long.class, "id",
				(ModuleQuery) new ModuleQuery().name(SystemModuleNameConstants.SYSTEM_MODULE));
		assertNotNull(moduleMap);
		assertEquals(1, moduleMap.size());

		Module fetchedModuleData = moduleMap.get(moduleData.getId());
		assertEquals(moduleData, fetchedModuleData);
	}

	@Test
	public void testDeactivateDeactivatableModule() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Module moduleData = systemModule.findModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
		assertEquals(RecordStatus.ACTIVE, moduleData.getStatus());

		systemModule.deactivateModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
		moduleData = systemModule.findModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
		assertEquals(RecordStatus.INACTIVE, moduleData.getStatus());
	}

	@Test(expected = UnifyException.class)
	public void testDeactivateNonDeactivatableModule() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Module moduleData = systemModule.findModule(SystemModuleNameConstants.SYSTEM_MODULE);
		assertEquals(RecordStatus.ACTIVE, moduleData.getStatus());

		systemModule.deactivateModule(SystemModuleNameConstants.SYSTEM_MODULE);
	}

	@Test
	public void testActivateModule() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		systemModule.deactivateModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
		Module moduleData = systemModule.findModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
		assertEquals(RecordStatus.INACTIVE, moduleData.getStatus());

		systemModule.activateModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);

		moduleData = systemModule.findModule(TestCustomerModuleNameConstants.CUSTOMER_MODULE);
		assertEquals(RecordStatus.ACTIVE, moduleData.getStatus());
	}

	@Test
	public void testFindMenus() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ApplicationMenuQuery query = new ApplicationMenuQuery();
		query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
		List<ApplicationMenu> menuList = systemModule.findMenus(query);
		assertNotNull(menuList);
		assertEquals(1, menuList.size());
	}

	@Test
	public void testFindMenuById() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ApplicationMenuQuery query = new ApplicationMenuQuery();
		query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
		List<ApplicationMenu> menuList = systemModule.findMenus(query);

		ApplicationMenu menuData = systemModule.findMenu(menuList.get(0).getId());
		assertEquals(menuList.get(0), menuData);
	}

	@Test
	public void testFindMenuItems() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ApplicationMenuItemQuery query = new ApplicationMenuItemQuery();
		query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
		List<ApplicationMenuItem> menuItemList = systemModule.findMenuItems(query);
		assertNotNull(menuItemList);
		assertFalse(menuItemList.isEmpty());
	}

	@Test
	public void testFindMenuItemById() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ApplicationMenuItemQuery query = new ApplicationMenuItemQuery();
		query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
		List<ApplicationMenuItem> menuItemList = systemModule.findMenuItems(query);
		assertFalse(menuItemList.isEmpty());

		ApplicationMenuItem menuItemData = systemModule.findMenuItem(menuItemList.get(0).getId());
		assertEquals(menuItemList.get(0), menuItemData);
	}

	@Test
	public void testSaveMenuOrder() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ApplicationMenuQuery query = new ApplicationMenuQuery();
		query.orderById().ignoreEmptyCriteria(true);
		List<ApplicationMenu> menuList = systemModule.findMenus(query);
		assertEquals(0, menuList.get(0).getDisplayOrder());
		assertEquals(0, menuList.get(1).getDisplayOrder());

		systemModule.saveMenuOrder(menuList);
		menuList = systemModule.findMenus(query);
		assertEquals(0, menuList.get(0).getDisplayOrder());
		assertEquals(1, menuList.get(1).getDisplayOrder());
	}

	@Test
	public void testSaveMenuItemOrder() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ApplicationMenuItemQuery query = new ApplicationMenuItemQuery();
		query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
		query.orderById();
		List<ApplicationMenuItem> menuItemList = systemModule.findMenuItems(query);
		assertEquals(0, menuItemList.get(0).getDisplayOrder());
		assertEquals(0, menuItemList.get(1).getDisplayOrder());
		assertEquals(0, menuItemList.get(2).getDisplayOrder());
		assertEquals(0, menuItemList.get(3).getDisplayOrder());

		systemModule.saveMenuItemOrder(menuItemList);
		menuItemList = systemModule.findMenuItems(query);
		assertEquals(0, menuItemList.get(0).getDisplayOrder());
		assertEquals(1, menuItemList.get(1).getDisplayOrder());
		assertEquals(2, menuItemList.get(2).getDisplayOrder());
		assertEquals(3, menuItemList.get(3).getDisplayOrder());
	}

	@Test
	public void testFindSysParameterByName() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		SystemParameter sysParameterData = systemModule
				.findSysParameter(SystemModuleSysParamConstants.SYSPARAM_APPLICATION_VERSION);
		assertNotNull(sysParameterData);
		assertEquals(SystemModuleSysParamConstants.SYSPARAM_APPLICATION_VERSION,
				sysParameterData.getName());
		assertFalse(
				"$m{system.sysparam.applicationversion}".equals(sysParameterData.getDescription()));
		assertEquals("!ui-text size:24", sysParameterData.getEditor());
		assertEquals("1.0.0", sysParameterData.getValue());
		assertEquals(Boolean.FALSE, sysParameterData.getControl());
		assertEquals(Boolean.FALSE, sysParameterData.getEditable());
	}

	@Test
	public void testFindSysParameterById() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		SystemParameter sysParameterData = systemModule
				.findSysParameter(SystemModuleSysParamConstants.SYSPARAM_APPLICATION_VERSION);
		SystemParameter fetchedSysParameterData
				= systemModule.findSysParameter(sysParameterData.getId());
		assertEquals(sysParameterData, fetchedSysParameterData);
	}

	@Test
	public void testFindSysParameters() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		SystemParameterQuery query = new SystemParameterQuery();
		query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
		query.order("name");
		List<SystemParameter> sysParameterList = systemModule.findSysParameters(query);
		assertNotNull(sysParameterList);
		assertEquals(SystemModuleSysParamConstants.SYSPARAM_APPLICATION_VERSION,
				sysParameterList.get(0).getName());
		assertEquals(SystemModuleSysParamConstants.SYSPARAM_CLIENT_TITLE,
				sysParameterList.get(1).getName());
	}

	@Test
	public void testFindSystemControlStates() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		SystemParameterQuery query = new SystemParameterQuery();
		query.moduleName(SystemModuleNameConstants.SYSTEM_MODULE);
		List<SystemControlState> systemControlList = systemModule.findSystemControlStates(query);
		assertNotNull(systemControlList);
		assertEquals(1, systemControlList.size());

		SystemControlState systemControlState = systemControlList.get(0);
		assertEquals(SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_ENABLED,
				systemControlState.getName());
		assertFalse("$m{system.sysparam.enablesystemscheduler}"
				.equals(systemControlState.getDescription()));
		assertEquals(Boolean.FALSE, systemControlState.isEnabled());
	}

	@Test
	public void testGetSysParameterValue() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		int maxTrigger = systemModule.getSysParameterValue(int.class,
				SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_MAX_TRIGGER);
		assertTrue(maxTrigger > 0);
	}

	@Test
	public void testSetSysParameterValue() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		int count = systemModule.setSysParameterValue(
				SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_MAX_TRIGGER, "20");
		assertEquals(1, count);
		int maxTrigger = systemModule.getSysParameterValue(int.class,
				SystemModuleSysParamConstants.SYSPARAM_SYSTEM_SCHEDULER_MAX_TRIGGER);
		assertEquals(20, maxTrigger);
	}

	@Test
	public void testFindNormalizedScheduledTaskParameters() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		Inputs paramValues = document.getScheduledTaskParams();
		assertNotNull(paramValues);
		assertEquals(1, paramValues.size());
		Input paramValue = paramValues.getInput("batchSize");
		assertEquals("batchSize", paramValue.getName());
		assertEquals("Batch Size", paramValue.getDescription());
		assertEquals("!ui-integer", paramValue.getEditor());
		assertEquals(Integer.class, paramValue.getType());
	}

	@Test
	public void testCreateScheduledTask() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		Inputs paramValues = document.getScheduledTaskParams();
		paramValues.setInputValue("batchSize", "400");
		Long scheduledTaskId = systemModule.createScheduledTask(document);
		assertNotNull(scheduledTaskId);
	}

	@Test(expected = UnifyException.class)
	public void testCreateScheduledTaskWithMandatoryParamNotSet() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		systemModule.createScheduledTask(document);
	}

	@Test
	public void testFindScheduledTasks() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		Inputs paramValues = document.getScheduledTaskParams();
		paramValues.setInputValue("batchSize", "400");
		systemModule.createScheduledTask(document);
		List<ScheduledTask> scheduledTaskList = systemModule
				.findScheduledTasks(new ScheduledTaskQuery().taskName("testschedulabletask"));
		assertNotNull(scheduledTaskList);
		assertEquals(1, scheduledTaskList.size());
		assertEquals(scheduledTaskData.getDescription(), scheduledTaskList.get(0).getDescription());
		assertEquals(scheduledTaskData.getExpires(), scheduledTaskList.get(0).getExpires());
		assertEquals(scheduledTaskData.getStartTime(), scheduledTaskList.get(0).getStartTime());
		assertEquals(scheduledTaskData.getTaskName(), scheduledTaskList.get(0).getTaskName());
	}

	@Test
	public void testFindScheduledTaskById() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		Inputs paramValues = document.getScheduledTaskParams();
		paramValues.setInputValue("batchSize", "400");
		Long scheduledTaskId = systemModule.createScheduledTask(document);

		ScheduledTask fetchedScheduledTaskData = systemModule.findScheduledTask(scheduledTaskId);
		assertNotNull(fetchedScheduledTaskData);
		assertEquals(scheduledTaskData, fetchedScheduledTaskData);
	}

	@Test
	public void testUpdateScheduledTask() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		Inputs paramValues = document.getScheduledTaskParams();
		paramValues.setInputValue("batchSize", "400");
		Long scheduledTaskId = systemModule.createScheduledTask(document);

		ScheduledTaskLargeData fetchDocument
				= systemModule.findScheduledTaskDocument(scheduledTaskId);
		ScheduledTask fetchedScheduledTaskData = fetchDocument.getData();
		fetchedScheduledTaskData.setFrequency(Integer.valueOf(2));
		fetchedScheduledTaskData.setFrequencyUnit(FrequencyUnit.HOUR);
		fetchedScheduledTaskData.setStartTime(new Date());
		fetchDocument.getScheduledTaskParams().setInputValue("batchSize", "1000");

		systemModule.updateScheduledTask(fetchDocument);

		ScheduledTaskLargeData updateDocument
				= systemModule.findScheduledTaskDocument(scheduledTaskId);
		ScheduledTask updatedScheduledTaskData = updateDocument.getData();
		assertFalse(scheduledTaskData.equals(updatedScheduledTaskData));
		assertEquals(fetchedScheduledTaskData, updatedScheduledTaskData);
		assertEquals("1000", updateDocument.getScheduledTaskParams().getInputValue("batchSize"));
	}

	@Test
	public void testDeleteScheduledTask() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		document.getScheduledTaskParams().setInputValue("batchSize", "400");
		Long scheduledTaskId = systemModule.createScheduledTask(document);

		int count = systemModule.deleteScheduledTask(scheduledTaskId);
		assertEquals(1, count);
	}

	@Test
	public void testDeactivateScheduledTask() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		document.getScheduledTaskParams().setInputValue("batchSize", "400");
		Long scheduledTaskId = systemModule.createScheduledTask(document);

		ScheduledTask fetchedScheduledTaskData = systemModule.findScheduledTask(scheduledTaskId);
		assertEquals(RecordStatus.ACTIVE, fetchedScheduledTaskData.getStatus());

		systemModule.deactivateScheduledTask(scheduledTaskId);

		fetchedScheduledTaskData = systemModule.findScheduledTask(scheduledTaskId);
		assertEquals(RecordStatus.INACTIVE, fetchedScheduledTaskData.getStatus());
	}

	@Test
	public void testActivateScheduledTask() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		document.getScheduledTaskParams().setInputValue("batchSize", "400");
		Long scheduledTaskId = systemModule.createScheduledTask(document);

		ScheduledTask fetchedScheduledTaskData = systemModule.findScheduledTask(scheduledTaskId);
		assertEquals(RecordStatus.ACTIVE, fetchedScheduledTaskData.getStatus());

		systemModule.deactivateScheduledTask(scheduledTaskId);
		systemModule.activateScheduledTask(scheduledTaskId);

		fetchedScheduledTaskData = systemModule.findScheduledTask(scheduledTaskId);
		assertEquals(RecordStatus.ACTIVE, fetchedScheduledTaskData.getStatus());
	}

	@Test
	public void testCreateScheduledTaskHistory() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		document.getScheduledTaskParams().setInputValue("batchSize", "400");
		Long scheduledTaskId = systemModule.createScheduledTask(document);

		Long scheduledTaskHistId = systemModule.createScheduledTaskHistory(scheduledTaskId,
				TaskStatus.INITIALISED, null);
		assertNotNull(scheduledTaskHistId);
	}

	@Test
	public void testFindScheduledTaskHistory() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		document.getScheduledTaskParams().setInputValue("batchSize", "400");
		Long scheduledTaskId = systemModule.createScheduledTask(document);

		systemModule.createScheduledTaskHistory(scheduledTaskId, TaskStatus.INITIALISED, null);
		systemModule.createScheduledTaskHistory(scheduledTaskId, TaskStatus.CANCELED,
				"Some message!");

		List<ScheduledTaskHist> scheduledTaskHistList = systemModule
				.findScheduledTaskHistory((ScheduledTaskHistQuery) new ScheduledTaskHistQuery()
						.scheduledTaskId(scheduledTaskId).orderById());
		assertNotNull(scheduledTaskHistList);
		assertEquals(2, scheduledTaskHistList.size());

		ScheduledTaskHist scheduledTaskHistData = scheduledTaskHistList.get(0);
		assertEquals(scheduledTaskId, scheduledTaskHistData.getScheduledTaskId());
		assertEquals("testschedulabletask", scheduledTaskHistData.getTaskName());
		assertNull(scheduledTaskHistData.getErrorMsg());
		assertEquals(TaskStatus.INITIALISED, scheduledTaskHistData.getTaskStatus());

		scheduledTaskHistData = scheduledTaskHistList.get(1);
		assertEquals(scheduledTaskId, scheduledTaskHistData.getScheduledTaskId());
		assertEquals("testschedulabletask", scheduledTaskHistData.getTaskName());
		assertEquals("Some message!", scheduledTaskHistData.getErrorMsg());
		assertEquals(TaskStatus.CANCELED, scheduledTaskHistData.getTaskStatus());
	}

	@Test
	public void testFindScheduledTaskHistoryById() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		ScheduledTask scheduledTaskData = this.getScheduledTaskData();
		ScheduledTaskLargeData document = systemModule
				.loadScheduledTaskDocumentValues(new ScheduledTaskLargeData(scheduledTaskData));
		document.getScheduledTaskParams().setInputValue("batchSize", "400");
		Long scheduledTaskId = systemModule.createScheduledTask(document);

		Long scheduledTaskHistId = systemModule.createScheduledTaskHistory(scheduledTaskId,
				TaskStatus.INITIALISED, "Started!");
		ScheduledTaskHist scheduledTaskHistData
				= systemModule.findScheduledTaskHist(scheduledTaskHistId);
		assertEquals(scheduledTaskId, scheduledTaskHistData.getScheduledTaskId());
		assertEquals("testschedulabletask", scheduledTaskHistData.getTaskName());
		assertEquals("Started!", scheduledTaskHistData.getErrorMsg());
		assertEquals(TaskStatus.INITIALISED, scheduledTaskHistData.getTaskStatus());
	}

	@Test
	public void testCreateTheme() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Theme themeData = this.getThemeData();
		Long themeId = systemModule.createTheme(themeData);
		assertNotNull(themeId);
	}

	@Test
	public void testFindThemeById() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Theme themeData = this.getThemeData();
		Long themeId = systemModule.createTheme(themeData);

		Theme fetchedThemeData = systemModule.findTheme(themeId);
		assertNotNull(fetchedThemeData);
		assertEquals(themeData, fetchedThemeData);
	}

	@Test
	public void testFindThemes() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Theme themeData = this.getThemeData();
		systemModule.createTheme(themeData);

		List<Theme> themeList = systemModule.findThemes(new ThemeQuery().name("thm001"));
		assertNotNull(themeList);
		assertEquals(1, themeList.size());
		assertEquals(themeData.getName(), themeList.get(0).getName());
		assertEquals(themeData.getDescription(), themeList.get(0).getDescription());
	}

	@Test
	public void testUpdateTheme() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Theme themeData = this.getThemeData();
		Long themeId = systemModule.createTheme(themeData);

		Theme fetchedThemeData = systemModule.findTheme(themeId);
		fetchedThemeData.setDescription("Red Boat");
		fetchedThemeData.setResourcePath("/redboat");
		int count = systemModule.updateTheme(fetchedThemeData);
		assertEquals(1, count);

		Theme updatedThemeData = systemModule.findTheme(themeId);
		assertEquals(fetchedThemeData, updatedThemeData);
		assertFalse(themeData.equals(updatedThemeData));
	}

	@Test
	public void testDeleteTheme() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		Theme themeData = this.getThemeData();
		Long themeId = systemModule.createTheme(themeData);

		int count = systemModule.deleteTheme(themeId);
		assertEquals(1, count);
	}

	@Test
	public void testCreateDashboardTile() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		DashboardTile dashboardTileData = this.getDashboardTileData();
		Long dashboardTileId = systemModule.createDashboardTile(dashboardTileData);
		assertNotNull(dashboardTileId);
	}

	@Test
	public void testFindDashboardTileById() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		DashboardTile dashboardTileData = this.getDashboardTileData();
		Long dashboardTileId = systemModule.createDashboardTile(dashboardTileData);

		DashboardTile fetchedDashboardTileData = systemModule.findDashboardTile(dashboardTileId);
		assertNotNull(fetchedDashboardTileData);
		assertEquals(dashboardTileData.getName(), fetchedDashboardTileData.getName());
		assertEquals(dashboardTileData.getCaption(), fetchedDashboardTileData.getCaption());
		assertEquals(dashboardTileData.getDescription(), fetchedDashboardTileData.getDescription());
		assertEquals(dashboardTileData.getGenerator(), fetchedDashboardTileData.getGenerator());
		assertEquals(dashboardTileData.getImageSrc(), fetchedDashboardTileData.getImageSrc());
		assertEquals(dashboardTileData.getStatus(), fetchedDashboardTileData.getStatus());
	}

	@Test
	public void testFindDashboardTiles() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		DashboardTile dashboardTileData = this.getDashboardTileData();
		systemModule.createDashboardTile(dashboardTileData);

		List<DashboardTile> dashboardTileList
				= systemModule.findDashboardTiles(new DashboardTileQuery().name("tile001"));
		assertNotNull(dashboardTileList);
		assertEquals(1, dashboardTileList.size());
		DashboardTile fetchedDashboardTileData = dashboardTileList.get(0);
		assertEquals(dashboardTileData.getName(), fetchedDashboardTileData.getName());
		assertEquals(dashboardTileData.getCaption(), fetchedDashboardTileData.getCaption());
		assertEquals(dashboardTileData.getDescription(), fetchedDashboardTileData.getDescription());
		assertEquals(dashboardTileData.getGenerator(), fetchedDashboardTileData.getGenerator());
		assertEquals(dashboardTileData.getImageSrc(), fetchedDashboardTileData.getImageSrc());
		assertEquals(dashboardTileData.getStatus(), fetchedDashboardTileData.getStatus());
	}

	@Test
	public void testUpdateDashboardTile() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		DashboardTile dashboardTileData = this.getDashboardTileData();
		Long dashboardTileId = systemModule.createDashboardTile(dashboardTileData);

		DashboardTile fetchedDashboardTileData = systemModule.findDashboardTile(dashboardTileId);
		fetchedDashboardTileData.setDescription("New Currencies");
		int count = systemModule.updateDashboardTile(fetchedDashboardTileData);
		assertEquals(1, count);

		DashboardTile updatedDashboardTileData = systemModule.findDashboardTile(dashboardTileId);
		assertEquals(fetchedDashboardTileData, updatedDashboardTileData);
		assertFalse(dashboardTileData.equals(updatedDashboardTileData));
	}

	@Test
	public void testDeleteDashboardTile() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		DashboardTile dashboardTileData = this.getDashboardTileData();
		Long dashboardTileId = systemModule.createDashboardTile(dashboardTileData);

		int count = systemModule.deleteDashboardTile(dashboardTileId);
		assertEquals(1, count);
	}

	@Test
	public void testSaveDashboardTileOrder() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		DashboardTile dashboardTileData = this.getDashboardTileData();
		systemModule.createDashboardTile(dashboardTileData);

		List<DashboardTile> dashboardTileList
				= systemModule.findDashboardTiles(new DashboardTileQuery().name("tile001"));
		assertEquals(0, dashboardTileList.get(0).getDisplayOrder());

		systemModule.saveDashboardTileOrder(dashboardTileList);
		dashboardTileList
				= systemModule.findDashboardTiles(new DashboardTileQuery().name("tile001"));
		assertEquals(0, dashboardTileList.get(0).getDisplayOrder());
	}

	@Test
	public void testGenerateTiles() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);

		DashboardTile dashboardTileData = this.getDashboardTileData();
		systemModule.createDashboardTile(dashboardTileData);

		List<DashboardTile> dashboardTileList
				= systemModule.findDashboardTiles(new DashboardTileQuery().name("tile001"));

		List<Tile> tileList = systemModule.generateTiles(dashboardTileList);
		assertNotNull(tileList);
		assertEquals(1, tileList.size());

		Tile tile = tileList.get(0);
		assertEquals(dashboardTileData.getCaption(), tile.getCaption());
		assertEquals(dashboardTileData.getPath(), tile.getActionPath());
		assertEquals(dashboardTileData.getImageSrc(), tile.getImageSrc());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onSetup() throws Exception {
		this.deleteAll(DashboardTile.class, Theme.class, ScheduledTaskHist.class,
				ParameterValues.class, ScheduledTask.class);
	}

	@Override
	protected void onTearDown() throws Exception {

	}

	private ScheduledTask getScheduledTaskData() {
		ScheduledTask scheduledTaskData = new ScheduledTask();
		scheduledTaskData.setDescription("Test Scheduled Taskable");
		scheduledTaskData.setExpires(Boolean.FALSE);
		scheduledTaskData.setStartTime(new Date());
		scheduledTaskData.setTaskName("testschedulabletask");
		return scheduledTaskData;
	}

	private Theme getThemeData() {
		Theme themeData = new Theme();
		themeData.setName("thm001");
		themeData.setDescription("Blue Boat");
		themeData.setResourcePath("/blueboat");
		themeData.setStatus(RecordStatus.ACTIVE);
		return themeData;
	}

	private DashboardTile getDashboardTileData() throws Exception {
		SystemModule systemModule = (SystemModule) this
				.getComponent(SystemModuleNameConstants.SYSTEMBUSINESSMODULE);
		Long moduleId = systemModule.getModuleId(SystemModuleNameConstants.SYSTEM_MODULE);
		DashboardTile dashboardTileData = new DashboardTile();
		dashboardTileData.setModuleId(moduleId);
		dashboardTileData.setName("tile001");
		dashboardTileData.setDescription("Currencies");
		dashboardTileData.setImageSrc("/currencies.png");
		dashboardTileData.setCaption("Manage Currencies");
		dashboardTileData.setPath("/accounts/currencies");
		dashboardTileData.setStatus(RecordStatus.ACTIVE);
		return dashboardTileData;
	}
}
