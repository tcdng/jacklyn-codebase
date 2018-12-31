/*
 * Copyright 2018 The Code Department
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
package com.tcdng.jacklyn.organization.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.tcdng.jacklyn.organization.entities.PrivilegeQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.data.AssignParams;

/**
 * List command for privileges that have IDs in a specified ID list and also
 * optionally belong to a particular module. Expects two parameters - the
 * privilege group ID and the list of privilege IDs.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("privilegeinlist")
public class PrivilegeInListCommand extends AbstractAssignParamsOrganizationListCommand {

    @Override
    public List<? extends Listable> execute(Locale locale, AssignParams params) throws UnifyException {
        if (params.isAssignedIdList() && QueryUtils.isValidStringCriteria(params.getFilterId1())) {
            PrivilegeQuery query = new PrivilegeQuery();
            query.privilegeCategoryId(params.getFilterId1(Long.class));
            if (QueryUtils.isValidStringCriteria(params.getFilterId2())) {
                query.moduleId(params.getFilterId2(Long.class));
            }

            query.idIn(params.getAssignedIdList(Long.class));
            query.select("id", "description").order("description");
            return getOrganizationService().findPrivileges(query);
        }

        return Collections.emptyList();
    }
}
