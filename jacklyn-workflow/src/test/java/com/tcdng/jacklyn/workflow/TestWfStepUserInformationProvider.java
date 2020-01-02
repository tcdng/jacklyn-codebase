/*
 * Copyright 2018-2020 The Code Department
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

package com.tcdng.jacklyn.workflow;

import java.util.Collection;
import java.util.Collections;

import com.tcdng.jacklyn.notification.data.NotificationContact;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.workflow.business.AbstractWfStepUserInformationProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Default application workflow step user information provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("test-wfstepuserinfoprovider")
public class TestWfStepUserInformationProvider extends AbstractWfStepUserInformationProvider {

    @Override
    public Collection<String> getEligibleUsersForWorkflowStep(WorkflowParticipantType participant,
            String stepGlobalName, String branchCode, String departmentCode, String... preferredRoles)
            throws UnifyException {
        return Collections.emptyList();
    }

    @Override
    public Collection<NotificationContact> getEligibleEmailContactsForWorkflowStep(WorkflowParticipantType participant,
            String stepGlobalName, String branchCode, String departmentCode, String... preferredRoles)
            throws UnifyException {
        return Collections.emptyList();
    }

    @Override
    public Collection<NotificationContact> getEmailContactsForUser(String userLoginId) throws UnifyException {
        return Collections.emptyList();
    }

    @Override
    public Collection<NotificationContact> getEligibleMobilePhoneContactsForWorkflowStep(
            WorkflowParticipantType participant, String globalStepName, String branchCode, String departmentCode,
            String... preferredRoles) throws UnifyException {
        return Collections.emptyList();
    }

    @Override
    public Collection<NotificationContact> getMobilePhoneContactsForUser(String userLoginId) throws UnifyException {
        return Collections.emptyList();
    }
}
