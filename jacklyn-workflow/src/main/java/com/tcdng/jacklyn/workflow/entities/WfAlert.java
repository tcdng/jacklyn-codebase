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

package com.tcdng.jacklyn.workflow.entities;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.shared.workflow.WorkflowAlertType;
import com.tcdng.jacklyn.shared.workflow.WorkflowParticipantType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Workflow alert definition data.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "JKWFALERT", uniqueConstraints = { @UniqueConstraint({ "wfStepId", "name" }),
        @UniqueConstraint({ "wfStepId", "description" }) })
public class WfAlert extends BaseEntity {

    @ForeignKey(WfStep.class)
    private Long wfStepId;

    @ForeignKey(name = "NOTIFICATION_CHANNEL")
    private NotificationType channel;
    
    @ForeignKey(name = "PARTICIPANT_TY")
    private WorkflowParticipantType participant;

    @ForeignKey(name = "ALERT_TY")
    private WorkflowAlertType type;

    @Column(name = "ALERT_NM", length = 32)
    private String name;

    @Column(name = "ALERT_DESC", length = 64)
    private String description;

    @Column(name = "DOC_NM", length = 32)
    private String docName;

    @Column(name = "FIRE_ON_PREV_STEP_NM", length = 64, nullable = true)
    private String fireOnPrevStepName;

    @Column(name = "NOTIFICATION_TMPL_CD")
    private String notificationTemplateCode;

    @ListOnly(key = "wfStepId", property = "name")
    private String wfStepName;

    @ListOnly(key = "wfStepId", property = "description")
    private String wfStepDesc;

    @ListOnly(key = "type", property = "description")
    private String wfTypeDesc;

    @ListOnly(key = "channel", property = "description")
    private String notificationChannelDesc;

    @ListOnly(key = "participant", property = "description")
    private String participantDesc;

    @Override
    public String getDescription() {
        return description;
    }

    public Long getWfStepId() {
        return wfStepId;
    }

    public void setWfStepId(Long wfStepId) {
        this.wfStepId = wfStepId;
    }

    public NotificationType getChannel() {
        return channel;
    }

    public void setChannel(NotificationType channel) {
        this.channel = channel;
    }

    public WorkflowParticipantType getParticipant() {
        return participant;
    }

    public void setParticipant(WorkflowParticipantType participant) {
        this.participant = participant;
    }

    public WorkflowAlertType getType() {
        return type;
    }

    public void setType(WorkflowAlertType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getFireOnPrevStepName() {
        return fireOnPrevStepName;
    }

    public void setFireOnPrevStepName(String fireOnPrevStepName) {
        this.fireOnPrevStepName = fireOnPrevStepName;
    }

    public String getNotificationTemplateCode() {
        return notificationTemplateCode;
    }

    public void setNotificationTemplateCode(String notificationTemplateCode) {
        this.notificationTemplateCode = notificationTemplateCode;
    }

    public String getWfStepName() {
        return wfStepName;
    }

    public void setWfStepName(String wfStepName) {
        this.wfStepName = wfStepName;
    }

    public String getWfStepDesc() {
        return wfStepDesc;
    }

    public void setWfStepDesc(String wfStepDesc) {
        this.wfStepDesc = wfStepDesc;
    }

    public String getWfTypeDesc() {
        return wfTypeDesc;
    }

    public void setWfTypeDesc(String wfTypeDesc) {
        this.wfTypeDesc = wfTypeDesc;
    }

    public String getNotificationChannelDesc() {
        return notificationChannelDesc;
    }

    public void setNotificationChannelDesc(String notificationChannelDesc) {
        this.notificationChannelDesc = notificationChannelDesc;
    }

    public String getParticipantDesc() {
        return participantDesc;
    }

    public void setParticipantDesc(String participantDesc) {
        this.participantDesc = participantDesc;
    }

}
