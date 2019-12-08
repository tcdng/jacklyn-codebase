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
package com.tcdng.jacklyn.audit.entities;

import java.util.Collection;

import com.tcdng.jacklyn.common.entities.BaseInstallEntityQuery;

/**
 * Query class for audit type fields.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class AuditFieldQuery extends BaseInstallEntityQuery<AuditField> {

    public AuditFieldQuery() {
        super(AuditField.class);
    }

    public AuditFieldQuery auditTypeId(Long auditTypeId) {
        return (AuditFieldQuery) addEquals("auditTypeId", auditTypeId);
    }

    public AuditFieldQuery fieldName(String fieldName) {
        return (AuditFieldQuery) addEquals("fieldName", fieldName);
    }

    public AuditFieldQuery fieldNameLike(String fieldName) {
        return (AuditFieldQuery) addLike("fieldName", fieldName);
    }

    public AuditFieldQuery fieldNameIn(Collection<String> fieldNames) {
        return (AuditFieldQuery) addAmongst("fieldName", fieldNames);
    }

    public AuditFieldQuery fieldNameNotIn(Collection<String> fieldNames) {
        return (AuditFieldQuery) addNotAmongst("fieldName", fieldNames);
    }
}
