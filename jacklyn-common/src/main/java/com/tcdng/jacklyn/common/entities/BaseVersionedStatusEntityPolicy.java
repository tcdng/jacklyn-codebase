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
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Entity;

/**
 * Entity policy class for entity with version number and status.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("versionedstatusentity-policy")
public class BaseVersionedStatusEntityPolicy extends BaseStatusEntityPolicy {

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        ((BaseVersionedStatusEntity) record).setVersionNo(1L);
        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        BaseVersionedStatusEntity versionedRecord = (BaseVersionedStatusEntity) record;
        versionedRecord.setVersionNo(versionedRecord.getVersionNo() + 1L);
    }

    @Override
    public void onUpdateError(Entity record) {
        BaseVersionedStatusEntity versionedRecord = (BaseVersionedStatusEntity) record;
        versionedRecord.setVersionNo(versionedRecord.getVersionNo() - 1L);
    }
}
