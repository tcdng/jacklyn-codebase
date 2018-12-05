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
import com.tcdng.jacklyn.notification.entities.NotificationTemplate;
import com.tcdng.jacklyn.notification.entities.NotificationTemplateQuery;
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
		extends AbstractNotificationRecordController<NotificationTemplate> {

	private Long searchModuleId;

	private String searchName;

	private String searchDescription;

	private RecordStatus searchStatus;

	public NotificationTemplateController() {
		super(NotificationTemplate.class, "notification.notificationtemplate.hint",
				ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
						| ManageRecordModifier.COPY_TO_ADD | ManageRecordModifier.CLIPBOARD
						| ManageRecordModifier.REPORTABLE);
	}

	public Long getSearchModuleId() {
		return searchModuleId;
	}

	public void setSearchModuleId(Long searchModuleId) {
		this.searchModuleId = searchModuleId;
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

	@Override
	protected List<NotificationTemplate> find() throws UnifyException {
		NotificationTemplateQuery query = new NotificationTemplateQuery();
		if (QueryUtils.isValidLongCriteria(searchModuleId)) {
			query.moduleId(searchModuleId);
		}
		if (QueryUtils.isValidStringCriteria(searchName)) {
			query.nameLike(searchName);
		}
		if (QueryUtils.isValidStringCriteria(searchDescription)) {
			query.descriptionLike(searchDescription);
		}
		if (getSearchStatus() != null) {
			query.status(getSearchStatus());
		}
		query.ignoreEmptyCriteria(true);
		return getNotificationModule().findNotificationTemplates(query);
	}

	@Override
	protected NotificationTemplate find(Long id) throws UnifyException {
		return getNotificationModule().findNotificationTemplate(id);
	}

	@Override
	protected NotificationTemplate prepareCreate() throws UnifyException {
		return new NotificationTemplate();
	}

	@Override
	protected Object create(NotificationTemplate notificationTemplateData) throws UnifyException {
		return getNotificationModule().createNotificationTemplate(notificationTemplateData);
	}

	@Override
	protected int update(NotificationTemplate notificationTemplateData) throws UnifyException {
		return getNotificationModule().updateNotificationTemplate(notificationTemplateData);
	}

	@Override
	protected int delete(NotificationTemplate notificationTemplateData) throws UnifyException {
		return getNotificationModule().deleteNotificationTemplate(notificationTemplateData.getId());
	}

}
