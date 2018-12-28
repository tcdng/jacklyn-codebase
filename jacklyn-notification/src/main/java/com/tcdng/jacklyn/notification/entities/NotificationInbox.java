/*
 * Copyright 2018 The Code Department
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

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.data.SystemNotification;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.notification.constants.NotificationModuleNameConstants;
import com.tcdng.jacklyn.shared.notification.NotificationInboxReadStatus;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Notification inbox entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = NotificationModuleNameConstants.NOTIFICATION_MODULE, title = "Notification Inbox", reportable = true)
@Table("NOTIFICATIONINBOX")
public class NotificationInbox extends BaseEntity implements SystemNotification {

    @ForeignKey(name = "REC_ST")
    private NotificationInboxReadStatus status;

    @Column(name = "NOTIF_SUBJECT", length = 64)
    private String subject;

    @Column(name = "NOTIF_MESSAGE", length = 512)
    private String message;

    @Column(name = "NOTIF_LINK", length = 64, nullable = true)
    private String link;

    @Column(name = "NOTIF_TARGET", nullable = true)
    private String target;

    @Column(name = "USER_ID")
    private String userId;

    @ListOnly(key = "status", property = "description")
    private String statusDesc;

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

    @Override
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

}
