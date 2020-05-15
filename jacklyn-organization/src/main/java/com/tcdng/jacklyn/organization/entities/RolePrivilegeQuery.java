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
package com.tcdng.jacklyn.organization.entities;

import java.util.Collection;
import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntityQuery;

/**
 * Role privilege query.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class RolePrivilegeQuery extends BaseEntityQuery<RolePrivilege> {

    public RolePrivilegeQuery() {
        super(RolePrivilege.class);
    }

    public RolePrivilegeQuery roleId(Long roleId) {
        return (RolePrivilegeQuery) addEquals("roleId", roleId);
    }

    public RolePrivilegeQuery roleIdIn(List<Long> roleIdList) {
        return (RolePrivilegeQuery) addAmongst("roleId", roleIdList);
    }

    public RolePrivilegeQuery roleName(String roleName) {
        return (RolePrivilegeQuery) addEquals("roleName", roleName);
    }

    public RolePrivilegeQuery privilegeId(Long privilegeId) {
        return (RolePrivilegeQuery) addEquals("privilegeId", privilegeId);
    }

    public RolePrivilegeQuery privilegeIdIn(Collection<Long> privilegeId) {
        return (RolePrivilegeQuery) addAmongst("privilegeId", privilegeId);
    }

    public RolePrivilegeQuery privilegeIdNotIn(Collection<Long> privilegeId) {
        return (RolePrivilegeQuery) addNotAmongst("privilegeId", privilegeId);
    }

    public RolePrivilegeQuery moduleId(Long moduleId) {
        return (RolePrivilegeQuery) addEquals("moduleId", moduleId);
    }

    public RolePrivilegeQuery privilegeGroupId(Long privilegeGroupId) {
        return (RolePrivilegeQuery) addEquals("privilegeGroupId", privilegeGroupId);
    }

    public RolePrivilegeQuery privilegeCategoryId(Long privilegeCategoryId) {
        return (RolePrivilegeQuery) addEquals("privilegeCategoryId", privilegeCategoryId);
    }

    public RolePrivilegeQuery privilegeName(String privilegeName) {
        return (RolePrivilegeQuery) addEquals("privilegeName", privilegeName);
    }

    public RolePrivilegeQuery categoryName(String categoryName) {
        return (RolePrivilegeQuery) addEquals("categoryName", categoryName);
    }

    public RolePrivilegeQuery categoryNameNot(String categoryName) {
        return (RolePrivilegeQuery) addNotEquals("categoryName", categoryName);
    }

    public RolePrivilegeQuery privilegeStatus(RecordStatus privilegeStatus) {
        return (RolePrivilegeQuery) addEquals("privilegeStatus", privilegeStatus);
    }

    @Override
    public RolePrivilegeQuery addSelect(String field) {
        return (RolePrivilegeQuery) super.addSelect(field);
    }

    @Override
    public RolePrivilegeQuery addOrder(String field) {
        return (RolePrivilegeQuery) super.addOrder(field);
    }
}
