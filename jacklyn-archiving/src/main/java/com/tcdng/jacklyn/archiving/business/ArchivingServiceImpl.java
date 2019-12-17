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
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.file.FileSystemIO;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default archiving service implementation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(ArchivingModuleNameConstants.ARCHIVINGSERVICE)
public class ArchivingServiceImpl extends AbstractJacklynBusinessService implements ArchivingService {

    private static final String FILE_ARCHIVE_PROCESS_LOCK = "arch::filearchiveprocess-lock";

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
    public ArchivingField findArchivingField(ArchivingFieldQuery query) throws UnifyException {
        return db().list(query);
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

    @Synchronized(FILE_ARCHIVE_PROCESS_LOCK)
    @SuppressWarnings({ "unchecked" })
    @Taskable(
            name = FileArchiveTaskConstants.BUILDLOBFILEARCHIVETASK, description = "Build LOB File Archive Taskable",
            parameters = { @Parameter(
                    name = FileArchiveTaskParamConstants.FILEARCHIVECONFIGNAME,
                    description = "$m{archiving.filearchiveconfig.parameter.archiveconfig}",
                    editor = "!ui-select list:filearchiveconfiglist listKey:name blankOption:$s{}", mandatory = true),
                    @Parameter(name = FileArchiveTaskParamConstants.WORKINGDT, type = Date.class) },
            schedulable = true)
    public Long executeBuildLobFileArchiveTask(TaskMonitor taskMonitor, String fileArchiveConfigName, Date workingDt)
            throws UnifyException {
        Long fileArchiveId = null;
        FileArchiveConfig fileArchiveConfig = db().list(new FileArchiveConfigQuery().name(fileArchiveConfigName));
        if (fileArchiveConfig == null) {
            throw new UnifyException(ArchivingModuleErrorConstants.FILEARCHIVECONFIG_NAME_UNKNOWN,
                    fileArchiveConfigName);
        }

        if (RecordStatus.INACTIVE.equals(fileArchiveConfig.getStatus())) {
            throw new UnifyException(ArchivingModuleErrorConstants.FILEARCHIVECONFIG_INACTIVE, fileArchiveConfigName);
        }

        // Fetch ID's of records to backup. (Involves generic persistence
        // operations)
        Class<? extends Entity> entityClazz =
                (Class<? extends Entity>) ReflectUtils.getClassForName(fileArchiveConfig.getRecordName());
        Query<? extends Entity> query = Query.of(entityClazz);
        query.addBetween(fileArchiveConfig.getDateFieldName(), CalendarUtils.getMidnightDate(workingDt),
                CalendarUtils.getLastSecondDate(workingDt));
        query.addEquals(fileArchiveConfig.getIndicatorFieldName(), Boolean.FALSE);
        query.addOrder("id").setLimit(fileArchiveConfig.getMaxItemsPerFile());
        List<Long> targetIdList = db().valueList(Long.class, "id", query);

        if (!targetIdList.isEmpty()) {
            ArchivingPolicy archivingPolicy = null;
            if (!StringUtils.isBlank(fileArchiveConfig.getArchivingPolicyName())) {
                archivingPolicy = (ArchivingPolicy) getComponent(fileArchiveConfig.getArchivingPolicyName());
            }

            FileArchiveNameGenerator fileArchiveNameGenerator =
                    (FileArchiveNameGenerator) getComponent(fileArchiveConfig.getFilenameGenerator());
            String filename =
                    fileArchiveNameGenerator.generateFileArchiveName(FileArchiveType.LOB_FILE_ARCHIVE,
                            fileArchiveConfigName, workingDt);
            FileArchive fileArchive = new FileArchive();
            fileArchive.setFileArchiveConfigId(fileArchiveConfig.getId());
            fileArchive.setFilename(filename);
            fileArchive.setArchiveDt(CalendarUtils.getMidnightDate(workingDt));
            fileArchiveId = (Long) db().create(fileArchive);

            long fileIndex = 0;
            OutputStream outputStream = null;
            try {
                String lobFieldName = fileArchiveConfig.getFieldName();
                String localArchivePath =
                        JacklynUtils.getExtendedFilePath(fileArchiveConfig.getLocalArchivePath(),
                                fileArchiveConfig.getLocalArchiveDateFormat(), workingDt);
                String absoluteFilename = fileSystemIO.buildFilename(localArchivePath, filename);
                outputStream = fileSystemIO.openFileOutputStream(absoluteFilename);
                FileArchiveEntry fileArchiveEntry = new FileArchiveEntry();
                fileArchiveEntry.setFileArchiveId(fileArchiveId);
                for (Long archivedItemId : targetIdList) {
                    if (taskMonitor.isCanceled()) {
                        setRollbackTransactions();
                        break;
                    }

                    // Read LOB to archive
                    byte[] lobToArchive = null;
                    if (archivingPolicy != null) {
                        lobToArchive = archivingPolicy.getLobToArchive(archivedItemId);
                    } else {
                        query.clear();
                        query.addEquals("id", archivedItemId);
                        if (ArchivingFieldType.BLOB.equals(fileArchiveConfig.getFieldType())) {
                            lobToArchive = db().value(byte[].class, lobFieldName, query);
                        } else {
                            String clobToArchive = db().value(String.class, lobFieldName, query);
                            lobToArchive = clobToArchive.getBytes("UTF-8");
                        }
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
                throwOperationErrorException(e);
            } finally {
                fileSystemIO.close(outputStream);
            }

            // Delete archived
            if (archivingPolicy != null) {
                archivingPolicy.deleteLobFromAchivable(targetIdList);
            }

            boolean deleteRowOnArchive = fileArchiveConfig.getDeleteRowOnArchive();
            query.clear();
            query.addAmongst("id", targetIdList);
            if (deleteRowOnArchive) {
                // Delete entire rows
                db().deleteAll(query);
            } else {
                Update update = new Update().add(fileArchiveConfig.getIndicatorFieldName(), Boolean.TRUE);
                if (archivingPolicy == null) {
                    // Delete LOB column by updating to NULL
                    update.add(fileArchiveConfig.getFieldName(), null);
                }
                db().updateAll(query, update);
            }
        }

        return fileArchiveId;
    }

    @Override
    public byte[] retriveArchivedBlob(String recordName, String fieldName, Long archivedItemId) throws UnifyException {
        FileArchiveEntry fileArchiveEntry =
                db().list(new FileArchiveEntryQuery().recordName(recordName).fieldName(fieldName)
                        .archivedItemId(archivedItemId));
        if (fileArchiveEntry != null) {
            String localArchivePath =
                    JacklynUtils.getExtendedFilePath(fileArchiveEntry.getLocalArchivePath(),
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
        db().updateAll(new ArchivableDefinitionQuery().installed(Boolean.TRUE), update);
        db().updateAll(new ArchivingFieldQuery().installed(Boolean.TRUE), update);

        // Install new and update old
        ArchivableDefinition archiveDefinition = new ArchivableDefinition();
        ArchivingField archivableField = new ArchivingField();

        for (ModuleConfig moduleConfig : moduleConfigList) {
            Long moduleId = systemService.getModuleId(moduleConfig.getName());

            ArchivesConfig archivesConfig = moduleConfig.getArchives();
            if (archivesConfig != null && DataUtils.isNotBlank(archivesConfig.getArchiveList())) {
                logDebug("Installing archiving definitions for module [{0}]...",
                        resolveApplicationMessage(moduleConfig.getDescription()));
                ArchivableDefinitionQuery adQuery = new ArchivableDefinitionQuery();
                for (ArchiveConfig archiveConfig : moduleConfig.getArchives().getArchiveList()) {
                    ManagedConfig managedConfig =
                            JacklynUtils.getManagedConfig(moduleConfig, archiveConfig.getArchivable());
                    adQuery.clear();
                    adQuery.name(archiveConfig.getName());
                    ArchivableDefinition oldArchiveDefinition = db().find(adQuery);
                    if (oldArchiveDefinition == null) {
                        archiveDefinition.setModuleId(moduleId);
                        archiveDefinition.setName(archiveConfig.getName());
                        archiveDefinition.setDescription(archiveConfig.getDescription());
                        archiveDefinition.setRecordType(archiveConfig.getArchivable());
                        archiveDefinition.setInstalled(Boolean.TRUE);
                        Long archivableDefId = (Long) db().create(archiveDefinition);

                        archivableField.setArchivableDefId(archivableDefId);
                        for (FieldConfig fieldConfig : managedConfig.getFieldList()) {
                            if (fieldConfig.isArchivable()) {
                                archivableField.setDescription(fieldConfig.getDescription());
                                archivableField.setFieldName(fieldConfig.getName());
                                archivableField.setFieldType(fieldConfig.getArchFieldType());
                                archivableField.setInstalled(Boolean.TRUE);
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
                                    archivableField.setInstalled(Boolean.TRUE);
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
