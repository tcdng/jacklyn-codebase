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

package com.tcdng.jacklyn.shared.workflow;

import com.tcdng.jacklyn.shared.FormWidgetType;
import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Workflow form widget type constants. This enumeration is deprecated. Use {@link FormWidgetType}.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Deprecated
@StaticList("wfformwidgettypelist")
public enum WorkflowFormWidgetType implements EnumConst {

    TEXT("TXT", "ui-text"), NAME("NME", "ui-name"), WORD("WRD", "ui-word"), DATE("DTE", "ui-date"), TIME("TME",
            "ui-time"), DECIMAL("DEC", "ui-decimal"), INTEGER("INT", "ui-integer"), CHECKBOX("CHK",
                    "ui-checkbox"), RADIOBUTTONS("RAD", "ui-radiobuttons"), SEARCH("SER", "ui-search"), SELECT("SEL",
                            "ui-select"), IMAGE("IMG", "ui-image"), PICTURE("PIC",
                                    "ui-picture"), MONEY("MON", "ui-money"), PASSWORD("PWD", "ui-password");

    private final String code;

    private final String uplType;

    private WorkflowFormWidgetType(String code, String uplType) {
        this.code = code;
        this.uplType = uplType;
    }

    @Override
    public String code() {
        return code;
    }

    public String uplType() {
        return uplType;
    }

    public static WorkflowFormWidgetType fromCode(String code) {
        return EnumUtils.fromCode(WorkflowFormWidgetType.class, code);
    }

    public static WorkflowFormWidgetType fromName(String name) {
        return EnumUtils.fromName(WorkflowFormWidgetType.class, name);
    }

}
