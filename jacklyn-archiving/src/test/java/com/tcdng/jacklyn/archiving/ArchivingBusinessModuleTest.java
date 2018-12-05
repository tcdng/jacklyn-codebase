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
package com.tcdng.jacklyn.archiving;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.tcdng.jacklyn.archiving.business.ArchivingModule;
import com.tcdng.jacklyn.archiving.constants.ArchivingModuleNameConstants;
import com.tcdng.jacklyn.archiving.constants.FileArchiveTaskConstants;
import com.tcdng.jacklyn.archiving.constants.FileArchiveTaskParamConstants;
import com.tcdng.jacklyn.archiving.entities.ArchivableDefinition;
import com.tcdng.jacklyn.archiving.entities.ArchivableDefinitionQuery;
import com.tcdng.jacklyn.archiving.entities.ArchivingField;
import com.tcdng.jacklyn.archiving.entities.ArchivingFieldQuery;
import com.tcdng.jacklyn.archiving.entities.FileArchive;
import com.tcdng.jacklyn.archiving.entities.FileArchiveConfig;
import com.tcdng.jacklyn.archiving.entities.FileArchiveConfigQuery;
import com.tcdng.jacklyn.archiving.entities.FileArchiveEntry;
import com.tcdng.jacklyn.common.AbstractJacklynTest;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.shared.archiving.ArchivingFieldType;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.file.TestFileSystemIO;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.task.TaskStatus;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Archiving business module tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ArchivingBusinessModuleTest extends AbstractJacklynTest {

	@Test
	public void testFindArchivables() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);
		ArchivableDefinitionQuery query = new ArchivableDefinitionQuery();
		query.ignoreEmptyCriteria(true);
		List<ArchivableDefinition> archivableList
				= archivingModule.findArchivableDefinitions(query);
		assertNotNull(archivableList);
		assertEquals(1, archivableList.size());

		ArchivableDefinition archiveDefinitionData = archivableList.get(0);
		assertEquals("TestChequeImage", archiveDefinitionData.getDescription());
		assertEquals(TestChequeImageData.class.getName(), archiveDefinitionData.getRecordType());
	}

	@Test
	public void testFindArchivableFields() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);
		ArchivingFieldQuery query = new ArchivingFieldQuery();
		query.recordName(TestChequeImageData.class.getName());
		query.orderByFieldType();
		List<ArchivingField> archivableFieldList = archivingModule.findArchivingFields(query);
		assertNotNull(archivableFieldList);
		assertEquals(4, archivableFieldList.size());

		ArchivingField archivableFieldData = archivableFieldList.get(0);
		assertEquals("image", archivableFieldData.getFieldName());
		assertEquals("Image", archivableFieldData.getDescription());
		assertEquals(ArchivingFieldType.BLOB, archivableFieldData.getFieldType());

		archivableFieldData = archivableFieldList.get(1);
		assertEquals("template", archivableFieldData.getFieldName());
		assertEquals("Template", archivableFieldData.getDescription());
		assertEquals(ArchivingFieldType.CLOB, archivableFieldData.getFieldType());

		archivableFieldData = archivableFieldList.get(2);
		assertEquals("createDt", archivableFieldData.getFieldName());
		assertEquals("Create Dt", archivableFieldData.getDescription());
		assertEquals(ArchivingFieldType.TIMESTAMP, archivableFieldData.getFieldType());

		archivableFieldData = archivableFieldList.get(3);
		assertEquals("updateDt", archivableFieldData.getFieldName());
		assertEquals("Update Dt", archivableFieldData.getDescription());
		assertEquals(ArchivingFieldType.TIMESTAMP, archivableFieldData.getFieldType());
	}

	@Test
	public void testCreateFileArchiveConfig() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		Long fileArchiveConfigId = archivingModule.createFileArchiveConfig(fileArchiveConfigData);
		assertNotNull(fileArchiveConfigId);
	}

	@Test
	public void testFindFileArchiveConfigById() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		Long fileArchiveConfigId = archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		FileArchiveConfig fetchedFileArchiveConfigData
				= archivingModule.findFileArchiveConfig(fileArchiveConfigId);
		assertNotNull(fetchedFileArchiveConfigData);
		assertEquals(fileArchiveConfigData, fetchedFileArchiveConfigData);
	}

	@Test
	public void testFindFileArchiveConfigs() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		List<FileArchiveConfig> fileArchiveConfigList = archivingModule
				.findFileArchiveConfigs(new FileArchiveConfigQuery().name("ChequeImageArchive"));
		FileArchiveConfig foundFileArchiveConfigData = fileArchiveConfigList.get(0);
		assertNotNull(fileArchiveConfigList);
		assertEquals(1, fileArchiveConfigList.size());
		assertEquals(fileArchiveConfigData.getName(), foundFileArchiveConfigData.getName());
		assertEquals(fileArchiveConfigData.getDescription(),
				foundFileArchiveConfigData.getDescription());
		assertEquals(fileArchiveConfigData.getLocalArchivePath(),
				foundFileArchiveConfigData.getLocalArchivePath());
		assertEquals(fileArchiveConfigData.getFilenameGenerator(),
				foundFileArchiveConfigData.getFilenameGenerator());
		assertEquals(fileArchiveConfigData.getMaxItemsPerFile(),
				foundFileArchiveConfigData.getMaxItemsPerFile());
		assertEquals(fileArchiveConfigData.getDeleteRowOnArchive(),
				foundFileArchiveConfigData.getDeleteRowOnArchive());
		assertEquals(fileArchiveConfigData.getStatus(), foundFileArchiveConfigData.getStatus());
	}

	@Test
	public void testUpdateFileArchiveConfig() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		Long fileArchiveConfigId = archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		FileArchiveConfig fetchedFileArchiveConfigData
				= archivingModule.findFileArchiveConfig(fileArchiveConfigId);
		fetchedFileArchiveConfigData.setDescription("Inward Cheque ImageImpl Archive");
		int count = archivingModule.updateFileArchiveConfig(fetchedFileArchiveConfigData);
		assertEquals(1, count);

		FileArchiveConfig updatedFileArchiveConfigData
				= archivingModule.findFileArchiveConfig(fileArchiveConfigId);
		assertEquals(fetchedFileArchiveConfigData, updatedFileArchiveConfigData);
		assertFalse(fileArchiveConfigData.equals(updatedFileArchiveConfigData));
	}

	@Test
	public void testDeleteFileArchiveConfig() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		Long fileArchiveConfigId = archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		int count = archivingModule.deleteFileArchiveConfig(fileArchiveConfigId);
		assertEquals(1, count);
	}

	@Test(timeout = 8000)
	public void testBuildLobFileArchiveTaskForBlobWithDeleteColumn() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		byte[] image = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
		TestChequeImageData testChequeImageData = new TestChequeImageData();
		testChequeImageData.setImage(image);
		Long testChequeImageId = (Long) createRecord(testChequeImageData);

		TestChequeImageData fetchedTestChequeImageData
				= findRecord(TestChequeImageData.class, testChequeImageId);
		assertNotNull(fetchedTestChequeImageData.getImage());
		Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImageData.getCreateDt());
		TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		fetchedTestChequeImageData = findRecord(TestChequeImageData.class, testChequeImageId);
		assertNotNull(fetchedTestChequeImageData);
		assertNull(fetchedTestChequeImageData.getImage());
	}

	@Test(timeout = 8000)
	public void testBuildLobFileArchiveTaskForBlobWithDeleteRow() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		fileArchiveConfigData.setDeleteRowOnArchive(Boolean.TRUE);
		archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		byte[] image = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
		TestChequeImageData testChequeImageData = new TestChequeImageData();
		testChequeImageData.setImage(image);
		Long testChequeImageId = (Long) createRecord(testChequeImageData);

		TestChequeImageData fetchedTestChequeImageData
				= findRecord(TestChequeImageData.class, testChequeImageId);
		assertNotNull(fetchedTestChequeImageData.getImage());
		Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImageData.getCreateDt());
		TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		boolean deleted = false;
		try {
			findRecord(TestChequeImageData.class, testChequeImageId);
		} catch (Exception e) {
			deleted = true;
		}
		assertTrue(deleted);
	}

	@Test(timeout = 8000)
	public void testBuildLobFileArchiveTaskForBlobWithDeleteColumnAndWorkingDateOnly()
			throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		byte[] image1 = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
		TestChequeImageData testChequeImageData = new TestChequeImageData();
		testChequeImageData.setImage(image1);
		Long testChequeImageId1 = (Long) createRecord(testChequeImageData);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 2);
		byte[] image2
				= new byte[] { (byte) 0x2B, (byte) 0xDD, (byte) 0x05, (byte) 0x72, (byte) 0x3E };
		testChequeImageData.setImage(image2);
		testChequeImageData.setCreateDt(cal.getTime());
		Long testChequeImageId2 = (Long) createRecord(testChequeImageData);

		TestChequeImageData fetchedTestChequeImageData
				= findRecord(TestChequeImageData.class, testChequeImageId1);
		assertNotNull(fetchedTestChequeImageData.getImage());
		Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImageData.getCreateDt());
		TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		fetchedTestChequeImageData = findRecord(TestChequeImageData.class, testChequeImageId1);
		assertNotNull(fetchedTestChequeImageData);
		assertNull(fetchedTestChequeImageData.getImage());

		fetchedTestChequeImageData = findRecord(TestChequeImageData.class, testChequeImageId2);
		assertNotNull(fetchedTestChequeImageData);
		assertNotNull(fetchedTestChequeImageData.getImage());
	}

	@Test(timeout = 8000)
	public void testBuildLobFileArchiveTaskForBlobWithDeleteRowAndWorkingDateOnly()
			throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		fileArchiveConfigData.setDeleteRowOnArchive(Boolean.TRUE);
		archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		byte[] image1 = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
		TestChequeImageData testChequeImageData = new TestChequeImageData();
		testChequeImageData.setImage(image1);
		Long testChequeImageId1 = (Long) createRecord(testChequeImageData);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 2);
		byte[] image2
				= new byte[] { (byte) 0x2B, (byte) 0xDD, (byte) 0x05, (byte) 0x72, (byte) 0x3E };
		testChequeImageData.setImage(image2);
		testChequeImageData.setCreateDt(cal.getTime());
		Long testChequeImageId2 = (Long) createRecord(testChequeImageData);

		TestChequeImageData fetchedTestChequeImageData
				= findRecord(TestChequeImageData.class, testChequeImageId1);
		assertNotNull(fetchedTestChequeImageData.getImage());
		Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImageData.getCreateDt());
		TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		boolean deleted = false;
		try {
			findRecord(TestChequeImageData.class, testChequeImageId1);
		} catch (Exception e) {
			deleted = true;
		}
		assertTrue(deleted);

		fetchedTestChequeImageData = findRecord(TestChequeImageData.class, testChequeImageId2);
		assertNotNull(fetchedTestChequeImageData);
		assertNotNull(fetchedTestChequeImageData.getImage());
	}

	@Test(timeout = 8000)
	public void testBuildLobFileArchiveTaskForClob() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(true);
		archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		TestChequeImageData testChequeImageData = new TestChequeImageData();
		testChequeImageData.setTemplate("Hello World!");
		Long testChequeImageId = (Long) createRecord(testChequeImageData);

		TestChequeImageData fetchedTestChequeImageData
				= findRecord(TestChequeImageData.class, testChequeImageId);
		Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImageData.getCreateDt());
		TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		fetchedTestChequeImageData = findRecord(TestChequeImageData.class, testChequeImageId);
		assertNotNull(fetchedTestChequeImageData);
		assertNull(fetchedTestChequeImageData.getTemplate());
	}

	@Test(timeout = 8000)
	public void testRetriveArchivedBlob() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		byte[] image = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
		TestChequeImageData testChequeImageData = new TestChequeImageData();
		testChequeImageData.setImage(image);
		Long testChequeImageId = (Long) createRecord(testChequeImageData);

		TestChequeImageData fetchedTestChequeImageData
				= findRecord(TestChequeImageData.class, testChequeImageId);
		assertNotNull(fetchedTestChequeImageData.getImage());
		Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImageData.getCreateDt());
		TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		byte[] archivedImage = archivingModule.retriveArchivedBlob(
				TestChequeImageData.class.getName(), "image", testChequeImageId);
		assertNotNull(archivedImage);
		assertTrue(Arrays.equals(image, archivedImage));
	}

	@Test(timeout = 8000)
	public void testRetriveArchivedBlobMultipleItems() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		fileArchiveConfigData.setMaxItemsPerFile(Integer.valueOf(3));
		archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		TestChequeImageData testChequeImageData = new TestChequeImageData();
		Date workingDt = CalendarUtils.getMidnightDate(new Date());
		testChequeImageData.setCreateDt(workingDt);
		byte[] image1 = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
		testChequeImageData.setImage(image1);
		Long testChequeImageId1 = (Long) createRecord(testChequeImageData);

		byte[] image2
				= new byte[] { (byte) 0xC4, (byte) 0x80, (byte) 0x2F, (byte) 0xD4, (byte) 0xBB };
		testChequeImageData.setImage(image2);
		Long testChequeImageId2 = (Long) createRecord(testChequeImageData);

		byte[] image3 = new byte[] { (byte) 0x01, (byte) 0x36, (byte) 0x44 };
		testChequeImageData.setImage(image3);
		Long testChequeImageId3 = (Long) createRecord(testChequeImageData);

		TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		byte[] archivedImage = archivingModule.retriveArchivedBlob(
				TestChequeImageData.class.getName(), "image", testChequeImageId1);
		assertNotNull(archivedImage);
		assertTrue(Arrays.equals(image1, archivedImage));

		archivedImage = archivingModule.retriveArchivedBlob(TestChequeImageData.class.getName(),
				"image", testChequeImageId2);
		assertNotNull(archivedImage);
		assertTrue(Arrays.equals(image2, archivedImage));

		archivedImage = archivingModule.retriveArchivedBlob(TestChequeImageData.class.getName(),
				"image", testChequeImageId3);
		assertNotNull(archivedImage);
		assertTrue(Arrays.equals(image3, archivedImage));
	}

	@Test(timeout = 8000)
	public void testRetriveArchivedBlobMultipleItemsAndSingleRun() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		fileArchiveConfigData.setMaxItemsPerFile(Integer.valueOf(2));
		archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		TestChequeImageData testChequeImageData = new TestChequeImageData();
		Date workingDt = CalendarUtils.getMidnightDate(new Date());
		testChequeImageData.setCreateDt(workingDt);
		byte[] image1 = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
		testChequeImageData.setImage(image1);
		Long testChequeImageId1 = (Long) createRecord(testChequeImageData);

		byte[] image2
				= new byte[] { (byte) 0xC4, (byte) 0x80, (byte) 0x2F, (byte) 0xD4, (byte) 0xBB };
		testChequeImageData.setImage(image2);
		Long testChequeImageId2 = (Long) createRecord(testChequeImageData);

		byte[] image3 = new byte[] { (byte) 0x01, (byte) 0x36, (byte) 0x44 };
		testChequeImageData.setImage(image3);
		Long testChequeImageId3 = (Long) createRecord(testChequeImageData);

		// Single pass
		TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		byte[] archivedImage = archivingModule.retriveArchivedBlob(
				TestChequeImageData.class.getName(), "image", testChequeImageId1);
		assertNotNull(archivedImage);
		assertTrue(Arrays.equals(image1, archivedImage));

		archivedImage = archivingModule.retriveArchivedBlob(TestChequeImageData.class.getName(),
				"image", testChequeImageId2);
		assertNotNull(archivedImage);
		assertTrue(Arrays.equals(image2, archivedImage));

		// This item has not been archived because of batch limit of 2 and a
		// single pass
		archivedImage = archivingModule.retriveArchivedBlob(TestChequeImageData.class.getName(),
				"image", testChequeImageId3);
		assertNull(archivedImage);
	}

	@Test(timeout = 8000)
	public void testRetriveArchivedBlobMultipleItemsAndMultipleRun() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(false);
		fileArchiveConfigData.setMaxItemsPerFile(Integer.valueOf(2));
		archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		TestChequeImageData testChequeImageData = new TestChequeImageData();
		Date workingDt = CalendarUtils.getMidnightDate(new Date());
		testChequeImageData.setCreateDt(workingDt);
		byte[] image1 = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
		testChequeImageData.setImage(image1);
		Long testChequeImageId1 = (Long) createRecord(testChequeImageData);

		byte[] image2
				= new byte[] { (byte) 0xC4, (byte) 0x80, (byte) 0x2F, (byte) 0xD4, (byte) 0xBB };
		testChequeImageData.setImage(image2);
		Long testChequeImageId2 = (Long) createRecord(testChequeImageData);

		byte[] image3 = new byte[] { (byte) 0x01, (byte) 0x36, (byte) 0x44 };
		testChequeImageData.setImage(image3);
		Long testChequeImageId3 = (Long) createRecord(testChequeImageData);

		// Mutiple pass
		TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		byte[] archivedImage = archivingModule.retriveArchivedBlob(
				TestChequeImageData.class.getName(), "image", testChequeImageId1);
		assertNotNull(archivedImage);
		assertTrue(Arrays.equals(image1, archivedImage));

		archivedImage = archivingModule.retriveArchivedBlob(TestChequeImageData.class.getName(),
				"image", testChequeImageId2);
		assertNotNull(archivedImage);
		assertTrue(Arrays.equals(image2, archivedImage));

		archivedImage = archivingModule.retriveArchivedBlob(TestChequeImageData.class.getName(),
				"image", testChequeImageId3);
		assertNotNull(archivedImage);
		assertTrue(Arrays.equals(image3, archivedImage));
	}

	@Test(timeout = 8000)
	public void testRetriveArchivedClob() throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);

		FileArchiveConfig fileArchiveConfigData = getFileArchiveConfigData(true);
		archivingModule.createFileArchiveConfig(fileArchiveConfigData);

		TestChequeImageData testChequeImageData = new TestChequeImageData();
		testChequeImageData.setTemplate("Hello World!");
		Long testChequeImageId = (Long) createRecord(testChequeImageData);

		TestChequeImageData fetchedTestChequeImageData
				= findRecord(TestChequeImageData.class, testChequeImageId);
		Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImageData.getCreateDt());
		TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		String archivedTemplate = archivingModule.retriveArchivedClob(
				TestChequeImageData.class.getName(), "template", testChequeImageId);
		assertEquals("Hello World!", archivedTemplate);
	}

	@Override
	protected void doAddSettingsAndDependencies() throws Exception {
		super.doAddSettingsAndDependencies();
		addDependency("filesystemio", TestFileSystemIO.class, true, true);
	}

	@Override
	protected void onSetup() throws Exception {

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onTearDown() throws Exception {
		deleteAll(TestChequeImageData.class, FileArchiveEntry.class, FileArchive.class,
				FileArchiveConfig.class);
	}

	private FileArchiveConfig getFileArchiveConfigData(boolean clob) throws Exception {
		ArchivingModule archivingModule = (ArchivingModule) getComponent(
				ArchivingModuleNameConstants.ARCHIVINGBUSINESSMODULE);
		ArchivingFieldQuery query = new ArchivingFieldQuery();
		query.recordName(TestChequeImageData.class.getName());
		query.orderByFieldType();
		List<ArchivingField> archivableFieldList = archivingModule.findArchivingFields(query);

		FileArchiveConfig fileArchiveConfigData = new FileArchiveConfig();
		ArchivingField afd = null;
		if (clob) {
			afd = archivableFieldList.get(1);
		} else {
			afd = archivableFieldList.get(0);
		}
		fileArchiveConfigData.setArchivableDefId(afd.getArchivableDefId());
		fileArchiveConfigData.setArchivableFieldId(afd.getId());
		fileArchiveConfigData.setArchivableDateFieldId(archivableFieldList.get(2).getId());
		fileArchiveConfigData.setName("ChequeImageArchive");
		fileArchiveConfigData.setDescription("Cheque ImageImpl Archive");
		fileArchiveConfigData.setLocalArchivePath("c:\\archive\\images");
		fileArchiveConfigData.setFilenameGenerator("default-filearchivenamegenerator");
		fileArchiveConfigData.setMaxItemsPerFile(2);
		fileArchiveConfigData.setDeleteRowOnArchive(Boolean.FALSE);
		fileArchiveConfigData.setStatus(RecordStatus.ACTIVE);
		return fileArchiveConfigData;
	}

	private TaskMonitor launchBlobFileArchiveTask(String configName, Date workingDt)
			throws Exception {
		return ((TaskLauncher) getComponent(ApplicationComponents.APPLICATION_TASKLAUNCHER))
				.launchTask(TaskSetup.newBuilder()
						.addTask(FileArchiveTaskConstants.BUILDLOBFILEARCHIVETASK)
						.setParam(FileArchiveTaskParamConstants.FILEARCHIVECONFIGNAME, configName)
						.setParam(FileArchiveTaskParamConstants.WORKINGDT, workingDt).build());
	}
}
