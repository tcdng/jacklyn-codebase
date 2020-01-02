/*
 * Copyright 2018-2020 The Code Department.
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
package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.shared.system.SystemParamType;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity for storing system parameter information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SystemModuleNameConstants.SYSTEM_MODULE, title = "System Parameter", reportable = true,
        auditable = true)
@Table(name = "JKSYSPARAMETER", uniqueConstraints = { @UniqueConstraint({ "moduleId", "name" }) })
public class SystemParameter extends BaseEntity {

    @ForeignKey(Module.class)
    private Long moduleId;

    @ForeignKey(name = "PARAM_TYPE")
    private SystemParamType type;

    @Column(name = "SYSPARAMETER_NM", length = 32)
    private String name;

    @Column(name = "SYSPARAMETER_DESC", length = 64)
    private String description;

    @Column(name = "PARAM_VALUE", length = 192, nullable=true)
    private String value;

    @Column(length = 128)
    private String editor;

    @Column
    private Boolean control;

    @Column
    private Boolean editable;

    @ListOnly(name = "MODULE_NM", key = "moduleId", property = "name")
    private String moduleName;

    @ListOnly(name = "MODULE_DESC", key = "moduleId", property = "description")
    private String moduleDesc;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public void setType(SystemParamType type) {
        this.type = type;
    }

    public SystemParamType getType() {
        return type;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Boolean getControl() {
        return control;
    }

    public void setControl(Boolean control) {
        this.control = control;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
