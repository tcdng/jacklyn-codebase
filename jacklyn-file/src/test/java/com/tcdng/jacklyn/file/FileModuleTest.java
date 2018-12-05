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
import com.tcdng.jacklyn.common.TestBankData;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.file.business.FileModule;
import com.tcdng.jacklyn.file.constants.BatchFileReadTaskConstants;
import com.tcdng.jacklyn.file.constants.FileModuleNameConstants;
import com.tcdng.jacklyn.file.constants.FileTransferTaskConstants;
import com.tcdng.jacklyn.file.data.BatchFileReadInputParameters;
import com.tcdng.jacklyn.file.entities.BatchFileDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileDefinitionQuery;
import com.tcdng.jacklyn.file.entities.BatchFileFieldDefinition;
import com.tcdng.jacklyn.file.entities.BatchFileReadConfig;
import com.tcdng.jacklyn.file.entities.BatchFileReadConfigQuery;
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
import com.tcdng.unify.core.business.GenericBusinessModule;
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
public class FileModuleTest extends AbstractJacklynTest {

	@Test
	public void testCreateFileTransferConfig() throws Exception {
		FileTransferConfig fileTransferConfigData = getUploadFileTransferConfigData();
		Long fileTransferConfigId
				= getFileModule().createFileTransferConfig(fileTransferConfigData);
		assertNotNull(fileTransferConfigId);
	}

	@Test
	public void testFindFileTransferConfigs() throws Exception {
		FileTransferConfig downloadConfig = getDownloadFileTransferConfigData();
		FileTransferConfig uploadConfig = getUploadFileTransferConfigData();
		getFileModule().createFileTransferConfig(downloadConfig);
		getFileModule().createFileTransferConfig(uploadConfig);

		FileTransferConfigQuery query = new FileTransferConfigQuery();
		query.orderById();
		query.ignoreEmptyCriteria(true);
		List<FileTransferConfig> fileTransferConfigList
				= getFileModule().findFileTransferConfigs(query);
		assertNotNull(fileTransferConfigList);
		assertEquals(2, fileTransferConfigList.size());

		FileTransferConfig foundDownloadConfig = fileTransferConfigList.get(0);
		assertEquals(downloadConfig.getDescription(), foundDownloadConfig.getDescription());
		assertEquals(downloadConfig.getDeleteSourceOnTransfer(),
				foundDownloadConfig.getDeleteSourceOnTransfer());
		assertEquals(downloadConfig.getDirection(), foundDownloadConfig.getDirection());
		assertEquals(downloadConfig.getFileTransferPolicy(),
				foundDownloadConfig.getFileTransferPolicy());
		assertEquals(downloadConfig.getFileTransferServer(),
				foundDownloadConfig.getFileTransferServer());
		assertEquals(downloadConfig.getLocalPath(), foundDownloadConfig.getLocalPath());
		assertEquals(downloadConfig.getMaxTransferAttempts(),
				foundDownloadConfig.getMaxTransferAttempts());
		assertEquals(downloadConfig.getRemoteHost(), foundDownloadConfig.getRemoteHost());
		assertEquals(downloadConfig.getRemotePath(), foundDownloadConfig.getRemotePath());
		assertEquals(downloadConfig.getRemotePort(), foundDownloadConfig.getRemotePort());
		assertEquals(downloadConfig.getStatus(), foundDownloadConfig.getStatus());

		FileTransferConfig foundUploadConfig = fileTransferConfigList.get(1);
		assertEquals(uploadConfig.getDescription(), foundUploadConfig.getDescription());
		assertEquals(uploadConfig.getDeleteSourceOnTransfer(),
				foundUploadConfig.getDeleteSourceOnTransfer());
		assertEquals(uploadConfig.getDirection(), foundUploadConfig.getDirection());
		assertEquals(uploadConfig.getFileTransferPolicy(),
				foundUploadConfig.getFileTransferPolicy());
		assertEquals(uploadConfig.getFileTransferServer(),
				foundUploadConfig.getFileTransferServer());
		assertEquals(uploadConfig.getLocalPath(), foundUploadConfig.getLocalPath());
		assertEquals(uploadConfig.getMaxTransferAttempts(),
				foundUploadConfig.getMaxTransferAttempts());
		assertEquals(uploadConfig.getRemoteHost(), foundUploadConfig.getRemoteHost());
		assertEquals(uploadConfig.getRemotePath(), foundUploadConfig.getRemotePath());
		assertEquals(uploadConfig.getRemotePort(), foundUploadConfig.getRemotePort());
		assertEquals(uploadConfig.getStatus(), foundUploadConfig.getStatus());
	}

