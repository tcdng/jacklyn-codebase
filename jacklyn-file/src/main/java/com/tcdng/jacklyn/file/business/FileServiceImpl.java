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
package com.tcdng.jacklyn.file.business;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.file.constants.BatchFileReadTaskConstants;
import com.tcdng.jacklyn.file.constants.FileModuleErrorConstants;
import com.tcdng.jacklyn.file.constants.FileModuleNameConstants;
import com.tcdng.jacklyn.file.constants.FileTransferTaskConstants;
import com.tcdng.jacklyn.file.data.BatchFileReadDefinitionLargeData;
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
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.TransactionAttribute;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.batch.BatchFileReadConfig;
import com.tcdng.unify.core.batch.BatchFileReadProcessor;
import com.tcdng.unify.core.batch.BatchFileReader;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.Inputs;
import com.tcdng.unify.core.file.FileFilter;
import com.tcdng.unify.core.file.FileInfo;
import com.tcdng.unify.core.file.FileTransferServer;
import com.tcdng.unify.core.file.FileTransferSetup;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of file business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(FileModuleNameConstants.FILESERVICE)
public class FileServiceImpl extends AbstractJacklynBusinessService implements FileService {

    private static final String UPDATE_FILE_TRANSFER_LIST_LOCK = "fil::updatefiletransferlist-lock";

    private static final String EXECUTE_FILE_TRANSFER_LOCK = "fil::executefiletransfer-lock";

