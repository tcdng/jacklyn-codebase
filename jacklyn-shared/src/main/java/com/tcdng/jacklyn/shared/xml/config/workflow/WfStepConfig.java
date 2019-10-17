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
package com.tcdng.jacklyn.shared.xml.config.workflow;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepPriority;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepType;
import com.tcdng.jacklyn.shared.xml.adapter.WorkflowParticipantTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.adapter.WorkflowStepPriorityXmlAdapter;
import com.tcdng.jacklyn.shared.xml.adapter.WorkflowStepTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Workflow step configuration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfStepConfig extends BaseConfig {

    private WorkflowStepType type;

    private WorkflowParticipantType participant;

    private WorkflowStepPriority priority;

    private String label;
    
    private String workAssigner;

    private Integer itemsPerSession;

    private Integer expiryHours;

    private Boolean audit;

    private Boolean branchOnly;

    private Boolean departmentOnly;

    private Boolean includeForwarder;

    private WfEnrichmentsConfig wfEnrichmentsConfig;

    private WfRoutingsConfig wfRoutingsConfig;

    private WfRecordActionsConfig wfRecordActionsConfig;

    private WfUserActionsConfig wfUserActionsConfig;

    private WfFormPrivilegesConfig wfFormPrivilegesConfig;

    private WfPoliciesConfig wfPoliciesConfig;

    private WfAlertsConfig wfAlertsConfig;

    public WfStepConfig() {
        this.type = WorkflowStepType.AUTOMATIC;
        this.participant = WorkflowParticipantType.NONE;
        this.priority = WorkflowStepPriority.NORMAL;
        this.audit = Boolean.FALSE;
        this.branchOnly = Boolean.FALSE;
        this.departmentOnly = Boolean.FALSE;
        this.includeForwarder = Boolean.FALSE;
        this.itemsPerSession = Integer.valueOf(0);
        this.expiryHours = Integer.valueOf(0);
    }

    public WorkflowStepType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(WorkflowStepTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(WorkflowStepType type) {
        if (type != null) {
            this.type = type;
        }
    }

    public WorkflowParticipantType getParticipant() {
        return participant;
    }

    @XmlJavaTypeAdapter(WorkflowParticipantTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setParticipant(WorkflowParticipantType participant) {
        if (participant != null) {
            this.participant = participant;
        }
    }

    public WorkflowStepPriority getPriority() {
        return priority;
    }

    @XmlJavaTypeAdapter(WorkflowStepPriorityXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setPriority(WorkflowStepPriority priority) {
        if (priority != null) {
            this.priority = priority;
        }
    }

    public String getLabel() {
        return label;
    }

    @XmlAttribute
    public void setLabel(String label) {
        this.label = label;
    }

    public String getWorkAssigner() {
        return workAssigner;
    }

    @XmlAttribute(name = "assigner")
    public void setWorkAssigner(String workAssigner) {
        this.workAssigner = workAssigner;
    }

    public Integer getItemsPerSession() {
        return itemsPerSession;
    }

    @XmlAttribute(name = "max-session-items", required = true)
    public void setItemsPerSession(Integer itemsPerSession) {
        this.itemsPerSession = itemsPerSession;
    }

    public Integer getExpiryHours() {
        return expiryHours;
    }

    @XmlAttribute(name = "expiry-hours", required = true)
    public void setExpiryHours(Integer expiryHours) {
        this.expiryHours = expiryHours;
    }

    public Boolean getAudit() {
        return audit;
    }

    @XmlAttribute
    public void setAudit(Boolean audit) {
        this.audit = audit;
    }

    public Boolean getBranchOnly() {
        return branchOnly;
    }

    @XmlAttribute(name = "branch-only")
    public void setBranchOnly(Boolean branchOnly) {
        this.branchOnly = branchOnly;
    }

    public Boolean getDepartmentOnly() {
        return departmentOnly;
    }

    @XmlAttribute(name = "department-only")
    public void setDepartmentOnly(Boolean departmentOnly) {
        this.departmentOnly = departmentOnly;
    }

    public Boolean getIncludeForwarder() {
        return includeForwarder;
    }

    @XmlAttribute(name = "include-forwarder")
    public void setIncludeForwarder(Boolean includeForwarder) {
        this.includeForwarder = includeForwarder;
    }

    public WfEnrichmentsConfig getWfEnrichmentsConfig() {
        return wfEnrichmentsConfig;
    }

    @XmlElement(name = "enrichments", required = true)
    public void setWfEnrichmentsConfig(WfEnrichmentsConfig wfEnrichmentsConfig) {
        this.wfEnrichmentsConfig = wfEnrichmentsConfig;
    }

    public WfPoliciesConfig getWfPoliciesConfig() {
        return wfPoliciesConfig;
    }

    @XmlElement(name = "policies", required = true)
    public void setWfPoliciesConfig(WfPoliciesConfig wfPoliciesConfig) {
        this.wfPoliciesConfig = wfPoliciesConfig;
    }

    public WfAlertsConfig getWfAlertsConfig() {
        return wfAlertsConfig;
    }

    @XmlElement(name = "alerts", required = true)
    public void setWfAlertsConfig(WfAlertsConfig wfAlertsConfig) {
        this.wfAlertsConfig = wfAlertsConfig;
    }

    public WfRoutingsConfig getWfRoutingsConfig() {
        return wfRoutingsConfig;
    }

    @XmlElement(name = "routings", required = true)
    public void setWfRoutingsConfig(WfRoutingsConfig wfRoutingsConfig) {
        this.wfRoutingsConfig = wfRoutingsConfig;
    }

    public WfRecordActionsConfig getWfRecordActionsConfig() {
        return wfRecordActionsConfig;
    }

    @XmlElement(name = "record-actions", required = true)
    public void setWfRecordActionsConfig(WfRecordActionsConfig wfRecordActionsConfig) {
        this.wfRecordActionsConfig = wfRecordActionsConfig;
    }

    public WfUserActionsConfig getWfUserActionsConfig() {
        return wfUserActionsConfig;
    }

    @XmlElement(name = "user-actions", required = true)
    public void setWfUserActionsConfig(WfUserActionsConfig wfUserActionsConfig) {
        this.wfUserActionsConfig = wfUserActionsConfig;
    }

    public WfFormPrivilegesConfig getWfFormPrivilegesConfig() {
        return wfFormPrivilegesConfig;
    }

    @XmlElement(name = "form-privileges")
    public void setWfFormPrivilegesConfig(WfFormPrivilegesConfig wfFormPrivilegesConfig) {
        this.wfFormPrivilegesConfig = wfFormPrivilegesConfig;
    }

}
