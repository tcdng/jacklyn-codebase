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
package com.tcdng.jacklyn.common.entities;

import com.tcdng.jacklyn.common.annotation.Format;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Policy;

/**
 * Base class for entity that require status.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Policy("statusentity-policy")
public abstract class BaseStatusEntity extends BaseEntity {

    @ForeignKey(name = "REC_ST", position = ColumnPositionConstants.BASE_COLUMN_POSITION)
    private RecordStatus status;

    @Format(description = "$m{common.status}")
    @ListOnly(key = "status", property = "description")
    private String statusDesc;

    public RecordStatus getStatus() {
        return status;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
