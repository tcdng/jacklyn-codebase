/*
 * Copyright 2018-2020 The Code Department.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * UnaddLessThan required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.jacklyn.notification.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.entities.BaseTimestampedEntityQuery;
import com.tcdng.jacklyn.shared.notification.NotificationStatus;
import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Message query class.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class NotificationQuery extends BaseTimestampedEntityQuery<Notification> {

    public NotificationQuery() {
        super(Notification.class);
    }

    public NotificationQuery notificationTemplateId(Long notificationTemplateId) {
        return (NotificationQuery) addEquals("notificationTemplateId", notificationTemplateId);
    }

    public NotificationQuery moduleId(Long moduleId) {
        return (NotificationQuery) addEquals("moduleId", moduleId);
    }

    public NotificationQuery moduleName(String moduleName) {
        return (NotificationQuery) addEquals("moduleName", moduleName);
    }

    public NotificationQuery notificationTemplateName(String notificationTemplateName) {
        return (NotificationQuery) addEquals("notificationTemplateName", notificationTemplateName);
    }

    public NotificationQuery notificationType(NotificationType notificationType) {
        return (NotificationQuery) addEquals("notificationType", notificationType);
    }

    public NotificationQuery sentOn(Date date) {
        return (NotificationQuery) addBetween("sentDt", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }

    public NotificationQuery due(Date now) {
        return (NotificationQuery) addLessThanEqual("dueDt", now);
    }

    public NotificationQuery createdOn(Date date) {
        return (NotificationQuery) addBetween("createDt", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }

    public NotificationQuery status(NotificationStatus status) {
        return (NotificationQuery) addEquals("status", status);
    }
}
