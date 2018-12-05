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
 * Workflow form definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = WorkflowModuleNameConstants.WORKFLOW_MODULE, title = "Workflow Form", reportable = true)
@Table(name = "WFFORM", uniqueConstraints = { @UniqueConstraint({ "wfCategoryId", "name" }),
		@UniqueConstraint({ "wfCategoryId", "description" }) })
public class WfForm extends BaseStatusEntity {

	@ForeignKey(WfCategory.class)
	private Long wfCategoryId;

	@ForeignKey(WfDoc.class)
	private Long wfDocId;

	@Column(name = "FORM_NM", length = 32)
	private String name;

	@Column(name = "FORM_DESC", length = 64)
	private String description;

	@ChildList
	private List<WfFormTab> tabList;

	@ChildList
	private List<WfFormSection> sectionList;

	@ChildList
	private List<WfFormField> fieldList;

	@ListOnly(key = "wfCategoryId", property = "name")
	private String wfCategoryName;

	@ListOnly(key = "wfCategoryId", property = "description")
	private String wfCategoryDesc;

	@ListOnly(key = "wfCategoryId", property = "updateDt")
	private Date updateDt;

	@ListOnly(key = "wfDocId", property = "name")
	private String wfDocName;

	@ListOnly(key = "wfDocId", property = "description")
	private String wfDocDesc;

	@Override
	public String getDescription() {
		return this.description;
	}

	public Long getWfCategoryId() {
		return wfCategoryId;
	}

	public void setWfCategoryId(Long wfCategoryId) {
		this.wfCategoryId = wfCategoryId;
	}

	public Long getWfDocId() {
		return wfDocId;
	}

	public void setWfDocId(Long wfDocId) {
		this.wfDocId = wfDocId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<WfFormTab> getTabList() {
		return tabList;
	}

	public void setTabList(List<WfFormTab> tabList) {
		this.tabList = tabList;
	}

	public List<WfFormSection> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<WfFormSection> sectionList) {
		this.sectionList = sectionList;
	}

	public List<WfFormField> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<WfFormField> fieldList) {
		this.fieldList = fieldList;
	}

	public String getWfDocName() {
		return wfDocName;
	}

	public void setWfDocName(String wfDocName) {
		this.wfDocName = wfDocName;
	}

	public String getWfDocDesc() {
		return wfDocDesc;
	}

	public void setWfDocDesc(String wfDocDesc) {
		this.wfDocDesc = wfDocDesc;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

}
