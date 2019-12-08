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
import com.tcdng.jacklyn.shared.archiving.ArchivingFieldType;

/**
 * Archiving field query class.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ArchivingFieldQuery extends BaseInstallEntityQuery<ArchivingField> {

    public ArchivingFieldQuery() {
        super(ArchivingField.class);
    }

    public ArchivingFieldQuery archivableDefId(Long archivableDefId) {
        return (ArchivingFieldQuery) addEquals("archivableDefId", archivableDefId);
    }

    public ArchivingFieldQuery fieldName(String fieldName) {
        return (ArchivingFieldQuery) addEquals("fieldName", fieldName);
    }

    public ArchivingFieldQuery fieldType(ArchivingFieldType fieldType) {
        return (ArchivingFieldQuery) addEquals("fieldType", fieldType);
    }

    public ArchivingFieldQuery fieldTypeIn(ArchivingFieldType[] fieldType) {
        return (ArchivingFieldQuery) addEquals("fieldType", fieldType);
    }

    public ArchivingFieldQuery fieldTypeNot(ArchivingFieldType fieldType) {
        return (ArchivingFieldQuery) addNotEqual("fieldType", fieldType);
    }

    public ArchivingFieldQuery fieldTypeNotIn(ArchivingFieldType[] fieldType) {
        return (ArchivingFieldQuery) addNotEqual("fieldType", fieldType);
    }

    public ArchivingFieldQuery recordName(String recordName) {
        return (ArchivingFieldQuery) addEquals("recordName", recordName);
    }

    public ArchivingFieldQuery orderByFieldType() {
        return (ArchivingFieldQuery) addOrder("fieldType");
    }

    public ArchivingFieldQuery orderByDescription() {
        return (ArchivingFieldQuery) addOrder("description");
    }
}
