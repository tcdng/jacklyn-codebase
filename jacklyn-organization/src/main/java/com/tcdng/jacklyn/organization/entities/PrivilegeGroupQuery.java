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

import com.tcdng.jacklyn.common.entities.BaseEntityQuery;

/**
 * Query for privilege groups.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class PrivilegeGroupQuery extends BaseEntityQuery<PrivilegeGroup> {

    public PrivilegeGroupQuery() {
        super(PrivilegeGroup.class);
    }

    public PrivilegeGroupQuery moduleName(String moduleName) {
        return (PrivilegeGroupQuery) equals("moduleName", moduleName);
    }

    public PrivilegeGroupQuery categoryName(String categoryName) {
        return (PrivilegeGroupQuery) equals("categoryName", categoryName);
    }

    public PrivilegeGroupQuery moduleId(Long moduleId) {
        return (PrivilegeGroupQuery) equals("moduleId", moduleId);
    }

    public PrivilegeGroupQuery privilegeCategoryId(Long privilegeCategoryId) {
        return (PrivilegeGroupQuery) equals("privilegeCategoryId", privilegeCategoryId);
    }

    public PrivilegeGroupQuery orderByFullDesc() {
        return (PrivilegeGroupQuery) order("moduleDesc", "description");
    }
}
