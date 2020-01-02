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

package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.entities.BaseStatusEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;

/**
 * Authentication entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table("JKAUTHENTICATION")
public class Authentication extends BaseStatusEntity {

    @Column(length = 32)
    private String name;

    @Column(length = 64)
    private String description;

    @Column(length = 32)
    private String userName;

    @Column(length = 256)
    private String password;

    @Column(length = 32)
    private String cryptograph;

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCryptograph() {
        return cryptograph;
    }

    public void setCryptograph(String cryptograph) {
        this.cryptograph = cryptograph;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
