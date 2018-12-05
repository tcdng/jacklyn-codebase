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

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.workflow.entities.WfCategory;
import com.tcdng.jacklyn.workflow.entities.WfCategoryQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing workflow categories.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/workflow/wfcategory")
@UplBinding("web/workflow/upl/managewfcategory.upl")
public class WfCategoryController extends AbstractWorkflowRecordController<WfCategory> {

	private String searchName;

	private String searchDescription;

	private RecordStatus searchStatus;

	public WfCategoryController() {
		super(WfCategory.class, "workflow.wfcategory.hint", ManageRecordModifier.SECURE
				| ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
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

	public RecordStatus getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(RecordStatus searchStatus) {
		this.searchStatus = searchStatus;
	}

	@Override
	protected List<WfCategory> find() throws UnifyException {
		WfCategoryQuery query = new WfCategoryQuery();
		if (QueryUtils.isValidStringCriteria(searchName)) {
			query.nameLike(searchName);
		}

		if (QueryUtils.isValidStringCriteria(searchDescription)) {
			query.descriptionLike(searchDescription);
		}

		if (searchStatus != null) {
			query.status(searchStatus);
		}

		query.order("description").ignoreEmptyCriteria(true);
		return getWorkflowModule().findWfCategories(query);
	}

	@Override
	protected WfCategory find(Long wfCategoryId) throws UnifyException {
		return getWorkflowModule().findWfCategory(wfCategoryId);
	}

	@Override
	protected WfCategory prepareCreate() throws UnifyException {
		return new WfCategory();
	}

	@Override
	protected Object create(WfCategory wfCategoryData) throws UnifyException {
		return null;
	}

	@Override
	protected int update(WfCategory wfCategoryData) throws UnifyException {
		return getWorkflowModule().updateWfCategory(wfCategoryData);
	}

	@Override
	protected int delete(WfCategory record) throws UnifyException {
		return 0;
	}

}
