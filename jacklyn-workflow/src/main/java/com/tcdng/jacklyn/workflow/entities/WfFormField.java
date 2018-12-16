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

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Workflow form field definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "WFFORMFIELD", uniqueConstraints = { @UniqueConstraint({ "wfFormId", "binding" }) })
public class WfFormField extends BaseEntity {

	@ForeignKey(WfForm.class)
	private Long wfFormId;

	@Column(name = "SECTION_NM", length = 32)
	private String sectionName;

	@Column
	private String binding;

	@Column(name = "FIELD_LABEL", length = 64, nullable = true)
	private String label;

	@Column(length = 128, nullable = true)
	private String editorUpl;

	@Column(name = "REQUIRED_FG")
	private Boolean required;

	@ListOnly(key = "wfFormId", property = "name")
	private String wfFormName;

	@ListOnly(key = "wfFormId", property = "description")
	private String wfFormDesc;

	@Override
	public String getDescription() {
		return this.binding;
	}

	public Long getWfFormId() {
		return wfFormId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public void setWfFormId(Long wfFormId) {
		this.wfFormId = wfFormId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getEditorUpl() {
		return editorUpl;
	}

	public void setEditorUpl(String editorUpl) {
		this.editorUpl = editorUpl;
	}

	public String getBinding() {
		return binding;
	}

	public void setBinding(String binding) {
		this.binding = binding;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getWfFormName() {
		return wfFormName;
	}

	public void setWfFormName(String wfFormName) {
		this.wfFormName = wfFormName;
	}

	public String getWfFormDesc() {
		return wfFormDesc;
	}

	public void setWfFormDesc(String wfFormDesc) {
		this.wfFormDesc = wfFormDesc;
	}

}
