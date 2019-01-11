/*
 * Copyright 2018-2019 The Code Department.
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
package com.tcdng.jacklyn.organization.entities;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Role privilege entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "ROLEPRIVILEGE", uniqueConstraints = { @UniqueConstraint({ "roleId", "privilegeId" }) })
public class RolePrivilege extends BaseEntity {

    @ForeignKey(Role.class)
    private Long roleId;

    @ForeignKey(Privilege.class)
    private Long privilegeId;

    @ListOnly(key = "roleId", property = "name")
    private String roleName;

    @ListOnly(key = "roleId", property = "description")
    private String roleDesc;

    @ListOnly(key = "privilegeId", property = "name")
    private String privilegeName;

    @ListOnly(key = "privilegeId", property = "description")
    private String privilegeDesc;

    @ListOnly(key = "privilegeId", property = "status")
    private RecordStatus privilegeStatus;

    @ListOnly(key = "privilegeId", property = "privilegeGroupId")
    private Long privilegeGroupId;

    @ListOnly(key = "privilegeId", property = "moduleId")
    private Long moduleId;

    @ListOnly(key = "privilegeId", property = "privilegeCategoryId")
    private Long privilegeCategoryId;

    @ListOnly(key = "privilegeId", property = "categoryName")
    private String categoryName;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(roleDesc, " - ", privilegeDesc);
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getPrivilegeDesc() {
        return privilegeDesc;
    }

    public void setPrivilegeDesc(String privilegeDesc) {
        this.privilegeDesc = privilegeDesc;
    }

    public RecordStatus getPrivilegeStatus() {
        return privilegeStatus;
    }

    public void setPrivilegeStatus(RecordStatus privilegeStatus) {
        this.privilegeStatus = privilegeStatus;
    }

    public Long getPrivilegeGroupId() {
        return privilegeGroupId;
    }

    public void setPrivilegeGroupId(Long privilegeGroupId) {
        this.privilegeGroupId = privilegeGroupId;
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

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }
}
