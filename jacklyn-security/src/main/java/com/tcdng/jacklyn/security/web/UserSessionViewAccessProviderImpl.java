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

package com.tcdng.jacklyn.security.web;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.tcdng.jacklyn.common.web.AbstractUserSessionViewAccessProvider;
import com.tcdng.jacklyn.organization.business.OrganizationService;
import com.tcdng.jacklyn.security.business.SecurityService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Security module implementation of user session view access provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("security-usersessionviewaccessprovider")
public class UserSessionViewAccessProviderImpl extends AbstractUserSessionViewAccessProvider {

    @Configurable
    private OrganizationService organizationService;

    @Configurable
    private SecurityService securityService;

    @Override
    public List<Long> findUserSessionBranchIds() throws UnifyException {
        UserToken userToken = getUserToken();
        if (!userToken.isReservedUser() && !isAppAdminView()) {
            String branchCode = userToken.getBranchCode();
            if (!StringUtils.isBlank(branchCode)) {
                if (isHubAdminView()) {
                    return organizationService.findHubBranchIdsByBranch(branchCode);
                }

                return Arrays.asList(organizationService.findBranchId(branchCode));
            }
        }

        return Collections.emptyList();
    }

    @Override
    public List<Long> findUserSessionDepartmentIds() throws UnifyException {
        UserToken userToken = getUserToken();
        if (!userToken.isReservedUser() && !isAppAdminView()) {
            return securityService.findUserDepartmentIds(userToken.getUserLoginId());
        }

        return Collections.emptyList();
    }

}
