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
import com.tcdng.jacklyn.notification.constants.NotificationModuleAuditConstants;
import com.tcdng.jacklyn.notification.entities.Notification;
import com.tcdng.jacklyn.notification.entities.NotificationQuery;
import com.tcdng.jacklyn.notification.web.beans.NotificationPageBean;
import com.tcdng.jacklyn.shared.notification.NotificationStatus;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for managing notifications.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/notification/notification")
@UplBinding("web/notification/upl/managenotification.upl")
public class NotificationController extends AbstractNotificationFormController<NotificationPageBean, Notification> {

    public NotificationController() {
        super(NotificationPageBean.class, Notification.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    @Action
    public String markAsUnsent() throws UnifyException {
        List<Long> messageIds = getSelectedIds();
        if (!messageIds.isEmpty()) {
            getNotificationService().setNotificationStatus(messageIds, NotificationStatus.NOT_SENT);
            logUserEvent(NotificationModuleAuditConstants.MARK_OUTWARD_UNSENT, getSelectedDescription());
            hintUser("$m{hint.message.marked.unsent}");
        }
        return findRecords();
    }

    @Action
    public String markAsSent() throws UnifyException {
        List<Long> messageIds = getSelectedIds();
        if (!messageIds.isEmpty()) {
            getNotificationService().setNotificationStatus(messageIds, NotificationStatus.SENT);
            logUserEvent(NotificationModuleAuditConstants.MARK_OUTWARD_SENT, getSelectedDescription());
            hintUser("$m{hint.message.marked.sent}");
        }
        return findRecords();
    }

    @Action
    public String markAsAborted() throws UnifyException {
        List<Long> messageIds = getSelectedIds();
        if (!messageIds.isEmpty()) {
            getNotificationService().setNotificationStatus(messageIds, NotificationStatus.ABORTED);
            logUserEvent(NotificationModuleAuditConstants.MARK_OUTWARD_ABORT, getSelectedDescription());
            hintUser("$m{hint.message.marked.abort}");
        }
        return findRecords();
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        NotificationPageBean pageBean = getPageBean();
        if (pageBean.getSearchCreateDt() == null) {
            pageBean.setSearchCreateDt(getNotificationService().getToday());
        }
    }

    @Override
    protected List<Notification> find() throws UnifyException {
        NotificationPageBean pageBean = getPageBean();
        NotificationQuery query = new NotificationQuery();
        query.createdOn(pageBean.getSearchCreateDt());
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchModuleId())) {
            query.moduleId(pageBean.getSearchModuleId());
        }
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchNotificationTemplateId())) {
            query.notificationTemplateId(pageBean.getSearchNotificationTemplateId());
        }
        if (pageBean.getSearchNotificationType() != null) {
            query.notificationType(pageBean.getSearchNotificationType());
        }
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        return getNotificationService().findNotifications(query);
    }

    @Override
    protected Notification find(Long id) throws UnifyException {
        return getNotificationService().findNotification(id);
    }

    @Override
    protected Notification prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(Notification record) throws UnifyException {
        return null;
    }

    @Override
    protected int update(Notification record) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(Notification record) throws UnifyException {
        return 0;
    }

}
