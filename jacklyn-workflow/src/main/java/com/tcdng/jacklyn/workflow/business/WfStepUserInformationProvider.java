/*
 * Copyright 2018-2019 The Code Department
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
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Workflow step user information provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface WfStepUserInformationProvider extends UnifyComponent {

    /**
     * Gets the list of eligible users for a workflow step.
     * 
     * @param participant
     *            the participant type
     * @param stepGlobalName
     *            the workflow step
     * @param branchCode
     *            restrict to branch code
     * @param departmentCode
     *            restrict to department code
     * @param preferredRoles
     *            optional preferred role names
     * @return the list of contact information
     * @throws UnifyException
     *             if an error occurs
     */
    Collection<String> getEligibleUsersForWorkflowStep(WorkflowParticipantType participant, String stepGlobalName,
            String branchCode, String departmentCode, String... preferredRoles) throws UnifyException;

    /**
     * Gets the list of notification email contacts for a workflow step.
     * 
     * @param participant
     *            the participant type
     * @param stepGlobalName
     *            the workflow step
     * @param branchCode
     *            restrict to branch code
     * @param departmentCode
     *            restrict to department code
     * @param preferredRoles
     *            optional preferred role names
     * @return the list of contact information
     * @throws UnifyException
     *             if an error occurs
     */
    Collection<NotificationContact> getEligibleEmailContactsForWorkflowStep(WorkflowParticipantType participant,
            String stepGlobalName, String branchCode, String departmentCode, String... preferredRoles)
            throws UnifyException;

    /**
     * Gets the list of notification email contacts for a user.
     * 
     * @param userLoginId
     *            the user login ID
     * @return the list of contact information
     * @throws UnifyException
     *             if an error occurs
     */
    Collection<NotificationContact> getEmailContactsForUser(String userLoginId) throws UnifyException;

    /**
     * Gets the list of notification mobile phone contacts for a workflow step.
     * 
     * @param participant
     *            the participant type
     * @param globalStepName
     *            the workflow step global name
     * @param branchCode
     *            restrict to branch code
     * @param departmentCode
     *            restrict to department code
     * @param preferredRoles
     *            optional preferred role names
     * @return the list of contact information
     * @throws UnifyException
     *             if an error occurs
     */
    Collection<NotificationContact> getEligibleMobilePhoneContactsForWorkflowStep(WorkflowParticipantType participant,
            String globalStepName, String branchCode, String departmentCode, String... preferredRoles)
            throws UnifyException;
}
