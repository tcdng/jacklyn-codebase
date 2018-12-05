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

import java.util.Date;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseTimestampedEntity;
import com.tcdng.jacklyn.notification.constants.NotificationModuleNameConstants;
import com.tcdng.jacklyn.shared.notification.NotificationStatus;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Message entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = NotificationModuleNameConstants.NOTIFICATION_MODULE, title = "Message", reportable = true)
@Table("NOTIFICATION")
public class Notification extends BaseTimestampedEntity {

	@ForeignKey(NotificationTemplate.class)
	private Long notificationTemplateId;

	@ForeignKey(NotificationChannel.class)
	private Long notificationChannelId;

	@Column(length = 64)
	private String senderName;

	@Column(length = 64)
	private String senderContact;

	@Column
	private Integer attempts;

	@Column(type = ColumnType.TIMESTAMP)
	private Date dueDt;

	@Column(type = ColumnType.TIMESTAMP, nullable = true)
	private Date sentDt;

	@Column(name = "REC_ST")
	private NotificationStatus status;

	@Column
	private byte[] dictionary;

	@ListOnly(key = "notificationTemplateId", property = "moduleId")
	private Long moduleId;

	@ListOnly(key = "notificationTemplateId", property = "moduleName")
	private String moduleName;

	@ListOnly(key = "notificationTemplateId", property = "name")
	private String notificationTemplateName;

	@ListOnly(key = "notificationTemplateId", property = "description")
	private String notificationTemplateDesc;

	@ListOnly(key = "notificationTemplateId", property = "subject")
	private String subject;

	@ListOnly(key = "notificationTemplateId", property = "attachmentGenerator")
	private String attachmentGenerator;

	@ListOnly(key = "notificationChannelId", property = "name")
	private String notificationChannelName;

	@Override
	public String getDescription() {
		return notificationTemplateDesc;
	}

	public Long getNotificationTemplateId() {
		return notificationTemplateId;
	}

	public void setNotificationTemplateId(Long notificationTemplateId) {
		this.notificationTemplateId = notificationTemplateId;
	}

	public Long getNotificationChannelId() {
		return notificationChannelId;
	}

	public void setNotificationChannelId(Long notificationChannelId) {
		this.notificationChannelId = notificationChannelId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderContact() {
		return senderContact;
	}

	public void setSenderContact(String senderContact) {
		this.senderContact = senderContact;
	}

	public Integer getAttempts() {
		return attempts;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	public Date getDueDt() {
		return dueDt;
	}

	public void setDueDt(Date dueDt) {
		this.dueDt = dueDt;
	}

	public Date getSentDt() {
		return sentDt;
	}

	public void setSentDt(Date sentDt) {
		this.sentDt = sentDt;
	}

	public NotificationStatus getStatus() {
		return status;
	}

	public void setStatus(NotificationStatus status) {
		this.status = status;
	}

	public byte[] getDictionary() {
		return dictionary;
	}

	public void setDictionary(byte[] dictionary) {
		this.dictionary = dictionary;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getNotificationTemplateName() {
		return notificationTemplateName;
	}

	public void setNotificationTemplateName(String notificationTemplateName) {
		this.notificationTemplateName = notificationTemplateName;
	}

	public String getNotificationTemplateDesc() {
		return notificationTemplateDesc;
	}

	public void setNotificationTemplateDesc(String notificationTemplateDesc) {
		this.notificationTemplateDesc = notificationTemplateDesc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAttachmentGenerator() {
		return attachmentGenerator;
	}

	public void setAttachmentGenerator(String attachmentGenerator) {
		this.attachmentGenerator = attachmentGenerator;
	}

	public String getNotificationChannelName() {
		return notificationChannelName;
	}

	public void setNotificationChannelName(String notificationChannelName) {
		this.notificationChannelName = notificationChannelName;
	}
}
