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

import com.tcdng.jacklyn.archiving.business.ArchivingService;
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
 * Archiving business service tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ArchivingServiceTest extends AbstractJacklynTest {

    @Test
    public void testFindArchivables() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);
        ArchivableDefinitionQuery query = new ArchivableDefinitionQuery();
        query.ignoreEmptyCriteria(true);
        List<ArchivableDefinition> archivableList = archivingService.findArchivableDefinitions(query);
        assertNotNull(archivableList);
        assertEquals(1, archivableList.size());

        ArchivableDefinition archiveDefinition = archivableList.get(0);
        assertEquals("TestChequeImage", archiveDefinition.getDescription());
        assertEquals(TestChequeImage.class.getName(), archiveDefinition.getRecordType());
    }

    @Test
    public void testFindArchivableFields() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);
        ArchivingFieldQuery query = new ArchivingFieldQuery();
        query.recordName(TestChequeImage.class.getName());
        query.orderByFieldType();
        List<ArchivingField> archivableFieldList = archivingService.findArchivingFields(query);
        assertNotNull(archivableFieldList);
        assertEquals(4, archivableFieldList.size());

        ArchivingField archivableField = archivableFieldList.get(0);
        assertEquals("image", archivableField.getFieldName());
        assertEquals("Image", archivableField.getDescription());
        assertEquals(ArchivingFieldType.BLOB, archivableField.getFieldType());

        archivableField = archivableFieldList.get(1);
        assertEquals("template", archivableField.getFieldName());
        assertEquals("Template", archivableField.getDescription());
        assertEquals(ArchivingFieldType.CLOB, archivableField.getFieldType());

        archivableField = archivableFieldList.get(2);
        assertEquals("createDt", archivableField.getFieldName());
        assertEquals("Create Dt", archivableField.getDescription());
        assertEquals(ArchivingFieldType.TIMESTAMP, archivableField.getFieldType());

        archivableField = archivableFieldList.get(3);
        assertEquals("updateDt", archivableField.getFieldName());
        assertEquals("Update Dt", archivableField.getDescription());
        assertEquals(ArchivingFieldType.TIMESTAMP, archivableField.getFieldType());
    }

    @Test
    public void testCreateFileArchiveConfig() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        Long fileArchiveConfigId = archivingService.createFileArchiveConfig(fileArchiveConfig);
        assertNotNull(fileArchiveConfigId);
    }

    @Test
    public void testFindFileArchiveConfigById() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        Long fileArchiveConfigId = archivingService.createFileArchiveConfig(fileArchiveConfig);

        FileArchiveConfig fetchedFileArchiveConfig = archivingService.findFileArchiveConfig(fileArchiveConfigId);
        assertNotNull(fetchedFileArchiveConfig);
        assertEquals(fileArchiveConfig, fetchedFileArchiveConfig);
    }

    @Test
    public void testFindFileArchiveConfigs() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        archivingService.createFileArchiveConfig(fileArchiveConfig);

        List<FileArchiveConfig> fileArchiveConfigList = archivingService
                .findFileArchiveConfigs(new FileArchiveConfigQuery().name("ChequeImageArchive"));
        FileArchiveConfig foundFileArchiveConfig = fileArchiveConfigList.get(0);
        assertNotNull(fileArchiveConfigList);
        assertEquals(1, fileArchiveConfigList.size());
        assertEquals(fileArchiveConfig.getName(), foundFileArchiveConfig.getName());
        assertEquals(fileArchiveConfig.getDescription(), foundFileArchiveConfig.getDescription());
        assertEquals(fileArchiveConfig.getLocalArchivePath(), foundFileArchiveConfig.getLocalArchivePath());
        assertEquals(fileArchiveConfig.getFilenameGenerator(), foundFileArchiveConfig.getFilenameGenerator());
        assertEquals(fileArchiveConfig.getMaxItemsPerFile(), foundFileArchiveConfig.getMaxItemsPerFile());
        assertEquals(fileArchiveConfig.getDeleteRowOnArchive(), foundFileArchiveConfig.getDeleteRowOnArchive());
        assertEquals(fileArchiveConfig.getStatus(), foundFileArchiveConfig.getStatus());
    }

    @Test
    public void testUpdateFileArchiveConfig() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        Long fileArchiveConfigId = archivingService.createFileArchiveConfig(fileArchiveConfig);

        FileArchiveConfig fetchedFileArchiveConfig = archivingService.findFileArchiveConfig(fileArchiveConfigId);
        fetchedFileArchiveConfig.setDescription("Inward Cheque ImageImpl Archive");
        int count = archivingService.updateFileArchiveConfig(fetchedFileArchiveConfig);
        assertEquals(1, count);

        FileArchiveConfig updatedFileArchiveConfig = archivingService.findFileArchiveConfig(fileArchiveConfigId);
        assertEquals(fetchedFileArchiveConfig, updatedFileArchiveConfig);
        assertFalse(fileArchiveConfig.equals(updatedFileArchiveConfig));
    }

    @Test
    public void testDeleteFileArchiveConfig() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        Long fileArchiveConfigId = archivingService.createFileArchiveConfig(fileArchiveConfig);

        int count = archivingService.deleteFileArchiveConfig(fileArchiveConfigId);
        assertEquals(1, count);
    }

    @Test(timeout = 8000)
    public void testBuildLobFileArchiveTaskForBlobWithDeleteColumn() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        archivingService.createFileArchiveConfig(fileArchiveConfig);

        byte[] image = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
        TestChequeImage testChequeImage = new TestChequeImage();
        testChequeImage.setImage(image);
        Long testChequeImageId = (Long) createRecord(testChequeImage);

        TestChequeImage fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId);
        assertNotNull(fetchedTestChequeImage.getImage());
        Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImage.getCreateDt());
        TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId);
        assertNotNull(fetchedTestChequeImage);
        assertNull(fetchedTestChequeImage.getImage());
    }

    @Test(timeout = 8000)
    public void testBuildLobFileArchiveTaskForBlobWithDeleteRow() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        fileArchiveConfig.setDeleteRowOnArchive(Boolean.TRUE);
        archivingService.createFileArchiveConfig(fileArchiveConfig);

        byte[] image = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
        TestChequeImage testChequeImage = new TestChequeImage();
        testChequeImage.setImage(image);
        Long testChequeImageId = (Long) createRecord(testChequeImage);

        TestChequeImage fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId);
        assertNotNull(fetchedTestChequeImage.getImage());
        Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImage.getCreateDt());
        TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        boolean deleted = false;
        try {
            findRecord(TestChequeImage.class, testChequeImageId);
        } catch (Exception e) {
            deleted = true;
        }
        assertTrue(deleted);
    }

    @Test(timeout = 8000)
    public void testBuildLobFileArchiveTaskForBlobWithDeleteColumnAndWorkingDateOnly() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        archivingService.createFileArchiveConfig(fileArchiveConfig);

        byte[] image1 = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
        TestChequeImage testChequeImage = new TestChequeImage();
        testChequeImage.setImage(image1);
        Long testChequeImageId1 = (Long) createRecord(testChequeImage);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 2);
        byte[] image2 = new byte[] { (byte) 0x2B, (byte) 0xDD, (byte) 0x05, (byte) 0x72, (byte) 0x3E };
        testChequeImage.setImage(image2);
        testChequeImage.setCreateDt(cal.getTime());
        Long testChequeImageId2 = (Long) createRecord(testChequeImage);

        TestChequeImage fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId1);
        assertNotNull(fetchedTestChequeImage.getImage());
        Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImage.getCreateDt());
        TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId1);
        assertNotNull(fetchedTestChequeImage);
        assertNull(fetchedTestChequeImage.getImage());

        fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId2);
        assertNotNull(fetchedTestChequeImage);
        assertNotNull(fetchedTestChequeImage.getImage());
    }

    @Test(timeout = 8000)
    public void testBuildLobFileArchiveTaskForBlobWithDeleteRowAndWorkingDateOnly() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        fileArchiveConfig.setDeleteRowOnArchive(Boolean.TRUE);
        archivingService.createFileArchiveConfig(fileArchiveConfig);

        byte[] image1 = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
        TestChequeImage testChequeImage = new TestChequeImage();
        testChequeImage.setImage(image1);
        Long testChequeImageId1 = (Long) createRecord(testChequeImage);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 2);
        byte[] image2 = new byte[] { (byte) 0x2B, (byte) 0xDD, (byte) 0x05, (byte) 0x72, (byte) 0x3E };
        testChequeImage.setImage(image2);
        testChequeImage.setCreateDt(cal.getTime());
        Long testChequeImageId2 = (Long) createRecord(testChequeImage);

        TestChequeImage fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId1);
        assertNotNull(fetchedTestChequeImage.getImage());
        Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImage.getCreateDt());
        TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        boolean deleted = false;
        try {
            findRecord(TestChequeImage.class, testChequeImageId1);
        } catch (Exception e) {
            deleted = true;
        }
        assertTrue(deleted);

        fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId2);
        assertNotNull(fetchedTestChequeImage);
        assertNotNull(fetchedTestChequeImage.getImage());
    }

    @Test(timeout = 8000)
    public void testBuildLobFileArchiveTaskForClob() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(true);
        archivingService.createFileArchiveConfig(fileArchiveConfig);

        TestChequeImage testChequeImage = new TestChequeImage();
        testChequeImage.setTemplate("Hello World!");
        Long testChequeImageId = (Long) createRecord(testChequeImage);

        TestChequeImage fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId);
        Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImage.getCreateDt());
        TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId);
        assertNotNull(fetchedTestChequeImage);
        assertNull(fetchedTestChequeImage.getTemplate());
    }

    @Test(timeout = 8000)
    public void testRetriveArchivedBlob() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        archivingService.createFileArchiveConfig(fileArchiveConfig);

        byte[] image = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
        TestChequeImage testChequeImage = new TestChequeImage();
        testChequeImage.setImage(image);
        Long testChequeImageId = (Long) createRecord(testChequeImage);

        TestChequeImage fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId);
        assertNotNull(fetchedTestChequeImage.getImage());
        Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImage.getCreateDt());
        TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        byte[] archivedImage = archivingService.retriveArchivedBlob(TestChequeImage.class.getName(), "image",
                testChequeImageId);
        assertNotNull(archivedImage);
        assertTrue(Arrays.equals(image, archivedImage));
    }

    @Test(timeout = 8000)
    public void testRetriveArchivedBlobMultipleItems() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        fileArchiveConfig.setMaxItemsPerFile(Integer.valueOf(3));
        archivingService.createFileArchiveConfig(fileArchiveConfig);

        TestChequeImage testChequeImage = new TestChequeImage();
        Date workingDt = CalendarUtils.getMidnightDate(new Date());
        testChequeImage.setCreateDt(workingDt);
        byte[] image1 = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
        testChequeImage.setImage(image1);
        Long testChequeImageId1 = (Long) createRecord(testChequeImage);

        byte[] image2 = new byte[] { (byte) 0xC4, (byte) 0x80, (byte) 0x2F, (byte) 0xD4, (byte) 0xBB };
        testChequeImage.setImage(image2);
        Long testChequeImageId2 = (Long) createRecord(testChequeImage);

        byte[] image3 = new byte[] { (byte) 0x01, (byte) 0x36, (byte) 0x44 };
        testChequeImage.setImage(image3);
        Long testChequeImageId3 = (Long) createRecord(testChequeImage);

        TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        byte[] archivedImage = archivingService.retriveArchivedBlob(TestChequeImage.class.getName(), "image",
                testChequeImageId1);
        assertNotNull(archivedImage);
        assertTrue(Arrays.equals(image1, archivedImage));

        archivedImage = archivingService.retriveArchivedBlob(TestChequeImage.class.getName(), "image",
                testChequeImageId2);
        assertNotNull(archivedImage);
        assertTrue(Arrays.equals(image2, archivedImage));

        archivedImage = archivingService.retriveArchivedBlob(TestChequeImage.class.getName(), "image",
                testChequeImageId3);
        assertNotNull(archivedImage);
        assertTrue(Arrays.equals(image3, archivedImage));
    }

    @Test(timeout = 8000)
    public void testRetriveArchivedBlobMultipleItemsAndSingleRun() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        fileArchiveConfig.setMaxItemsPerFile(Integer.valueOf(2));
        archivingService.createFileArchiveConfig(fileArchiveConfig);

        TestChequeImage testChequeImage = new TestChequeImage();
        Date workingDt = CalendarUtils.getMidnightDate(new Date());
        testChequeImage.setCreateDt(workingDt);
        byte[] image1 = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
        testChequeImage.setImage(image1);
        Long testChequeImageId1 = (Long) createRecord(testChequeImage);

        byte[] image2 = new byte[] { (byte) 0xC4, (byte) 0x80, (byte) 0x2F, (byte) 0xD4, (byte) 0xBB };
        testChequeImage.setImage(image2);
        Long testChequeImageId2 = (Long) createRecord(testChequeImage);

        byte[] image3 = new byte[] { (byte) 0x01, (byte) 0x36, (byte) 0x44 };
        testChequeImage.setImage(image3);
        Long testChequeImageId3 = (Long) createRecord(testChequeImage);

        // Single pass
        TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        byte[] archivedImage = archivingService.retriveArchivedBlob(TestChequeImage.class.getName(), "image",
                testChequeImageId1);
        assertNotNull(archivedImage);
        assertTrue(Arrays.equals(image1, archivedImage));

        archivedImage = archivingService.retriveArchivedBlob(TestChequeImage.class.getName(), "image",
                testChequeImageId2);
        assertNotNull(archivedImage);
        assertTrue(Arrays.equals(image2, archivedImage));

        // This item has not been archived because of batch limit of 2 and a
        // single pass
        archivedImage = archivingService.retriveArchivedBlob(TestChequeImage.class.getName(), "image",
                testChequeImageId3);
        assertNull(archivedImage);
    }

    @Test(timeout = 8000)
    public void testRetriveArchivedBlobMultipleItemsAndMultipleRun() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(false);
        fileArchiveConfig.setMaxItemsPerFile(Integer.valueOf(2));
        archivingService.createFileArchiveConfig(fileArchiveConfig);

        TestChequeImage testChequeImage = new TestChequeImage();
        Date workingDt = CalendarUtils.getMidnightDate(new Date());
        testChequeImage.setCreateDt(workingDt);
        byte[] image1 = new byte[] { (byte) 0x34, (byte) 0x3F, (byte) 0xAC, (byte) 0xD4 };
        testChequeImage.setImage(image1);
        Long testChequeImageId1 = (Long) createRecord(testChequeImage);

        byte[] image2 = new byte[] { (byte) 0xC4, (byte) 0x80, (byte) 0x2F, (byte) 0xD4, (byte) 0xBB };
        testChequeImage.setImage(image2);
        Long testChequeImageId2 = (Long) createRecord(testChequeImage);

        byte[] image3 = new byte[] { (byte) 0x01, (byte) 0x36, (byte) 0x44 };
        testChequeImage.setImage(image3);
        Long testChequeImageId3 = (Long) createRecord(testChequeImage);

        // Mutiple pass
        TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        byte[] archivedImage = archivingService.retriveArchivedBlob(TestChequeImage.class.getName(), "image",
                testChequeImageId1);
        assertNotNull(archivedImage);
        assertTrue(Arrays.equals(image1, archivedImage));

        archivedImage = archivingService.retriveArchivedBlob(TestChequeImage.class.getName(), "image",
                testChequeImageId2);
        assertNotNull(archivedImage);
        assertTrue(Arrays.equals(image2, archivedImage));

        archivedImage = archivingService.retriveArchivedBlob(TestChequeImage.class.getName(), "image",
                testChequeImageId3);
        assertNotNull(archivedImage);
        assertTrue(Arrays.equals(image3, archivedImage));
    }

    @Test(timeout = 8000)
    public void testRetriveArchivedClob() throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);

        FileArchiveConfig fileArchiveConfig = getFileArchiveConfig(true);
        archivingService.createFileArchiveConfig(fileArchiveConfig);

        TestChequeImage testChequeImage = new TestChequeImage();
        testChequeImage.setTemplate("Hello World!");
        Long testChequeImageId = (Long) createRecord(testChequeImage);

        TestChequeImage fetchedTestChequeImage = findRecord(TestChequeImage.class, testChequeImageId);
        Date workingDt = CalendarUtils.getMidnightDate(fetchedTestChequeImage.getCreateDt());
        TaskMonitor tm = launchBlobFileArchiveTask("ChequeImageArchive", workingDt);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        String archivedTemplate = archivingService.retriveArchivedClob(TestChequeImage.class.getName(), "template",
                testChequeImageId);
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
        deleteAll(TestChequeImage.class, FileArchiveEntry.class, FileArchive.class, FileArchiveConfig.class);
    }

    private FileArchiveConfig getFileArchiveConfig(boolean clob) throws Exception {
        ArchivingService archivingService = (ArchivingService) getComponent(
                ArchivingModuleNameConstants.ARCHIVINGSERVICE);
        ArchivingFieldQuery query = new ArchivingFieldQuery();
        query.recordName(TestChequeImage.class.getName());
        query.orderByFieldType();
        List<ArchivingField> archivableFieldList = archivingService.findArchivingFields(query);

        FileArchiveConfig fileArchiveConfig = new FileArchiveConfig();
        ArchivingField afd = null;
        if (clob) {
            afd = archivableFieldList.get(1);
        } else {
            afd = archivableFieldList.get(0);
        }
        fileArchiveConfig.setArchivableDefId(afd.getArchivableDefId());
        fileArchiveConfig.setArchivableFieldId(afd.getId());
        fileArchiveConfig.setArchivableDateFieldId(archivableFieldList.get(2).getId());
        fileArchiveConfig.setName("ChequeImageArchive");
        fileArchiveConfig.setDescription("Cheque ImageImpl Archive");
        fileArchiveConfig.setLocalArchivePath("c:\\archive\\images");
        fileArchiveConfig.setFilenameGenerator("default-filearchivenamegenerator");
        fileArchiveConfig.setMaxItemsPerFile(2);
        fileArchiveConfig.setDeleteRowOnArchive(Boolean.FALSE);
        fileArchiveConfig.setStatus(RecordStatus.ACTIVE);
        return fileArchiveConfig;
    }

    private TaskMonitor launchBlobFileArchiveTask(String configName, Date workingDt) throws Exception {
        return ((TaskLauncher) getComponent(ApplicationComponents.APPLICATION_TASKLAUNCHER))
                .launchTask(TaskSetup.newBuilder().addTask(FileArchiveTaskConstants.BUILDLOBFILEARCHIVETASK)
                        .setParam(FileArchiveTaskParamConstants.FILEARCHIVECONFIGNAME, configName)
                        .setParam(FileArchiveTaskParamConstants.WORKINGDT, workingDt).build());
    }
}
