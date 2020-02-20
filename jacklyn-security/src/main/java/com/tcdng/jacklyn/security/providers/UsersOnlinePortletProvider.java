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

package com.tcdng.jacklyn.security.providers;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.security.business.SecurityService;
import com.tcdng.jacklyn.security.entities.UserQuery;
import com.tcdng.jacklyn.statistics.data.QuickPercentage;
import com.tcdng.jacklyn.statistics.providers.AbstractQuickPercentageProvider;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Users online portlet provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("usersonline-portletprovider")
public class UsersOnlinePortletProvider extends AbstractQuickPercentageProvider {

    @Configurable
    private SystemService systemService;

    @Configurable
    private SecurityService securityService;

    @Override
    protected QuickPercentage doProvide(Object... params) throws UnifyException {
        return new QuickPercentage(systemService.getUniqueActiveUserSessions(),
                securityService.countUsers((UserQuery) new UserQuery().status(RecordStatus.ACTIVE)));
    }

}
