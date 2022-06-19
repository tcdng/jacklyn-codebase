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
package com.tcdng.jacklyn.shared.workflow;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Workflow participant type constants.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList(name = "wfparticipanttypelist", description = "Workflow Participant Type List")
public enum WorkflowParticipantType implements EnumConst {

    NONE("NON"), ALL("ALL"), PERSONNEL("PER"), SUPERVISOR("SUP");

    private final String code;

    private WorkflowParticipantType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return NONE.code;
    }

    public boolean isParticipant() {
        return !NONE.equals(this);
    }

    public boolean isSupervisor() {
        return SUPERVISOR.equals(this);
    }

    public boolean isPersonnel() {
        return PERSONNEL.equals(this);
    }

    public boolean isAllParticipants() {
        return ALL.equals(this);
    }
    
    public static WorkflowParticipantType fromCode(String code) {
        return EnumUtils.fromCode(WorkflowParticipantType.class, code);
    }

    public static WorkflowParticipantType fromName(String name) {
        return EnumUtils.fromName(WorkflowParticipantType.class, name);
    }
}