	@Test
	public void testFindFileTransferConfig() throws Exception {
		FileTransferConfig downloadConfig = getDownloadFileTransferConfigData();
		Long id = getFileModule().createFileTransferConfig(downloadConfig);

		FileTransferConfig fetchedDownloadConfig = getFileModule().findFileTransferConfig(id);
		assertNotNull(fetchedDownloadConfig);
		assertEquals(downloadConfig, fetchedDownloadConfig);
	}

	@Test
	public void testUpdateFileTransferConfig() throws Exception {
		FileTransferConfig downloadConfig = getDownloadFileTransferConfigData();
		Long id = getFileModule().createFileTransferConfig(downloadConfig);

		FileTransferConfig fetchedDownloadConfig = getFileModule().findFileTransferConfig(id);
		fetchedDownloadConfig.setDeleteSourceOnTransfer(Boolean.TRUE);
		fetchedDownloadConfig.setLocalPath("c:\\banking\\reports");
		fetchedDownloadConfig.setMaxTransferAttempts(Integer.valueOf(20));
		int updateCount = getFileModule().updateFileTransferConfig(fetchedDownloadConfig);
		assertEquals(1, updateCount);

		FileTransferConfig updatedDownloadConfig = getFileModule().findFileTransferConfig(id);
		assertFalse(downloadConfig.equals(updatedDownloadConfig));
		assertEquals(fetchedDownloadConfig, updatedDownloadConfig);
	}

	@Test
	public void testDeleteFileTransferConfig() throws Exception {
		FileTransferConfig downloadConfig = getDownloadFileTransferConfigData();
		Long id = getFileModule().createFileTransferConfig(downloadConfig);
		int updateCount = getFileModule().deleteFileTransferConfig(id);
		assertEquals(1, updateCount);

		FileTransferConfigQuery query = new FileTransferConfigQuery();
		query.id(id);
		List<FileTransferConfig> list = getFileModule().findFileTransferConfigs(query);
		assertEquals(0, list.size());
	}

	@Test
	public void testCreateBatchFileDefinition() throws Exception {
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		Long batchFileDefinitionId
				= getFileModule().createBatchFileDefinition(batchFileDefinitionData);
		assertNotNull(batchFileDefinitionId);
	}

	@Test
	public void testFindBatchFileDefinitions() throws Exception {
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		getFileModule().createBatchFileDefinition(batchFileDefinitionData);

		BatchFileDefinitionQuery query = new BatchFileDefinitionQuery();
		query.orderById();
		query.ignoreEmptyCriteria(true);
		List<BatchFileDefinition> batchFileDefinitionList
				= getFileModule().findBatchFileDefinitions(query);
		assertNotNull(batchFileDefinitionList);
		assertEquals(1, batchFileDefinitionList.size());

		assertEquals(batchFileDefinitionData.getName(), batchFileDefinitionList.get(0).getName());
		assertEquals(batchFileDefinitionData.getDescription(),
				batchFileDefinitionList.get(0).getDescription());
	}

	@Test
	public void testFindBatchFileDefinition() throws Exception {
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		Long id = getFileModule().createBatchFileDefinition(batchFileDefinitionData);

		BatchFileDefinition fetchBatchFileDefinitionData
				= getFileModule().findBatchFileDefinition(id);
		assertNotNull(fetchBatchFileDefinitionData);
		assertEquals(batchFileDefinitionData.getName(), fetchBatchFileDefinitionData.getName());
		assertEquals(batchFileDefinitionData.getDescription(),
				fetchBatchFileDefinitionData.getDescription());
	}

