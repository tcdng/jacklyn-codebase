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
package com.tcdng.jacklyn.common.constants;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Assignment status.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList("assignmentstatuslist")
public enum AssignmentStatus implements EnumConst {

    ASSIGNED("A"), UNASSIGNED("U");

    private final String code;

    private AssignmentStatus(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    public static AssignmentStatus fromCode(String code) {
        return EnumUtils.fromCode(AssignmentStatus.class, code);
    }

    public static AssignmentStatus fromName(String name) {
        return EnumUtils.fromName(AssignmentStatus.class, name);
    }
}
