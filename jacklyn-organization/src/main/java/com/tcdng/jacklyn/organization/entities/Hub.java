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

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.organization.constants.OrganizationModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.Tooling;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Represents hub entity.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Tooling(name = "hub", description = "Hub")
@Managed(
        module = OrganizationModuleNameConstants.ORGANIZATION_MODULE, title = "Hub", reportable = true,
        auditable = true)
@Table(name = "JKHUB", uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class Hub extends BaseVersionedStatusEntity {

    @Column(name = "HUB_NM", length = 32)
    private String name;

    @Column(name = "HUB_DESC", length = 64)
    private String description;

    @Override
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
