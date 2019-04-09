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
package com.tcdng.jacklyn.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.tcdng.jacklyn.common.AbstractJacklynTest;
import com.tcdng.jacklyn.common.TestBank;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.file.business.FileService;
import com.tcdng.jacklyn.file.constants.BatchFileReadTaskConstants;
import com.tcdng.jacklyn.file.constants.FileModuleNameConstants;
import com.tcdng.jacklyn.file.constants.FileTransferTaskConstants;
import com.tcdng.jacklyn.file.data.BatchFileReadInputParameters;
import com.tcdng.jacklyn.file.entities.BatchFileDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileDefinitionQuery;
import com.tcdng.jacklyn.file.entities.BatchFileFieldDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileReadDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileReadDefinitionQuery;
import com.tcdng.jacklyn.file.entities.FileInbox;
import com.tcdng.jacklyn.file.entities.FileInboxQuery;
import com.tcdng.jacklyn.file.entities.FileOutbox;
import com.tcdng.jacklyn.file.entities.FileOutboxQuery;
import com.tcdng.jacklyn.file.entities.FileTransferConfig;
import com.tcdng.jacklyn.file.entities.FileTransferConfigQuery;
import com.tcdng.jacklyn.shared.file.FileInboxReadStatus;
import com.tcdng.jacklyn.shared.file.FileInboxStatus;
import com.tcdng.jacklyn.shared.file.FileOutboxStatus;
import com.tcdng.jacklyn.shared.file.FileTransferDirection;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.Setting;
import com.tcdng.unify.core.batch.ConstraintAction;
import com.tcdng.unify.core.business.GenericService;
import com.tcdng.unify.core.data.Input;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.file.TestFileTransferServer;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.task.TaskStatus;
import com.tcdng.unify.core.util.IOUtils;

