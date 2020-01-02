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
package com.tcdng.jacklyn.security.entities;

import java.util.Calendar;
import java.util.Date;

import com.tcdng.jacklyn.common.entities.BaseVersionedTimestampedStatusEntityPolicy;
import com.tcdng.jacklyn.security.constants.SecurityModuleSysParamConstants;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.Entity;

/**
 * User data entity policy.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("userpolicy")
public class UserPolicy extends BaseVersionedTimestampedStatusEntityPolicy {

    @Configurable
    private SystemService systemService;

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        User user = (User) record;
        if (user.getChangePassword() == null) {
            user.setChangePassword(Boolean.TRUE);
        }

        if (user.getPasswordExpires() == null) {
            user.setPasswordExpires(Boolean.TRUE);
        }

        if (user.getLoginLocked() == null) {
            user.setLoginLocked(Boolean.FALSE);
        }

        if (user.getAllowMultipleLogin() == null) {
            user.setAllowMultipleLogin(Boolean.FALSE);
        }

        calcPasswordExpiryDate(user, now);

        user.setLoginAttempts(Integer.valueOf(0));
        user.setLastLoginDt(null);
        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        calcPasswordExpiryDate((User) record, now);
        super.preUpdate(record, now);
    }

    private void calcPasswordExpiryDate(User user, Date now) throws UnifyException {
        if (user.getPasswordExpires() && user.getPasswordExpiryDt() == null && systemService
                .getSysParameterValue(boolean.class, SecurityModuleSysParamConstants.ENABLE_PASSWORD_EXPIRY)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_YEAR,
                    systemService.getSysParameterValue(int.class, SecurityModuleSysParamConstants.PASSWORD_EXPIRY_DAYS));
            user.setPasswordExpiryDt(cal.getTime());
        } else {
            user.setPasswordExpiryDt(null);
        }
    }
}
