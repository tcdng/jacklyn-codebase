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
package com.tcdng.jacklyn.notification.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.notification.entities.NotificationChannel;
import com.tcdng.jacklyn.notification.entities.NotificationChannelQuery;
import com.tcdng.jacklyn.notification.web.beans.NotificationChannelPageBean;
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
public class NotificationChannelController
        extends AbstractNotificationFormController<NotificationChannelPageBean, NotificationChannel> {

    public NotificationChannelController() {
        super(NotificationChannelPageBean.class, NotificationChannel.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.COPY_TO_ADD
                        | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<NotificationChannel> find() throws UnifyException {
        NotificationChannelPageBean pageBean = getPageBean();
        NotificationChannelQuery query = new NotificationChannelQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }

        if (pageBean.getSearchNotificationType() != null) {
            query.notificationType(pageBean.getSearchNotificationType());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.ignoreEmptyCriteria(true);
        return getNotificationService().findNotificationChannels(query);
    }

    @Override
    protected NotificationChannel find(Long id) throws UnifyException {
        return getNotificationService().findNotificationChannel(id);
    }

    @Override
    protected Object create(NotificationChannel notificationChannelData) throws UnifyException {
        return getNotificationService().createNotificationChannel(notificationChannelData);
    }

    @Override
    protected int update(NotificationChannel notificationChannelData) throws UnifyException {
        return getNotificationService().updateNotificationChannel(notificationChannelData);
    }

    @Override
    protected int delete(NotificationChannel notificationChannelData) throws UnifyException {
        return getNotificationService().deleteNotificationChannel(notificationChannelData.getId());
    }

}
