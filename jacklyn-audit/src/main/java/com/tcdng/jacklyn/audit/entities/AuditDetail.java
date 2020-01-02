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
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;

/**
 * Entity for storing audit trail detail information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = AuditModuleNameConstants.AUDIT_MODULE, title = "Audit Detail")
@Table("JKAUDITDETAIL")
public class AuditDetail extends BaseEntity {

    @ForeignKey(AuditTrail.class)
    private Long auditTrailId;

    @Column(length = 256)
    private String detail;

    public AuditDetail(String detail) {
        this.detail = detail;
    }

    public AuditDetail() {

    }
    
    @Override
    public String getDescription() {
        return this.detail;
    }

    public Long getAuditTrailId() {
        return auditTrailId;
    }

    public void setAuditTrailId(Long auditTrailId) {
        this.auditTrailId = auditTrailId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
