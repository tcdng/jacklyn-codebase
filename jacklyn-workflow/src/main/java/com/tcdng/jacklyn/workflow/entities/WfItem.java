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
package com.tcdng.jacklyn.workflow.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.entities.BaseTimestampedEntity;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Represents a workflow item.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Table("JKWFITEM")
public class WfItem extends BaseTimestampedEntity {

    @ForeignKey(type = WfItemEvent.class, nullable = true)
    private Long wfHistEventId;

    @Column(name = "GLOBAL_TEMPLATE_NM", length = 64)
    private String globalTemplateName;

    @Column(nullable = true)
    private Long ownerId;

    @Column(length = 32, nullable = true)
    private String wfStepName;

    @Column(name = "WFITEM_DESC", length = 128)
    private String description;

    @Column(nullable = true)
    private WorkflowParticipantType participantType;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date stepDt;

    @Column(nullable = true)
    private String heldBy;

    @Column(nullable = true)
    private String forwardedBy;

    @ListOnly(key = "wfHistEventId", property = "wfItemHistId")
    private Long wfItemHistId;

    @ListOnly(key = "wfHistEventId", property = "documentId")
    private Long documentId;

    public Long getWfItemHistId() {
        return wfItemHistId;
    }

    public void setWfItemHistId(Long wfItemHistId) {
        this.wfItemHistId = wfItemHistId;
    }

    public String getGlobalTemplateName() {
        return globalTemplateName;
    }

    public void setGlobalTemplateName(String globalTemplateName) {
        this.globalTemplateName = globalTemplateName;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getWfHistEventId() {
        return wfHistEventId;
    }

    public void setWfHistEventId(Long wfHistEventId) {
        this.wfHistEventId = wfHistEventId;
    }

    @Override
    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getWfStepName() {
        return wfStepName;
    }

    public void setWfStepName(String wfStepName) {
        this.wfStepName = wfStepName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStepDt() {
        return stepDt;
    }

    public void setStepDt(Date stepDt) {
        this.stepDt = stepDt;
    }

    public String getHeldBy() {
        return heldBy;
    }

    public void setHeldBy(String heldBy) {
        this.heldBy = heldBy;
    }

    public String getForwardedBy() {
        return forwardedBy;
    }

    public void setForwardedBy(String forwardedBy) {
        this.forwardedBy = forwardedBy;
    }

    public WorkflowParticipantType getParticipantType() {
        return participantType;
    }

    public void setParticipantType(WorkflowParticipantType participantType) {
        this.participantType = participantType;
    }

}