	@Test
	public void testUpdateBatchFileDefinition() throws Exception {
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		Long id = getFileModule().createBatchFileDefinition(batchFileDefinitionData);

		BatchFileDefinition fetchedBatchFileDefinition
				= getFileModule().findBatchFileDefinition(id);
		fetchedBatchFileDefinition.setDescription("Test Definiton Main");
		int updateCount = getFileModule().updateBatchFileDefinition(fetchedBatchFileDefinition);
		assertEquals(1, updateCount);

		BatchFileDefinition updatedBatchFileDefinition
				= getFileModule().findBatchFileDefinition(id);
		assertFalse(batchFileDefinitionData.equals(updatedBatchFileDefinition));
		assertEquals(fetchedBatchFileDefinition, updatedBatchFileDefinition);
	}

	@Test
	public void testDeleteBatchFileDefinition() throws Exception {
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		Long id = getFileModule().createBatchFileDefinition(batchFileDefinitionData);
		int updateCount = getFileModule().deleteBatchFileDefinition(id);
		assertEquals(1, updateCount);

		BatchFileDefinitionQuery query = new BatchFileDefinitionQuery();
		query.id(id);
		List<BatchFileDefinition> list = getFileModule().findBatchFileDefinitions(query);
		assertEquals(0, list.size());
	}

	@Test
	public void testCreateBatchFileReadConfig() throws Exception {
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		Long batchFileDefinitionId
				= getFileModule().createBatchFileDefinition(batchFileDefinitionData);
		BatchFileReadConfig batchFileReadConfigData
				= getBatchFileReadConfigData(batchFileDefinitionId);
		Long batchUploadConfigId
				= getFileModule().createBatchFileReadConfig(batchFileReadConfigData);
		assertNotNull(batchUploadConfigId);
	}

	@Test
	public void testFindBatchFileReadConfigs() throws Exception {
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		Long batchFileDefinitionId
				= getFileModule().createBatchFileDefinition(batchFileDefinitionData);
		BatchFileReadConfig batchFileReadConfigData
				= getBatchFileReadConfigData(batchFileDefinitionId);
		getFileModule().createBatchFileReadConfig(batchFileReadConfigData);

		BatchFileReadConfigQuery query = new BatchFileReadConfigQuery();
		query.orderById();
		query.ignoreEmptyCriteria(true);
		List<BatchFileReadConfig> batchUploadConfigList
				= getFileModule().findBatchFileReadConfigs(query);
		assertNotNull(batchUploadConfigList);
		assertEquals(1, batchUploadConfigList.size());

		BatchFileReadConfig foundBatchFileReadConfigData = batchUploadConfigList.get(0);
		assertEquals(batchFileReadConfigData.getBatchFileDefinitionId(),
				foundBatchFileReadConfigData.getBatchFileDefinitionId());
		assertEquals(batchFileReadConfigData.getReadProcessor(),
				foundBatchFileReadConfigData.getReadProcessor());
		assertEquals(batchFileReadConfigData.getConstraintAction(),
				foundBatchFileReadConfigData.getConstraintAction());
		assertEquals(batchFileReadConfigData.getDescription(),
				foundBatchFileReadConfigData.getDescription());
		assertEquals(batchFileReadConfigData.getFileReader(),
				foundBatchFileReadConfigData.getFileReader());
		assertEquals(batchFileReadConfigData.getName(), foundBatchFileReadConfigData.getName());
		assertEquals(batchFileReadConfigData.getStatus(), foundBatchFileReadConfigData.getStatus());
	}

