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

import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepPriority;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepType;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity that represents workflow step definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(
        name = "JKWFSTEP", uniqueConstraints = { @UniqueConstraint({ "wfTemplateId", "name" }),
                @UniqueConstraint({ "wfTemplateId", "description" }) })
public class WfStep extends BaseEntity {

    @ForeignKey(WfTemplate.class)
    private Long wfTemplateId;

    @ForeignKey
    private WorkflowStepType stepType;

    @ForeignKey
    private WorkflowParticipantType participantType;

    @ForeignKey
    private WorkflowStepPriority priorityLevel;

    @Column(name = "STEP_NM", length = 32)
    private String name;

    @Column(name = "STEP_DESC", length = 64)
    private String description;

    @Column(name = "STEP_LABEL", length = 64, nullable = true)
    private String label;

    @Column(length = 64, nullable = true)
    private String workAssigner;
    
    @Column(length = 64, nullable = true)
    private String branch;
    
    @Column(length = 64, nullable = true)
    private String origin;

    @Column
    private Integer itemsPerSession;

    @Column
    private Integer expiryHours;

    @Column(name = "AUDIT_FG")
    private Boolean audit;

    @Column(name = "BRANCH_ONLY_FG")
    private Boolean branchOnly;

    @Column(name = "DEPARTMENT_ONLY_FG")
    private Boolean departmentOnly;

    @Column(name = "INCLUDE_FORWARDER_FG")
    private Boolean includeForwarder;

    @ChildList
    private List<WfBranch> branchList;

    @ChildList
    private List<WfEnrichment> enrichmentList;

    @ChildList
    private List<WfRouting> routingList;

    @ChildList
    private List<WfRecordAction> recordActionList;

    @ChildList
    private List<WfUserAction> userActionList;

    @ChildList
    private List<WfFormPrivilege> formPrivilegeList;

    @ChildList
    private List<WfAlert> alertList;

    @ChildList
    private List<WfPolicy> policyList;

    @ListOnly(key = "wfTemplateId", property = "name")
    private String wfTemplateName;

    @ListOnly(key = "wfTemplateId", property = "wfCategoryId")
    private Long wfCategoryId;

    @ListOnly(key = "wfTemplateId", property = "wfCategoryName")
    private String wfCategoryName;

    @ListOnly(key = "wfTemplateId", property = "wfCategoryStatus")
    private RecordStatus wfCategoryStatus;

    @ListOnly(key = "wfTemplateId", property = "wfCategoryVersion")
    private String wfCategoryVersion;

    @ListOnly(key = "stepType", property = "description")
    private String stepTypeDesc;

    @ListOnly(key = "participantType", property = "description")
    private String participantTypeDesc;

    @ListOnly(key = "priorityLevel", property = "description")
    private String priorityLevelDesc;

    @Override
    public String getDescription() {
        return description;
    }

