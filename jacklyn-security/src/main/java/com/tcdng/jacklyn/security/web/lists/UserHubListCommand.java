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
package com.tcdng.jacklyn.security.web.lists;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.tcdng.jacklyn.organization.business.OrganizationService;
import com.tcdng.jacklyn.organization.entities.HubQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.ZeroParams;

/**
 * User hub list command.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("userhublist")
public class UserHubListCommand extends AbstractZeroParamsSecurityListCommand {

    @Configurable
    private OrganizationService organizationService;

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        UserToken userToken = getUserToken();
        if (!userToken.isReservedUser() && !isAppAdminView()) {
            return Arrays.asList(organizationService.findHubByBranch(userToken.getBranchCode()));
        }

        return organizationService.findHubs(
                (HubQuery) new HubQuery().ignoreEmptyCriteria(true).addOrder("description"));
    }

}
