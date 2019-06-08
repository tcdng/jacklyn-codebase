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
package com.tcdng.jacklyn.common.entities;

import java.util.Date;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.system.entities.SequencedEntityPolicy;

/**
 * Base entity policy.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("baseentity-policy")
public class BaseEntityPolicy extends SequencedEntityPolicy {

    String SYSTEM_LOGINID = "SYSTEM";

    public BaseEntityPolicy() {

    }

    public BaseEntityPolicy(boolean setNow) {
        super(setNow);
    }

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        BaseEntity baseEntity = ((BaseEntity) record);
        Long id = baseEntity.getId();
        if (id == null || id >= 0) {
            return super.preCreate(record, now);
        }
        return id;
    }
    
    protected String getUserLoginId() throws UnifyException {
        UserToken userToken = getUserToken();
        if (userToken != null) {
            return userToken.getUserLoginId();
        }
        
        return SYSTEM_LOGINID;
    }
}
