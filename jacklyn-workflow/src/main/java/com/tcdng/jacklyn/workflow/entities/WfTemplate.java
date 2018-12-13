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
import com.tcdng.jacklyn.common.entities.BaseStatusEntity;
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
@Managed(module = WorkflowModuleNameConstants.WORKFLOW_MODULE, title = "Workflow Template",
		reportable = true, auditable = true)
@Table(name = "WFTEMPLATE", uniqueConstraints = { @UniqueConstraint({ "wfCategoryId", "name" }),
		@UniqueConstraint({ "wfCategoryId", "description" }) })
public class WfTemplate extends BaseStatusEntity {

	@ForeignKey(WfCategory.class)
	private Long wfCategoryId;

	@Column(name = "WFTEMPLATE_NM", length = 32)
	private String name;

	@Column(name = "WFTEMPLATE_DESC", length = 64)
	private String description;

	@Column(name = "MANUAL_OPTION_FG")
	private Boolean manualOption;

	@ChildList
	private List<WfStep> stepList;

	@ListOnly(key = "wfCategoryId", property = "name")
	private String wfCategoryName;

	@ListOnly(key = "wfCategoryId", property = "description")
	private String wfCategoryDesc;

	@ListOnly(key = "wfCategoryId", property = "updateDt")
	private Date updateDt;

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

	public List<WfStep> getStepList() {
		return stepList;
	}

	public void setStepList(List<WfStep> stepList) {
		this.stepList = stepList;
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

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

}