    public String getExtDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(wfTemplateName).append("::").append(description);
        return sb.toString();
    }

    public Long getWfTemplateId() {
        return wfTemplateId;
    }

    public void setWfTemplateId(Long wfTemplateId) {
        this.wfTemplateId = wfTemplateId;
    }

    public WorkflowStepType getStepType() {
        return stepType;
    }

    public void setStepType(WorkflowStepType stepType) {
        this.stepType = stepType;
    }

    public WorkflowParticipantType getParticipantType() {
        return participantType;
    }

    public void setParticipantType(WorkflowParticipantType participantType) {
        this.participantType = participantType;
    }

    public WorkflowStepPriority getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(WorkflowStepPriority priorityLevel) {
        this.priorityLevel = priorityLevel;
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

    public String getWorkAssigner() {
        return workAssigner;
    }

    public void setWorkAssigner(String workAssigner) {
        this.workAssigner = workAssigner;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getItemsPerSession() {
        return itemsPerSession;
    }

    public void setItemsPerSession(Integer itemsPerSession) {
        this.itemsPerSession = itemsPerSession;
    }

    public Integer getExpiryHours() {
        return expiryHours;
    }

    public void setExpiryHours(Integer expiryHours) {
        this.expiryHours = expiryHours;
    }

    public Boolean getAudit() {
        return audit;
    }

    public void setAudit(Boolean audit) {
        this.audit = audit;
    }

    public Boolean getBranchOnly() {
        return branchOnly;
    }

    public void setBranchOnly(Boolean branchOnly) {
        this.branchOnly = branchOnly;
    }

    public Boolean getDepartmentOnly() {
        return departmentOnly;
    }

    public void setDepartmentOnly(Boolean departmentOnly) {
        this.departmentOnly = departmentOnly;
    }

    public Boolean getIncludeForwarder() {
        return includeForwarder;
    }

    public void setIncludeForwarder(Boolean includeForwarder) {
        this.includeForwarder = includeForwarder;
    }

    public List<WfBranch> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<WfBranch> branchList) {
        this.branchList = branchList;
    }

    public List<WfEnrichment> getEnrichmentList() {
        return enrichmentList;
    }

    public void setEnrichmentList(List<WfEnrichment> enrichmentList) {
        this.enrichmentList = enrichmentList;
    }

    public List<WfRouting> getRoutingList() {
        return routingList;
    }

    public void setRoutingList(List<WfRouting> routingList) {
        this.routingList = routingList;
    }

    public List<WfRecordAction> getRecordActionList() {
        return recordActionList;
    }

    public void setRecordActionList(List<WfRecordAction> recordActionList) {
        this.recordActionList = recordActionList;
    }

    public List<WfUserAction> getUserActionList() {
        return userActionList;
    }

    public void setUserActionList(List<WfUserAction> userActionList) {
        this.userActionList = userActionList;
    }

    public List<WfFormPrivilege> getFormPrivilegeList() {
        return formPrivilegeList;
    }

    public void setFormPrivilegeList(List<WfFormPrivilege> formPrivilegeList) {
        this.formPrivilegeList = formPrivilegeList;
    }

    public List<WfAlert> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<WfAlert> alertList) {
        this.alertList = alertList;
    }

    public List<WfPolicy> getPolicyList() {
        return policyList;
    }

    public void setPolicyList(List<WfPolicy> policyList) {
        this.policyList = policyList;
    }

    public String getWfTemplateName() {
        return wfTemplateName;
    }

    public void setWfTemplateName(String wfTemplateName) {
        this.wfTemplateName = wfTemplateName;
    }

    public Long getWfCategoryId() {
        return wfCategoryId;
    }

    public void setWfCategoryId(Long wfCategoryId) {
        this.wfCategoryId = wfCategoryId;
    }

    public String getWfCategoryName() {
        return wfCategoryName;
    }

    public void setWfCategoryName(String wfCategoryName) {
        this.wfCategoryName = wfCategoryName;
    }

    public String getStepTypeDesc() {
        return stepTypeDesc;
    }

    public void setStepTypeDesc(String stepTypeDesc) {
        this.stepTypeDesc = stepTypeDesc;
    }

    public String getParticipantTypeDesc() {
        return participantTypeDesc;
    }

    public void setParticipantTypeDesc(String participantTypeDesc) {
        this.participantTypeDesc = participantTypeDesc;
    }

    public String getPriorityLevelDesc() {
        return priorityLevelDesc;
    }

    public void setPriorityLevelDesc(String priorityLevelDesc) {
        this.priorityLevelDesc = priorityLevelDesc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RecordStatus getWfCategoryStatus() {
        return wfCategoryStatus;
    }

    public void setWfCategoryStatus(RecordStatus wfCategoryStatus) {
        this.wfCategoryStatus = wfCategoryStatus;
    }

    public String getWfCategoryVersion() {
        return wfCategoryVersion;
    }

    public void setWfCategoryVersion(String wfCategoryVersion) {
        this.wfCategoryVersion = wfCategoryVersion;
    }

}
