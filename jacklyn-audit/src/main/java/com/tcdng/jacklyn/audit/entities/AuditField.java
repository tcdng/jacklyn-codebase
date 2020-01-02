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
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity for storing auditable record field information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = AuditModuleNameConstants.AUDIT_MODULE, title = "Audit Field")
@Table(name = "JKAUDITFIELD", uniqueConstraints = { @UniqueConstraint({ "auditTypeId", "fieldName" }) })
public class AuditField extends BaseInstallEntity {

    @ForeignKey(AuditType.class)
    private Long auditTypeId;

    @Column(name = "FIELD_NM", length = 64)
    private String fieldName;

    @Column(name = "FIELD_DESC", length = 64)
    private String fieldDescription;

    @Column(nullable = true)
    private String formatter;

    @Column(nullable = true)
    private String list;

    @Column
    private boolean mask;

    @Override
    public String getDescription() {
        return this.fieldDescription;
    }

    public Long getAuditTypeId() {
        return auditTypeId;
    }

    public void setAuditTypeId(Long auditTypeId) {
        this.auditTypeId = auditTypeId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public boolean isMask() {
        return mask;
    }

    public void setMask(boolean mask) {
        this.mask = mask;
    }
}
