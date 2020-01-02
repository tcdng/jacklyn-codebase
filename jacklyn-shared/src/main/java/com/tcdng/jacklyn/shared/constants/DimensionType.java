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

package com.tcdng.jacklyn.shared.constants;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Dimension type.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList("dimensiontypelist")
public enum DimensionType implements EnumConst {

    PERCENTAGE("PER", "%"), PIXELS("PIX", "px");

    private final String code;

    private final String symbol;

    private DimensionType(String code, String symbol) {
        this.code = code;
        this.symbol = symbol;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultCode() {
        return PERCENTAGE.code;
    }

    public String symbol() {
        return symbol;
    }

    public static DimensionType fromCode(String code) {
        return EnumUtils.fromCode(DimensionType.class, code);
    }

    public static DimensionType fromName(String name) {
        return EnumUtils.fromName(DimensionType.class, name);
    }
}
