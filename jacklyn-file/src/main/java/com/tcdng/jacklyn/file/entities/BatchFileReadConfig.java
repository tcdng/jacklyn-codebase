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
package com.tcdng.jacklyn.file.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.file.constants.FileModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.batch.ConstraintAction;

/**
 * Batch file read configuration.
 *
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = FileModuleNameConstants.FILE_MODULE, title = "Batch File Read Configuration", reportable = true,
        auditable = true)
@Table(name = "BATCHFILEREADCONFIG", uniqueConstraints = { @UniqueConstraint({ "name" }) })
public class BatchFileReadConfig extends BaseVersionedStatusEntity {

    @ForeignKey(BatchFileDefinition.class)
    private Long batchFileDefinitionId;

    @ForeignKey
    private ConstraintAction constraintAction;

    @Column
    private String name;

    @Column(length = 64)
    private String description;

    @Column(length = 48)
    private String fileReader;

    @Column(length = 48)
    private String readProcessor;

    @ListOnly(key = "batchFileDefinitionId", property = "name")
    private String batchFileDefinitionName;

    @ListOnly(key = "batchFileDefinitionId", property = "description")
    private String batchFileDefinitionDesc;

    @ListOnly(key = "constraintAction", property = "description")
    private String constraintActionDesc;

    public Long getBatchFileDefinitionId() {
        return batchFileDefinitionId;
    }

    public void setBatchFileDefinitionId(Long batchFileDefinitionId) {
        this.batchFileDefinitionId = batchFileDefinitionId;
    }

    public ConstraintAction getConstraintAction() {
        return constraintAction;
    }

    public void setConstraintAction(ConstraintAction constraintAction) {
        this.constraintAction = constraintAction;
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

    public String getFileReader() {
        return fileReader;
    }

    public void setFileReader(String fileReader) {
        this.fileReader = fileReader;
    }

    public String getReadProcessor() {
        return readProcessor;
    }

    public void setReadProcessor(String readProcessor) {
        this.readProcessor = readProcessor;
    }

    public String getBatchFileDefinitionName() {
        return batchFileDefinitionName;
    }

    public void setBatchFileDefinitionName(String batchFileDefinitionName) {
        this.batchFileDefinitionName = batchFileDefinitionName;
    }

    public String getBatchFileDefinitionDesc() {
        return batchFileDefinitionDesc;
    }

    public void setBatchFileDefinitionDesc(String batchFileDefinitionDesc) {
        this.batchFileDefinitionDesc = batchFileDefinitionDesc;
    }

    public String getConstraintActionDesc() {
        return constraintActionDesc;
    }

    public void setConstraintActionDesc(String constraintActionDesc) {
        this.constraintActionDesc = constraintActionDesc;
    }
}
