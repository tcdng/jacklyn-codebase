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

import java.util.Date;

import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntityQuery;
import com.tcdng.unify.core.criterion.OrBuilder;

/**
 * Query class for roles.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class RoleQuery extends BaseVersionedStatusEntityQuery<Role> {

    public RoleQuery() {
        super(Role.class);
    }

    @Override
    public RoleQuery addOrder(String field) {
        return (RoleQuery) super.addOrder(field);
    }

    @Override
    public RoleQuery addSelect(String field) {
        return (RoleQuery) super.addSelect(field);
    }

    public RoleQuery departmentId(Long departmentId) {
        return (RoleQuery) addEquals("departmentId", departmentId);
    }

    public RoleQuery name(String name) {
        return (RoleQuery) addEquals("name", name);
    }

    public RoleQuery nameLike(String name) {
        return (RoleQuery) addLike("name", name);
    }

    public RoleQuery description(String description) {
        return (RoleQuery) addEquals("description", description);
    }

    public RoleQuery descriptionLike(String description) {
        return (RoleQuery) addLike("description", description);
    }

    public RoleQuery activeBefore(Date activeBefore) {
        return (RoleQuery) addRestriction(
                new OrBuilder().lessEqual("activeBefore", activeBefore).isNull("activeBefore").build());
    }

    public RoleQuery activeAfter(Date activeAfter) {
        return (RoleQuery) addRestriction(
                new OrBuilder().greaterEqual("activeAfter", activeAfter).isNull("activeAfter").build());
    }
}
