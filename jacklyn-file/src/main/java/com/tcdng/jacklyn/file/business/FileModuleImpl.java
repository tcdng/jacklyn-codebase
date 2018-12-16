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
package com.tcdng.jacklyn.file.business;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessModule;
import com.tcdng.jacklyn.file.constants.BatchFileReadTaskConstants;
import com.tcdng.jacklyn.file.constants.FileModuleErrorConstants;
import com.tcdng.jacklyn.file.constants.FileModuleNameConstants;
import com.tcdng.jacklyn.file.constants.FileTransferTaskConstants;
import com.tcdng.jacklyn.file.data.BatchFileReadConfigLargeData;
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
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.TransactionAttribute;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.batch.BatchFileConfig;
import com.tcdng.unify.core.batch.BatchFileReadProcessor;
import com.tcdng.unify.core.batch.BatchFileReadProcessorInputConstants;
import com.tcdng.unify.core.batch.BatchFileReadProcessorOutputConstants;
import com.tcdng.unify.core.batch.BatchFileReader;
import com.tcdng.unify.core.business.BusinessLogicOutput;
import com.tcdng.unify.core.data.Inputs;
import com.tcdng.unify.core.file.FileFilter;
import com.tcdng.unify.core.file.FileInfo;
import com.tcdng.unify.core.file.FileTransferInfo;
import com.tcdng.unify.core.file.FileTransferServer;
import com.tcdng.unify.core.operation.Update;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of file business module.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(FileModuleNameConstants.FILEBUSINESSMODULE)
public class FileModuleImpl extends AbstractJacklynBusinessModule implements FileModule {

    private static final String BATCHFILE_READ_CONFIG = "batchFileReadConfig";

