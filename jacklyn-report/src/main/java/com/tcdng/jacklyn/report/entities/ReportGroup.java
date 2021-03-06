/*
 * Copyright 2018-2020 The Code Department.
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
package com.tcdng.jacklyn.report.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.report.constants.ReportModuleNameConstants;
import com.tcdng.jacklyn.system.entities.Module;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Report group.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = ReportModuleNameConstants.REPORT_MODULE, title = "Report Group", reportable = true, auditable = true)
@Table(name = "JKREPORTGROUP", uniqueConstraints = { @UniqueConstraint({ "name" }),
		@UniqueConstraint({ "description" }) })
public class ReportGroup extends BaseVersionedStatusEntity {

	@ForeignKey(Module.class)
	private Long moduleId;

	@Column(name = "REPORTGROUP_NM")
	private String name;

	@Column(name = "REPORTGROUP_DESC", length = 48)
	private String description;

	@ListOnly(key = "moduleId", property = "description")
	private String moduleDesc;

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

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModuleDesc() {
		return moduleDesc;
	}

	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}
}
