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
package com.tcdng.jacklyn.security.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.security.entities.RoleQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.web.data.AssignParams;

/**
 * List command for roles that have IDs in a specified ID list. Expects a
 * parameter - the list of role IDs.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("roleinlist")
public class RoleInListCommand extends AbstractAssignParamsSecurityListCommand {

    @Override
    public List<? extends Listable> execute(Locale locale, AssignParams params) throws UnifyException {
        if (params.isAssignedIdList()) {
            RoleQuery query = new RoleQuery();
            query.idIn(params.getAssignedIdList(Long.class));
            query.status(RecordStatus.ACTIVE).order("description");
            return getSecurityBusinessModule().findRoles(query);
        }

        return Collections.emptyList();
    }
}