    @Override
    public List<FileTransferConfig> findFileTransferConfigs(FileTransferConfigQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public FileTransferConfig findFileTransferConfig(Long id) throws UnifyException {
        return db().find(FileTransferConfig.class, id);
    }

    @Override
    public Long createFileTransferConfig(FileTransferConfig fileTransferConfig) throws UnifyException {
        return (Long) db().create(fileTransferConfig);
    }

    @Override
    public int updateFileTransferConfig(FileTransferConfig fileTransferConfig) throws UnifyException {
        return db().updateByIdVersion(fileTransferConfig);
    }

    @Override
    public int deleteFileTransferConfig(Long id) throws UnifyException {
        return db().delete(FileTransferConfig.class, id);
    }

    @Override
    public byte[] readRemoteBlock(Long fileTransferConfigId, Date workingDt, String filename, long fileIndex,
            int length) throws UnifyException {
        FileTransferConfig fileTransferConfig = db().list(FileTransferConfig.class, fileTransferConfigId);
        FileTransferInfo fileTransferInfo = getFileTransferInfo(fileTransferConfig, workingDt);
        FileTransferServer fileServer = (FileTransferServer) this
                .getComponent(fileTransferConfig.getFileTransferServer());
        if (fileServer.remoteFileExists(fileTransferInfo, filename)) {
            return fileServer.readRemoteBlock(fileTransferInfo, filename, fileIndex, length);
        }
        return null;
    }

    @Override
    public List<FileInbox> findFileInboxItems(FileInboxQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public FileInbox findFileInboxItem(Long id) throws UnifyException {
        return db().list(FileInbox.class, id);
    }

    @Override
    public int updateFileInboxItemsReadStatus(FileInboxQuery query, FileInboxReadStatus readStatus)
            throws UnifyException {
        return db().updateAll(query, new Update().add("readStatus", readStatus));
    }

    @Override
    public List<FileOutbox> findFileOutboxItems(FileOutboxQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public FileOutbox findFileOutboxItem(Long id) throws UnifyException {
        return db().list(FileOutbox.class, id);
    }

    @Taskable(name = FileTransferTaskConstants.FILETRANSFERCONFIGTESTTASK,
            description = "Test File DataTransfer Configuration Task",
            parameters = { @Parameter(name = FileTransferTaskConstants.FILETRANSFERCONFIGDATA,
                    type = FileTransferConfig.class) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    public boolean executeTestFileTransferConfigTask(TaskMonitor taskMonitor, FileTransferConfig fileTransferConfig)
            throws UnifyException {
        FileTransferInfo fileTransferInfo = getFileTransferInfo(fileTransferConfig, null);
        FileTransferServer fileServer = (FileTransferServer) this
                .getComponent(fileTransferConfig.getFileTransferServer());

        addTaskMessage(taskMonitor, "file.taskmonitor.testinglocalpath");
        fileServer.getLocalFileList(fileTransferInfo);

        addTaskMessage(taskMonitor, "file.taskmonitor.testingremotepath");
        fileServer.getRemoteFileList(fileTransferInfo);
        addTaskMessage(taskMonitor, "file.taskmonitor.testingsuccess");
        return true;
    }

    @Transactional(TransactionAttribute.REQUIRES_NEW)
    @Synchronized("updatefiletransferlist-lock")
    @Taskable(name = FileTransferTaskConstants.FILETRANSFERLISTUPDATETASK,
            description = "File DataTransfer List Update Task",
            parameters = { @Parameter(name = FileTransferTaskConstants.FILETRANSFERCONFIGNAME,
                    description = "$m{file.filetransfertask.parameter.transferconfig}",
                    editor = "!ui-select list:filetransferconfiglist listKey:name blankOption:$s{}", mandatory = true),
                    @Parameter(name = FileTransferTaskConstants.WORKINGDT, type = Date.class) },
            schedulable = true)
    public int executeUpdateFileTransferListTask(TaskMonitor taskMonitor, String fileTransferConfigName, Date workingDt)
            throws UnifyException {
        int updatedCount = 0;
        FileTransferConfig fileTransferConfig = getFileTransferConfig(fileTransferConfigName);
        FileTransferServer fileTransferServer = (FileTransferServer) this
                .getComponent(fileTransferConfig.getFileTransferServer());
        FileTransferPolicy fileTransferPolicy = (FileTransferPolicy) this
                .getComponent(fileTransferConfig.getFileTransferPolicy());
        FileTransferInfo fileTransferInfo = getFileTransferInfo(fileTransferConfig, workingDt);
        if (FileTransferDirection.UPLOAD.equals(fileTransferConfig.getDirection())) {
            List<FileInfo> fileInfoList = fileTransferServer.getLocalFileList(fileTransferInfo);
            if (!fileInfoList.isEmpty()) {
                int size = fileInfoList.size();
                List<String> filenames = new ArrayList<String>(size);
                for (int i = 0; i < size; i++) {
                    filenames.add(fileInfoList.get(i).getFilename());
                }

                Set<String> oldFilenameSet = db().valueSet(String.class, "filename",
                        new FileOutboxQuery().setFilenameIn(filenames));

                FileOutbox fileOutbox = new FileOutbox();
                fileOutbox.setFileTransferConfigId(fileTransferConfig.getId());
                fileOutbox.setStatus(FileOutboxStatus.NOT_SENT);
                String semaphoreSuffix = fileTransferPolicy.getSourceSemaphoreSuffix();
                for (FileInfo fileInfo : fileInfoList) {
                    if (fileInfo.isFile() && !fileInfo.isHidden() && !oldFilenameSet.contains(fileInfo.getFilename())) {
                        if (!StringUtils.isBlank(semaphoreSuffix)) {
                            File semaphoreFile = new File(fileInfo.getAbsolutePath() + semaphoreSuffix);
                            if (!semaphoreFile.isFile()) {
                                // No semaphore file. File not ready for upload
                                continue;
                            }
                        }
                        fileOutbox.setFilename(fileInfo.getFilename());
                        fileOutbox.setFileLength(fileInfo.getLength());
                        db().create(fileOutbox);
                        updatedCount++;
                    }
                }
            }
        } else {
            List<FileInfo> fileInfoList = fileTransferServer.getRemoteFileList(fileTransferInfo);
            if (!fileInfoList.isEmpty()) {
                int size = fileInfoList.size();
                List<String> filenames = new ArrayList<String>(size);
                for (int i = 0; i < size; i++) {
                    filenames.add(fileInfoList.get(i).getFilename());
                }

                Set<String> oldFilenameSet = new HashSet<String>(
                        db().valueList(String.class, "filename", new FileInboxQuery().setFilenameIn(filenames)));

                FileInbox fileInbox = new FileInbox();
                fileInbox.setFileTransferConfigId(fileTransferConfig.getId());
                fileInbox.setReadStatus(FileInboxReadStatus.NOT_READ);
                fileInbox.setStatus(FileInboxStatus.NOT_RECEIVED);
                String semaphoreSuffix = fileTransferPolicy.getSourceSemaphoreSuffix();
                for (FileInfo fileInfo : fileInfoList) {
                    if (fileInfo.isFile() && !fileInfo.isHidden() && !oldFilenameSet.contains(fileInfo.getFilename())) {
                        if (!StringUtils.isBlank(semaphoreSuffix)) {
                            if (!fileTransferServer.remoteFileExists(fileTransferInfo,
                                    fileInfo.getFilename() + semaphoreSuffix)) {
                                // No semaphore file. File not ready for
                                // download
                                continue;
                            }
                        }
                        fileInbox.setFilename(fileInfo.getFilename());
                        fileInbox.setFileLength(fileInfo.getLength());
                        db().create(fileInbox);
                        updatedCount++;
                    }
                }
            }
        }
        return updatedCount;
    }

    @Synchronized("executefiletransfer-lock")
    @Taskable(name = FileTransferTaskConstants.FILETRANSFERTASK, description = "File DataTransfer Task", parameters = {
            @Parameter(name = FileTransferTaskConstants.FILETRANSFERCONFIGNAME,
                    description = "$m{file.filetransfertask.parameter.transferconfig}",
                    editor = "!ui-select list:filetransferconfiglist listKey:name blankOption:$s{}", mandatory = true),
            @Parameter(name = FileTransferTaskConstants.WORKINGDT, type = Date.class),
            @Parameter(name = FileTransferTaskConstants.UPDATEFILEBOX,
                    description = "$m{file.filetransfertask.parameter.updateboxflag}",
                    editor = "!ui-select list:booleanlist blankOption:$s{}", type = boolean.class, mandatory = true) },
            schedulable = true)
    public int executeFileTransferTask(TaskMonitor taskMonitor, String fileTransferConfigName, Date workingDt,
            boolean updateFilebox) throws UnifyException {
        int transferCount = 0;
        if (updateFilebox) {
            executeUpdateFileTransferListTask(taskMonitor, fileTransferConfigName, workingDt);
        }

        FileTransferConfig fileTransferConfig = getFileTransferConfig(fileTransferConfigName);
        FileTransferInfo fileTransferInfo = getFileTransferInfo(fileTransferConfig, workingDt);
        FileTransferServer fileServer = (FileTransferServer) this
                .getComponent(fileTransferConfig.getFileTransferServer());
        FileTransferPolicy fileTransferPolicy = (FileTransferPolicy) this
                .getComponent(fileTransferConfig.getFileTransferPolicy());
        FileFilter fileFilter = new FileFilter(fileTransferPolicy.getFilePrefixes(workingDt),
                fileTransferPolicy.getFileSuffixes(workingDt), true);
        if (FileTransferDirection.UPLOAD.equals(fileTransferConfig.getDirection())) {
            List<FileOutbox> fileOutboxList = db().listAll(new FileOutboxQuery().status(FileOutboxStatus.NOT_SENT)
                    .fileTransferConfigId(fileTransferConfig.getId()).createdOn(workingDt).order("filename"));

            List<FileOutbox> upFileOutboxList = new ArrayList<FileOutbox>();
            for (FileOutbox fileOutbox : fileOutboxList) {
                if (fileFilter.accept(fileOutbox.getFilename(), true)) {
                    upFileOutboxList.add(fileOutbox);
                }
            }

            if (!fileServer.remoteDirectoryExists(fileTransferInfo)) {
                fileServer.createRemoteDirectory(fileTransferInfo);
            }

            String semaphoreSuffix = fileTransferPolicy.getTargetSemaphoreSuffix();
            for (FileOutbox fileOutbox : upFileOutboxList) {
                if (!taskMonitor.isCanceled()
                        && uploadFile(taskMonitor, fileServer, fileTransferInfo, fileOutbox, semaphoreSuffix)) {
                    transferCount++;
                }
            }
            addTaskMessage(taskMonitor, "file.taskmonitor.uploadcompleted");
        } else {
            List<FileInbox> fileInboxList = db().listAll(new FileInboxQuery().status(FileInboxStatus.NOT_RECEIVED)
                    .fileTransferConfigId(fileTransferConfig.getId()).createdOn(workingDt).order("filename"));

            List<FileInbox> downFileInboxList = new ArrayList<FileInbox>();
            for (FileInbox fileInbox : fileInboxList) {
                if (fileFilter.accept(fileInbox.getFilename(), true)) {
                    downFileInboxList.add(fileInbox);
                }
            }

            IOUtils.ensureDirectoryExists(fileTransferInfo.getLocalPath());
            String semaphoreSuffix = fileTransferPolicy.getTargetSemaphoreSuffix();
            for (FileInbox fileInbox : downFileInboxList) {
                if (!taskMonitor.isCanceled()
                        && downloadFile(taskMonitor, fileServer, fileTransferInfo, fileInbox, semaphoreSuffix)) {
                    transferCount++;
                }
            }
            addTaskMessage(taskMonitor, "file.taskmonitor.downloadcompleted");
        }
        return transferCount;
    }

    @Taskable(name = BatchFileReadTaskConstants.BATCHFILEREADTASK, description = "Batch Upload Task",
            parameters = { @Parameter(name = BatchFileReadTaskConstants.BATCHFILEREADINPUTPARAMS,
                    type = BatchFileReadInputParameters.class) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    public int executeBatchUploadTask(TaskMonitor taskMonitor,
            BatchFileReadInputParameters batchFileReadInputParameters) throws UnifyException {
        BatchFileReadConfig batchFileReadConfig = getBatchUploadConfig(batchFileReadInputParameters.getName());

        Map<String, Object> inputParameters = getBatchFileReadConfigParameters(batchFileReadConfig);
        inputParameters
                .putAll(DataUtils.getInputHolderNameValueMap(batchFileReadInputParameters.getInputParameterList()));

        Object[] fileObjects = new Object[1];
        fileObjects[0] = batchFileReadInputParameters.getFile();
        if (fileObjects[0] == null) {
            fileObjects[0] = batchFileReadInputParameters.getFileBlob();
        }
        inputParameters.put(BatchFileReadProcessorInputConstants.FILEOBJECTS, fileObjects);

        BusinessLogicOutput output = executeBusinessLogic(taskMonitor, batchFileReadConfig.getReadProcessor(),
                inputParameters);

        taskMonitor.getCurrentTaskOutput().setResult(BatchFileReadProcessorOutputConstants.BATCHFILEREADRESULT,
                output.getResult(Object.class, BatchFileReadProcessorOutputConstants.BATCHFILEREADRESULT));
        return 0;
    }

    @Override
    public Map<String, Object> getBatchFileReadConfigExecutionParameters(Long batchFileReadConfigId)
            throws UnifyException {
        return getBatchFileReadConfigParameters(db().list(BatchFileReadConfig.class, batchFileReadConfigId));
    }

    @Transactional(TransactionAttribute.REQUIRES_NEW)
    public boolean uploadFile(TaskMonitor taskMonitor, FileTransferServer fileServer, FileTransferInfo fileTransferInfo,
            FileOutbox fileOutbox, String semaphoreSuffix) throws UnifyException {
        int uploadAttempts = fileOutbox.getUploadAttempts() + 1;
        try {
            String filename = fileOutbox.getFilename();
            addTaskMonitorSessionMessage(taskMonitor, "file.taskmonitor.uploading", filename);
            fileServer.uploadFile(fileTransferInfo, filename, filename);

            if (!StringUtils.isBlank(semaphoreSuffix)) {
                fileServer.createRemoteFile(fileTransferInfo, filename + semaphoreSuffix);
            }

            db().updateAll(new FileOutboxQuery().id(fileOutbox.getId()),
                    new Update().add("uploadAttempts", uploadAttempts).add("uploadedOn", new Date()).add("status",
                            FileOutboxStatus.SENT));
            return true;
        } catch (UnifyException e) {
            Update update = new Update().add("uploadAttempts", uploadAttempts);
            if (uploadAttempts >= fileOutbox.getMaxTransferAttempts()) {
                update.add("status", FileOutboxStatus.ABORTED);
            }
            db().updateAll(new FileOutboxQuery().id(fileOutbox.getId()), update);
            taskMonitor.addException(e);
            logError(e);
        }
        return false;
    }

    @Transactional(TransactionAttribute.REQUIRES_NEW)
    public boolean downloadFile(TaskMonitor taskMonitor, FileTransferServer fileServer,
            FileTransferInfo fileTransferInfo, FileInbox fileInbox, String semaphoreSuffix) throws UnifyException {
        int downloadAttempts = fileInbox.getDownloadAttempts() + 1;
        try {
            String filename = fileInbox.getFilename();
            addTaskMonitorSessionMessage(taskMonitor, "file.taskmonitor.downloading", filename);
            fileServer.downloadFile(fileTransferInfo, filename, filename);

            if (!StringUtils.isBlank(semaphoreSuffix)) {
                fileServer.createLocalFile(fileTransferInfo, filename + semaphoreSuffix);
            }

            db().updateAll(new FileInboxQuery().id(fileInbox.getId()),
                    new Update().add("downloadAttempts", downloadAttempts).add("downloadedOn", new Date()).add("status",
                            FileInboxStatus.RECEIVED));
            return true;
        } catch (UnifyException e) {
            Update update = new Update().add("downloadAttempts", downloadAttempts);
            if (downloadAttempts >= fileInbox.getMaxTransferAttempts()) {
                update.add("status", FileInboxStatus.ABORTED);
            }
            db().updateAll(new FileInboxQuery().id(fileInbox.getId()), update);
            taskMonitor.addException(e);
            logError(e);
        }
        return false;
    }

    @Override
    public List<BatchFileDefinition> findBatchFileDefinitions(BatchFileDefinitionQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public BatchFileDefinition findBatchFileDefinition(Long id) throws UnifyException {
        return db().find(BatchFileDefinition.class, id);
    }

    @Override
    public Long createBatchFileDefinition(BatchFileDefinition record) throws UnifyException {
        setBatchFileFieldDefinitionIndexes(record);
        return (Long) db().create(record);
    }

    @Override
    public int updateBatchFileDefinition(BatchFileDefinition record) throws UnifyException {
        setBatchFileFieldDefinitionIndexes(record);
        return db().updateByIdVersion(record);
    }

    @Override
    public int deleteBatchFileDefinition(Long id) throws UnifyException {
        return db().delete(BatchFileDefinition.class, id);
    }

    @Override
    public BatchFileConfig getBatchConfig(String batchFileDefinitionName) throws UnifyException {
        BatchFileDefinition batchFileDefinition = db()
                .find(new BatchFileDefinitionQuery().name(batchFileDefinitionName));
        if (batchFileDefinition == null) {
            throw new UnifyException(FileModuleErrorConstants.BATCHFILEDEFINITION_NAME_UNKNOWN,
                    batchFileDefinitionName);
        }

        BatchFileConfig batchFileConfig = new BatchFileConfig(batchFileDefinition.isSkipFirst());
        for (BatchFileFieldDefinition bffda : batchFileDefinition.getFieldDefList()) {
            batchFileConfig.addFieldConfig(bffda.getName(), bffda.getMappedField(), bffda.getFormatter(),
                    bffda.getPadDirection(), bffda.getLength(), bffda.isTrim(), bffda.isPad(),
                    bffda.isUpdateOnConstraint(), bffda.getPadChar());
        }
        return batchFileConfig;
    }

    @Override
    public List<BatchFileReadConfig> findBatchFileReadConfigs(BatchFileReadConfigQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public BatchFileReadConfig findBatchFileReadConfig(Long id) throws UnifyException {
        return db().find(BatchFileReadConfig.class, id);
    }

    @Override
    public BatchFileReadConfigLargeData findBatchFileReadConfigDocument(Long id) throws UnifyException {
        BatchFileReadConfig batchFileReadConfig = db().find(BatchFileReadConfig.class, id);
        Inputs fileReaderParams = getParameterBusinessModule()
                .fetchNormalizedInputs(batchFileReadConfig.getFileReader(), BATCHFILE_READ_CONFIG, id);
        return new BatchFileReadConfigLargeData(batchFileReadConfig, fileReaderParams);
    }

    @Override
    public Long createBatchFileReadConfig(BatchFileReadConfig record) throws UnifyException {
        return (Long) db().create(record);
    }

    @Override
    public Long createBatchFileReadConfig(BatchFileReadConfigLargeData document) throws UnifyException {
        BatchFileReadConfig batchFileReadConfig = document.getData();
        Long batchFileReadConfigId = (Long) db().create(batchFileReadConfig);
        getParameterBusinessModule().updateParameterValues(batchFileReadConfig.getFileReader(), BATCHFILE_READ_CONFIG,
                batchFileReadConfigId, document.getFileReaderParams());
        return batchFileReadConfigId;
    }

    @Override
    public int updateBatchFileReadConfig(BatchFileReadConfig record) throws UnifyException {
        return db().updateByIdVersion(record);
    }

    @Override
    public int updateBatchFileReadConfig(BatchFileReadConfigLargeData document) throws UnifyException {
        BatchFileReadConfig batchFileReadConfig = document.getData();
        int updateCount = db().updateByIdVersion(batchFileReadConfig);
        getParameterBusinessModule().updateParameterValues(batchFileReadConfig.getFileReader(), BATCHFILE_READ_CONFIG,
                batchFileReadConfig.getId(), document.getFileReaderParams());
        return updateCount;
    }

    @Override
    public int deleteBatchFileReadConfig(Long id) throws UnifyException {
        String fileReader = db().value(String.class, "fileReader", new BatchFileReadConfigQuery().id(id));
        getParameterBusinessModule().deleteParameterValues(fileReader, BATCHFILE_READ_CONFIG, id);
        return db().delete(BatchFileReadConfig.class, id);
    }

    @Override
    public BatchFileReadConfigLargeData loadBatchFileReadConfigDocumentValues(BatchFileReadConfigLargeData document)
            throws UnifyException {
        BatchFileReadConfig batchFileReadConfig = document.getData();
        Inputs fileReaderParams = getParameterBusinessModule().fetchNormalizedInputs(
                batchFileReadConfig.getFileReader(), BATCHFILE_READ_CONFIG, batchFileReadConfig.getId());
        return new BatchFileReadConfigLargeData(batchFileReadConfig, fileReaderParams);
    }

    @Override
    public BatchFileReadInputParameters getBatchFileReadInputParameters(Long batchUploadConfigId)
            throws UnifyException {
        BatchFileReadConfig batchFileReadConfig = db().find(BatchFileReadConfig.class, batchUploadConfigId);
        BatchFileReadInputParameters batchFileReadInputParameters = new BatchFileReadInputParameters();
        batchFileReadInputParameters.setName(batchFileReadConfig.getName());
        batchFileReadInputParameters.setDescription(batchFileReadConfig.getDescription());
        batchFileReadInputParameters.addInputParameterList(
                getParameterBusinessModule().fetchInputList(batchFileReadConfig.getReadProcessor()));
        return batchFileReadInputParameters;
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
        List<UnifyComponentConfig> uccList = getComponentConfigs(BatchFileReadProcessor.class);
        uccList.addAll(getComponentConfigs(BatchFileReader.class));
        for (UnifyComponentConfig ucc : uccList) {
            getParameterBusinessModule().defineParameters(ucc.getName(), ucc.getType());
        }
    }

    private FileTransferConfig getFileTransferConfig(String name) throws UnifyException {
        FileTransferConfig fileTransferConfig = db().list(new FileTransferConfigQuery().name(name));
        if (fileTransferConfig == null) {
            throw new UnifyException(FileModuleErrorConstants.FILETRANSFERCONFIG_NAME_UNKNOWN, name);
        }
        return fileTransferConfig;
    }

    private FileTransferInfo getFileTransferInfo(FileTransferConfig fileTransferConfig, Date workingDt)
            throws UnifyException {
        FileTransferPolicy fileTransferPolicy = (FileTransferPolicy) this
                .getComponent(fileTransferConfig.getFileTransferPolicy());
        String serverPath = fileTransferPolicy.getExtendedRemotePath(fileTransferConfig.getRemotePath(),
                fileTransferConfig.getRemoteDateFormat(), workingDt);
        String localPath = fileTransferPolicy.getExtendedLocalPath(fileTransferConfig.getLocalPath(),
                fileTransferConfig.getLocalDateFormat(), workingDt);
        int remotePort = DataUtils.convert(int.class, fileTransferConfig.getRemotePort(), null);
        return FileTransferInfo.newBuilder().remoteHost(fileTransferConfig.getRemoteHost()).remotePort(remotePort)
                .useAuthenticationId(fileTransferConfig.getAuthenticationId())
                .useAuthenticationPassword(fileTransferConfig.getAuthenticationPassword()).remotePath(serverPath)
                .localPath(localPath).filterByPrefixes(fileTransferPolicy.getFilePrefixes(workingDt))
                .filterByExtensions(fileTransferPolicy.getFileSuffixes(workingDt))
                .deleteSourceOnTransfer(Boolean.TRUE.equals(fileTransferConfig.getDeleteSourceOnTransfer())).build();
    }

    private void setBatchFileFieldDefinitionIndexes(BatchFileDefinition batchFileDefinition) throws UnifyException {
        if (batchFileDefinition.getFieldDefList() != null) {
            int i = 0;
            for (BatchFileFieldDefinition batchFileFieldDefinition : batchFileDefinition.getFieldDefList()) {
                batchFileFieldDefinition.setIndex(i);
                i++;
            }
        }
    }

    private BatchFileReadConfig getBatchUploadConfig(String name) throws UnifyException {
        BatchFileReadConfig batchFileReadConfig = db().list(new BatchFileReadConfigQuery().name(name));
        if (batchFileReadConfig == null) {
            throw new UnifyException(FileModuleErrorConstants.BATCHUPLOADCONFIG_NAME_UNKNOWN, name);
        }
        return batchFileReadConfig;
    }

    private Map<String, Object> getBatchFileReadConfigParameters(BatchFileReadConfig batchFileReadConfig)
            throws UnifyException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        BatchFileConfig batchFileConfig = getBatchConfig(batchFileReadConfig.getBatchFileDefinitionName());
        batchFileConfig.setReader(batchFileReadConfig.getFileReader());
        batchFileConfig.setOnConstraint(batchFileReadConfig.getConstraintAction());
        parameters.put(BatchFileReadProcessorInputConstants.BATCHFILECONFIG, batchFileConfig);
        parameters.putAll(getParameterBusinessModule().findParameterTypeValues(batchFileReadConfig.getFileReader(),
                BATCHFILE_READ_CONFIG, batchFileReadConfig.getId()));
        return parameters;
    }
}
