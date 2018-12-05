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
package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity that represents input control information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SystemModuleNameConstants.SYSTEM_MODULE, title = "Input Control Definition",
		reportable = true, auditable = true)
@Table(name = "INPUTCTRLDEF", uniqueConstraints = { @UniqueConstraint({ "moduleId", "name" }),
		@UniqueConstraint({ "description" }) })
public class InputCtrlDef extends BaseVersionedStatusEntity {

	@ForeignKey(Module.class)
	private Long moduleId;

	@Column(name = "INPUTCTRLDEF_NM")
	private String name;

	@Column(name = "INPUTCTRLDEF_DESC", length = 48)
	private String description;

	@Column(name = "CONTROL_DSCR", length = 80)
	private String control;

	@ListOnly(name = "MODULE_NM", key = "moduleId", property = "name")
	private String moduleName;

	@ListOnly(name = "MODULE_DESC", key = "moduleId", property = "description")
	private String moduleDesc;

	@Override
	public String getDescription() {
		return this.description;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleDesc() {
		return moduleDesc;
	}

	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}
}
