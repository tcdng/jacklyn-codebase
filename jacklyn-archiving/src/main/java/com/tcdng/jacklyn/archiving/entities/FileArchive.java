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
package com.tcdng.jacklyn.archiving.entities;

import java.util.Date;

import com.tcdng.jacklyn.archiving.constants.ArchivingModuleNameConstants;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseTimestampedEntity;
import com.tcdng.jacklyn.shared.archiving.ArchivingFieldType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.util.StringUtils;

/**
 * File archive.
 *
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = ArchivingModuleNameConstants.ARCHIVING_MODULE, title = "File Archive", reportable = true)
@Table(name = "JKFILEARCHIVE", uniqueConstraints = { @UniqueConstraint({ "filename" }) })
public class FileArchive extends BaseTimestampedEntity {

    @ForeignKey(FileArchiveConfig.class)
    private Long fileArchiveConfigId;

    @Column(length = 64)
    private String filename;

    @Column
    private Date archiveDt;

    @ListOnly(key = "fileArchiveConfigId", property = "archivableFieldId")
    private Long archivableFieldId;

    @ListOnly(key = "fileArchiveConfigId", property = "localArchivePath")
    private String localArchivePath;

    @ListOnly(key = "fileArchiveConfigId", property = "localArchiveDateFormat")
    private String localArchiveDateFormat;

    @ListOnly(key = "fileArchiveConfigId", property = "backupFileTransferCfgId")
    private Long backupFileTransferCfgId;

    @ListOnly(key = "fileArchiveConfigId", property = "fieldName")
    private String fieldName;

    @ListOnly(key = "fileArchiveConfigId", property = "fieldType")
    private ArchivingFieldType fieldType;

    @ListOnly(key = "fileArchiveConfigId", property = "recordName")
    private String recordName;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(this.fieldName, "-", this.archiveDt);
    }

    public Long getFileArchiveConfigId() {
        return fileArchiveConfigId;
    }

    public void setFileArchiveConfigId(Long fileArchiveConfigId) {
        this.fileArchiveConfigId = fileArchiveConfigId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getArchiveDt() {
        return archiveDt;
    }

    public void setArchiveDt(Date archiveDt) {
        this.archiveDt = archiveDt;
    }

    public Long getArchivableFieldId() {
        return archivableFieldId;
    }

    public void setArchivableFieldId(Long archivableFieldId) {
        this.archivableFieldId = archivableFieldId;
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

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }
}
