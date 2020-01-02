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
import com.tcdng.jacklyn.notification.entities.NotificationTemplate;
import com.tcdng.jacklyn.notification.entities.NotificationTemplateQuery;
import com.tcdng.jacklyn.notification.web.beans.NotificationTemplatePageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing message templates.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/notification/notificationtemplate")
@UplBinding("web/notification/upl/managenotificationtemplate.upl")
public class NotificationTemplateController
        extends AbstractNotificationFormController<NotificationTemplatePageBean, NotificationTemplate> {

    public NotificationTemplateController() {
        super(NotificationTemplatePageBean.class, NotificationTemplate.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.CRUD | ManageRecordModifier.COPY_TO_ADD
                        | ManageRecordModifier.CLIPBOARD | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<NotificationTemplate> find() throws UnifyException {
        NotificationTemplatePageBean pageBean = getPageBean();
        NotificationTemplateQuery query = new NotificationTemplateQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchModuleId())) {
            query.moduleId(pageBean.getSearchModuleId());
        }
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchName())) {
            query.nameLike(pageBean.getSearchName());
        }
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchDescription())) {
            query.descriptionLike(pageBean.getSearchDescription());
        }
        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }
        query.ignoreEmptyCriteria(true);
        return getNotificationService().findNotificationTemplates(query);
    }

    @Override
    protected NotificationTemplate find(Long id) throws UnifyException {
        return getNotificationService().findNotificationTemplate(id);
    }

    @Override
    protected NotificationTemplate prepareCreate() throws UnifyException {
        return new NotificationTemplate();
    }

    @Override
    protected Object create(NotificationTemplate notificationTemplateData) throws UnifyException {
        return getNotificationService().createNotificationTemplate(notificationTemplateData);
    }

    @Override
    protected int update(NotificationTemplate notificationTemplateData) throws UnifyException {
        return getNotificationService().updateNotificationTemplate(notificationTemplateData);
    }

    @Override
    protected int delete(NotificationTemplate notificationTemplateData) throws UnifyException {
        return getNotificationService().deleteNotificationTemplate(notificationTemplateData.getId());
    }

}