/**
 * File module tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class FileServiceTest extends AbstractJacklynTest {

    @Test
    public void testCreateFileTransferConfig() throws Exception {
        FileTransferConfig fileTransferConfig = getUploadFileTransferConfig();
        Long fileTransferConfigId = getFileService().createFileTransferConfig(fileTransferConfig);
        assertNotNull(fileTransferConfigId);
    }

    @Test
    public void testFindFileTransferConfigs() throws Exception {
        FileTransferConfig downloadConfig = getDownloadFileTransferConfig();
        FileTransferConfig uploadConfig = getUploadFileTransferConfig();
        getFileService().createFileTransferConfig(downloadConfig);
        getFileService().createFileTransferConfig(uploadConfig);

        FileTransferConfigQuery query = new FileTransferConfigQuery();
        query.orderById();
        query.ignoreEmptyCriteria(true);
        List<FileTransferConfig> fileTransferConfigList = getFileService().findFileTransferConfigs(query);
        assertNotNull(fileTransferConfigList);
        assertEquals(2, fileTransferConfigList.size());

        FileTransferConfig foundDownloadConfig = fileTransferConfigList.get(0);
        assertEquals(downloadConfig.getDescription(), foundDownloadConfig.getDescription());
        assertEquals(downloadConfig.getDeleteSourceOnTransfer(), foundDownloadConfig.getDeleteSourceOnTransfer());
        assertEquals(downloadConfig.getDirection(), foundDownloadConfig.getDirection());
        assertEquals(downloadConfig.getFileTransferPolicy(), foundDownloadConfig.getFileTransferPolicy());
        assertEquals(downloadConfig.getFileTransferServer(), foundDownloadConfig.getFileTransferServer());
        assertEquals(downloadConfig.getLocalPath(), foundDownloadConfig.getLocalPath());
        assertEquals(downloadConfig.getMaxTransferAttempts(), foundDownloadConfig.getMaxTransferAttempts());
        assertEquals(downloadConfig.getRemoteHost(), foundDownloadConfig.getRemoteHost());
        assertEquals(downloadConfig.getRemotePath(), foundDownloadConfig.getRemotePath());
        assertEquals(downloadConfig.getRemotePort(), foundDownloadConfig.getRemotePort());
        assertEquals(downloadConfig.getStatus(), foundDownloadConfig.getStatus());

        FileTransferConfig foundUploadConfig = fileTransferConfigList.get(1);
        assertEquals(uploadConfig.getDescription(), foundUploadConfig.getDescription());
        assertEquals(uploadConfig.getDeleteSourceOnTransfer(), foundUploadConfig.getDeleteSourceOnTransfer());
        assertEquals(uploadConfig.getDirection(), foundUploadConfig.getDirection());
        assertEquals(uploadConfig.getFileTransferPolicy(), foundUploadConfig.getFileTransferPolicy());
        assertEquals(uploadConfig.getFileTransferServer(), foundUploadConfig.getFileTransferServer());
        assertEquals(uploadConfig.getLocalPath(), foundUploadConfig.getLocalPath());
        assertEquals(uploadConfig.getMaxTransferAttempts(), foundUploadConfig.getMaxTransferAttempts());
        assertEquals(uploadConfig.getRemoteHost(), foundUploadConfig.getRemoteHost());
        assertEquals(uploadConfig.getRemotePath(), foundUploadConfig.getRemotePath());
        assertEquals(uploadConfig.getRemotePort(), foundUploadConfig.getRemotePort());
        assertEquals(uploadConfig.getStatus(), foundUploadConfig.getStatus());
    }

    @Test
    public void testFindFileTransferConfig() throws Exception {
        FileTransferConfig downloadConfig = getDownloadFileTransferConfig();
        Long id = getFileService().createFileTransferConfig(downloadConfig);

        FileTransferConfig fetchedDownloadConfig = getFileService().findFileTransferConfig(id);
        assertNotNull(fetchedDownloadConfig);
        assertEquals(downloadConfig, fetchedDownloadConfig);
    }

    @Test
    public void testUpdateFileTransferConfig() throws Exception {
        FileTransferConfig downloadConfig = getDownloadFileTransferConfig();
        Long id = getFileService().createFileTransferConfig(downloadConfig);

        FileTransferConfig fetchedDownloadConfig = getFileService().findFileTransferConfig(id);
        fetchedDownloadConfig.setDeleteSourceOnTransfer(Boolean.TRUE);
        fetchedDownloadConfig.setLocalPath("c:\\banking\\reports");
        fetchedDownloadConfig.setMaxTransferAttempts(Integer.valueOf(20));
        int updateCount = getFileService().updateFileTransferConfig(fetchedDownloadConfig);
        assertEquals(1, updateCount);

        FileTransferConfig updatedDownloadConfig = getFileService().findFileTransferConfig(id);
        assertFalse(downloadConfig.equals(updatedDownloadConfig));
        assertEquals(fetchedDownloadConfig, updatedDownloadConfig);
    }

    @Test
    public void testDeleteFileTransferConfig() throws Exception {
        FileTransferConfig downloadConfig = getDownloadFileTransferConfig();
        Long id = getFileService().createFileTransferConfig(downloadConfig);
        int updateCount = getFileService().deleteFileTransferConfig(id);
        assertEquals(1, updateCount);

        FileTransferConfigQuery query = new FileTransferConfigQuery();
        query.id(id);
        List<FileTransferConfig> list = getFileService().findFileTransferConfigs(query);
        assertEquals(0, list.size());
    }

    @Test
    public void testCreateBatchFileDefinition() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        Long batchFileDefinitionId = getFileService().createBatchFileDefinition(batchFileDefinition);
        assertNotNull(batchFileDefinitionId);
    }

    @Test
    public void testFindBatchFileDefinitions() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        getFileService().createBatchFileDefinition(batchFileDefinition);

        BatchFileDefinitionQuery query = new BatchFileDefinitionQuery();
        query.orderById();
        query.ignoreEmptyCriteria(true);
        List<BatchFileDefinition> batchFileDefinitionList = getFileService().findBatchFileDefinitions(query);
        assertNotNull(batchFileDefinitionList);
        assertEquals(1, batchFileDefinitionList.size());

        assertEquals(batchFileDefinition.getName(), batchFileDefinitionList.get(0).getName());
        assertEquals(batchFileDefinition.getDescription(), batchFileDefinitionList.get(0).getDescription());
    }

    @Test
    public void testFindBatchFileDefinition() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        Long id = getFileService().createBatchFileDefinition(batchFileDefinition);

        BatchFileDefinition fetchBatchFileDefinition = getFileService().findBatchFileDefinition(id);
        assertNotNull(fetchBatchFileDefinition);
        assertEquals(batchFileDefinition.getName(), fetchBatchFileDefinition.getName());
        assertEquals(batchFileDefinition.getDescription(), fetchBatchFileDefinition.getDescription());
    }

    @Test
    public void testUpdateBatchFileDefinition() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        Long id = getFileService().createBatchFileDefinition(batchFileDefinition);

        BatchFileDefinition fetchedBatchFileDefinition = getFileService().findBatchFileDefinition(id);
        fetchedBatchFileDefinition.setDescription("Test Definiton Main");
        int updateCount = getFileService().updateBatchFileDefinition(fetchedBatchFileDefinition);
        assertEquals(1, updateCount);

        BatchFileDefinition updatedBatchFileDefinition = getFileService().findBatchFileDefinition(id);
        assertFalse(batchFileDefinition.equals(updatedBatchFileDefinition));
        assertEquals(fetchedBatchFileDefinition, updatedBatchFileDefinition);
    }

    @Test
    public void testDeleteBatchFileDefinition() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        Long id = getFileService().createBatchFileDefinition(batchFileDefinition);
        int updateCount = getFileService().deleteBatchFileDefinition(id);
        assertEquals(1, updateCount);

        BatchFileDefinitionQuery query = new BatchFileDefinitionQuery();
        query.id(id);
        List<BatchFileDefinition> list = getFileService().findBatchFileDefinitions(query);
        assertEquals(0, list.size());
    }

    @Test
    public void testCreateBatchFileReadDefinition() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        Long batchFileDefinitionId = getFileService().createBatchFileDefinition(batchFileDefinition);
        BatchFileReadDefinition batchFileReadDefinition = getBatchFileReadDefinition(batchFileDefinitionId);
        Long batchUploadConfigId = getFileService().createBatchFileReadDefinition(batchFileReadDefinition);
        assertNotNull(batchUploadConfigId);
    }

    @Test
    public void testFindBatchFileReadDefinitions() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        Long batchFileDefinitionId = getFileService().createBatchFileDefinition(batchFileDefinition);
        BatchFileReadDefinition batchFileReadDefinition = getBatchFileReadDefinition(batchFileDefinitionId);
        getFileService().createBatchFileReadDefinition(batchFileReadDefinition);

        BatchFileReadDefinitionQuery query = new BatchFileReadDefinitionQuery();
        query.orderById();
        query.ignoreEmptyCriteria(true);
        List<BatchFileReadDefinition> batchUploadConfigList = getFileService().findBatchFileReadDefinitions(query);
        assertNotNull(batchUploadConfigList);
        assertEquals(1, batchUploadConfigList.size());

        BatchFileReadDefinition foundBatchFileReadDefinition = batchUploadConfigList.get(0);
        assertEquals(batchFileReadDefinition.getBatchFileDefinitionId(),
                foundBatchFileReadDefinition.getBatchFileDefinitionId());
        assertEquals(batchFileReadDefinition.getReadProcessor(), foundBatchFileReadDefinition.getReadProcessor());
        assertEquals(batchFileReadDefinition.getConstraintAction(), foundBatchFileReadDefinition.getConstraintAction());
        assertEquals(batchFileReadDefinition.getDescription(), foundBatchFileReadDefinition.getDescription());
        assertEquals(batchFileReadDefinition.getFileReader(), foundBatchFileReadDefinition.getFileReader());
        assertEquals(batchFileReadDefinition.getName(), foundBatchFileReadDefinition.getName());
        assertEquals(batchFileReadDefinition.getStatus(), foundBatchFileReadDefinition.getStatus());
    }

    @Test
    public void testFindBatchFileReadDefinition() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        Long batchFileDefinitionId = getFileService().createBatchFileDefinition(batchFileDefinition);
        BatchFileReadDefinition batchFileReadDefinition = getBatchFileReadDefinition(batchFileDefinitionId);
        Long id = getFileService().createBatchFileReadDefinition(batchFileReadDefinition);

        BatchFileReadDefinition fetchedDownloadConfig = getFileService().findBatchFileReadDefinition(id);
        assertNotNull(fetchedDownloadConfig);
        assertEquals(batchFileReadDefinition, fetchedDownloadConfig);
    }

    @Test
    public void testUpdateBatchFileReadDefinition() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        Long batchFileDefinitionId = getFileService().createBatchFileDefinition(batchFileDefinition);
        BatchFileReadDefinition batchFileReadDefinition = getBatchFileReadDefinition(batchFileDefinitionId);
        Long id = getFileService().createBatchFileReadDefinition(batchFileReadDefinition);

        BatchFileReadDefinition fetchedBatchFileReadDefinition = getFileService().findBatchFileReadDefinition(id);
        fetchedBatchFileReadDefinition.setDescription("Main Sample Config");
        int updateCount = getFileService().updateBatchFileReadDefinition(fetchedBatchFileReadDefinition);
        assertEquals(1, updateCount);

        BatchFileReadDefinition updatedBatchFileReadDefinition = getFileService().findBatchFileReadDefinition(id);
        assertFalse(batchFileReadDefinition.equals(updatedBatchFileReadDefinition));
        assertEquals(fetchedBatchFileReadDefinition, updatedBatchFileReadDefinition);
    }

    @Test
    public void testDeleteBatchFileReadDefinition() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        Long batchFileDefinitionId = getFileService().createBatchFileDefinition(batchFileDefinition);
        BatchFileReadDefinition batchFileReadDefinition = getBatchFileReadDefinition(batchFileDefinitionId);
        Long id = getFileService().createBatchFileReadDefinition(batchFileReadDefinition);
        int updateCount = getFileService().deleteBatchFileReadDefinition(id);
        assertEquals(1, updateCount);

        BatchFileReadDefinitionQuery query = new BatchFileReadDefinitionQuery();
        query.id(id);
        List<BatchFileReadDefinition> list = getFileService().findBatchFileReadDefinitions(query);
        assertEquals(0, list.size());
    }

    @Test
    public void testGetBatchFileReadInputParameters() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        Long batchFileDefinitionId = getFileService().createBatchFileDefinition(batchFileDefinition);
        BatchFileReadDefinition batchFileReadDefinition = getBatchFileReadDefinition(batchFileDefinitionId);
        Long batchConfigId = getFileService().createBatchFileReadDefinition(batchFileReadDefinition);

        BatchFileReadInputParameters buip = getFileService().getBatchFileReadInputParameters(batchConfigId);
        assertNotNull(buip);
        assertEquals("BankBatchConfig", buip.getName());
        assertEquals("Bank Batch Config", buip.getDescription());

        List<Input> inputParameterList = buip.getInputParameterList();
        assertNotNull(inputParameterList);
        assertEquals(1, inputParameterList.size());

        Input placeHolder = inputParameterList.get(0);
        assertEquals("country", placeHolder.getName());
        assertEquals("Country", placeHolder.getDescription());
    }

    @Test(timeout = 4000)
    public void testFindFileInboxItems() throws Exception {
        FileService fileService = (FileService) getComponent(FileModuleNameConstants.FILESERVICE);
        FileTransferConfig downloadConfig = getDownloadFileTransferConfig();
        fileService.createFileTransferConfig(downloadConfig);

        TaskMonitor tm = launchFileTransferTask("downloadFileTransfer", new Date(), true);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        FileInboxQuery query = new FileInboxQuery();
        query.fileTransferConfigName("downloadFileTransfer");
        query.order("filename");
        List<FileInbox> fileInboxList = fileService.findFileInboxItems(query);
        assertNotNull(fileInboxList);
        assertEquals(3, fileInboxList.size());

        FileInbox fileInbox = fileInboxList.get(0);
        assertEquals("butterfly.png", fileInbox.getFilename());
        assertEquals(FileInboxReadStatus.NOT_READ, fileInbox.getReadStatus());
        assertEquals(FileInboxStatus.RECEIVED, fileInbox.getStatus());

        fileInbox = fileInboxList.get(1);
        assertEquals("customer.html", fileInbox.getFilename());
        assertEquals(FileInboxReadStatus.NOT_READ, fileInbox.getReadStatus());
        assertEquals(FileInboxStatus.RECEIVED, fileInbox.getStatus());

        fileInbox = fileInboxList.get(2);
        assertEquals("hello.txt", fileInbox.getFilename());
        assertEquals(FileInboxReadStatus.NOT_READ, fileInbox.getReadStatus());
        assertEquals(FileInboxStatus.RECEIVED, fileInbox.getStatus());
    }

    @Test(timeout = 4000)
    public void testFindFileInboxItem() throws Exception {
        FileService fileService = (FileService) getComponent(FileModuleNameConstants.FILESERVICE);
        FileTransferConfig downloadConfig = getDownloadFileTransferConfig();
        fileService.createFileTransferConfig(downloadConfig);

        TaskMonitor tm = launchFileTransferTask("downloadFileTransfer", new Date(), true);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        FileInboxQuery query = new FileInboxQuery();
        query.fileTransferConfigName("downloadFileTransfer");
        List<FileInbox> fileInboxList = fileService.findFileInboxItems(query);

        FileInbox fileInbox = fileInboxList.get(0);
        FileInbox fetchedFileInbox = fileService.findFileInboxItem(fileInbox.getId());
        assertEquals(fileInbox, fetchedFileInbox);
    }

    @Test(timeout = 4000)
    public void testUpdateFileInboxItemsReadStatus() throws Exception {
        FileService fileService = (FileService) getComponent(FileModuleNameConstants.FILESERVICE);
        FileTransferConfig downloadConfig = getDownloadFileTransferConfig();
        fileService.createFileTransferConfig(downloadConfig);

        TaskMonitor tm = launchFileTransferTask("downloadFileTransfer", new Date(), true);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        FileInboxQuery query = new FileInboxQuery();
        query.fileTransferConfigName("downloadFileTransfer");
        query.setFilenameIn(Arrays.asList(new String[] { "butterfly.png", "hello.txt" }));
        fileService.updateFileInboxItemsReadStatus(query, FileInboxReadStatus.READ);

        query = new FileInboxQuery();
        query.fileTransferConfigName("downloadFileTransfer");
        query.order("filename");
        List<FileInbox> fileInboxList = fileService.findFileInboxItems(query);

        FileInbox fileInbox = fileInboxList.get(0);
        assertEquals("butterfly.png", fileInbox.getFilename());
        assertEquals(FileInboxReadStatus.READ, fileInbox.getReadStatus());
        assertEquals(FileInboxStatus.RECEIVED, fileInbox.getStatus());

        fileInbox = fileInboxList.get(1);
        assertEquals("customer.html", fileInbox.getFilename());
        assertEquals(FileInboxReadStatus.NOT_READ, fileInbox.getReadStatus());
        assertEquals(FileInboxStatus.RECEIVED, fileInbox.getStatus());

        fileInbox = fileInboxList.get(2);
        assertEquals("hello.txt", fileInbox.getFilename());
        assertEquals(FileInboxReadStatus.READ, fileInbox.getReadStatus());
        assertEquals(FileInboxStatus.RECEIVED, fileInbox.getStatus());
    }

    @Test(timeout = 4000)
    public void testFindFileOutboxItems() throws Exception {
        FileService fileService = (FileService) getComponent(FileModuleNameConstants.FILESERVICE);
        FileTransferConfig uploadConfig = getUploadFileTransferConfig();
        fileService.createFileTransferConfig(uploadConfig);

        TaskMonitor tm = launchFileTransferTask("uploadFileTransfer", new Date(), true);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        FileOutboxQuery query = new FileOutboxQuery();
        query.fileTransferConfigName("uploadFileTransfer");
        query.order("filename");
        List<FileOutbox> fileOutboxList = fileService.findFileOutboxItems(query);
        assertNotNull(fileOutboxList);
        assertEquals(2, fileOutboxList.size());

        FileOutbox fileOutbox = fileOutboxList.get(0);
        assertEquals("doctor.png", fileOutbox.getFilename());
        assertEquals(FileOutboxStatus.SENT, fileOutbox.getStatus());

        fileOutbox = fileOutboxList.get(1);
        assertEquals("transactions.txt", fileOutbox.getFilename());
        assertEquals(FileOutboxStatus.SENT, fileOutbox.getStatus());
    }

    @Test(timeout = 4000)
    public void testFindFileOutboxItem() throws Exception {
        FileService fileService = (FileService) getComponent(FileModuleNameConstants.FILESERVICE);
        FileTransferConfig uploadConfig = getUploadFileTransferConfig();
        fileService.createFileTransferConfig(uploadConfig);

        TaskMonitor tm = launchFileTransferTask("uploadFileTransfer", new Date(), true);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        FileOutboxQuery query = new FileOutboxQuery();
        query.fileTransferConfigName("uploadFileTransfer");
        List<FileOutbox> fileInboxList = fileService.findFileOutboxItems(query);

        FileOutbox fileOutbox = fileInboxList.get(0);
        FileOutbox fetchedFileOutbox = fileService.findFileOutboxItem(fileOutbox.getId());
        assertEquals(fileOutbox, fetchedFileOutbox);
    }

    @Test(timeout = 4000)
    public void testStartTestFileTransferConfigTask() throws Exception {
        FileService fileService = (FileService) getComponent(FileModuleNameConstants.FILESERVICE);
        FileTransferConfig downloadConfig = getDownloadFileTransferConfig();
        fileService.createFileTransferConfig(downloadConfig);
        TaskMonitor tm = ((TaskLauncher) getComponent(ApplicationComponents.APPLICATION_TASKLAUNCHER))
                .launchTask(TaskSetup.newBuilder().addTask(FileTransferTaskConstants.FILETRANSFERCONFIGTESTTASK)
                        .setParam(FileTransferTaskConstants.FILETRANSFERCONFIGDATA, downloadConfig).build());
        assertNotNull(tm);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));
    }

    @Test(timeout = 4000)
    public void testStartUpdateFileTransferListTask() throws Exception {
        FileService fileService = (FileService) getComponent(FileModuleNameConstants.FILESERVICE);
        FileTransferConfig downloadConfig = getDownloadFileTransferConfig();
        FileTransferConfig uploadConfig = getUploadFileTransferConfig();
        fileService.createFileTransferConfig(downloadConfig);
        fileService.createFileTransferConfig(uploadConfig);

        TaskMonitor tm = launchUpdateFileTransferListTask("downloadFileTransfer", new Date());
        assertNotNull(tm);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        tm = launchUpdateFileTransferListTask("uploadFileTransfer", new Date());
        assertNotNull(tm);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));
    }

    @Test(timeout = 4000)
    public void testStartExecuteFileTransferTask() throws Exception {
        FileService fileService = (FileService) getComponent(FileModuleNameConstants.FILESERVICE);
        FileTransferConfig downloadConfig = getDownloadFileTransferConfig();
        FileTransferConfig uploadConfig = getUploadFileTransferConfig();
        fileService.createFileTransferConfig(downloadConfig);
        fileService.createFileTransferConfig(uploadConfig);

        TaskMonitor tm = launchFileTransferTask("downloadFileTransfer", new Date(), true);
        assertNotNull(tm);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        tm = launchFileTransferTask("uploadFileTransfer", new Date(), true);
        assertNotNull(tm);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));
    }

    @Test
    public void testStartBatchFileReadTask() throws Exception {
        BatchFileDefinition batchFileDefinition = newBatchFileDefinition();
        Long batchFileDefinitionId = getFileService().createBatchFileDefinition(batchFileDefinition);
        BatchFileReadDefinition batchFileReadDefinition = getBatchFileReadDefinition(batchFileDefinitionId);
        Long batchConfigId = getFileService().createBatchFileReadDefinition(batchFileReadDefinition);

        BatchFileReadInputParameters buip = getFileService().getBatchFileReadInputParameters(batchConfigId);
        byte[] file = IOUtils.createInMemoryTextFile("011First Bank          FBN 011000001",
                "032Union Bank          UBN 032000001", "044Access Bank         ACC 044000001");
        buip.setFileBlob(file);
        buip.setParameter("country", "Nigeria");

        TaskMonitor tm = ((TaskLauncher) getComponent(ApplicationComponents.APPLICATION_TASKLAUNCHER))
                .launchTask(TaskSetup.newBuilder().addTask(BatchFileReadTaskConstants.BATCHFILEREADTASK)
                        .setParam(BatchFileReadTaskConstants.BATCHFILEREADINPUTPARAMS, buip).build());
        assertNotNull(tm);
        waitForTask(tm);
        assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

        GenericService genericService = (GenericService) this
                .getComponent(ApplicationComponents.APPLICATION_GENERICSERVICE);
        List<TestBank> bankList = genericService
                .listAll(new Query<TestBank>(TestBank.class).order("id").ignoreEmptyCriteria(true));
        assertNotNull(bankList);
        assertEquals(3, bankList.size());

        TestBank bank = bankList.get(0);
        assertEquals("011", bank.getName());
        assertEquals("First Bank", bank.getDescription());
        assertEquals("FBN", bank.getShortName());
        assertEquals("011000001", bank.getHoRoutingNo());
        assertEquals("Nigeria", bank.getCountry());

        bank = bankList.get(1);
        assertEquals("032", bank.getName());
        assertEquals("Union Bank", bank.getDescription());
        assertEquals("UBN", bank.getShortName());
        assertEquals("032000001", bank.getHoRoutingNo());
        assertEquals("Nigeria", bank.getCountry());

        bank = bankList.get(2);
        assertEquals("044", bank.getName());
        assertEquals("Access Bank", bank.getDescription());
        assertEquals("ACC", bank.getShortName());
        assertEquals("044000001", bank.getHoRoutingNo());
        assertEquals("Nigeria", bank.getCountry());
    }

    @Override
    protected void doAddSettingsAndDependencies() throws Exception {
        super.doAddSettingsAndDependencies();

        addDependency("test-filetransferserver", TestFileTransferServer.class,
                new Setting("localFilenames", new String[] { "doctor.png", "transactions.txt" }),
                new Setting("remoteFilenames", new String[] { "butterfly.png", "hello.txt", "customer.html" }));
    }

    @Override
    protected void onSetup() throws Exception {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onTearDown() throws Exception {
        deleteAll(FileInbox.class, FileOutbox.class, BatchFileReadDefinition.class, BatchFileDefinition.class,
                FileTransferConfig.class);
    }

    private FileTransferConfig getUploadFileTransferConfig() {
        FileTransferConfig fileTransferConfig = new FileTransferConfig();
        fileTransferConfig.setName("uploadFileTransfer");
        fileTransferConfig.setDeleteSourceOnTransfer(Boolean.FALSE);
        fileTransferConfig.setDescription("EOD Report Transfer");
        fileTransferConfig.setDirection(FileTransferDirection.UPLOAD);
        fileTransferConfig.setFileTransferPolicy("wildcard-filetransferpolicy");
        fileTransferConfig.setFileTransferServer("test-filetransferserver");
        fileTransferConfig.setLocalPath("c:\\data\\reports");
        fileTransferConfig.setMaxTransferAttempts(Integer.valueOf(10));
        fileTransferConfig.setRemoteHost("192.168.1.1");
        fileTransferConfig.setRemotePath("/eodreports");
        fileTransferConfig.setStatus(RecordStatus.ACTIVE);
        return fileTransferConfig;
    }

    private FileTransferConfig getDownloadFileTransferConfig() {
        FileTransferConfig fileTransferConfig = new FileTransferConfig();
        fileTransferConfig.setName("downloadFileTransfer");
        fileTransferConfig.setDeleteSourceOnTransfer(Boolean.FALSE);
        fileTransferConfig.setDescription("BPXF File Transfer");
        fileTransferConfig.setDirection(FileTransferDirection.DOWNLOAD);
        fileTransferConfig.setFileTransferPolicy("wildcard-filetransferpolicy");
        fileTransferConfig.setFileTransferServer("test-filetransferserver");
        fileTransferConfig.setLocalPath("c:\\truncation\\bpxf");
        fileTransferConfig.setMaxTransferAttempts(Integer.valueOf(4));
        fileTransferConfig.setRemoteHost("10.0.2.15");
        fileTransferConfig.setRemotePath("/incoming/bfxf");
        fileTransferConfig.setStatus(RecordStatus.ACTIVE);
        return fileTransferConfig;
    }

    private BatchFileDefinition newBatchFileDefinition() {
        BatchFileDefinition batchFileDefinition = new BatchFileDefinition();
        batchFileDefinition.setName("fil-001");
        batchFileDefinition.setDescription("Test Definition");

        List<BatchFileFieldDefinition> fieldDefList = new ArrayList<BatchFileFieldDefinition>();
        fieldDefList.add(newBatchFileFieldDefinition("name", 3));
        fieldDefList.add(newBatchFileFieldDefinition("description", 20));
        fieldDefList.add(newBatchFileFieldDefinition("shortName", 4));
        fieldDefList.add(newBatchFileFieldDefinition("hoRoutingNo", 9));
        batchFileDefinition.setFieldDefList(fieldDefList);

        return batchFileDefinition;
    }

    private BatchFileFieldDefinition newBatchFileFieldDefinition(String name, int length) {
        BatchFileFieldDefinition batchFileFieldDefinition = new BatchFileFieldDefinition();
        batchFileFieldDefinition.setName(name);
        batchFileFieldDefinition.setLength(length);
        batchFileFieldDefinition.setTrim(true);
        return batchFileFieldDefinition;
    }

    private BatchFileReadDefinition getBatchFileReadDefinition(Long batchFileDefinitionId) {
        BatchFileReadDefinition batchFileReadDefinition = new BatchFileReadDefinition();
        batchFileReadDefinition.setBatchFileDefinitionId(batchFileDefinitionId);
        batchFileReadDefinition.setReadProcessor("test-bankbatchfilereadprocessor");
        batchFileReadDefinition.setFileReader("fixedlength-batchfilereader");
        batchFileReadDefinition.setConstraintAction(ConstraintAction.FAIL);
        batchFileReadDefinition.setDescription("Bank Batch Config");
        batchFileReadDefinition.setName("BankBatchConfig");
        return batchFileReadDefinition;
    }

    private FileService getFileService() throws Exception {
        return (FileService) getComponent(FileModuleNameConstants.FILESERVICE);
    }

    private TaskMonitor launchFileTransferTask(String configName, Date workingDt, boolean updateList) throws Exception {
        return ((TaskLauncher) getComponent(ApplicationComponents.APPLICATION_TASKLAUNCHER))
                .launchTask(TaskSetup.newBuilder().addTask(FileTransferTaskConstants.FILETRANSFERTASK)
                        .setParam(FileTransferTaskConstants.FILETRANSFERCONFIGNAME, configName)
                        .setParam(FileTransferTaskConstants.UPDATEFILEBOX, updateList)
                        .setParam(FileTransferTaskConstants.WORKINGDT, workingDt).build());
    }

    private TaskMonitor launchUpdateFileTransferListTask(String configName, Date workingDt) throws Exception {
        return ((TaskLauncher) getComponent(ApplicationComponents.APPLICATION_TASKLAUNCHER))
                .launchTask(TaskSetup.newBuilder().addTask(FileTransferTaskConstants.FILETRANSFERLISTUPDATETASK)
                        .setParam(FileTransferTaskConstants.FILETRANSFERCONFIGNAME, configName)
                        .setParam(FileTransferTaskConstants.WORKINGDT, workingDt).build());
    }
}
