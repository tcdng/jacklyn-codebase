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

import com.tcdng.jacklyn.common.entities.BaseLargeData;

/**
 * Workflow template large data.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfTemplateLargeData extends BaseLargeData<WfTemplate> {

	private List<WfStep> stepList;

	private List<WfEnrichment> enrichmentList;

	private List<WfRouting> routingList;

	private List<WfRecordAction> recordActionList;

	private List<WfUserAction> userActionList;

	private List<WfFormPrivilege> formPrivilegeList;

	private List<WfPolicy> policyList;

	private List<WfAlert> alertList;

	public WfTemplateLargeData() {
		this(new WfTemplate());
	}

	public WfTemplateLargeData(WfTemplate wfTemplateData) {
		super(wfTemplateData);
	}

	public WfTemplateLargeData(WfTemplate wfTemplateData, List<WfStep> stepList,
			List<WfEnrichment> enrichmentList, List<WfRouting> routingList,
			List<WfRecordAction> recordActionList, List<WfUserAction> userActionList,
			List<WfFormPrivilege> formPrivilegeList, List<WfPolicy> policyList,
			List<WfAlert> alertList) {
		super(wfTemplateData);
		this.stepList = stepList;
		this.enrichmentList = enrichmentList;
		this.routingList = routingList;
		this.recordActionList = recordActionList;
		this.userActionList = userActionList;
		this.formPrivilegeList = formPrivilegeList;
		this.policyList = policyList;
		this.alertList = alertList;
	}

	public List<WfStep> getStepList() {
		return stepList;
	}

	public List<WfRouting> getRoutingList() {
		return routingList;
	}

	public List<WfRecordAction> getRecordActionList() {
		return recordActionList;
	}

	public List<WfUserAction> getUserActionList() {
		return userActionList;
	}

	public List<WfFormPrivilege> getFormPrivilegeList() {
		return formPrivilegeList;
	}

	public List<WfEnrichment> getEnrichmentList() {
		return enrichmentList;
	}

	public List<WfPolicy> getPolicyList() {
		return policyList;
	}

	public List<WfAlert> getAlertList() {
		return alertList;
	}

}
