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
package com.tcdng.jacklyn.archiving.business;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import com.tcdng.jacklyn.archiving.constants.ArchivingModuleErrorConstants;
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
import com.tcdng.jacklyn.archiving.entities.FileArchiveEntryQuery;
import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.utils.JacklynUtils;
import com.tcdng.jacklyn.file.business.FileService;
import com.tcdng.jacklyn.shared.archiving.ArchivingFieldType;
import com.tcdng.jacklyn.shared.archiving.FileArchiveType;
import com.tcdng.jacklyn.shared.xml.config.module.ArchiveConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ArchivesConfig;
import com.tcdng.jacklyn.shared.xml.config.module.FieldConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ManagedConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.file.FileSystemIO;
import com.tcdng.unify.core.operation.Update;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Default archiving service implementation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(ArchivingModuleNameConstants.ARCHIVINGSERVICE)
public class ArchivingServiceImpl extends AbstractJacklynBusinessService implements ArchivingService {

    @Configurable
    private SystemService systemService;

    @Configurable
    private FileService fileService;

    @Configurable("filesystemio")
    private FileSystemIO fileSystemIO;

    @Override
    public List<ArchivableDefinition> findArchivableDefinitions(ArchivableDefinitionQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public ArchivableDefinition findArchivableDefinition(Long archivableId) throws UnifyException {
        return db().list(ArchivableDefinition.class, archivableId);
    }

    @Override
    public List<ArchivingField> findArchivingFields(ArchivingFieldQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public ArchivingField findArchivingField(Long archivableFieldId) throws UnifyException {
        return db().list(ArchivingField.class, archivableFieldId);
    }

    @Override
    public Long createFileArchiveConfig(FileArchiveConfig fileArchiveConfig) throws UnifyException {
        return (Long) db().create(fileArchiveConfig);
    }

    @Override
    public FileArchiveConfig findFileArchiveConfig(Long fileArchiveConfigId) throws UnifyException {
        return db().find(FileArchiveConfig.class, fileArchiveConfigId);
    }

    @Override
    public List<FileArchiveConfig> findFileArchiveConfigs(FileArchiveConfigQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public int updateFileArchiveConfig(FileArchiveConfig fileArchiveConfig) throws UnifyException {
        return db().updateById(fileArchiveConfig);
    }

    @Override
    public int deleteFileArchiveConfig(Long id) throws UnifyException {
        return db().delete(FileArchiveConfig.class, id);
    }

    @Synchronized("filearchiveprocesslock")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Taskable(name = FileArchiveTaskConstants.BUILDLOBFILEARCHIVETASK, description = "Build LOB File Archive Taskable",
            parameters = { @Parameter(name = FileArchiveTaskParamConstants.FILEARCHIVECONFIGNAME,
                    description = "$m{archiving.filearchiveconfig.parameter.archiveconfig}",
                    editor = "!ui-select list:filearchiveconfiglist listKey:name blankOption:$s{}", mandatory = true),
                    @Parameter(name = FileArchiveTaskParamConstants.WORKINGDT, type = Date.class) },
            schedulable = true)
    public Long executeBuildLobFileArchiveTask(TaskMonitor taskMonitor, String fileArchiveConfigName, Date workingDt)
            throws UnifyException {
        Long fileArchiveId = null;
        try {
            FileArchiveConfig fileArchiveConfig = db().list(new FileArchiveConfigQuery().name(fileArchiveConfigName));
            if (fileArchiveConfig == null) {
                throw new UnifyException(ArchivingModuleErrorConstants.FILEARCHIVECONFIG_NAME_UNKNOWN,
                        fileArchiveConfigName);
            }

            if (RecordStatus.INACTIVE.equals(fileArchiveConfig.getStatus())) {
                throw new UnifyException(ArchivingModuleErrorConstants.FILEARCHIVECONFIG_INACTIVE,
                        fileArchiveConfigName);
            }

            // Fetch ID's of records to backup. (Involves generic persistence
            // operations)
            String lobFieldName = fileArchiveConfig.getFieldName();
            Class<? extends Entity> recordClazz = (Class<? extends Entity>) ReflectUtils
                    .getClassForName(fileArchiveConfig.getRecordName());
            Query<? extends Entity> query = new Query(recordClazz);
            query.between(fileArchiveConfig.getDateFieldName(), CalendarUtils.getMidnightDate(workingDt),
                    CalendarUtils.getLastSecondDate(workingDt));
            query.isNotNull(lobFieldName);
            query.order("id").limit(fileArchiveConfig.getMaxItemsPerFile());
            List<Long> targetIdList = db().valueList(Long.class, "id", query);

            if (!targetIdList.isEmpty()) {
                FileArchiveNameGenerator fileArchiveNameGenerator = (FileArchiveNameGenerator) this
                        .getComponent(fileArchiveConfig.getFilenameGenerator());
                String filename = fileArchiveNameGenerator.generateFileArchiveName(FileArchiveType.LOB_FILE_ARCHIVE,
                        fileArchiveConfigName, workingDt);
                FileArchive fileArchive = new FileArchive();
                fileArchive.setFileArchiveConfigId(fileArchiveConfig.getId());
                fileArchive.setFilename(filename);
                fileArchive.setArchiveDt(CalendarUtils.getMidnightDate(workingDt));
                fileArchiveId = (Long) db().create(fileArchive);

                long fileIndex = 0;
                OutputStream outputStream = null;
                try {
                    String localArchivePath = JacklynUtils.getExtendedFilePath(fileArchiveConfig.getLocalArchivePath(),
                            fileArchiveConfig.getLocalArchiveDateFormat(), workingDt);
                    String absoluteFilename = fileSystemIO.buildFilename(localArchivePath, filename);
                    outputStream = fileSystemIO.openFileOutputStream(absoluteFilename);
                    FileArchiveEntry fileArchiveEntry = new FileArchiveEntry();
                    fileArchiveEntry.setFileArchiveId(fileArchiveId);
                    for (Long archivedItemId : targetIdList) {
                        if (taskMonitor.isCanceled()) {
                            setRollback();
                            break;
                        }

                        // Read LOB to archive
                        query.clear();
                        query.equals("id", archivedItemId);
                        byte[] lobToArchive = null;
                        if (ArchivingFieldType.BLOB.equals(fileArchiveConfig.getFieldType())) {
                            lobToArchive = db().value(byte[].class, lobFieldName, query);
                        } else {
                            String clobToArchive = db().value(String.class, lobFieldName, query);
                            lobToArchive = clobToArchive.getBytes("UTF-8");
                        }

                        // Append LOB to file with a write
                        fileSystemIO.writeAll(outputStream, lobToArchive);

                        // Create file archive entry record
                        fileArchiveEntry.setArchivedItemId(archivedItemId);
                        fileArchiveEntry.setFileIndex(fileIndex);
                        fileArchiveEntry.setArchivedItemLength(lobToArchive.length);
                        db().create(fileArchiveEntry);

                        // Next index
                        fileIndex += lobToArchive.length;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    throwOperationErrorException(e);
                } finally {
                    fileSystemIO.close(outputStream);
                }

                // Delete archived
                boolean deleteRowOnArchive = fileArchiveConfig.getDeleteRowOnArchive();
                query.clear();
                query.amongst("id", targetIdList);
                if (deleteRowOnArchive) {
                    // Delete entire rows
                    db().deleteAll(query);
                } else {
                    // Delete LOB column by updating to NULL
                    db().updateAll(query, new Update().add(lobFieldName, null));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return fileArchiveId;
    }

    @Override
    public byte[] retriveArchivedBlob(String recordName, String fieldName, Long archivedItemId) throws UnifyException {
        FileArchiveEntry fileArchiveEntry = db().list(
                new FileArchiveEntryQuery().recordName(recordName).fieldName(fieldName).archivedItemId(archivedItemId));
        if (fileArchiveEntry != null) {
            String localArchivePath = JacklynUtils.getExtendedFilePath(fileArchiveEntry.getLocalArchivePath(),
                    fileArchiveEntry.getLocalArchiveDateFormat(), fileArchiveEntry.getArchiveDt());
            String absoluteFilename = fileSystemIO.buildFilename(localArchivePath, fileArchiveEntry.getFilename());
            if (fileSystemIO.isFile(absoluteFilename)) {
                // Read from local
                InputStream inputStream = null;
                try {
                    inputStream = fileSystemIO.openFileInputStream(absoluteFilename, fileArchiveEntry.getFileIndex());
                    byte[] archivedLob = new byte[fileArchiveEntry.getArchivedItemLength()];
                    fileSystemIO.read(archivedLob, inputStream);
                    return archivedLob;
                } finally {
                    fileSystemIO.close(inputStream);
                }
            } else {
                // Read from backup if exist
                Long backupFileTransferCfgId = fileArchiveEntry.getBackupFileTransferCfgId();
                if (backupFileTransferCfgId != null) {
                    return fileService.readRemoteBlock(backupFileTransferCfgId, fileArchiveEntry.getArchiveDt(),
                            fileArchiveEntry.getFilename(), fileArchiveEntry.getFileIndex(),
                            fileArchiveEntry.getArchivedItemLength());
                }
            }
        }
        return null;
    }

    @Override
    public String retriveArchivedClob(String recordName, String fieldName, Long archivedItemId) throws UnifyException {
        try {
            byte[] archivedLob = retriveArchivedBlob(recordName, fieldName, archivedItemId);
            if (archivedLob != null) {
                return new String(archivedLob, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            throwOperationErrorException(e);
        }
        return null;
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
        // Uninstall old records
        Update update = new Update().add("installed", Boolean.FALSE);
        db().updateAll(new ArchivableDefinitionQuery(), update);
        db().updateAll(new ArchivingFieldQuery(), update);

        // Install new and update old
        ArchivableDefinition archiveDefinition = new ArchivableDefinition();
        ArchivingField archivableField = new ArchivingField();

        for (ModuleConfig moduleConfig : moduleConfigList) {
            Long moduleId = systemService.getModuleId(moduleConfig.getName());

            ArchivesConfig archivesConfig = moduleConfig.getArchives();
            if (archivesConfig != null) {
                logDebug("Installing archiving definitions for module [{0}]...",
                        resolveApplicationMessage(moduleConfig.getDescription()));
                ArchivableDefinitionQuery adQuery = new ArchivableDefinitionQuery();
                for (ArchiveConfig archiveConfig : moduleConfig.getArchives().getArchiveList()) {
                    ManagedConfig managedConfig = JacklynUtils.getManagedConfig(moduleConfig,
                            archiveConfig.getArchivable());
                    adQuery.clear();
                    adQuery.name(archiveConfig.getName());
                    ArchivableDefinition oldArchiveDefinition = db().find(adQuery);
                    if (oldArchiveDefinition == null) {
                        archiveDefinition.setModuleId(moduleId);
                        archiveDefinition.setName(archiveConfig.getName());
                        archiveDefinition.setDescription(archiveConfig.getDescription());
                        archiveDefinition.setRecordType(archiveConfig.getArchivable());
                        Long archivableDefId = (Long) db().create(archiveDefinition);

                        archivableField.setArchivableDefId(archivableDefId);
                        for (FieldConfig fieldConfig : managedConfig.getFieldList()) {
                            if (fieldConfig.isArchivable()) {
                                archivableField.setDescription(fieldConfig.getDescription());
                                archivableField.setFieldName(fieldConfig.getName());
                                archivableField.setFieldType(fieldConfig.getArchFieldType());
                                db().create(archivableField);
                            }
                        }
                    } else {
                        Long archivableDefId = oldArchiveDefinition.getId();
                        oldArchiveDefinition.setName(archiveConfig.getName());
                        oldArchiveDefinition.setDescription(archiveConfig.getDescription());
                        oldArchiveDefinition.setRecordType(archiveConfig.getArchivable());
                        oldArchiveDefinition.setInstalled(Boolean.TRUE);
                        db().updateById(oldArchiveDefinition);

                        archivableField.setArchivableDefId(archivableDefId);
                        for (FieldConfig fieldConfig : managedConfig.getFieldList()) {
                            ArchivingFieldQuery afQuery = new ArchivingFieldQuery();
                            if (fieldConfig.isArchivable()) {
                                afQuery.clear();
                                afQuery.archivableDefId(archivableDefId);
                                afQuery.fieldName(fieldConfig.getName());
                                ArchivingField oldArchivableField = db().find(afQuery);
                                if (oldArchivableField == null) {
                                    archivableField.setDescription(fieldConfig.getDescription());
                                    archivableField.setFieldName(fieldConfig.getName());
                                    archivableField.setFieldType(fieldConfig.getArchFieldType());
                                    db().create(archivableField);
                                } else {
                                    oldArchivableField.setDescription(fieldConfig.getDescription());
                                    oldArchivableField.setFieldType(fieldConfig.getArchFieldType());
                                    oldArchivableField.setInstalled(Boolean.TRUE);
                                    db().updateById(oldArchivableField);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
