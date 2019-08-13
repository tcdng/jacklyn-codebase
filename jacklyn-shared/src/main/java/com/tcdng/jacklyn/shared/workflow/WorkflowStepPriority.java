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
package com.tcdng.jacklyn.shared.workflow;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Workflow step priority levels.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList("wfstepprioritylist")
public enum WorkflowStepPriority implements EnumConst {

    CRITICAL("C"), HIGH("H"), NORMAL("N"), LOW("L");

    private final String code;

    private WorkflowStepPriority(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return NORMAL.code;
    }

    public static WorkflowStepPriority fromCode(String code) {
        return EnumUtils.fromCode(WorkflowStepPriority.class, code);
    }

    public static WorkflowStepPriority fromName(String name) {
        return EnumUtils.fromName(WorkflowStepPriority.class, name);
    }
}
