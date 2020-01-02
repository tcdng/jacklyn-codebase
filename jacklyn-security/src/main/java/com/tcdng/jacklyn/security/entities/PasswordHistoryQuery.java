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

/**
 * Query class for password history.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class PasswordHistoryQuery extends BaseEntityQuery<PasswordHistory> {

    public PasswordHistoryQuery() {
        super(PasswordHistory.class);
    }

    public PasswordHistoryQuery userId(Long userId) {
        return (PasswordHistoryQuery) addEquals("userId", userId);
    }

    public PasswordHistoryQuery password(String password) {
        return (PasswordHistoryQuery) addEquals("password", password);
    }
}
