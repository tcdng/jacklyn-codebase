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
package com.tcdng.jacklyn.workflow.entities;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.shared.workflow.WorkflowFormElementType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity that represents workflow step form privilege.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "JKWFFORMPRIVILEGE", uniqueConstraints = { @UniqueConstraint({ "wfStepId", "type", "wfFormElementName" }) })
public class WfFormPrivilege extends BaseEntity {

    @ForeignKey(WfStep.class)
    private Long wfStepId;

    @ForeignKey
    private WorkflowFormElementType type;

    @Column(name = "DOC_NM", length = 32)
    private String docName;

    @Column(name = "ELEMENT_NM")
    private String wfFormElementName;

    @Column(name = "VISIBLE_FG")
    private Boolean visible;

    @Column(name = "EDITABLE_FG")
    private Boolean editable;

    @Column(name = "DISABLED_FG")
    private Boolean disabled;

    @Column(name = "REQUIRED_FG")
    private Boolean required;

    @ListOnly(key = "wfStepId", property = "name")
    private String wfStepName;

    @ListOnly(key = "wfStepId", property = "description")
    private String wfStepDesc;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @Override
    public String getDescription() {
        return this.wfFormElementName;
    }

    public Long getWfStepId() {
        return wfStepId;
    }

    public void setWfStepId(Long wfStepId) {
        this.wfStepId = wfStepId;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public WorkflowFormElementType getType() {
        return type;
    }

    public void setType(WorkflowFormElementType type) {
        this.type = type;
    }

    public String getWfFormElementName() {
        return wfFormElementName;
    }

    public void setWfFormElementName(String wfFormElementName) {
        this.wfFormElementName = wfFormElementName;
    }

    public String getWfStepName() {
        return wfStepName;
    }

    public void setWfStepName(String wfStepName) {
        this.wfStepName = wfStepName;
    }

    public String getWfStepDesc() {
        return wfStepDesc;
    }

    public void setWfStepDesc(String wfStepDesc) {
        this.wfStepDesc = wfStepDesc;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }
}
