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
package com.tcdng.jacklyn.shared.security;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Biometric types.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList(name = "biometrictypelist", description = "Biometric Type List")
public enum BiometricType implements EnumConst {

    PHOTOGRAPH("P"), SIGNATURE("S"), THUMBPRINT("T"), RETINALSCAN("R");

    private final String code;

    private BiometricType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return PHOTOGRAPH.code;
    }

    public static BiometricType fromCode(String code) {
        return EnumUtils.fromCode(BiometricType.class, code);
    }

    public static BiometricType fromName(String name) {
        return EnumUtils.fromName(BiometricType.class, name);
    }
}
