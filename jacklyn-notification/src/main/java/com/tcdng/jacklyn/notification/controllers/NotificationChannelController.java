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
package com.tcdng.jacklyn.notification.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.notification.entities.NotificationChannel;
import com.tcdng.jacklyn.notification.entities.NotificationChannelQuery;
import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing notification channels.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/notification/notificationchannel")
@UplBinding("web/notification/upl/managenotificationchannel.upl")
public class NotificationChannelController extends AbstractNotificationRecordController<NotificationChannel> {

    private String searchName;

    private String searchDescription;

    private RecordStatus searchStatus;

    private NotificationType searchNotificationType;

    public NotificationChannelController() {
        super(NotificationChannel.class, "notification.notificationchannel.hint",
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.COPY_TO_ADD
                        | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.REPORTABLE);
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public RecordStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(RecordStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public NotificationType getSearchNotificationType() {
        return searchNotificationType;
    }

    public void setSearchNotificationType(NotificationType searchNotificationType) {
        this.searchNotificationType = searchNotificationType;
    }

    @Override
    protected List<NotificationChannel> find() throws UnifyException {
        NotificationChannelQuery query = new NotificationChannelQuery();
        if (QueryUtils.isValidStringCriteria(searchName)) {
            query.nameLike(searchName);
        }
        if (QueryUtils.isValidStringCriteria(searchDescription)) {
            query.descriptionLike(searchDescription);
        }

        if (searchNotificationType != null) {
            query.notificationType(searchNotificationType);
        }

        if (searchStatus != null) {
            query.status(searchStatus);
        }
        query.ignoreEmptyCriteria(true);
        return getNotificationModule().findNotificationChannels(query);
    }

    @Override
    protected NotificationChannel find(Long id) throws UnifyException {
        return getNotificationModule().findNotificationChannel(id);
    }

    @Override
    protected NotificationChannel prepareCreate() throws UnifyException {
        return new NotificationChannel();
    }

    @Override
    protected Object create(NotificationChannel notificationChannelData) throws UnifyException {
        return getNotificationModule().createNotificationChannel(notificationChannelData);
    }

    @Override
    protected int update(NotificationChannel notificationChannelData) throws UnifyException {
        return getNotificationModule().updateNotificationChannel(notificationChannelData);
    }

    @Override
    protected int delete(NotificationChannel notificationChannelData) throws UnifyException {
        return getNotificationModule().deleteNotificationChannel(notificationChannelData.getId());
    }

}
