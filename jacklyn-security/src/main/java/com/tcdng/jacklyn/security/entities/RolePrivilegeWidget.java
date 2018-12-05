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

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.security.constants.SecurityModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Role privilege widget entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SecurityModuleNameConstants.SECURITY_MODULE, title = "Role Privilege Widget",
		reportable = true, auditable = true)
@Table(name = "ROLEPRIVILEGEWGT", uniqueConstraints = { @UniqueConstraint({ "rolePrivilegeId" }) })
public class RolePrivilegeWidget extends BaseEntity {

	@ForeignKey(RolePrivilege.class)
	private Long rolePrivilegeId;

	@Column(name = "VISIBLE_FG")
	private boolean visible;

	@Column(name = "EDITABLE_FG")
	private boolean editable;

	@Column(name = "DISABLED_FG")
	private boolean disabled;

	@Column(name = "REQUIRED_FG")
	private boolean required;

	@ListOnly(key = "rolePrivilegeId", property = "roleId")
	private Long roleId;

	@ListOnly(key = "rolePrivilegeId", property = "roleDesc")
	private String roleDesc;

	@ListOnly(key = "rolePrivilegeId", property = "moduleId")
	private Long moduleId;

	@ListOnly(key = "rolePrivilegeId", property = "privilegeCategoryId")
	private Long privilegeCategoryId;

	@ListOnly(key = "rolePrivilegeId", property = "categoryName")
	private String categoryName;

	@ListOnly(key = "rolePrivilegeId", property = "privilegeDesc")
	private String privilegeDesc;

	@ListOnly(key = "rolePrivilegeId", property = "privilegeName")
	private String privilegeName;

	@Override
	public String getDescription() {
		return null;
	}

	public Long getRolePrivilegeId() {
		return rolePrivilegeId;
	}

	public void setRolePrivilegeId(Long rolePrivilegeId) {
		this.rolePrivilegeId = rolePrivilegeId;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getPrivilegeDesc() {
		return privilegeDesc;
	}

	public void setPrivilegeDesc(String privilegeDesc) {
		this.privilegeDesc = privilegeDesc;
	}

	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
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
}
