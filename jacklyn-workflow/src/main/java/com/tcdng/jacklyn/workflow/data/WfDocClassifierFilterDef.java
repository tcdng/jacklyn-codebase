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

package com.tcdng.jacklyn.workflow.data;

import java.io.Serializable;

import com.tcdng.unify.core.criterion.FilterConditionType;

/**
 * Workflow document classifier filter definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfDocClassifierFilterDef implements Serializable {

    private static final long serialVersionUID = 5301262527629289555L;

    private FilterConditionType op;

    private String fieldName;

    private String value1;

    private String value2;

    private boolean fieldOnly;

    public WfDocClassifierFilterDef(FilterConditionType op, String fieldName, String value1, String value2,
            boolean fieldOnly) {
        this.op = op;
        this.fieldName = fieldName;
        this.value1 = value1;
        this.value2 = value2;
        this.fieldOnly = fieldOnly;
    }

    public FilterConditionType getOp() {
        return op;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    public boolean isFieldOnly() {
        return fieldOnly;
    }

}