	@Test
	public void testFindBatchFileReadConfig() throws Exception {
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		Long batchFileDefinitionId
				= getFileModule().createBatchFileDefinition(batchFileDefinitionData);
		BatchFileReadConfig batchFileReadConfigData
				= getBatchFileReadConfigData(batchFileDefinitionId);
		Long id = getFileModule().createBatchFileReadConfig(batchFileReadConfigData);

		BatchFileReadConfig fetchedDownloadConfig = getFileModule().findBatchFileReadConfig(id);
		assertNotNull(fetchedDownloadConfig);
		assertEquals(batchFileReadConfigData, fetchedDownloadConfig);
	}

	@Test
	public void testUpdateBatchFileReadConfig() throws Exception {
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		Long batchFileDefinitionId
				= getFileModule().createBatchFileDefinition(batchFileDefinitionData);
		BatchFileReadConfig batchFileReadConfigData
				= getBatchFileReadConfigData(batchFileDefinitionId);
		Long id = getFileModule().createBatchFileReadConfig(batchFileReadConfigData);

		BatchFileReadConfig fetchedBatchFileReadConfig
				= getFileModule().findBatchFileReadConfig(id);
		fetchedBatchFileReadConfig.setDescription("Main Sample Config");
		int updateCount = getFileModule().updateBatchFileReadConfig(fetchedBatchFileReadConfig);
		assertEquals(1, updateCount);

		BatchFileReadConfig updatedBatchFileReadConfig
				= getFileModule().findBatchFileReadConfig(id);
		assertFalse(batchFileReadConfigData.equals(updatedBatchFileReadConfig));
		assertEquals(fetchedBatchFileReadConfig, updatedBatchFileReadConfig);
	}

	@Test
	public void testDeleteBatchFileReadConfig() throws Exception {
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		Long batchFileDefinitionId
				= getFileModule().createBatchFileDefinition(batchFileDefinitionData);
		BatchFileReadConfig batchFileReadConfigData
				= getBatchFileReadConfigData(batchFileDefinitionId);
		Long id = getFileModule().createBatchFileReadConfig(batchFileReadConfigData);
		int updateCount = getFileModule().deleteBatchFileReadConfig(id);
		assertEquals(1, updateCount);

		BatchFileReadConfigQuery query = new BatchFileReadConfigQuery();
		query.id(id);
		List<BatchFileReadConfig> list = getFileModule().findBatchFileReadConfigs(query);
		assertEquals(0, list.size());
	}

	@Test
	public void testGetBatchFileReadInputParameters() throws Exception {
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		Long batchFileDefinitionId
				= getFileModule().createBatchFileDefinition(batchFileDefinitionData);
		BatchFileReadConfig batchFileReadConfigData
				= getBatchFileReadConfigData(batchFileDefinitionId);
		Long batchConfigId = getFileModule().createBatchFileReadConfig(batchFileReadConfigData);

		BatchFileReadInputParameters buip
				= getFileModule().getBatchFileReadInputParameters(batchConfigId);
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
		FileModule fileModule
				= (FileModule) getComponent(FileModuleNameConstants.FILEBUSINESSMODULE);
		FileTransferConfig downloadConfig = getDownloadFileTransferConfigData();
		fileModule.createFileTransferConfig(downloadConfig);

		TaskMonitor tm = launchFileTransferTask("downloadFileTransfer", new Date(), true);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		FileInboxQuery query = new FileInboxQuery();
		query.fileTransferConfigName("downloadFileTransfer");
		query.order("filename");
		List<FileInbox> fileInboxList = fileModule.findFileInboxItems(query);
		assertNotNull(fileInboxList);
		assertEquals(3, fileInboxList.size());

		FileInbox fileInboxData = fileInboxList.get(0);
		assertEquals("butterfly.png", fileInboxData.getFilename());
		assertEquals(FileInboxReadStatus.NOT_READ, fileInboxData.getReadStatus());
		assertEquals(FileInboxStatus.RECEIVED, fileInboxData.getStatus());

		fileInboxData = fileInboxList.get(1);
		assertEquals("customer.html", fileInboxData.getFilename());
		assertEquals(FileInboxReadStatus.NOT_READ, fileInboxData.getReadStatus());
		assertEquals(FileInboxStatus.RECEIVED, fileInboxData.getStatus());

		fileInboxData = fileInboxList.get(2);
		assertEquals("hello.txt", fileInboxData.getFilename());
		assertEquals(FileInboxReadStatus.NOT_READ, fileInboxData.getReadStatus());
		assertEquals(FileInboxStatus.RECEIVED, fileInboxData.getStatus());
	}

