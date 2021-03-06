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

import com.tcdng.jacklyn.common.entities.BaseEntityQuery;

/**
 * Role privilege widget query.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class RolePrivilegeWidgetQuery extends BaseEntityQuery<RolePrivilegeWidget> {

    public RolePrivilegeWidgetQuery() {
        super(RolePrivilegeWidget.class);
    }

    public RolePrivilegeWidgetQuery rolePrivilegeId(Long rolePrivilegeId) {
        return (RolePrivilegeWidgetQuery) addEquals("rolePrivilegeId", rolePrivilegeId);
    }

    public RolePrivilegeWidgetQuery rolePrivilegeIdIn(Collection<Long> rolePrivilegeId) {
        return (RolePrivilegeWidgetQuery) addAmongst("rolePrivilegeId", rolePrivilegeId);
    }

    public RolePrivilegeWidgetQuery roleId(Long roleId) {
        return (RolePrivilegeWidgetQuery) addEquals("roleId", roleId);
    }

    public RolePrivilegeWidgetQuery moduleId(Long moduleId) {
        return (RolePrivilegeWidgetQuery) addEquals("moduleId", moduleId);
    }

    public RolePrivilegeWidgetQuery privilegeCategoryId(Long privilegeCategoryId) {
        return (RolePrivilegeWidgetQuery) addEquals("privilegeCategoryId", privilegeCategoryId);
    }

    public RolePrivilegeWidgetQuery categoryName(String categoryName) {
        return (RolePrivilegeWidgetQuery) addEquals("categoryName", categoryName);
    }

}
