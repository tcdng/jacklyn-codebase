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

package com.tcdng.jacklyn.system.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.tcdng.jacklyn.shared.system.SystemAssetType;
import com.tcdng.jacklyn.system.entities.SystemAssetQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.data.AssignParams;

/**
 * System asset in list command.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("systemassetinlist")
public class SystemAssetInListCommand extends AbstractAssignParamsSystemListCommand {

    @Override
    public List<? extends Listable> execute(Locale locale, AssignParams params) throws UnifyException {
        if (params.isAssignedIdList()) {
            SystemAssetQuery query = new SystemAssetQuery();
            query.idIn(params.getAssignedIdList(Long.class));

            if (QueryUtils.isValidStringCriteria(params.getFilterId1())) {
                query.type(params.getFilterId1(SystemAssetType.class));
            }

            if (QueryUtils.isValidStringCriteria(params.getFilterId2())) {
                query.moduleId(params.getFilterId2(Long.class));
            }

            query.installed(Boolean.TRUE);
            query.addOrder("description");
            return getSystemService().findSystemAssets(query);
        }

        return Collections.emptyList();
    }

}
