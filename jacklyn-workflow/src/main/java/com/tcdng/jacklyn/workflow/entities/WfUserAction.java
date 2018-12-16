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
package com.tcdng.jacklyn.workflow.entities;

import java.util.List;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.constant.RequirementType;

/**
 * Workflow user action definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "WFUSERACTION", uniqueConstraints = { @UniqueConstraint({ "wfStepId", "name" }),
        @UniqueConstraint({ "wfStepId", "description" }) })
public class WfUserAction extends BaseEntity {

    @ForeignKey(WfStep.class)
    private Long wfStepId;

    @Column(name = "TARGET_STEP_NM", length = 32)
    private String targetWfStepName;

    @ForeignKey
    private RequirementType noteReqType;

    @Column(name = "USERACTION_NM", length = 32)
    private String name;

    @Column(name = "USERACTION_DESC", length = 64)
    private String description;

    @Column(name = "USERACTION_LABEL", length = 64, nullable = true)
    private String label;

    @Column(name = "VALIDATE_PAGE_FG")
    private Boolean validatePage;

    @ListOnly(key = "wfStepId", property = "name")
    private String wfStepName;

    @ListOnly(key = "wfStepId", property = "description")
    private String wfStepDesc;

    @ListOnly(key = "noteReqType", property = "description")
    private String noteReqTypeDesc;

    @ChildList
    private List<WfAttachmentCheck> attachmentCheckList;

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWfStepId() {
        return wfStepId;
    }

    public void setWfStepId(Long wfStepId) {
        this.wfStepId = wfStepId;
    }

    public String getTargetWfStepName() {
        return targetWfStepName;
    }

    public void setTargetWfStepName(String targetWfStepName) {
        this.targetWfStepName = targetWfStepName;
    }

    public RequirementType getNoteReqType() {
        return noteReqType;
    }

    public void setNoteReqType(RequirementType noteReqType) {
        this.noteReqType = noteReqType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<WfAttachmentCheck> getAttachmentCheckList() {
        return attachmentCheckList;
    }

    public void setAttachmentCheckList(List<WfAttachmentCheck> attachmentCheckList) {
        this.attachmentCheckList = attachmentCheckList;
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

    public String getNoteReqTypeDesc() {
        return noteReqTypeDesc;
    }

    public void setNoteReqTypeDesc(String noteReqTypeDesc) {
        this.noteReqTypeDesc = noteReqTypeDesc;
    }

    public Boolean getValidatePage() {
        return validatePage;
    }

    public void setValidatePage(Boolean validatePage) {
        this.validatePage = validatePage;
    }

}
