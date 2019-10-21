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
package com.tcdng.jacklyn.workflow.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepType;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Workflow step definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfStepDef extends BaseLabelWfDef {

    private Long wfTemplateId;

    private String templateGlobalName;

    private String globalName;

    private String workAssignerName;

    private WorkflowStepType stepType;

    private WorkflowParticipantType participantType;

    private List<WfBranchDef> branchList;

    private List<WfEnrichmentDef> enrichmentList;

    private List<WfRoutingDef> routingList;

    private List<WfRecordActionDef> recordActionList;

    private List<WfUserActionDef> userActionList;

    private List<WfFormPrivilegeDef> formPrivilegeList;

    private List<WfAlertDef> alertList;

    private List<WfPolicyDef> policyList;

    private Map<String, WfUserActionDef> userActions;

    private int itemsPerSession;

    private long expiryMilliSec;

    private boolean audit;

    private boolean branchOnly;

    private boolean departmentOnly;

    private boolean includeForwarder;

    private long versionTimestamp;

    public WfStepDef(Long wfTemplateId, String templateGlobalName, String globalName, String name, String description,
            String label, String workAssignerName, WorkflowStepType stepType, WorkflowParticipantType participantType,
            List<WfBranchDef> branchList, List<WfEnrichmentDef> enrichmentList, List<WfRoutingDef> routingList,
            List<WfRecordActionDef> recordActionList, List<WfUserActionDef> userActionList,
            List<WfFormPrivilegeDef> formPrivilegeList, List<WfAlertDef> alertList, List<WfPolicyDef> policyList,
            int itemsPerSession, long expiryMilliSec, boolean audit, boolean branchOnly, boolean departmentOnly,
            boolean includeForwarder, long versionTimestamp) {
        super(name, description, label);
        this.wfTemplateId = wfTemplateId;
        this.templateGlobalName = templateGlobalName;
        this.globalName = globalName;
        this.workAssignerName = workAssignerName;
        this.stepType = stepType;
        this.participantType = participantType;
        this.itemsPerSession = itemsPerSession;
        this.expiryMilliSec = expiryMilliSec;
        this.audit = audit;
        this.branchOnly = branchOnly;
        this.departmentOnly = departmentOnly;
        this.includeForwarder = includeForwarder;
        this.versionTimestamp = versionTimestamp;

        this.branchList = DataUtils.unmodifiableList(branchList);
        this.enrichmentList = DataUtils.unmodifiableList(enrichmentList);
        this.routingList = DataUtils.unmodifiableList(routingList);
        this.recordActionList = DataUtils.unmodifiableList(recordActionList);
        this.userActionList = DataUtils.unmodifiableList(userActionList);
        if (!this.userActionList.isEmpty()) {
            this.userActions = new HashMap<String, WfUserActionDef>();
            for (WfUserActionDef wfUserActionDef : userActionList) {
                this.userActions.put(wfUserActionDef.getName(), wfUserActionDef);
            }
        } else {
            this.userActions = Collections.emptyMap();
        }

        this.formPrivilegeList = DataUtils.unmodifiableList(formPrivilegeList);
        this.alertList = DataUtils.unmodifiableList(alertList);
        this.policyList = DataUtils.unmodifiableList(policyList);
    }

    public Long getWfTemplateId() {
        return wfTemplateId;
    }

    public String getTemplateGlobalName() {
        return templateGlobalName;
    }

    public String getGlobalName() {
        return globalName;
    }

    public String getWorkAssignerName() {
        return workAssignerName;
    }

    public WorkflowStepType getStepType() {
        return stepType;
    }

    public WorkflowParticipantType getParticipantType() {
        return participantType;
    }

    public List<WfBranchDef> getBranchList() {
        return branchList;
    }

    public int branchCount() {
        return branchList.size();
    }
    
    public List<WfEnrichmentDef> getEnrichmentList() {
        return enrichmentList;
    }

    public List<WfRoutingDef> getRoutingList() {
        return routingList;
    }

    public List<WfRecordActionDef> getRecordActionList() {
        return recordActionList;
    }

    public List<WfUserActionDef> getUserActionList() {
        return userActionList;
    }

    public List<WfFormPrivilegeDef> getFormPrivilegeList() {
        return formPrivilegeList;
    }

    public List<WfAlertDef> getAlertList() {
        return alertList;
    }

    public List<WfPolicyDef> getPolicyList() {
        return policyList;
    }

    public int getItemsPerSession() {
        return itemsPerSession;
    }

    public long getExpiryMilliSec() {
        return expiryMilliSec;
    }

    public boolean isExpiry() {
        return expiryMilliSec > 0;
    }

    public boolean isAudit() {
        return audit;
    }

    public boolean isBranchOnly() {
        return branchOnly;
    }

    public boolean isDepartmentOnly() {
        return departmentOnly;
    }

    public boolean isIncludeForwarder() {
        return includeForwarder;
    }

    public long getVersionTimestamp() {
        return versionTimestamp;
    }

    public boolean isStart() {
        return stepType.isStart();
    }

    public boolean isError() {
        return stepType.isError();
    }

    public boolean isManual() {
        return stepType.isManual();
    }

    public boolean isSplit() {
        return stepType.isSplit();
    }

    public boolean isMerge() {
        return stepType.isMerge();
    }

    public boolean isAutomatic() {
        return WorkflowStepType.AUTOMATIC.equals(stepType);
    }

    public boolean isInteractive() {
        return WorkflowStepType.INTERACTIVE.equals(stepType);
    }

    public boolean isUserInteractive() {
        return stepType.isUserInteractive();
    }

    public boolean isEnd() {
        return stepType.isEnd();
    }

    public boolean isWithWorkAssigner() {
        return !StringUtils.isBlank(workAssignerName);
    }
    
    public Set<String> getWfActionNames() {
        return userActions.keySet();
    }

    public WfUserActionDef getWfUserActionDef(String actionName) throws UnifyException {
        WfUserActionDef wfUserActionDef = userActions.get(actionName);
        if (wfUserActionDef == null) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_STEP_ACTION_WITH_NAME_UNKNOWN,
                    getDescription(), actionName);
        }

        return wfUserActionDef;
    }

}