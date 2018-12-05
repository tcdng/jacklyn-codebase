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
package com.tcdng.jacklyn.report.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseInstallEntity;
import com.tcdng.jacklyn.report.constants.ReportModuleNameConstants;
import com.tcdng.jacklyn.system.entities.Module;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity for storing reportable definition information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = ReportModuleNameConstants.REPORT_MODULE, title = "Reportable Definition",
		reportable = true, auditable = true)
@Table(name = "REPORTABLEDEF", uniqueConstraints = { @UniqueConstraint({ "name" }) })
public class ReportableDefinition extends BaseInstallEntity {

	@ForeignKey(Module.class)
	private Long moduleId;

	@Column(name = "REPORTABLEDEF_NM", length = 64)
	private String name;

	@Column(length = 64)
	private String title;

	@Column(name = "REPORTABLEDEF_DESC", length = 64)
	private String description;

	@Column(name = "RECORD_NM", length = 256, nullable = true)
	private String recordName;

	@Column(length = 64, nullable = true)
	private String template;

	@Column(length = 64, nullable = true)
	private String processor;

	@Column
	boolean landscape;

	@Column
	boolean underlineRows;

	@Column
	boolean shadeOddRows;

	@Column
	boolean dynamic;

	@ListOnly(name = "MODULE_NM", key = "moduleId", property = "name")
	private String moduleName;

	@ListOnly(name = "MODULE_DESC", key = "moduleId", property = "description")
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public boolean isLandscape() {
		return landscape;
	}

	public void setLandscape(boolean landscape) {
		this.landscape = landscape;
	}

	public boolean isUnderlineRows() {
		return underlineRows;
	}

	public void setUnderlineRows(boolean underlineRows) {
		this.underlineRows = underlineRows;
	}

	public boolean isShadeOddRows() {
		return shadeOddRows;
	}

	public void setShadeOddRows(boolean shadeOddRows) {
		this.shadeOddRows = shadeOddRows;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
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
