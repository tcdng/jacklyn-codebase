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
package com.tcdng.jacklyn.organization.entities;

import java.util.Collection;

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
        return (RolePrivilegeQuery) equals("roleId", roleId);
    }

    public RolePrivilegeQuery roleName(String roleName) {
        return (RolePrivilegeQuery) equals("roleName", roleName);
    }

    public RolePrivilegeQuery privilegeId(Long privilegeId) {
        return (RolePrivilegeQuery) equals("privilegeId", privilegeId);
    }

    public RolePrivilegeQuery privilegeIdIn(Collection<Long> privilegeId) {
        return (RolePrivilegeQuery) amongst("privilegeId", privilegeId);
    }

    public RolePrivilegeQuery privilegeIdNotIn(Collection<Long> privilegeId) {
        return (RolePrivilegeQuery) notAmongst("privilegeId", privilegeId);
    }

    public RolePrivilegeQuery moduleId(Long moduleId) {
        return (RolePrivilegeQuery) equals("moduleId", moduleId);
    }

    public RolePrivilegeQuery privilegeGroupId(Long privilegeGroupId) {
        return (RolePrivilegeQuery) equals("privilegeGroupId", privilegeGroupId);
    }

    public RolePrivilegeQuery privilegeCategoryId(Long privilegeCategoryId) {
        return (RolePrivilegeQuery) equals("privilegeCategoryId", privilegeCategoryId);
    }

    public RolePrivilegeQuery categoryName(String categoryName) {
        return (RolePrivilegeQuery) equals("categoryName", categoryName);
    }

    public RolePrivilegeQuery categoryNameNot(String categoryName) {
        return (RolePrivilegeQuery) notEqual("categoryName", categoryName);
    }

    public RolePrivilegeQuery privilegeStatus(RecordStatus privilegeStatus) {
        return (RolePrivilegeQuery) equals("privilegeStatus", privilegeStatus);
    }

    @Override
    public RolePrivilegeQuery select(String field) {
        return (RolePrivilegeQuery) super.select(field);
    }

    @Override
    public RolePrivilegeQuery order(String field) {
        return (RolePrivilegeQuery) super.order(field);
    }
}
