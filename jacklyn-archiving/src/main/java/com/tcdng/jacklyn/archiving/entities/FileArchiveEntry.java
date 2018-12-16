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

import java.util.Date;

import com.tcdng.jacklyn.archiving.constants.ArchivingModuleNameConstants;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.shared.archiving.ArchivingFieldType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * File archive entry.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = ArchivingModuleNameConstants.ARCHIVING_MODULE, title = "File Archive Entry")
@Table(name = "FILEARCHIVEENTRY", uniqueConstraints = { @UniqueConstraint({ "fileArchiveId", "archivedItemId" }) })
public class FileArchiveEntry extends BaseEntity {

    @ForeignKey(FileArchive.class)
    private Long fileArchiveId;

    @Column
    private Long fileIndex;

    @Column
    private Long archivedItemId;

    @Column
    private Integer archivedItemLength;

    @ListOnly(key = "fileArchiveId", property = "archiveDt")
    private Date archiveDt;

    @ListOnly(key = "fileArchiveId", property = "fieldName")
    private String fieldName;

    @ListOnly(key = "fileArchiveId", property = "fieldType")
    private ArchivingFieldType fieldType;

    @ListOnly(key = "fileArchiveId", property = "recordName")
    private String recordName;

    @ListOnly(key = "fileArchiveId", property = "localArchivePath")
    private String localArchivePath;

    @ListOnly(key = "fileArchiveId", property = "localArchiveDateFormat")
    private String localArchiveDateFormat;

    @ListOnly(key = "fileArchiveId", property = "filename")
    private String filename;

    @ListOnly(key = "fileArchiveId", property = "backupFileTransferCfgId")
    private Long backupFileTransferCfgId;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getFileArchiveId() {
        return fileArchiveId;
    }

    public void setFileArchiveId(Long fileArchiveId) {
        this.fileArchiveId = fileArchiveId;
    }

    public Long getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(Long fileIndex) {
        this.fileIndex = fileIndex;
    }

    public Long getArchivedItemId() {
        return archivedItemId;
    }

    public void setArchivedItemId(Long archivedItemId) {
        this.archivedItemId = archivedItemId;
    }

    public Integer getArchivedItemLength() {
        return archivedItemLength;
    }

    public void setArchivedItemLength(Integer archivedItemLength) {
        this.archivedItemLength = archivedItemLength;
    }

    public Date getArchiveDt() {
        return archiveDt;
    }

    public void setArchiveDt(Date archiveDt) {
        this.archiveDt = archiveDt;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getBackupFileTransferCfgId() {
        return backupFileTransferCfgId;
    }

    public void setBackupFileTransferCfgId(Long backupFileTransferCfgId) {
        this.backupFileTransferCfgId = backupFileTransferCfgId;
    }
}
