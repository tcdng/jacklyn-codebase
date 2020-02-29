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

package com.tcdng.jacklyn.security.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.notification.data.NotificationContact;
import com.tcdng.jacklyn.organization.business.OrganizationService;
import com.tcdng.jacklyn.security.business.SecurityService;
import com.tcdng.jacklyn.security.entities.User;
import com.tcdng.jacklyn.security.entities.UserRole;
import com.tcdng.jacklyn.security.entities.UserRoleQuery;
import com.tcdng.jacklyn.shared.organization.RoleWfStepType;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.jacklyn.workflow.business.AbstractWfStepUserInformationProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default application workflow step user information provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("security-wfstepuserinfoprovider")
public class SecurityWfStepUserInformationProvider extends AbstractWfStepUserInformationProvider {

    @Configurable
    private OrganizationService organizationService;

    @Configurable
    private SecurityService securityService;

    @Override
    public Collection<String> getEligibleUsersForWorkflowStep(WorkflowParticipantType participant,
            String stepGlobalName, String branchCode, String departmentCode, String... preferredRoles)
            throws UnifyException {
        UserRoleQuery userRoleQuery =
                getUserRoleQueryForEligibleUsers(participant, stepGlobalName, branchCode, departmentCode,
                        preferredRoles);
        if (userRoleQuery != null) {
            return securityService.findUsers(userRoleQuery);
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<NotificationContact> getEligibleEmailContactsForWorkflowStep(WorkflowParticipantType participant,
            String stepGlobalName, String branchCode, String departmentCode, String... preferredRoles)
            throws UnifyException {
        UserRoleQuery userRoleQuery =
                getUserRoleQueryForEligibleUsers(participant, stepGlobalName, branchCode, departmentCode,
                        preferredRoles);
        if (userRoleQuery != null) {
            List<UserRole> userRoleList = securityService.findUserRoles(userRoleQuery);
            if (DataUtils.isNotBlank(userRoleList)) {
                List<NotificationContact> contactList = new ArrayList<NotificationContact>();
                for (UserRole userRole : userRoleList) {
                    contactList.add(new NotificationContact(userRole.getUserName(), userRole.getUserEmail()));
                }

                return contactList;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<NotificationContact> getEmailContactsForUser(String userLoginId) throws UnifyException {
        if (!StringUtils.isBlank(userLoginId)) {
            User user = securityService.findUser(userLoginId);
            if (user != null) {
                return Arrays.asList(new NotificationContact(userLoginId, user.getEmail()));
            }
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<NotificationContact> getEligibleMobilePhoneContactsForWorkflowStep(
            WorkflowParticipantType participant, String globalStepName, String branchCode, String departmentCode,
            String... preferredRoles) throws UnifyException {
        UserRoleQuery userRoleQuery =
                getUserRoleQueryForEligibleUsers(participant, globalStepName, branchCode, departmentCode,
                        preferredRoles);
        if (userRoleQuery != null) {
            List<UserRole> userRoleList = securityService.findUserRoles(userRoleQuery);
            if (DataUtils.isNotBlank(userRoleList)) {
                List<NotificationContact> contactList = new ArrayList<NotificationContact>();
                for (UserRole userRole : userRoleList) {
                    contactList.add(new NotificationContact(userRole.getUserName(), userRole.getUserMobileNo()));
                }

                return contactList;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<NotificationContact> getMobilePhoneContactsForUser(String userLoginId) throws UnifyException {
        if (!StringUtils.isBlank(userLoginId)) {
            User user = securityService.findUser(userLoginId);
            if (user != null) {
                return Arrays.asList(new NotificationContact(userLoginId, user.getMobileNo()));
            }
        }

        return Collections.emptyList();
    }

    private UserRoleQuery getUserRoleQueryForEligibleUsers(WorkflowParticipantType participant, String stepGlobalName,
            String branchCode, String departmentCode, String... preferredRoles) throws UnifyException {
        if (participant.isParticipant()) {
            List<String> roleList = null;
            if (preferredRoles.length > 0) {
                roleList = Arrays.asList(preferredRoles);
            } else {
                roleList = organizationService.findWfStepRoles(RoleWfStepType.USER_INTERACT, stepGlobalName);
            }

            if (DataUtils.isNotBlank(roleList)) {
                // Default to all participants in roles
                UserRoleQuery userRoleQuery =
                        (UserRoleQuery) new UserRoleQuery().roleNameIn(roleList).userStatus(RecordStatus.ACTIVE)
                                .addSelect("userName", "userEmail");

                // Restrict by participant type if necessary
                if (participant.isPersonnel()) {
                    userRoleQuery.isNotSupervisor();
                } else if (participant.isSupervisor()) {
                    userRoleQuery.isSupervisor();
                }

                // Restrict by branch if necessary
                if (!StringUtils.isBlank(branchCode)) {
                    userRoleQuery.branchCode(branchCode);
                }

                // Restrict by department if necessary
                if (!StringUtils.isBlank(departmentCode)) {
                    userRoleQuery.departmentName(departmentCode);
                }

                return userRoleQuery;
            }
        }
        return null;
    }

}
