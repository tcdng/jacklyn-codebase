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

package com.tcdng.jacklyn.workflow.business;

import java.util.Collection;

import com.tcdng.jacklyn.notification.data.NotificationContact;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.workflow.data.FlowingWfItem;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Convenient abstract base class for workflow item policies.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractWfItemPolicy extends AbstractUnifyComponent {

    @Configurable
    private WfStepUserInformationProvider wfStepUserInformationProvider;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected WfStepUserInformationProvider getWfStepUserInfoProvider() {
        return wfStepUserInformationProvider;
    }

    protected Collection<String> getEligibleUsers(FlowingWfItem.Reader flowingWfItemReader) throws UnifyException {
        FlowingWfItem.Restrictions restrictions = flowingWfItemReader.getRestrictions();
        return wfStepUserInformationProvider.getEligibleUsersForWorkflowStep(flowingWfItemReader.getStepParticipant(),
                flowingWfItemReader.getStepGlobalName(), restrictions.getBranchCode(),
                restrictions.getDepartmentCode());
    }

    protected Collection<NotificationContact> getEligibleEmailContacts(FlowingWfItem.Reader flowingWfItemReader,
            WorkflowParticipantType participant, String globalStepName) throws UnifyException {
        FlowingWfItem.Restrictions restrictions = flowingWfItemReader.getRestrictions();
        return wfStepUserInformationProvider.getEligibleEmailContactsForWorkflowStep(participant, globalStepName,
                restrictions.getBranchCode(), restrictions.getDepartmentCode());
    }

    protected Collection<NotificationContact> getEligibleMobilePhoneContacts(FlowingWfItem.Reader flowingWfItemReader,
            WorkflowParticipantType participant, String globalStepName) throws UnifyException {
        FlowingWfItem.Restrictions restrictions = flowingWfItemReader.getRestrictions();
        return wfStepUserInformationProvider.getEligibleMobilePhoneContactsForWorkflowStep(participant, globalStepName,
                restrictions.getBranchCode(), restrictions.getDepartmentCode());
    }

    protected Collection<NotificationContact> getUserEmailContacts(String userLoginId) throws UnifyException {
        return wfStepUserInformationProvider.getEmailContactsForUser(userLoginId);
    }
}
