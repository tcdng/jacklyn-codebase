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
package com.tcdng.jacklyn.workflow.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseTimestampedStatusEntity;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Workflow category entity.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Managed(module = WorkflowModuleNameConstants.WORKFLOW_MODULE, title = "Workflow Category", reportable = true,
        auditable = true)
@Table(name = "JKWFCATEGORY", uniqueConstraints = { @UniqueConstraint({ "name", "version" }),
        @UniqueConstraint({ "description", "version" }) })
public class WfCategory extends BaseTimestampedStatusEntity {

    @Column(name = "CATEGORY_NM", length = 32)
    private String name;

    @Column(name = "CATEGORY_DESC", length = 64)
    private String description;

    @Column(name = "CATEGORY_VERSION", length = 32)
    private String version;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
