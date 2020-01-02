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

import java.util.Collection;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntityQuery;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.shared.workflow.WorkflowStepType;

/**
 * Query class for workflow step definitions.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfStepQuery extends BaseEntityQuery<WfStep> {

    public WfStepQuery() {
        super(WfStep.class);
    }

    public WfStepQuery wfTemplateId(Long wfTemplateId) {
        return (WfStepQuery) addEquals("wfTemplateId", wfTemplateId);
    }

    public WfStepQuery wfCategoryId(Long wfCategoryId) {
        return (WfStepQuery) addEquals("wfCategoryId", wfCategoryId);
    }

    public WfStepQuery wfCategoryStatus(RecordStatus wfCategoryStatus) {
        return (WfStepQuery) addEquals("wfCategoryStatus", wfCategoryStatus);
    }

    public WfStepQuery name(String name) {
        return (WfStepQuery) addEquals("name", name);
    }

    public WfStepQuery namesIn(Collection<String> names) {
        return (WfStepQuery) addAmongst("name", names);
    }

    public WfStepQuery descriptionLike(String description) {
        return (WfStepQuery) addLike("description", description);
    }

    public WfStepQuery type(WorkflowStepType type) {
        return (WfStepQuery) addEquals("type", type);
    }

    public WfStepQuery isParticipation() {
        return (WfStepQuery) addNotEqual("participantType", WorkflowParticipantType.NONE);
    }
}
