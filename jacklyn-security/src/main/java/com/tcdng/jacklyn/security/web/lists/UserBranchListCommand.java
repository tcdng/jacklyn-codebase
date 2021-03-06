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
package com.tcdng.jacklyn.security.web.lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.tcdng.jacklyn.organization.business.OrganizationService;
import com.tcdng.jacklyn.organization.entities.BranchQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.ZeroParams;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * User branch list command.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("userbranchlist")
public class UserBranchListCommand extends AbstractZeroParamsSecurityListCommand {

    @Configurable
    private OrganizationService organizationService;

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        UserToken userToken = getUserToken();
        if (!userToken.isReservedUser() && !isAppAdminView()) {
            if (QueryUtils.isValidStringCriteria(userToken.getBranchCode())) {
                if (isHubAdminView()) {
                    return organizationService.findHubBranchesByBranch(userToken.getBranchCode());
                }

                return Arrays.asList(organizationService.findBranch(new BranchQuery().code(userToken.getBranchCode())));
            }

            return Collections.emptyList();
        }

        return organizationService
                .findBranches((BranchQuery) new BranchQuery().ignoreEmptyCriteria(true).addOrder("description"));
    }

}
