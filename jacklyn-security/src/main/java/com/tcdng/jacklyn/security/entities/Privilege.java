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
package com.tcdng.jacklyn.security.entities;

import com.tcdng.jacklyn.common.entities.BaseInstallEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Privilege entity.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Table(name = "PRIVILEGE",
		uniqueConstraints = { @UniqueConstraint({ "privilegeGroupId", "name" }) })
public class Privilege extends BaseInstallEntity {

	@ForeignKey(PrivilegeGroup.class)
	private Long privilegeGroupId;

	@Column(name = "PRIVILEGE_NM", length = 64)
	private String name;

	@Column(name = "PRIVILEGE_DESC", length = 64)
	private String description;

	@ListOnly(key = "privilegeGroupId", property = "moduleId")
	private Long moduleId;

	@ListOnly(name = "MODULE_NM", key = "privilegeGroupId", property = "moduleName")
	private String moduleName;

	@ListOnly(key = "privilegeGroupId", property = "privilegeCategoryId")
	private Long privilegeCategoryId;

	@ListOnly(key = "privilegeGroupId", property = "categoryName")
	private String categoryName;

	@ListOnly(key = "privilegeGroupId", property = "categoryDesc")
	private String categoryDesc;

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPrivilegeGroupId() {
		return privilegeGroupId;
	}

	public void setPrivilegeGroupId(Long privilegeGroupId) {
		this.privilegeGroupId = privilegeGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Long getPrivilegeCategoryId() {
		return privilegeCategoryId;
	}

	public void setPrivilegeCategoryId(Long privilegeCategoryId) {
		this.privilegeCategoryId = privilegeCategoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
}
