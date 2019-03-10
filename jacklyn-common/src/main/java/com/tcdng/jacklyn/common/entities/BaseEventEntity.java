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

import com.tcdng.jacklyn.common.annotation.Format;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.Tooling;

/**
 * Base class for entity that require a creation event timestamp.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Tooling(name = "baseEventEntity", description = "Base Event")
@Policy("evententity-policy")
public abstract class BaseEventEntity extends BaseEntity {

    @Format(formatter = "!datetimeformat")
    @Column(type = ColumnType.TIMESTAMP, position = ColumnPositionConstants.BASE_COLUMN_POSITION)
    private Date createDt;

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }
}
