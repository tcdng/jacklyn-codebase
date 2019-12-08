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

import com.tcdng.jacklyn.common.entities.BaseTimestampedEntityQuery;
import com.tcdng.unify.core.logging.EventType;

/**
 * Query class for audit trail.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class AuditTrailQuery extends BaseTimestampedEntityQuery<AuditTrail> {

    public AuditTrailQuery() {
        super(AuditTrail.class);
    }

    public AuditTrailQuery userLoginId(String userLoginId) {
        return (AuditTrailQuery) addEquals("userLoginId", userLoginId);
    }

    public AuditTrailQuery moduleId(Long moduleId) {
        return (AuditTrailQuery) addEquals("moduleId", moduleId);
    }

    public AuditTrailQuery moduleName(String moduleName) {
        return (AuditTrailQuery) addEquals("moduleName", moduleName);
    }

    public AuditTrailQuery auditDefinitionName(String auditDefinitionName) {
        return (AuditTrailQuery) addEquals("auditDefinitionName", auditDefinitionName);
    }

    public AuditTrailQuery recordName(String recordName) {
        return (AuditTrailQuery) addEquals("recordName", recordName);
    }

    public AuditTrailQuery recordId(Long recordId) {
        return (AuditTrailQuery) addEquals("recordId", recordId);
    }

    public AuditTrailQuery eventType(EventType eventType) {
        return (AuditTrailQuery) addEquals("eventType", eventType);
    }
}
