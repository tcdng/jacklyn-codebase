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
package com.tcdng.jacklyn.common.constants;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.annotation.Tooling;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Constant definitions for record status.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Tooling(description = "Record Status")
@StaticList(name = "recordstatuslist", description = "Record Status")
public enum RecordStatus implements EnumConst {

    INACTIVE("I"), ACTIVE("A");

    private final String code;

    private RecordStatus(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return INACTIVE.code;
    }

    public static RecordStatus fromCode(String code) {
        return EnumUtils.fromCode(RecordStatus.class, code);
    }

    public static RecordStatus fromName(String name) {
        return EnumUtils.fromName(RecordStatus.class, name);
    }
}
