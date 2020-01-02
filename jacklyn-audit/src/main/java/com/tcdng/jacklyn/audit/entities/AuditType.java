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
package com.tcdng.jacklyn.audit.entities;

import com.tcdng.jacklyn.audit.constants.AuditModuleNameConstants;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseInstallEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Audit type.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Managed(module = AuditModuleNameConstants.AUDIT_MODULE, title = "Audit Type")
@Table(name = "JKAUDITTYPE", uniqueConstraints = { @UniqueConstraint({ "recordName" }) })
public class AuditType extends BaseInstallEntity {

    @Column(name = "RECORD_NM", length = 128)
    private String recordName;

    @Override
    public String getDescription() {
        return this.recordName;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

}
