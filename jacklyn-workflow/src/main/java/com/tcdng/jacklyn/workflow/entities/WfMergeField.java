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
package com.tcdng.jacklyn.workflow.entities;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity that represents workflow merge field definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(
        name = "JKWFMERGEFLD", uniqueConstraints = { @UniqueConstraint({ "wfBranchId", "fieldName" }) })
public class WfMergeField extends BaseEntity {

    @ForeignKey(WfBranch.class)
    private Long wfBranchId;

    @Column(name = "FIELD_NM", length = 32)
    private String fieldName;

    @Override
    public String getDescription() {
        return fieldName;
    }

    public Long getWfBranchId() {
        return wfBranchId;
    }

    public void setWfBranchId(Long wfBranchId) {
        this.wfBranchId = wfBranchId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

}
