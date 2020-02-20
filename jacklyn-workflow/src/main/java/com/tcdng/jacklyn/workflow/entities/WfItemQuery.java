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
import java.util.Date;

import com.tcdng.jacklyn.common.entities.BaseTimestampedEntityQuery;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.IsNull;
import com.tcdng.unify.core.criterion.NotEqual;
import com.tcdng.unify.core.criterion.Or;

/**
 * Workflow item query.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class WfItemQuery extends BaseTimestampedEntityQuery<WfItem> {

    public WfItemQuery() {
        super(WfItem.class);
    }
    
    
    public WfItemQuery processGlobalNameIn(Collection<String> processGlobalName) {
        return (WfItemQuery) addAmongst("processGlobalName", processGlobalName);
    }
    
    public WfItemQuery wfItemSplitEventId(Long wfItemSplitEventId) {
        return (WfItemQuery) addEquals("wfItemSplitEventId", wfItemSplitEventId);
    }
    
    public WfItemQuery submissionId(Long submissionId) {
        return (WfItemQuery) addEquals("submissionId", submissionId);
    }

    public WfItemQuery branchCode(String branchCode) {
        return (WfItemQuery) addEquals("branchCode", branchCode);
    }

    public WfItemQuery departmentCode(String departmentCode) {
        return (WfItemQuery) addEquals("departmentCode", departmentCode);
    }

    public WfItemQuery stepGlobalName(String stepGlobalName) {
        return (WfItemQuery) addEquals("stepGlobalName", stepGlobalName);
    }

    public WfItemQuery descriptionLike(String description) {
        return (WfItemQuery) addLike("description", description);
    }

    public WfItemQuery heldBy(String heldBy) {
        return (WfItemQuery) addEquals("heldBy", heldBy);
    }

    public WfItemQuery isHeld() {
        return (WfItemQuery) addIsNotNull("heldBy");
    }

    public WfItemQuery isUnheld() {
        return (WfItemQuery) addIsNull("heldBy");
    }

    public WfItemQuery stepDt(Date stepDt) {
        return (WfItemQuery) addEquals("stepDt", stepDt);
    }

    public WfItemQuery isUnheldOrHeldBy(String heldBy) {
        return (WfItemQuery) addRestriction(new Or().add(new IsNull("heldBy")).add(new Equals("heldBy", heldBy)));
    }

    public WfItemQuery allOrParticipantType(WorkflowParticipantType participantType) {
        return (WfItemQuery) addRestriction(new Or().add(new Equals("participantType", WorkflowParticipantType.ALL)).add(
                new Equals("participantType", participantType)));
    }

    public WfItemQuery notForwardedBy(String userId) {
        return (WfItemQuery) addRestriction(new Or().add(new NotEqual("forwardedBy", userId)).add(new IsNull("forwardedBy")));
    }
}
