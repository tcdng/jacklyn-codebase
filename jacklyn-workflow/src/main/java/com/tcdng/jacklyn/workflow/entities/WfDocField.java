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

package com.tcdng.jacklyn.workflow.entities;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.constant.DataType;

/**
 * Workflow document field definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "JKWFDOCFIELD", uniqueConstraints = { @UniqueConstraint({ "wfDocId", "name" }),
        @UniqueConstraint({ "wfDocId", "description" }) })
public class WfDocField extends BaseEntity {

    @ForeignKey(WfDoc.class)
    private Long wfDocId;

    @ForeignKey(nullable = true)
    private DataType dataType;

    @Column(name = "PARENT_FIELD_NM", length = 32, nullable = true)
    private String parentName;

    @Column(name = "FIELD_NM", length = 32)
    private String name;

    @Column(name = "FIELD_DESC", length = 64)
    private String description;

    @Column(name = "LIST_FG")
    private Boolean list;
    
    @ListOnly(key = "wfDocId", property = "name")
    private String wfDocName;

    @ListOnly(key = "wfDocId", property = "description")
    private String wfDocDesc;

    @ListOnly(key = "dataType", property = "description")
    private String typeDesc;

    @Override
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWfDocId() {
        return wfDocId;
    }

    public void setWfDocId(Long wfDocId) {
        this.wfDocId = wfDocId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Boolean getList() {
        return list;
    }

    public void setList(Boolean list) {
        this.list = list;
    }

    public String getWfDocName() {
        return wfDocName;
    }

    public void setWfDocName(String wfDocName) {
        this.wfDocName = wfDocName;
    }

    public String getWfDocDesc() {
        return wfDocDesc;
    }

    public void setWfDocDesc(String wfDocDesc) {
        this.wfDocDesc = wfDocDesc;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

}
