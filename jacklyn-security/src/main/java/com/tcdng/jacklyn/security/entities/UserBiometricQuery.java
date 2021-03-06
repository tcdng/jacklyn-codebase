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
package com.tcdng.jacklyn.security.entities;

import com.tcdng.jacklyn.common.entities.BaseEntityQuery;
import com.tcdng.jacklyn.shared.security.BiometricType;

/**
 * User biometric entity query class.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class UserBiometricQuery extends BaseEntityQuery<UserBiometric> {

    public UserBiometricQuery() {
        super(UserBiometric.class);
    }

    public UserBiometricQuery userId(Long userId) {
        return (UserBiometricQuery) addEquals("userId", userId);
    }

    public UserBiometricQuery userLoginId(String userLoginId) {
        return (UserBiometricQuery) addEquals("userLoginId", userLoginId);
    }

    public UserBiometricQuery typeName(BiometricType typeName) {
        return (UserBiometricQuery) addEquals("typeName", typeName);
    }
}