	@Test(timeout = 4000)
	public void testFindFileInboxItem() throws Exception {
		FileModule fileModule
				= (FileModule) getComponent(FileModuleNameConstants.FILEBUSINESSMODULE);
		FileTransferConfig downloadConfig = getDownloadFileTransferConfigData();
		fileModule.createFileTransferConfig(downloadConfig);

		TaskMonitor tm = launchFileTransferTask("downloadFileTransfer", new Date(), true);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		FileInboxQuery query = new FileInboxQuery();
		query.fileTransferConfigName("downloadFileTransfer");
		List<FileInbox> fileInboxList = fileModule.findFileInboxItems(query);

		FileInbox fileInboxData = fileInboxList.get(0);
		FileInbox fetchedFileInboxData = fileModule.findFileInboxItem(fileInboxData.getId());
		assertEquals(fileInboxData, fetchedFileInboxData);
	}

	@Test(timeout = 4000)
	public void testUpdateFileInboxItemsReadStatus() throws Exception {
		FileModule fileModule
				= (FileModule) getComponent(FileModuleNameConstants.FILEBUSINESSMODULE);
		FileTransferConfig downloadConfig = getDownloadFileTransferConfigData();
		fileModule.createFileTransferConfig(downloadConfig);

		TaskMonitor tm = launchFileTransferTask("downloadFileTransfer", new Date(), true);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		FileInboxQuery query = new FileInboxQuery();
		query.fileTransferConfigName("downloadFileTransfer");
		query.setFilenameIn(Arrays.asList(new String[] { "butterfly.png", "hello.txt" }));
		fileModule.updateFileInboxItemsReadStatus(query, FileInboxReadStatus.READ);

		query = new FileInboxQuery();
		query.fileTransferConfigName("downloadFileTransfer");
		query.order("filename");
		List<FileInbox> fileInboxList = fileModule.findFileInboxItems(query);

		FileInbox fileInboxData = fileInboxList.get(0);
		assertEquals("butterfly.png", fileInboxData.getFilename());
		assertEquals(FileInboxReadStatus.READ, fileInboxData.getReadStatus());
		assertEquals(FileInboxStatus.RECEIVED, fileInboxData.getStatus());

		fileInboxData = fileInboxList.get(1);
		assertEquals("customer.html", fileInboxData.getFilename());
		assertEquals(FileInboxReadStatus.NOT_READ, fileInboxData.getReadStatus());
		assertEquals(FileInboxStatus.RECEIVED, fileInboxData.getStatus());

		fileInboxData = fileInboxList.get(2);
		assertEquals("hello.txt", fileInboxData.getFilename());
		assertEquals(FileInboxReadStatus.READ, fileInboxData.getReadStatus());
		assertEquals(FileInboxStatus.RECEIVED, fileInboxData.getStatus());
	}

	@Test(timeout = 4000)
	public void testFindFileOutboxItems() throws Exception {
		FileModule fileModule
				= (FileModule) getComponent(FileModuleNameConstants.FILEBUSINESSMODULE);
		FileTransferConfig uploadConfig = getUploadFileTransferConfigData();
		fileModule.createFileTransferConfig(uploadConfig);

		TaskMonitor tm = launchFileTransferTask("uploadFileTransfer", new Date(), true);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		FileOutboxQuery query = new FileOutboxQuery();
		query.fileTransferConfigName("uploadFileTransfer");
		query.order("filename");
		List<FileOutbox> fileOutboxList = fileModule.findFileOutboxItems(query);
		assertNotNull(fileOutboxList);
		assertEquals(2, fileOutboxList.size());

		FileOutbox fileOutboxData = fileOutboxList.get(0);
		assertEquals("doctor.png", fileOutboxData.getFilename());
		assertEquals(FileOutboxStatus.SENT, fileOutboxData.getStatus());

		fileOutboxData = fileOutboxList.get(1);
		assertEquals("transactions.txt", fileOutboxData.getFilename());
		assertEquals(FileOutboxStatus.SENT, fileOutboxData.getStatus());
	}

