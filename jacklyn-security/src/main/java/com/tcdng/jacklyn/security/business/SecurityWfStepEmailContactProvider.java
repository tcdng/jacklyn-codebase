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

package com.tcdng.jacklyn.security.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcdng.jacklyn.notification.data.NotificationContact;
import com.tcdng.jacklyn.organization.business.OrganizationService;
import com.tcdng.jacklyn.security.entities.UserRole;
import com.tcdng.jacklyn.security.entities.UserRoleQuery;
import com.tcdng.jacklyn.workflow.business.AbstractWfStepEmailContactProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Default application workflow step email contact provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("security-wfstepemailcontactprovider")
public class SecurityWfStepEmailContactProvider extends AbstractWfStepEmailContactProvider {

    @Configurable
    private OrganizationService organizationService;

    @Configurable
    private SecurityService securityService;

    @Override
    public List<NotificationContact> getEmailContacts(String stepGlobalName) throws UnifyException {
        List<String> roleList = organizationService.findWfStepRoles(stepGlobalName);
        if (DataUtils.isNotBlank(roleList)) {
            List<UserRole> userRoleList =
                    securityService.findUserRoles(
                            (UserRoleQuery) new UserRoleQuery().roleNameIn(roleList).select("userName", "userEmail"));
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

}
