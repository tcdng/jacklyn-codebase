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
package com.tcdng.jacklyn.common.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.annotation.Format;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.Tooling;

/**
 * Base class for entity that require a timestamp on create and every update.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Tooling(name = "baseTStmpEntity", description = "Base Timestamped")
@Policy("timestampedentity-policy")
public abstract class BaseTimestampedEntity extends BaseEventEntity {

    @Format(formatter = "!datetimeformat")
    @Column(type = ColumnType.TIMESTAMP_UTC, position = ColumnPositionConstants.BASE_COLUMN_POSITION)
    private Date updateDt;

    @Column(length = 64)
    private String updateBy;

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
