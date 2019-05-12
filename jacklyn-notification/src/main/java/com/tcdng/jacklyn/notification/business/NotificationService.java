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
package com.tcdng.jacklyn.notification.business;

import java.util.List;

import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.jacklyn.notification.data.Message;
import com.tcdng.jacklyn.notification.data.NotificationTemplateDef;
import com.tcdng.jacklyn.notification.entities.Notification;
import com.tcdng.jacklyn.notification.entities.NotificationChannel;
import com.tcdng.jacklyn.notification.entities.NotificationChannelQuery;
import com.tcdng.jacklyn.notification.entities.NotificationQuery;
import com.tcdng.jacklyn.notification.entities.NotificationTemplate;
import com.tcdng.jacklyn.notification.entities.NotificationTemplateQuery;
import com.tcdng.jacklyn.shared.notification.MessageType;
import com.tcdng.jacklyn.shared.notification.NotificationStatus;
import com.tcdng.jacklyn.shared.notification.data.ToolingAttachmentGenItem;
import com.tcdng.unify.core.UnifyException;

/**
 * Message business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface NotificationService extends JacklynBusinessService {

    /**
     * Creates a new notification channel.
     * 
     * @param notificationChannel
     *            the notification channel data
     * @return the created notification channel ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createNotificationChannel(NotificationChannel notificationChannel) throws UnifyException;

    /**
     * Finds notification channel by ID.
     * 
     * @param notificationChannelId
     *            the notification channel ID
     * @return the notification channel data
     * @throws UnifyException
     *             if notificationChannel with ID is not found
     */
    NotificationChannel findNotificationChannel(Long notificationChannelId) throws UnifyException;

    /**
     * Finds notification channels by query.
     * 
     * @param query
     *            the notification channel query
     * @return the list of notification channels found
     * @throws UnifyException
     *             if an error occurs
     */
    List<NotificationChannel> findNotificationChannels(NotificationChannelQuery query) throws UnifyException;

    /**
     * Updates a notification channel.
     * 
     * @param notificationChannel
     *            the notification channel data
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateNotificationChannel(NotificationChannel notificationChannel) throws UnifyException;

    /**
     * Deletes a notification channel.
     * 
     * @param id
     *            the notification channel ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteNotificationChannel(Long id) throws UnifyException;

    /**
     * Creates a new notification template.
     * 
     * @param notificationTemplate
     *            the notification template data
     * @return the created notification template ID
     * @throws UnifyException
     *             if an error occurs
     */
    Long createNotificationTemplate(NotificationTemplate notificationTemplate) throws UnifyException;

    /**
     * Finds notification template by ID.
     * 
     * @param notificationTemplateId
     *            the message template ID
     * @return the message template data
     * @throws UnifyException
     *             if notificationTemplate with ID is not found
     */
    NotificationTemplate findNotificationTemplate(Long notificationTemplateId) throws UnifyException;

    /**
     * Finds notification template by module and name.
     * 
     * @param moduleName
     *            the module name
     * @param name
     *            the template name
     * @return the notification template data if found, otherwise null
     * @throws UnifyException
     *             if an error occurs
     */
    NotificationTemplate findNotificationTemplate(String moduleName, String name) throws UnifyException;

    /**
     * Finds notification templates by query.
     * 
     * @param query
     *            the notification template query
     * @return the list of notification templates found
     * @throws UnifyException
     *             if an error occurs
     */
    List<NotificationTemplate> findNotificationTemplates(NotificationTemplateQuery query) throws UnifyException;

    /**
     * Updates a notification template.
     * 
     * @param notificationTemplate
     *            the notification template
     * @return the update count
     * @throws UnifyException
     *             if an error occurs
     */
    int updateNotificationTemplate(NotificationTemplate notificationTemplate) throws UnifyException;

    /**
     * Deletes a notification template.
     * 
     * @param id
     *            the notification template ID
     * @return the delete count
     * @throws UnifyException
     *             if an error occurs
     */
    int deleteNotificationTemplate(Long id) throws UnifyException;

    /**
     * Returns a runtime notification template definition.
     * 
     * @param templateGlobalName
     *            the template name
     * @return the runtime definition
     * @throws UnifyException
     *             if notification with global template name does not exist. if an
     *             error occurs
     */
    NotificationTemplateDef getRuntimeNotificationTemplateDef(String templateGlobalName) throws UnifyException;

    /**
     * Sends a notification. This is an asynchronous call where message is pushed
     * into communication system which is left to do actual notification
     * transmission.
     * 
     * @param message
     *            the message to send
     * @throws UnifyException
     *             if an error occurs
     */
    void sendNotification(Message message) throws UnifyException;

    /**
     * Finds notifications by query.
     * 
     * @param query
     *            the notification query
     * @return the list of notifications found
     * @throws UnifyException
     *             if an error occurs
     */
    List<Notification> findNotifications(NotificationQuery query) throws UnifyException;

    /**
     * Finds notification by ID.
     * 
     * @param notificationId
     *            the notification ID
     * @return the notification data
     * @throws UnifyException
     *             if notification with ID is not found
     */
    Notification findNotification(Long notificationId) throws UnifyException;

    /**
     * Sets the status of notifications with IDs in supplied list.
     * 
     * @param notificationIds
     *            the list of notification IDs
     * @param status
     *            the status to set notification to
     * @return the number of records updated
     * @throws UnifyException
     *             if an error occurs
     */
    int setNotificationStatus(List<Long> notificationIds, NotificationStatus status) throws UnifyException;

    /**
     * Creates system notification for particular users.
     * 
     * @param messageType
     *            the message type
     * @param subject
     *            the notification subject
     * @param message
     *            the notification message
     * @param link
     *            optional reference link
     * @param reference
     *            optional reference
     * @param userIdList
     *            the user ID list
     * @throws UnifyException
     *             if an error occurs
     */
    void createUserSystemNotifications(MessageType messageType, String subject, String message, String link,
            String reference, List<String> userIdList) throws UnifyException;

    /**
     * Finds all tooling attachment generator types.
     * 
     * @return list of attachment generator types
     * @throws UnifyException
     *             if an error occurs
     */
    List<ToolingAttachmentGenItem> findToolingAttachmentGenTypes() throws UnifyException;
}