	@Test(timeout = 4000)
	public void testFindFileOutboxItem() throws Exception {
		FileModule fileModule
				= (FileModule) getComponent(FileModuleNameConstants.FILEBUSINESSMODULE);
		FileTransferConfig uploadConfig = getUploadFileTransferConfigData();
		fileModule.createFileTransferConfig(uploadConfig);

		TaskMonitor tm = launchFileTransferTask("uploadFileTransfer", new Date(), true);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		FileOutboxQuery query = new FileOutboxQuery();
		query.fileTransferConfigName("uploadFileTransfer");
		List<FileOutbox> fileInboxList = fileModule.findFileOutboxItems(query);

		FileOutbox fileOutboxData = fileInboxList.get(0);
		FileOutbox fetchedFileOutboxData = fileModule.findFileOutboxItem(fileOutboxData.getId());
		assertEquals(fileOutboxData, fetchedFileOutboxData);
	}

	@Test(timeout = 4000)
	public void testStartTestFileTransferConfigTask() throws Exception {
		FileModule fileModule
				= (FileModule) getComponent(FileModuleNameConstants.FILEBUSINESSMODULE);
		FileTransferConfig downloadConfig = getDownloadFileTransferConfigData();
		fileModule.createFileTransferConfig(downloadConfig);
		TaskMonitor tm
				= ((TaskLauncher) getComponent(ApplicationComponents.APPLICATION_TASKLAUNCHER))
						.launchTask(TaskSetup.newBuilder()
								.addTask(FileTransferTaskConstants.FILETRANSFERCONFIGTESTTASK)
								.setParam(FileTransferTaskConstants.FILETRANSFERCONFIGDATA,
										downloadConfig)
								.build());
		assertNotNull(tm);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));
	}

	@Test(timeout = 4000)
	public void testStartUpdateFileTransferListTask() throws Exception {
		FileModule fileModule
				= (FileModule) getComponent(FileModuleNameConstants.FILEBUSINESSMODULE);
		FileTransferConfig downloadConfig = getDownloadFileTransferConfigData();
		FileTransferConfig uploadConfig = getUploadFileTransferConfigData();
		fileModule.createFileTransferConfig(downloadConfig);
		fileModule.createFileTransferConfig(uploadConfig);

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
		FileModule fileModule
				= (FileModule) getComponent(FileModuleNameConstants.FILEBUSINESSMODULE);
		FileTransferConfig downloadConfig = getDownloadFileTransferConfigData();
		FileTransferConfig uploadConfig = getUploadFileTransferConfigData();
		fileModule.createFileTransferConfig(downloadConfig);
		fileModule.createFileTransferConfig(uploadConfig);

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
		BatchFileDefinition batchFileDefinitionData = newBatchFileDefinitionData();
		Long batchFileDefinitionId
				= getFileModule().createBatchFileDefinition(batchFileDefinitionData);
		BatchFileReadConfig batchFileReadConfigData
				= getBatchFileReadConfigData(batchFileDefinitionId);
		Long batchConfigId = getFileModule().createBatchFileReadConfig(batchFileReadConfigData);

		BatchFileReadInputParameters buip
				= getFileModule().getBatchFileReadInputParameters(batchConfigId);
		byte[] file = IOUtils.createInMemoryTextFile("011First Bank          FBN 011000001",
				"032Union Bank          UBN 032000001", "044Access Bank         ACC 044000001");
		buip.setFileBlob(file);
		buip.setParameter("country", "Nigeria");

		TaskMonitor tm
				= ((TaskLauncher) getComponent(ApplicationComponents.APPLICATION_TASKLAUNCHER))
						.launchTask(TaskSetup.newBuilder()
								.addTask(BatchFileReadTaskConstants.BATCHFILEREADTASK)
								.setParam(BatchFileReadTaskConstants.BATCHFILEREADINPUTPARAMS, buip)
								.build());
		assertNotNull(tm);
		waitForTask(tm);
		assertEquals(TaskStatus.COMPLETED, tm.getTaskStatus(0));

		GenericBusinessModule genericBusinessModule = (GenericBusinessModule) this
				.getComponent(ApplicationComponents.APPLICATION_GENERICBUSINESSMODULE);
		List<TestBankData> bankList = genericBusinessModule.listAll(
				new Query<TestBankData>(TestBankData.class).order("id").ignoreEmptyCriteria(true));
		assertNotNull(bankList);
		assertEquals(3, bankList.size());

		TestBankData bankData = bankList.get(0);
		assertEquals("011", bankData.getName());
		assertEquals("First Bank", bankData.getDescription());
		assertEquals("FBN", bankData.getShortName());
		assertEquals("011000001", bankData.getHoRoutingNo());
		assertEquals("Nigeria", bankData.getCountry());

		bankData = bankList.get(1);
		assertEquals("032", bankData.getName());
		assertEquals("Union Bank", bankData.getDescription());
		assertEquals("UBN", bankData.getShortName());
		assertEquals("032000001", bankData.getHoRoutingNo());
		assertEquals("Nigeria", bankData.getCountry());

		bankData = bankList.get(2);
		assertEquals("044", bankData.getName());
		assertEquals("Access Bank", bankData.getDescription());
		assertEquals("ACC", bankData.getShortName());
		assertEquals("044000001", bankData.getHoRoutingNo());
		assertEquals("Nigeria", bankData.getCountry());
	}

	@Override
	protected void doAddSettingsAndDependencies() throws Exception {
		super.doAddSettingsAndDependencies();

		addDependency("test-filetransferserver", TestFileTransferServer.class,
				new Setting("localFilenames", new String[] { "doctor.png", "transactions.txt" }),
				new Setting("remoteFilenames",
						new String[] { "butterfly.png", "hello.txt", "customer.html" }));
	}

	@Override
	protected void onSetup() throws Exception {

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onTearDown() throws Exception {
		deleteAll(FileInbox.class, FileOutbox.class, BatchFileReadConfig.class,
				BatchFileDefinition.class, FileTransferConfig.class);
	}

	private FileTransferConfig getUploadFileTransferConfigData() {
		FileTransferConfig fileTransferConfigData = new FileTransferConfig();
		fileTransferConfigData.setName("uploadFileTransfer");
		fileTransferConfigData.setDeleteSourceOnTransfer(Boolean.FALSE);
		fileTransferConfigData.setDescription("EOD Report DataTransfer");
		fileTransferConfigData.setDirection(FileTransferDirection.UPLOAD);
		fileTransferConfigData.setFileTransferPolicy("wildcard-filetransferpolicy");
		fileTransferConfigData.setFileTransferServer("test-filetransferserver");
		fileTransferConfigData.setLocalPath("c:\\data\\reports");
		fileTransferConfigData.setMaxTransferAttempts(Integer.valueOf(10));
		fileTransferConfigData.setRemoteHost("192.168.1.1");
		fileTransferConfigData.setRemotePath("/eodreports");
		fileTransferConfigData.setStatus(RecordStatus.ACTIVE);
		return fileTransferConfigData;
	}

	private FileTransferConfig getDownloadFileTransferConfigData() {
		FileTransferConfig fileTransferConfigData = new FileTransferConfig();
		fileTransferConfigData.setName("downloadFileTransfer");
		fileTransferConfigData.setDeleteSourceOnTransfer(Boolean.FALSE);
		fileTransferConfigData.setDescription("BPXF File DataTransfer");
		fileTransferConfigData.setDirection(FileTransferDirection.DOWNLOAD);
		fileTransferConfigData.setFileTransferPolicy("wildcard-filetransferpolicy");
		fileTransferConfigData.setFileTransferServer("test-filetransferserver");
		fileTransferConfigData.setLocalPath("c:\\truncation\\bpxf");
		fileTransferConfigData.setMaxTransferAttempts(Integer.valueOf(4));
		fileTransferConfigData.setRemoteHost("10.0.2.15");
		fileTransferConfigData.setRemotePath("/incoming/bfxf");
		fileTransferConfigData.setStatus(RecordStatus.ACTIVE);
		return fileTransferConfigData;
	}

	private BatchFileDefinition newBatchFileDefinitionData() {
		BatchFileDefinition batchFileDefinitionData = new BatchFileDefinition();
		batchFileDefinitionData.setName("fil-001");
		batchFileDefinitionData.setDescription("Test Definition");

		List<BatchFileFieldDefinition> fieldDefList = new ArrayList<BatchFileFieldDefinition>();
		fieldDefList.add(newBatchFileFieldDefinition("name", 3));
		fieldDefList.add(newBatchFileFieldDefinition("description", 20));
		fieldDefList.add(newBatchFileFieldDefinition("shortName", 4));
		fieldDefList.add(newBatchFileFieldDefinition("hoRoutingNo", 9));
		batchFileDefinitionData.setFieldDefList(fieldDefList);

		return batchFileDefinitionData;
	}

	private BatchFileFieldDefinition newBatchFileFieldDefinition(String name, int length) {
		BatchFileFieldDefinition batchFileFieldDefinitionData = new BatchFileFieldDefinition();
		batchFileFieldDefinitionData.setName(name);
		batchFileFieldDefinitionData.setLength(length);
		batchFileFieldDefinitionData.setTrim(true);
		return batchFileFieldDefinitionData;
	}

	private BatchFileReadConfig getBatchFileReadConfigData(Long batchFileDefinitionId) {
		BatchFileReadConfig batchFileReadConfigData = new BatchFileReadConfig();
		batchFileReadConfigData.setBatchFileDefinitionId(batchFileDefinitionId);
		batchFileReadConfigData.setReadProcessor("test-bankbatchfilereadprocessor");
		batchFileReadConfigData.setFileReader("fixedlength-batchfilereader");
		batchFileReadConfigData.setConstraintAction(ConstraintAction.FAIL);
		batchFileReadConfigData.setDescription("Bank Batch Config");
		batchFileReadConfigData.setName("BankBatchConfig");
		return batchFileReadConfigData;
	}

	private FileModule getFileModule() throws Exception {
		return (FileModule) getComponent(FileModuleNameConstants.FILEBUSINESSMODULE);
	}

	private TaskMonitor launchFileTransferTask(String configName, Date workingDt,
			boolean updateList) throws Exception {
		return ((TaskLauncher) getComponent(ApplicationComponents.APPLICATION_TASKLAUNCHER))
				.launchTask(TaskSetup.newBuilder()
						.addTask(FileTransferTaskConstants.FILETRANSFERTASK)
						.setParam(FileTransferTaskConstants.FILETRANSFERCONFIGNAME, configName)
						.setParam(FileTransferTaskConstants.UPDATEFILEBOX, updateList)
						.setParam(FileTransferTaskConstants.WORKINGDT, workingDt).build());
	}

	private TaskMonitor launchUpdateFileTransferListTask(String configName, Date workingDt)
			throws Exception {
		return ((TaskLauncher) getComponent(ApplicationComponents.APPLICATION_TASKLAUNCHER))
				.launchTask(TaskSetup.newBuilder()
						.addTask(FileTransferTaskConstants.FILETRANSFERLISTUPDATETASK)
						.setParam(FileTransferTaskConstants.FILETRANSFERCONFIGNAME, configName)
						.setParam(FileTransferTaskConstants.WORKINGDT, workingDt).build());
	}
}
