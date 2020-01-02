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

import java.util.List;

import com.tcdng.jacklyn.audit.constants.AuditModuleNameConstants;
import com.tcdng.jacklyn.common.annotation.Format;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseTimestampedEntity;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Index;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.logging.EventType;

/**
 * Entity for storing audit trail information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = AuditModuleNameConstants.AUDIT_MODULE, title = "Audit Trail", reportable = true, auditable = true)
@Table(name = "JKAUDITTRAIL", indexes = {@Index("userLoginId")})
public class AuditTrail extends BaseTimestampedEntity {

    @ForeignKey(AuditDefinition.class)
    private Long auditDefinitionId;

    @Column(length = 64)
    private String userLoginId;

    @Column(length = 64)
    private String ipAddress;

    @Column(nullable = true)
    private Long recordId;

    @Column(name = "REMOTE_FG", nullable = true)
    private Boolean remoteEvent;

    @ChildList
    private List<AuditDetail> auditDetailList;

    @ListOnly(key = "auditDefinitionId", property = "name")
    private String auditDefinitionName;

    @ListOnly(key = "auditDefinitionId", property = "moduleId")
    private Long moduleId;

    @ListOnly(name = "MODULE_NM", key = "auditDefinitionId", property = "moduleName")
    private String moduleName;

    @Format(description = "$m{audit.audittrail.module}")
    @ListOnly(name = "MODULE_DESC", key = "auditDefinitionId", property = "moduleDesc")
    private String moduleDesc;

    @ListOnly(key = "auditDefinitionId", property = "eventType")
    private EventType eventType;

    @ListOnly(key = "auditDefinitionId", property = "description")
    private String auditDesc;

    @ListOnly(key = "auditDefinitionId", property = "actionDesc")
    private String actionDesc;

    @ListOnly(key = "auditDefinitionId", property = "recordName")
    private String recordName;

    @Override
    public String getDescription() {
        return this.userLoginId + " - " + this.actionDesc;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Boolean isRemoteEvent() {
        return remoteEvent;
    }

    public void setRemoteEvent(Boolean remoteEvent) {
        this.remoteEvent = remoteEvent;
    }

    public List<AuditDetail> getAuditDetailList() {
        return auditDetailList;
    }

    public void setAuditDetailList(List<AuditDetail> auditDetailList) {
        this.auditDetailList = auditDetailList;
    }

    public String getAuditDefinitionName() {
        return auditDefinitionName;
    }

    public void setAuditDefinitionName(String auditDefinitionName) {
        this.auditDefinitionName = auditDefinitionName;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    public Long getAuditDefinitionId() {
        return auditDefinitionId;
    }

    public void setAuditDefinitionId(Long auditDefinitionId) {
        this.auditDefinitionId = auditDefinitionId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }
}
