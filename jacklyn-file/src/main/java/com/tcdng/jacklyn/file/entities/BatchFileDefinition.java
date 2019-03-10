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

import java.util.List;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.file.constants.FileModuleNameConstants;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * File format definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = FileModuleNameConstants.FILE_MODULE, title = "Batch File Definition", reportable = true,
        auditable = true)
@Table(name = "JKBATCHFILEDEF",
        uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class BatchFileDefinition extends BaseVersionedStatusEntity {

    @Column(name = "BATCHFILEDEFINITION_NM")
    private String name;

    @Column(name = "BATCHFILEDEFINITION_DESC", length = 64)
    private String description;

    @Column
    private boolean skipFirst;

    @ChildList
    private List<BatchFileFieldDefinition> fieldDefList;

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

    public boolean isSkipFirst() {
        return skipFirst;
    }

    public void setSkipFirst(boolean skipFirst) {
        this.skipFirst = skipFirst;
    }

    public List<BatchFileFieldDefinition> getFieldDefList() {
        return fieldDefList;
    }

    public void setFieldDefList(List<BatchFileFieldDefinition> fieldDefList) {
        this.fieldDefList = fieldDefList;
    }
}
