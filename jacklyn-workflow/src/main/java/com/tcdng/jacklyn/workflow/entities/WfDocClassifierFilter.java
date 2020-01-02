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
package com.tcdng.jacklyn.workflow.entities;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.criterion.FilterConditionType;

/**
 * Workflow document classifier filter definition entity.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Table("JKWFDOCCLASSIFIERFILTER")
public class WfDocClassifierFilter extends BaseEntity {

    @ForeignKey(WfDocClassifier.class)
    private Long wfDocClassifierId;

    @ForeignKey
    private FilterConditionType op;

    @Column(length = 32)
    private String fieldName;

    @Column(name = "FILTER_VAL1", length = 64, nullable = true)
    private String value1;

    @Column(name = "FILTER_VAL2", length = 64, nullable = true)
    private String value2;

    @Column(name = "FIELDONLY_FG")
    private Boolean fieldOnly;

    @ListOnly(key = "op", property = "description")
    private String opDesc;

    @Override
    public String getDescription() {
        return fieldName;
    }

    public Long getWfDocClassifierId() {
        return wfDocClassifierId;
    }

    public void setWfDocClassifierId(Long wfDocClassifierId) {
        this.wfDocClassifierId = wfDocClassifierId;
    }

    public FilterConditionType getOp() {
        return op;
    }

    public void setOp(FilterConditionType op) {
        this.op = op;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public Boolean getFieldOnly() {
        return fieldOnly;
    }

    public void setFieldOnly(Boolean fieldOnly) {
        this.fieldOnly = fieldOnly;
    }

    public String getOpDesc() {
        return opDesc;
    }

    public void setOpDesc(String opDesc) {
        this.opDesc = opDesc;
    }
}