    private static final String BATCHFILE_READ_DEFINITION = "batchFileReadDef";

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
        FileTransferSetup fileTransferSetup = getFileTransferSetup(fileTransferConfig, workingDt);
        FileTransferServer fileServer =
                (FileTransferServer) this.getComponent(fileTransferConfig.getFileTransferServer());
        if (fileServer.remoteFileExists(fileTransferSetup, filename)) {
            return fileServer.readRemoteBlock(fileTransferSetup, filename, fileIndex, length);
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

    @Taskable(
            name = FileTransferTaskConstants.FILETRANSFERCONFIGTESTTASK,
            description = "Test File DataTransfer Configuration Task",
            parameters = { @Parameter(
                    name = FileTransferTaskConstants.FILETRANSFERCONFIGDATA, type = FileTransferConfig.class) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    public boolean executeTestFileTransferConfigTask(TaskMonitor taskMonitor, FileTransferConfig fileTransferConfig)
            throws UnifyException {
        FileTransferSetup fileTransferSetup = getFileTransferSetup(fileTransferConfig, null);
        FileTransferServer fileServer =
                (FileTransferServer) this.getComponent(fileTransferConfig.getFileTransferServer());

        addTaskMessage(taskMonitor, "$m{file.taskmonitor.testinglocalpath}");
        fileServer.getLocalFileList(fileTransferSetup);

        addTaskMessage(taskMonitor, "$m{file.taskmonitor.testingremotepath}");
        fileServer.getRemoteFileList(fileTransferSetup);
        addTaskMessage(taskMonitor, "$m{file.taskmonitor.testingsuccess}");
        return true;
    }

    @Transactional(TransactionAttribute.REQUIRES_NEW)
    @Synchronized(UPDATE_FILE_TRANSFER_LIST_LOCK)
    @Taskable(
            name = FileTransferTaskConstants.FILETRANSFERLISTUPDATETASK,
            description = "File DataTransfer List Update Task",
            parameters = { @Parameter(
                    name = FileTransferTaskConstants.FILETRANSFERCONFIGNAME,
                    description = "$m{file.filetransfertask.parameter.transferconfig}",
                    editor = "!ui-select list:filetransferconfiglist listKey:name blankOption:$s{}", mandatory = true),
                    @Parameter(name = FileTransferTaskConstants.WORKINGDT, type = Date.class) },
            schedulable = true)
    public int executeUpdateFileTransferListTask(TaskMonitor taskMonitor, String fileTransferConfigName, Date workingDt)
            throws UnifyException {
        int updatedCount = 0;
        FileTransferConfig fileTransferConfig = getFileTransferConfig(fileTransferConfigName);
        FileTransferServer fileTransferServer =
                (FileTransferServer) this.getComponent(fileTransferConfig.getFileTransferServer());
        FileTransferPolicy fileTransferPolicy =
                (FileTransferPolicy) this.getComponent(fileTransferConfig.getFileTransferPolicy());
        FileTransferSetup fileTransferSetup = getFileTransferSetup(fileTransferConfig, workingDt);
        if (FileTransferDirection.UPLOAD.equals(fileTransferConfig.getDirection())) {
            List<FileInfo> fileInfoList = fileTransferServer.getLocalFileList(fileTransferSetup);
            if (!fileInfoList.isEmpty()) {
                int size = fileInfoList.size();
                List<String> filenames = new ArrayList<String>(size);
                for (int i = 0; i < size; i++) {
                    filenames.add(fileInfoList.get(i).getFilename());
                }

                Set<String> oldFilenameSet =
                        db().valueSet(String.class, "filename", new FileOutboxQuery().setFilenameIn(filenames));

                FileOutbox fileOutbox = new FileOutbox();
                fileOutbox.setFileTransferConfigId(fileTransferConfig.getId());
                fileOutbox.setStatus(FileOutboxStatus.NOT_SENT);
                String semaphoreSuffix = fileTransferPolicy.getSourceSemaphoreSuffix();
                for (FileInfo fileInfo : fileInfoList) {
                    if (fileInfo.isFile() && !fileInfo.isHidden() && !oldFilenameSet.contains(fileInfo.getFilename())) {
                        if (StringUtils.isNotBlank(semaphoreSuffix)) {
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
            List<FileInfo> fileInfoList = fileTransferServer.getRemoteFileList(fileTransferSetup);
            if (!fileInfoList.isEmpty()) {
                int size = fileInfoList.size();
                List<String> filenames = new ArrayList<String>(size);
                for (int i = 0; i < size; i++) {
                    filenames.add(fileInfoList.get(i).getFilename());
                }

                Set<String> oldFilenameSet =
                        new HashSet<String>(db().valueList(String.class, "filename",
                                new FileInboxQuery().setFilenameIn(filenames)));

                FileInbox fileInbox = new FileInbox();
                fileInbox.setFileTransferConfigId(fileTransferConfig.getId());
                fileInbox.setReadStatus(FileInboxReadStatus.NOT_READ);
                fileInbox.setStatus(FileInboxStatus.NOT_RECEIVED);
                String semaphoreSuffix = fileTransferPolicy.getSourceSemaphoreSuffix();
                for (FileInfo fileInfo : fileInfoList) {
                    if (fileInfo.isFile() && !fileInfo.isHidden() && !oldFilenameSet.contains(fileInfo.getFilename())) {
                        if (StringUtils.isNotBlank(semaphoreSuffix)) {
                            if (!fileTransferServer.remoteFileExists(fileTransferSetup,
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

    @Synchronized(EXECUTE_FILE_TRANSFER_LOCK)
    @Taskable(
            name = FileTransferTaskConstants.FILETRANSFERTASK, description = "File DataTransfer Task",
            parameters = { @Parameter(
                    name = FileTransferTaskConstants.FILETRANSFERCONFIGNAME,
                    description = "$m{file.filetransfertask.parameter.transferconfig}",
                    editor = "!ui-select list:filetransferconfiglist listKey:name blankOption:$s{}", mandatory = true),
                    @Parameter(name = FileTransferTaskConstants.WORKINGDT, type = Date.class),
                    @Parameter(
                            name = FileTransferTaskConstants.UPDATEFILEBOX,
                            description = "$m{file.filetransfertask.parameter.updateboxflag}",
                            editor = "!ui-select list:booleanlist blankOption:$s{}", type = boolean.class,
                            mandatory = true) },
            schedulable = true)
    public int executeFileTransferTask(TaskMonitor taskMonitor, String fileTransferConfigName, Date workingDt,
            boolean updateFilebox) throws UnifyException {
        int transferCount = 0;
        if (updateFilebox) {
            executeUpdateFileTransferListTask(taskMonitor, fileTransferConfigName, workingDt);
        }

        FileTransferConfig fileTransferConfig = getFileTransferConfig(fileTransferConfigName);
        FileTransferSetup fileTransferSetup = getFileTransferSetup(fileTransferConfig, workingDt);
        FileTransferServer fileServer =
                (FileTransferServer) this.getComponent(fileTransferConfig.getFileTransferServer());
        FileTransferPolicy fileTransferPolicy =
                (FileTransferPolicy) this.getComponent(fileTransferConfig.getFileTransferPolicy());
        FileFilter fileFilter =
                new FileFilter(fileTransferPolicy.getFilePrefixes(workingDt),
                        fileTransferPolicy.getFileSuffixes(workingDt), true);
        if (FileTransferDirection.UPLOAD.equals(fileTransferConfig.getDirection())) {
            List<FileOutbox> fileOutboxList =
                    db().listAll(new FileOutboxQuery().status(FileOutboxStatus.NOT_SENT)
                            .fileTransferConfigId(fileTransferConfig.getId()).createdOn(workingDt).addOrder("filename"));

            List<FileOutbox> upFileOutboxList = new ArrayList<FileOutbox>();
            for (FileOutbox fileOutbox : fileOutboxList) {
                if (fileFilter.accept(fileOutbox.getFilename(), true)) {
                    upFileOutboxList.add(fileOutbox);
                }
            }

            if (!fileServer.remoteDirectoryExists(fileTransferSetup)) {
                fileServer.createRemoteDirectory(fileTransferSetup);
            }

            String semaphoreSuffix = fileTransferPolicy.getTargetSemaphoreSuffix();
            for (FileOutbox fileOutbox : upFileOutboxList) {
                if (!taskMonitor.isCanceled()
                        && uploadFile(taskMonitor, fileServer, fileTransferSetup, fileOutbox, semaphoreSuffix)) {
                    transferCount++;
                }
            }
            addTaskMessage(taskMonitor, "$m{file.taskmonitor.uploadcompleted}");
        } else {
            List<FileInbox> fileInboxList =
                    db().listAll(new FileInboxQuery().status(FileInboxStatus.NOT_RECEIVED)
                            .fileTransferConfigId(fileTransferConfig.getId()).createdOn(workingDt).addOrder("filename"));

            List<FileInbox> downFileInboxList = new ArrayList<FileInbox>();
            for (FileInbox fileInbox : fileInboxList) {
                if (fileFilter.accept(fileInbox.getFilename(), true)) {
                    downFileInboxList.add(fileInbox);
                }
            }

            IOUtils.ensureDirectoryExists(fileTransferSetup.getLocalPath());
            String semaphoreSuffix = fileTransferPolicy.getTargetSemaphoreSuffix();
            for (FileInbox fileInbox : downFileInboxList) {
                if (!taskMonitor.isCanceled()
                        && downloadFile(taskMonitor, fileServer, fileTransferSetup, fileInbox, semaphoreSuffix)) {
                    transferCount++;
                }
            }
            addTaskMessage(taskMonitor, "file.taskmonitor.downloadcompleted");
        }
        return transferCount;
    }

    @Taskable(
            name = BatchFileReadTaskConstants.BATCHFILEREADTASK, description = "Batch Upload Task",
            parameters = { @Parameter(
                    name = BatchFileReadTaskConstants.BATCHFILEREADINPUTPARAMS,
                    type = BatchFileReadInputParameters.class) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    public int executeBatchUploadTask(TaskMonitor taskMonitor,
            BatchFileReadInputParameters batchFileReadInputParameters) throws UnifyException {
        BatchFileReadConfig batchFileReadConfig =
                getBatchFileReadConfig(batchFileReadInputParameters.getName(),
                        Inputs.getTypeValuesByName(batchFileReadInputParameters.getInputParameterList()));

        Object file = batchFileReadInputParameters.getFile();
        if (file == null) {
            file = batchFileReadInputParameters.getFileBlob();
        }

        BatchFileReadProcessor processor =
                (BatchFileReadProcessor) getComponent(batchFileReadConfig.getReadProcessor());
        processor.process(batchFileReadConfig, file);

        return 0;
    }

    @Transactional(TransactionAttribute.REQUIRES_NEW)
    public boolean uploadFile(TaskMonitor taskMonitor, FileTransferServer fileServer, FileTransferSetup fileTransferSetup,
            FileOutbox fileOutbox, String semaphoreSuffix) throws UnifyException {
        int uploadAttempts = fileOutbox.getUploadAttempts() + 1;
        try {
            String filename = fileOutbox.getFilename();
            addTaskMessage(taskMonitor, "$m{file.taskmonitor.uploading}", filename);
            fileServer.uploadFile(fileTransferSetup, filename, filename);

            if (StringUtils.isNotBlank(semaphoreSuffix)) {
                fileServer.createRemoteFile(fileTransferSetup, filename + semaphoreSuffix);
            }

            db().updateAll(new FileOutboxQuery().id(fileOutbox.getId()),
                    new Update().add("uploadAttempts", uploadAttempts).add("uploadedOn", db().getNow()).add("status",
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
            FileTransferSetup fileTransferSetup, FileInbox fileInbox, String semaphoreSuffix) throws UnifyException {
        int downloadAttempts = fileInbox.getDownloadAttempts() + 1;
        try {
            String filename = fileInbox.getFilename();
            addTaskMessage(taskMonitor, "$m{file.taskmonitor.downloading}", filename);
            fileServer.downloadFile(fileTransferSetup, filename, filename);

            if (StringUtils.isNotBlank(semaphoreSuffix)) {
                fileServer.createLocalFile(fileTransferSetup, filename + semaphoreSuffix);
            }

            db().updateAll(new FileInboxQuery().id(fileInbox.getId()),
                    new Update().add("downloadAttempts", downloadAttempts).add("downloadedOn", db().getNow())
                            .add("status", FileInboxStatus.RECEIVED));
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
    public List<BatchFileReadDefinition> findBatchFileReadDefinitions(BatchFileReadDefinitionQuery query)
            throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public BatchFileReadDefinition findBatchFileReadDefinition(Long id) throws UnifyException {
        return db().find(BatchFileReadDefinition.class, id);
    }

    @Override
    public BatchFileReadDefinitionLargeData findBatchFileReadDefinitionDocument(Long id) throws UnifyException {
        BatchFileReadDefinition batchFileReadConfig = db().find(BatchFileReadDefinition.class, id);
        Inputs fileReaderParams =
                getParameterService().fetchNormalizedInputs(batchFileReadConfig.getFileReader(),
                        BATCHFILE_READ_DEFINITION, id);
        Inputs fileProcessorParams =
                getParameterService().fetchNormalizedInputs(batchFileReadConfig.getReadProcessor(),
                        BATCHFILE_READ_DEFINITION, id);
        return new BatchFileReadDefinitionLargeData(batchFileReadConfig, fileReaderParams, fileProcessorParams);
    }

    @Override
    public Long createBatchFileReadDefinition(BatchFileReadDefinition record) throws UnifyException {
        return (Long) db().create(record);
    }

    @Override
    public Long createBatchFileReadDefinition(BatchFileReadDefinitionLargeData document) throws UnifyException {
        BatchFileReadDefinition batchFileReadDefinition = document.getData();
        Long batchFileReadConfigId = (Long) db().create(batchFileReadDefinition);
        getParameterService().updateParameterValues(batchFileReadDefinition.getFileReader(), BATCHFILE_READ_DEFINITION,
                batchFileReadConfigId, document.getFileReaderParams());
        getParameterService().updateParameterValues(batchFileReadDefinition.getReadProcessor(),
                BATCHFILE_READ_DEFINITION, batchFileReadConfigId, document.getFileProcessorParams());
        return batchFileReadConfigId;
    }

    @Override
    public int updateBatchFileReadDefinition(BatchFileReadDefinition record) throws UnifyException {
        return db().updateByIdVersion(record);
    }

    @Override
    public int updateBatchFileReadDefinition(BatchFileReadDefinitionLargeData document) throws UnifyException {
        BatchFileReadDefinition batchFileReadDefinition = document.getData();
        int updateCount = db().updateByIdVersion(batchFileReadDefinition);
        getParameterService().updateParameterValues(batchFileReadDefinition.getFileReader(), BATCHFILE_READ_DEFINITION,
                batchFileReadDefinition.getId(), document.getFileReaderParams());
        getParameterService().updateParameterValues(batchFileReadDefinition.getReadProcessor(),
                BATCHFILE_READ_DEFINITION, batchFileReadDefinition.getId(), document.getFileProcessorParams());
        return updateCount;
    }

    @Override
    public int deleteBatchFileReadDefinition(Long id) throws UnifyException {
        String fileReader = db().value(String.class, "fileReader", new BatchFileReadDefinitionQuery().id(id));
        getParameterService().deleteParameterValues(fileReader, BATCHFILE_READ_DEFINITION, id);
        String readProcessor = db().value(String.class, "readProcessor", new BatchFileReadDefinitionQuery().id(id));
        getParameterService().deleteParameterValues(readProcessor, BATCHFILE_READ_DEFINITION, id);
        return db().delete(BatchFileReadDefinition.class, id);
    }

    @Override
    public BatchFileReadDefinitionLargeData loadBatchFileReadConfigDocumentValues(
            BatchFileReadDefinitionLargeData document) throws UnifyException {
        BatchFileReadDefinition batchFileReadDefinition = document.getData();
        Inputs fileReaderParams =
                getParameterService().fetchNormalizedInputs(batchFileReadDefinition.getFileReader(),
                        BATCHFILE_READ_DEFINITION, batchFileReadDefinition.getId());
        Inputs fileProcessorParams =
                getParameterService().fetchNormalizedInputs(batchFileReadDefinition.getReadProcessor(),
                        BATCHFILE_READ_DEFINITION, batchFileReadDefinition.getId());
        return new BatchFileReadDefinitionLargeData(batchFileReadDefinition, fileReaderParams, fileProcessorParams);
    }

    @Override
    public BatchFileReadInputParameters getBatchFileReadInputParameters(Long batchFileReadDefinitionId)
            throws UnifyException {
        BatchFileReadDefinition batchFileReadConfig =
                db().find(BatchFileReadDefinition.class, batchFileReadDefinitionId);
        BatchFileReadInputParameters batchFileReadInputParameters = new BatchFileReadInputParameters();
        batchFileReadInputParameters.setName(batchFileReadConfig.getName());
        batchFileReadInputParameters.setDescription(batchFileReadConfig.getDescription());
        batchFileReadInputParameters
                .addInputParameterList(getParameterService().fetchInputList(batchFileReadConfig.getReadProcessor()));
        return batchFileReadInputParameters;
    }

    @Override
    public BatchFileReadConfig getBatchFileReadConfig(String batchFileReadDefinitionName,
            Map<String, Object> parameters) throws UnifyException {
        BatchFileReadDefinition batchFileReadDefinition =
                db().list(new BatchFileReadDefinitionQuery().name(batchFileReadDefinitionName));
        if (batchFileReadDefinition == null) {
            throw new UnifyException(FileModuleErrorConstants.BATCHUPLOADCONFIG_NAME_UNKNOWN,
                    batchFileReadDefinitionName);
        }

        BatchFileReadConfig.Builder bb =
                BatchFileReadConfig.newBuilder().reader(batchFileReadDefinition.getFileReader())
                        .processor(batchFileReadDefinition.getReadProcessor())
                        .onConstraint(batchFileReadDefinition.getConstraintAction());
        if (batchFileReadDefinition.getBatchFileDefinitionId() != null) {
            BatchFileDefinition batchFileDefinition =
                    db().find(BatchFileDefinition.class, batchFileReadDefinition.getBatchFileDefinitionId());
            for (BatchFileFieldDefinition bffda : batchFileDefinition.getFieldDefList()) {
                bb.addFieldConfig(bffda.getBeanFieldName(), bffda.getFileFieldName(), bffda.getFormatter(),
                        bffda.getPadDirection(), bffda.getLength(), bffda.isTrim(), bffda.isPad(),
                        bffda.isUpdateOnConstraint(), bffda.getPadChar());
            }

            bb.skipFirstRecord(batchFileDefinition.isSkipFirst());
        }

        // Reader parameters
        bb.addParams(getParameterService().findParameterTypeValues(batchFileReadDefinition.getFileReader(),
                BATCHFILE_READ_DEFINITION, batchFileReadDefinition.getId()));

        // Processor parameters
        bb.addParams(getParameterService().findParameterTypeValues(batchFileReadDefinition.getReadProcessor(),
                BATCHFILE_READ_DEFINITION, batchFileReadDefinition.getId()));

        // Input parameters
        if (parameters != null) {
            bb.addParams(parameters);
        }

        return bb.build();
    }

    @Override
    public List<BatchFileFieldDefinition> mergeBatchFileFieldMapping(String beanType,
            List<BatchFileFieldDefinition> baseFieldDefList) throws UnifyException {
        List<BatchFileFieldDefinition> result = new ArrayList<BatchFileFieldDefinition>();
        Set<String> beanFieldNames = new HashSet<String>();
        if (DataUtils.isNotBlank(baseFieldDefList)) {
            for (BatchFileFieldDefinition baseFieldDefinition : baseFieldDefList) {
                beanFieldNames.add(baseFieldDefinition.getBeanFieldName());
                result.add(baseFieldDefinition);
            }
        }

        List<String> beanFieldNameList = ReflectUtils.getBeanCompliantFieldNames(beanType);
        for (String newBeanFieldName : beanFieldNameList) {
            if (!"id".equals(newBeanFieldName) && !beanFieldNames.contains(newBeanFieldName)) {
                BatchFileFieldDefinition batchFileFieldDefinition = new BatchFileFieldDefinition();
                batchFileFieldDefinition.setBeanFieldName(newBeanFieldName);
                batchFileFieldDefinition.setFileFieldName(newBeanFieldName);
                result.add(batchFileFieldDefinition);
            }
        }
        return result;
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
        List<UnifyComponentConfig> uccList = getComponentConfigs(BatchFileReadProcessor.class);
        uccList.addAll(getComponentConfigs(BatchFileReader.class));
        for (UnifyComponentConfig ucc : uccList) {
            getParameterService().defineParameters(ucc.getName(), ucc.getType());
        }
    }

    private FileTransferConfig getFileTransferConfig(String name) throws UnifyException {
        FileTransferConfig fileTransferConfig = db().list(new FileTransferConfigQuery().name(name));
        if (fileTransferConfig == null) {
            throw new UnifyException(FileModuleErrorConstants.FILETRANSFERCONFIG_NAME_UNKNOWN, name);
        }
        return fileTransferConfig;
    }

    private FileTransferSetup getFileTransferSetup(FileTransferConfig fileTransferConfig, Date workingDt)
            throws UnifyException {
        FileTransferPolicy fileTransferPolicy =
                (FileTransferPolicy) this.getComponent(fileTransferConfig.getFileTransferPolicy());
        String serverPath =
                fileTransferPolicy.getExtendedRemotePath(fileTransferConfig.getRemotePath(),
                        fileTransferConfig.getRemoteDateFormat(), workingDt);
        String localPath =
                fileTransferPolicy.getExtendedLocalPath(fileTransferConfig.getLocalPath(),
                        fileTransferConfig.getLocalDateFormat(), workingDt);
        int remotePort = DataUtils.convert(int.class, fileTransferConfig.getRemotePort(), null);
        return FileTransferSetup.newBuilder().remoteHost(fileTransferConfig.getRemoteHost()).remotePort(remotePort)
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
}
