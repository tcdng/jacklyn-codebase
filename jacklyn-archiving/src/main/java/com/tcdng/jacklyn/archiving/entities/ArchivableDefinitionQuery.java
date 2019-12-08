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
package com.tcdng.jacklyn.archiving.entities;

import com.tcdng.jacklyn.common.entities.BaseInstallEntityQuery;

/**
 * Query class for archivable defintion.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ArchivableDefinitionQuery extends BaseInstallEntityQuery<ArchivableDefinition> {

    public ArchivableDefinitionQuery() {
        super(ArchivableDefinition.class);
    }

    public ArchivableDefinitionQuery moduleId(Long moduleId) {
        return (ArchivableDefinitionQuery) addEquals("moduleId", moduleId);
    }

    public ArchivableDefinitionQuery moduleName(String moduleName) {
        return (ArchivableDefinitionQuery) addEquals("moduleName", moduleName);
    }

    public ArchivableDefinitionQuery name(String name) {
        return (ArchivableDefinitionQuery) addEquals("name", name);
    }

    public ArchivableDefinitionQuery orderByDescription() {
        return (ArchivableDefinitionQuery) addOrder("description");
    }
}
