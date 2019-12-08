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
import com.tcdng.unify.core.logging.EventType;

/**
 * Query class for audit definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class AuditDefinitionQuery extends BaseInstallEntityQuery<AuditDefinition> {

    public AuditDefinitionQuery() {
        super(AuditDefinition.class);
    }

    public AuditDefinitionQuery name(String name) {
        return (AuditDefinitionQuery) addEquals("name", name);
    }

    public AuditDefinitionQuery nameIn(Collection<String> name) {
        return (AuditDefinitionQuery) addAmongst("name", name);
    }

    public AuditDefinitionQuery recordName(String recordName) {
        return (AuditDefinitionQuery) addEquals("recordName", recordName);
    }

    public AuditDefinitionQuery moduleId(Long moduleId) {
        return (AuditDefinitionQuery) addEquals("moduleId", moduleId);
    }

    public AuditDefinitionQuery moduleName(String moduleName) {
        return (AuditDefinitionQuery) addEquals("moduleName", moduleName);
    }

    public AuditDefinitionQuery eventType(EventType eventType) {
        return (AuditDefinitionQuery) addEquals("eventType", eventType);
    }
}
