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

import java.util.Date;

import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntityQuery;
import com.tcdng.unify.core.operation.OrBuilder;

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
    public RoleQuery order(String field) {
        return (RoleQuery) super.order(field);
    }

    @Override
    public RoleQuery select(String field) {
        return (RoleQuery) super.select(field);
    }

    public RoleQuery departmentId(Long departmentId) {
        return (RoleQuery) equals("departmentId", departmentId);
    }

    public RoleQuery name(String name) {
        return (RoleQuery) equals("name", name);
    }

    public RoleQuery nameLike(String name) {
        return (RoleQuery) like("name", name);
    }

    public RoleQuery descriptionLike(String description) {
        return (RoleQuery) like("description", description);
    }

    public RoleQuery activeBefore(Date activeBefore) {
        return (RoleQuery) add(new OrBuilder().lessEqual("activeBefore", activeBefore).isNull("activeBefore"));
    }

    public RoleQuery activeAfter(Date activeAfter) {
        return (RoleQuery) add(new OrBuilder().greaterEqual("activeAfter", activeAfter).isNull("activeAfter"));
    }
}
