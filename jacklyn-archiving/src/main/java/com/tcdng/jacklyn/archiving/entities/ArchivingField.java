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

import com.tcdng.jacklyn.archiving.constants.ArchivingModuleNameConstants;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseInstallEntity;
import com.tcdng.jacklyn.shared.archiving.ArchivingFieldType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Archive field information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = ArchivingModuleNameConstants.ARCHIVING_MODULE, title = "Archiving Field")
@Table(name = "ARCHIVINGFLD", uniqueConstraints = { @UniqueConstraint({ "archivableDefId", "fieldName" }) })
public class ArchivingField extends BaseInstallEntity {

    @ForeignKey(ArchivableDefinition.class)
    private Long archivableDefId;

    @ForeignKey
    private ArchivingFieldType fieldType;

    @Column(name = "FIELD_NM", length = 32)
    private String fieldName;

    @Column(name = "FIELD_DESC", length = 64)
    private String description;

    @ListOnly(key = "archivableDefId", property = "recordType")
    private String recordName;

    @ListOnly(key = "fieldType", property = "description")
    private String fieldTypeDesc;

    @Override
    public String getDescription() {
        return description;
    }

    public Long getArchivableDefId() {
        return archivableDefId;
    }

    public void setArchivableDefId(Long archivableDefId) {
        this.archivableDefId = archivableDefId;
    }

    public ArchivingFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(ArchivingFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getFieldTypeDesc() {
        return fieldTypeDesc;
    }

    public void setFieldTypeDesc(String fieldTypeDesc) {
        this.fieldTypeDesc = fieldTypeDesc;
    }
}
