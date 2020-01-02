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

package com.tcdng.jacklyn.notification.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.data.SystemNotification;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.notification.constants.NotificationModuleNameConstants;
import com.tcdng.jacklyn.shared.notification.MessageType;
import com.tcdng.jacklyn.shared.notification.NotificationInboxReadStatus;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.Table;

/**
 * Notification inbox entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = NotificationModuleNameConstants.NOTIFICATION_MODULE, title = "Notification Inbox", reportable = true)
@Policy("notificationinbox-policy")
@Table("JKNOTIFICATIONINBOX")
public class NotificationInbox extends BaseEntity implements SystemNotification {

    @ForeignKey(name = "REC_ST")
    private NotificationInboxReadStatus status;

    @ForeignKey
    private MessageType messageType;

    @Column(name = "NOTIF_SUBJECT", length = 64)
    private String subject;

    @Column(name = "NOTIF_MESSAGE", length = 512)
    private String message;

    @Column(length = 64, nullable = true)
    private String actionLink;

    @Column(nullable = true)
    private String actionTarget;

    @Column(name = "USER_ID")
    private String userId;

    @Column(type = ColumnType.TIMESTAMP_UTC)
    private Date createDt;

    @ListOnly(key = "status", property = "description")
    private String statusDesc;

    @ListOnly(key = "messageType", property = "description")
    private String messageTypeDesc;

    @Override
    public String getDescription() {
        return subject;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public NotificationInboxReadStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationInboxReadStatus status) {
        this.status = status;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public String getActionLink() {
        return actionLink;
    }

    public void setActionLink(String actionLink) {
        this.actionLink = actionLink;
    }

    @Override
    public String getActionTarget() {
        return actionTarget;
    }

    public void setActionTarget(String actionTarget) {
        this.actionTarget = actionTarget;
    }

    @Override
    public String getIcon() {
        if (messageType != null) {
            return messageType.icon();
        }

        return null;
    }

    @Override
    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public String getMessageTypeDesc() {
        return messageTypeDesc;
    }

    public void setMessageTypeDesc(String messageTypeDesc) {
        this.messageTypeDesc = messageTypeDesc;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

}
