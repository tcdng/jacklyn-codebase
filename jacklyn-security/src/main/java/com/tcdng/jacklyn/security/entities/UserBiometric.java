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

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.shared.security.BiometricType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * User biometric entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "JKUSERBIOMETRIC", uniqueConstraints = { @UniqueConstraint({ "userId", "biometricId" }) })
public class UserBiometric extends BaseEntity {

    @ForeignKey(User.class)
    private Long userId;

    @ForeignKey(Biometric.class)
    private Long biometricId;

    @ListOnly(key = "userId", property = "loginId")
    private String userLoginId;

    @ListOnly(key = "biometricId", property = "type")
    private BiometricType typeName;

    @ListOnly(key = "biometricId", property = "typeDesc")
    private String typeDesc;

    @ListOnly(key = "biometricId", property = "biometric")
    private byte[] biometric;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBiometricId() {
        return biometricId;
    }

    public void setBiometricId(Long biometricId) {
        this.biometricId = biometricId;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public BiometricType getTypeName() {
        return typeName;
    }

    public void setTypeName(BiometricType typeName) {
        this.typeName = typeName;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public byte[] getBiometric() {
        return biometric;
    }

    public void setBiometric(byte[] biometric) {
        this.biometric = biometric;
    }

}
