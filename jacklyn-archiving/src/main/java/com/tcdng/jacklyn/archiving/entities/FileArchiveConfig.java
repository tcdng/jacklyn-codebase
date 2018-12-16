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
package com.tcdng.jacklyn.archiving.entities;

import com.tcdng.jacklyn.archiving.constants.ArchivingModuleNameConstants;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.file.entities.FileTransferConfig;
import com.tcdng.jacklyn.shared.archiving.ArchivingFieldType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * File archive configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = ArchivingModuleNameConstants.ARCHIVING_MODULE, title = "File Archive Config", reportable = true,
        auditable = true)
@Table(name = "FILEARCHIVECFG", uniqueConstraints = { @UniqueConstraint({ "name" }) })
public class FileArchiveConfig extends BaseVersionedStatusEntity {

    @ForeignKey(ArchivableDefinition.class)
    private Long archivableDefId;

    @ForeignKey(ArchivingField.class)
    private Long archivableFieldId;

    @ForeignKey(ArchivingField.class)
    private Long archivableDateFieldId;

    @ForeignKey(type = FileTransferConfig.class, nullable = true)
    private Long backupFileTransferCfgId;

    @Column(name = "FILEARCHIVECFG_NM", length = 32)
    private String name;

    @Column(name = "FILEARCHIVECFG_DESC", length = 40)
    private String description;

    @Column(length = 64)
    private String localArchivePath;

    @Column(length = 32, nullable = true)
    private String localArchiveDateFormat;

    @Column(length = 40)
    private String filenameGenerator;

    @Column
    private Integer maxItemsPerFile;

    @Column
    private Boolean deleteRowOnArchive;

    @ListOnly(key = "archivableDefId", property = "recordType")
    private String recordName;

    @ListOnly(key = "archivableFieldId", property = "fieldName")
    private String fieldName;

    @ListOnly(key = "archivableFieldId", property = "fieldType")
    private ArchivingFieldType fieldType;

    @ListOnly(key = "archivableFieldId", property = "fieldTypeDesc")
    private String fieldTypeDesc;

    @ListOnly(key = "archivableDateFieldId", property = "fieldName")
    private String dateFieldName;

    public Long getArchivableDefId() {
        return archivableDefId;
    }

    public void setArchivableDefId(Long archivableDefId) {
        this.archivableDefId = archivableDefId;
    }

    public Long getArchivableFieldId() {
        return archivableFieldId;
    }

    public void setArchivableFieldId(Long archivableFieldId) {
        this.archivableFieldId = archivableFieldId;
    }

    public Long getArchivableDateFieldId() {
        return archivableDateFieldId;
    }

    public void setArchivableDateFieldId(Long archivableDateFieldId) {
        this.archivableDateFieldId = archivableDateFieldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalArchivePath() {
        return localArchivePath;
    }

    public void setLocalArchivePath(String localArchivePath) {
        this.localArchivePath = localArchivePath;
    }

    public String getLocalArchiveDateFormat() {
        return localArchiveDateFormat;
    }

    public void setLocalArchiveDateFormat(String localArchiveDateFormat) {
        this.localArchiveDateFormat = localArchiveDateFormat;
    }

    public String getFilenameGenerator() {
        return filenameGenerator;
    }

    public void setFilenameGenerator(String filenameGenerator) {
        this.filenameGenerator = filenameGenerator;
    }

    public Integer getMaxItemsPerFile() {
        return maxItemsPerFile;
    }

    public void setMaxItemsPerFile(Integer maxItemsPerFile) {
        this.maxItemsPerFile = maxItemsPerFile;
    }

    public Boolean getDeleteRowOnArchive() {
        return deleteRowOnArchive;
    }

    public void setDeleteRowOnArchive(Boolean deleteRowOnArchive) {
        this.deleteRowOnArchive = deleteRowOnArchive;
    }

    public Long getBackupFileTransferCfgId() {
        return backupFileTransferCfgId;
    }

    public void setBackupFileTransferCfgId(Long backupFileTransferCfgId) {
        this.backupFileTransferCfgId = backupFileTransferCfgId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ArchivingFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(ArchivingFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldTypeDesc() {
        return fieldTypeDesc;
    }

    public void setFieldTypeDesc(String fieldTypeDesc) {
        this.fieldTypeDesc = fieldTypeDesc;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getDateFieldName() {
        return dateFieldName;
    }

    public void setDateFieldName(String dateFieldName) {
        this.dateFieldName = dateFieldName;
    }
}
