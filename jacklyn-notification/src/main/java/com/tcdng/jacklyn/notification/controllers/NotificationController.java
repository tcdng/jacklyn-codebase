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
package com.tcdng.jacklyn.notification.controllers;

import java.util.Date;
import java.util.List;

import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.jacklyn.notification.constants.NotificationModuleAuditConstants;
import com.tcdng.jacklyn.notification.entities.Notification;
import com.tcdng.jacklyn.notification.entities.NotificationQuery;
import com.tcdng.jacklyn.shared.notification.NotificationStatus;
import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.CalendarUtils;
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
public class NotificationController extends AbstractNotificationCrudController<Notification> {

    private Date searchCreateDt;

    private Long searchModuleId;

    private Long searchNotificationTemplateId;

    private NotificationType searchNotificationType;

    private NotificationStatus searchStatus;

    public NotificationController() {
        super(Notification.class, "$m{notification.notification.hint}",
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

    public Date getSearchCreateDt() {
        return searchCreateDt;
    }

    public void setSearchCreateDt(Date searchCreateDt) {
        this.searchCreateDt = searchCreateDt;
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
    }

    public Long getSearchNotificationTemplateId() {
        return searchNotificationTemplateId;
    }

    public void setSearchNotificationTemplateId(Long searchNotificationTemplateId) {
        this.searchNotificationTemplateId = searchNotificationTemplateId;
    }

    public NotificationType getSearchNotificationType() {
        return searchNotificationType;
    }

    public void setSearchNotificationType(NotificationType searchNotificationType) {
        this.searchNotificationType = searchNotificationType;
    }

    public NotificationStatus getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(NotificationStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        if (searchCreateDt == null) {
            searchCreateDt = CalendarUtils.getCurrentMidnightDate();
        }
    }

    @Override
    protected List<Notification> find() throws UnifyException {
        NotificationQuery query = new NotificationQuery();
        query.createdOn(searchCreateDt);
        if (QueryUtils.isValidLongCriteria(searchModuleId)) {
            query.moduleId(searchModuleId);
        }
        if (QueryUtils.isValidLongCriteria(searchNotificationTemplateId)) {
            query.notificationTemplateId(searchNotificationTemplateId);
        }
        if (getSearchNotificationType() != null) {
            query.notificationType(getSearchNotificationType());
        }
        if (getSearchStatus() != null) {
            query.status(getSearchStatus());
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
