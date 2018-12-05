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
 * Workflow form tab definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "WFFORMTAB", uniqueConstraints = { @UniqueConstraint({ "wfFormId", "name" }),
		@UniqueConstraint({ "wfFormId", "description" }) })
public class WfFormTab extends BaseEntity {

	@ForeignKey(WfForm.class)
	private Long wfFormId;

	@Column(name = "TAB_NM", length = 32)
	private String name;

	@Column(name = "TAB_DESC", length = 64)
	private String description;

	@Column(name = "TAB_LABEL", length = 64, nullable = true)
	private String label;

	@Column(name = "PSEUDO_FG")
	private Boolean pseudo;

	@ListOnly(key = "wfFormId", property = "name")
	private String wfFormName;

	@ListOnly(key = "wfFormId", property = "description")
	private String wfFormDesc;

	@Override
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getWfFormId() {
		return wfFormId;
	}

	public void setWfFormId(Long wfFormId) {
		this.wfFormId = wfFormId;
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

	public Boolean getPseudo() {
		return pseudo;
	}

	public void setPseudo(Boolean pseudo) {
		this.pseudo = pseudo;
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
