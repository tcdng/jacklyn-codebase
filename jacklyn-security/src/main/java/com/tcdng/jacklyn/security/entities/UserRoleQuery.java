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
package com.tcdng.jacklyn.security.entities;

import java.util.Collection;
import java.util.Date;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntityQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.operation.OrBuilder;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Query class for user roles.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class UserRoleQuery extends BaseEntityQuery<UserRole> {

    public UserRoleQuery() {
        super(UserRole.class);
    }

    public UserRoleQuery userId(Long userId) {
        return (UserRoleQuery) equals("userId", userId);
    }

    public UserRoleQuery userLoginId(String userLoginId) {
        return (UserRoleQuery) equals("userLoginId", userLoginId);
    }

    public UserRoleQuery roleId(Long roleId) {
        return (UserRoleQuery) equals("roleId", roleId);
    }

    public UserRoleQuery roleIdNot(Long roleId) {
        return (UserRoleQuery) notEqual("roleId", roleId);
    }

    public UserRoleQuery roleName(String roleName) {
        return (UserRoleQuery) equals("roleName", roleName);
    }

    public UserRoleQuery roleNameNot(String roleName) {
        return (UserRoleQuery) notEqual("roleName", roleName);
    }

    public UserRoleQuery roleNameIn(Collection<String> roleName) {
        return (UserRoleQuery) amongst("roleName", roleName);
    }

    public UserRoleQuery roleIdIn(Collection<Long> roleId) {
        return (UserRoleQuery) amongst("roleId", roleId);
    }

    public UserRoleQuery roleStatus(RecordStatus roleStatus) {
        return (UserRoleQuery) equals("roleStatus", roleStatus);
    }

    public UserRoleQuery roleActiveTime(Date date) throws UnifyException {
        date = CalendarUtils.getTimeOfDay(date);
        return (UserRoleQuery) add(new OrBuilder().less("activeBefore", date).isNull("activeBefore"))
                .add(new OrBuilder().greater("activeAfter", date).isNull("activeAfter"));
    }
}
