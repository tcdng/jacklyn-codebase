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

package com.tcdng.jacklyn.shared;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Form widget type constants.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList(name = "formwidgettypelist", description = "Form Widget Type List")
public enum FormWidgetType implements EnumConst {

    TEXT("TXT", "ui-text"),
    TEXTAREA("TXA", "ui-textarea"),
    NAME("NME", "ui-name"),
    WORD("WRD", "ui-word"),
    DATE("DTE", "ui-date"),
    TIME("TME", "ui-time"),
    DECIMAL("DEC", "ui-decimal"),
    INTEGER("INT", "ui-integer"),
    CHECKBOX("CHK", "ui-checkbox"),
    RADIOBUTTONS("RAD", "ui-radiobuttons"),
    SEARCH("SER", "ui-search"),
    SELECT("SEL", "ui-select"),
    IMAGE("IMG", "ui-image"),
    PICTURE("PIC", "ui-picture"),
    FILEUPLOAD("FLU", "ui-fileupload"),
    MONEY("MON", "ui-money"),
    PASSWORD("PWD", "ui-password");

    private final String code;

    private final String uplType;

    private FormWidgetType(String code, String uplType) {
        this.code = code;
        this.uplType = uplType;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultCode() {
        return TEXT.code;
    }

    public String uplType() {
        return uplType;
    }

    public static FormWidgetType fromCode(String code) {
        return EnumUtils.fromCode(FormWidgetType.class, code);
    }

    public static FormWidgetType fromName(String name) {
        return EnumUtils.fromName(FormWidgetType.class, name);
    }

}
