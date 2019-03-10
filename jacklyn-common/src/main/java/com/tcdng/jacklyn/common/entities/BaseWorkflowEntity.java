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
package com.tcdng.jacklyn.common.entities;

import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.Tooling;

/**
 * Base class for entity that requires a workflow flag.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Tooling(name = "baseWfEntity", description = "Base Workflow")
@Policy("workflowentity-policy")
public abstract class BaseWorkflowEntity extends BaseVersionedTimestampedStatusEntity {

    @Column(name = "WORKFLOW_FG")
    private Boolean workflowFlag;

    public Boolean getWorkflowFlag() {
        return workflowFlag;
    }

    public void setWorkflowFlag(Boolean workflowFlag) {
        this.workflowFlag = workflowFlag;
    }
}
