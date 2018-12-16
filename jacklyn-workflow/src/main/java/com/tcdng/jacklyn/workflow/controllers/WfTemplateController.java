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
package com.tcdng.jacklyn.workflow.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.annotation.CrudPanelList;
import com.tcdng.jacklyn.common.annotation.SessionLoading;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.workflow.entities.WfTemplate;
import com.tcdng.jacklyn.workflow.entities.WfTemplateLargeData;
import com.tcdng.jacklyn.workflow.entities.WfTemplateQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing workflow templates.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/workflow/wftemplate")
@UplBinding("web/workflow/upl/managewftemplate.upl")
@SessionLoading(crudPanelLists = {
		@CrudPanelList(panel = "frmWfStepListPanel", field = "largeData.stepList"),
		@CrudPanelList(panel = "frmWfEnrichmentListPanel", field = "largeData.enrichmentList"),
		@CrudPanelList(panel = "frmWfRoutingListPanel", field = "largeData.routingList"),
		@CrudPanelList(panel = "frmWfUserActionListPanel", field = "largeData.userActionList"),
		@CrudPanelList(panel = "frmWfRecordActionListPanel", field = "largeData.recordActionList"),
		@CrudPanelList(panel = "frmWfFormPrivilegeListPanel",
				field = "largeData.formPrivilegeList"),
		@CrudPanelList(panel = "frmWfPolicyListPanel", field = "largeData.policyList"),
		@CrudPanelList(panel = "frmWfAlertListPanel", field = "largeData.alertList") })
public class WfTemplateController extends AbstractWorkflowRecordController<WfTemplate> {

	private Long searchWfCategoryId;

	private String searchName;

	private String searchDescription;

	private WfTemplateLargeData largeData;

	public WfTemplateController() {
		super(WfTemplate.class, "workflow.wftemplate.hint", ManageRecordModifier.SECURE
				| ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
	}

	public Long getSearchWfCategoryId() {
		return searchWfCategoryId;
	}

	public void setSearchWfCategoryId(Long searchWfCategoryId) {
		this.searchWfCategoryId = searchWfCategoryId;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getSearchDescription() {
		return searchDescription;
	}

	public void setSearchDescription(String searchDescription) {
		this.searchDescription = searchDescription;
	}

	public WfTemplateLargeData getLargeData() {
		return largeData;
	}

	@Override
	protected List<WfTemplate> find() throws UnifyException {
		WfTemplateQuery query = new WfTemplateQuery();
		if (QueryUtils.isValidLongCriteria(searchWfCategoryId)) {
			query.wfCategoryId(searchWfCategoryId);
		}

		if (QueryUtils.isValidStringCriteria(searchName)) {
			query.nameLike(searchName);
		}

		if (QueryUtils.isValidStringCriteria(searchDescription)) {
			query.descriptionLike(searchDescription);
		}

		query.ignoreEmptyCriteria(true);
		return getWorkflowModule().findWfTemplates(query);
	}

	@Override
	protected WfTemplate find(Long id) throws UnifyException {
		largeData = getWorkflowModule().findLargeWfTemplate(id);
		return largeData.getData();
	}

	@Override
	protected WfTemplate prepareCreate() throws UnifyException {
		return null;
	}

	@Override
	protected Object create(WfTemplate wfDefData) throws UnifyException {
		return null;
	}

	@Override
	protected int update(WfTemplate wfDefData) throws UnifyException {
		return 0;
	}

	@Override
	protected int delete(WfTemplate wfDefData) throws UnifyException {
		return 0;
	}
}
