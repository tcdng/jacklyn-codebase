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

import java.util.Date;
import java.util.List;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity that represents workflow template.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = WorkflowModuleNameConstants.WORKFLOW_MODULE, title = "Workflow Template", reportable = true,
        auditable = true)
@Table(name = "WFTEMPLATE", uniqueConstraints = { @UniqueConstraint({ "wfCategoryId", "name" }),
        @UniqueConstraint({ "wfCategoryId", "description" }) })
public class WfTemplate extends BaseEntity {

    @ForeignKey(WfCategory.class)
    private Long wfCategoryId;

    @Column(name = "DOCUMENT_NM", length = 32)
    private String wfDocName;

    @Column(name = "TEMPLATE_NM", length = 32)
    private String name;

    @Column(name = "TEMPLATE_DESC", length = 64)
    private String description;

    @Column(name = "MANUAL_OPTION_FG")
    private Boolean manualOption;

    @ListOnly(key = "wfCategoryId", property = "name")
    private String wfCategoryName;

    @ListOnly(key = "wfCategoryId", property = "description")
    private String wfCategoryDesc;

    @ListOnly(key = "wfCategoryId", property = "version")
    private String wfCategoryVersion;

    @ListOnly(key = "wfCategoryId", property = "status")
    private RecordStatus wfCategoryStatus;

    @ListOnly(key = "wfCategoryId", property = "statusDesc")
    private String wfCategoryStatusDesc;

    @ListOnly(key = "wfCategoryId", property = "updateDt")
    private Date wfCategoryUpdateDt;

    @ChildList
    private List<WfMessage> messageList;

    @ChildList
    private List<WfStep> stepList;

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWfCategoryId() {
        return wfCategoryId;
    }

    public void setWfCategoryId(Long wfCategoryId) {
        this.wfCategoryId = wfCategoryId;
    }

    public String getWfDocName() {
        return wfDocName;
    }

    public void setWfDocName(String wfDocName) {
        this.wfDocName = wfDocName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getManualOption() {
        return manualOption;
    }

    public void setManualOption(Boolean manualOption) {
        this.manualOption = manualOption;
    }

    public String getWfCategoryName() {
        return wfCategoryName;
    }

    public void setWfCategoryName(String wfCategoryName) {
        this.wfCategoryName = wfCategoryName;
    }

    public String getWfCategoryDesc() {
        return wfCategoryDesc;
    }

    public void setWfCategoryDesc(String wfCategoryDesc) {
        this.wfCategoryDesc = wfCategoryDesc;
    }

    public String getWfCategoryVersion() {
        return wfCategoryVersion;
    }

    public void setWfCategoryVersion(String wfCategoryVersion) {
        this.wfCategoryVersion = wfCategoryVersion;
    }

    public RecordStatus getWfCategoryStatus() {
        return wfCategoryStatus;
    }

    public void setWfCategoryStatus(RecordStatus wfCategoryStatus) {
        this.wfCategoryStatus = wfCategoryStatus;
    }

    public String getWfCategoryStatusDesc() {
        return wfCategoryStatusDesc;
    }

    public void setWfCategoryStatusDesc(String wfCategoryStatusDesc) {
        this.wfCategoryStatusDesc = wfCategoryStatusDesc;
    }

    public Date getWfCategoryUpdateDt() {
        return wfCategoryUpdateDt;
    }

    public void setWfCategoryUpdateDt(Date wfCategoryUpdateDt) {
        this.wfCategoryUpdateDt = wfCategoryUpdateDt;
    }

    public List<WfMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<WfMessage> messageList) {
        this.messageList = messageList;
    }

    public List<WfStep> getStepList() {
        return stepList;
    }

    public void setStepList(List<WfStep> stepList) {
        this.stepList = stepList;
    }

}
