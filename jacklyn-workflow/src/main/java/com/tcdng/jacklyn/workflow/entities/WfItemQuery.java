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
package com.tcdng.jacklyn.workflow.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.entities.BaseTimestampedEntityQuery;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.unify.core.operation.Equal;
import com.tcdng.unify.core.operation.IsNull;
import com.tcdng.unify.core.operation.NotEqual;
import com.tcdng.unify.core.operation.Or;

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

    public WfItemQuery ownerId(Long ownerId) {
        return (WfItemQuery) equals("ownerId", ownerId);
    }

    public WfItemQuery templateGlobalName(String templateGlobalName) {
        return (WfItemQuery) equals("templateGlobalName", templateGlobalName);
    }

    public WfItemQuery wfStepName(String wfStepName) {
        return (WfItemQuery) equals("wfStepName", wfStepName);
    }

    public WfItemQuery descriptionLike(String description) {
        return (WfItemQuery) like("description", description);
    }

    public WfItemQuery heldBy(String heldBy) {
        return (WfItemQuery) equals("heldBy", heldBy);
    }

    public WfItemQuery isHeld() {
        return (WfItemQuery) isNotNull("heldBy");
    }

    public WfItemQuery isUnheld() {
        return (WfItemQuery) isNull("heldBy");
    }

    public WfItemQuery stepDt(Date stepDt) {
        return (WfItemQuery) equals("stepDt", stepDt);
    }

    public WfItemQuery isUnheldOrHeldBy(String heldBy) {
        return (WfItemQuery) add(new Or(new IsNull("heldBy"), new Equal("heldBy", heldBy)));
    }

    public WfItemQuery allOrParticipantType(WorkflowParticipantType participantType) {
        return (WfItemQuery) add(new Or(new Equal("participantType", WorkflowParticipantType.ALL),
                new Equal("participantType", participantType)));
    }

    public WfItemQuery notForwardedBy(String userId) {
        Or or = new Or(new NotEqual("forwardedBy", userId), new IsNull("forwardedBy"));
        return (WfItemQuery) add(or);
    }
}
