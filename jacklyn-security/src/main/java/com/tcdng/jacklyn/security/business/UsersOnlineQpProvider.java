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

package com.tcdng.jacklyn.security.business;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.security.entities.UserQuery;
import com.tcdng.jacklyn.statistics.business.AbstractIntegerQuickPercentageProvider;
import com.tcdng.jacklyn.statistics.data.IntegerQuickPercentage;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Users online quick percentage provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("usersonline-qpprovider")
public class UsersOnlineQpProvider extends AbstractIntegerQuickPercentageProvider {

    @Configurable
    private SystemService systemService;

    @Configurable
    private SecurityService securityService;

    @Override
    public IntegerQuickPercentage provide() throws UnifyException {
        return new IntegerQuickPercentage(systemService.getUniqueActiveUserSessions(),
                securityService.countUsers((UserQuery) new UserQuery().status(RecordStatus.ACTIVE)));
    }

}
