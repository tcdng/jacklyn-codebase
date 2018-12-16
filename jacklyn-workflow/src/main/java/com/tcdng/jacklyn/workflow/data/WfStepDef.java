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

/**
 * Workflow step definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfStepDef extends BaseLabelWfDef {

	private static final long serialVersionUID = 1353403743220906693L;

	private Long wfTemplateId;

	private String globalTemplateName;

	private String globalFormName;

	private String globalName;

	private String docViewer;

	private WorkflowStepType stepType;

	private WorkflowParticipantType participantType;

	private List<WfEnrichmentDef> enrichmentList;

	private List<WfRoutingDef> routingList;

	private List<WfRecordActionDef> recordActionList;

	private List<WfUserActionDef> userActionList;

	private List<WfFormPrivilegeDef> formPrivilegeList;

	private List<WfAlertDef> alertList;

	private List<WfPolicyDef> policyList;

	private Map<String, WfUserActionDef> userActions;

	private int itemsPerSession;

	private int expiryHours;

	private boolean audit;

	private boolean branchOnly;

	private boolean includeForwarder;

	public WfStepDef(Long wfTemplateId, String globalTemplateName, String globalFormName,
			String globalName, String docViewer, String name, String description, String label,
			WorkflowStepType stepType, WorkflowParticipantType participantType,
			List<WfEnrichmentDef> enrichmentList, List<WfRoutingDef> routingList,
			List<WfRecordActionDef> recordActionList, List<WfUserActionDef> userActionList,
			List<WfFormPrivilegeDef> formPrivilegeList, List<WfAlertDef> alertList,
			List<WfPolicyDef> policyList, int itemsPerSession, int expiryHours, boolean audit,
			boolean branchOnly, boolean includeForwarder) {
		super(name, description, label);
		this.wfTemplateId = wfTemplateId;
		this.globalTemplateName = globalTemplateName;
		this.globalFormName = globalFormName;
		this.globalName = globalName;
		this.docViewer = docViewer;
		this.stepType = stepType;
		this.participantType = participantType;
		this.itemsPerSession = itemsPerSession;
		this.expiryHours = expiryHours;
		this.audit = audit;
		this.branchOnly = branchOnly;
		this.includeForwarder = includeForwarder;

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

	public String getGlobalTemplateName() {
		return globalTemplateName;
	}

	public String getGlobalFormName() {
		return globalFormName;
	}

	public String getGlobalName() {
		return globalName;
	}

	public String getDocViewer() {
		return docViewer;
	}

	public WorkflowStepType getStepType() {
		return stepType;
	}

	public WorkflowParticipantType getParticipantType() {
		return participantType;
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

	public int getExpiryHours() {
		return expiryHours;
	}

	public boolean isAudit() {
		return audit;
	}

	public boolean isBranchOnly() {
		return branchOnly;
	}

	public boolean isIncludeForwarder() {
		return includeForwarder;
	}

	public boolean isStart() {
		return stepType.isStart();
	}

	public boolean isManual() {
		return stepType.isManual();
	}

	public boolean isReceptacle() {
		return stepType.isReceptacle();
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

	public Set<String> getWfActionNames() {
		return userActions.keySet();
	}

	public WfUserActionDef getWfUserActionDef(String actionName) throws UnifyException {
		WfUserActionDef wfUserActionDef = userActions.get(actionName);
		if (wfUserActionDef == null) {
			throw new UnifyException(
					WorkflowModuleErrorConstants.WORKFLOW_STEP_ACTION_WITH_NAME_UNKNOWN,
					getDescription(), actionName);
		}

		return wfUserActionDef;
	}

}