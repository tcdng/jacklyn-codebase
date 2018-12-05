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
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.notification.constants.NotificationModuleNameConstants;
import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.jacklyn.system.entities.Authentication;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.constant.NetworkSecurityType;

/**
 * Message channel entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = NotificationModuleNameConstants.NOTIFICATION_MODULE, title = "Message Channel",
		reportable = true, auditable = true)
@Table(name = "NOTIFICATIONCHANNEL",
		uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class NotificationChannel extends BaseVersionedStatusEntity {

	@ForeignKey
	private NotificationType notificationType;

	@ForeignKey(nullable = true)
	private NetworkSecurityType securityType;

	@ForeignKey(type = Authentication.class, nullable = true)
	private Long authenticationId;
	@Column(name = "NOTIFICATIONCHANNEL_NM", length = 48)
	private String name;

	@Column(name = "NOTIFICATIONCHANNEL_DESC", length = 64)
	private String description;

	@Column(nullable = true)
	private String hostAddress;

	@Column(nullable = true)
	private Integer hostPort;

	@ListOnly(key = "notificationType", property = "description")
	private String notificationTypeDesc;

	@Override
	public String getDescription() {
		return description;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public NetworkSecurityType getSecurityType() {
		return securityType;
	}

	public void setSecurityType(NetworkSecurityType securityType) {
		this.securityType = securityType;
	}

	public Long getAuthenticationId() {
		return authenticationId;
	}

	public void setAuthenticationId(Long authenticationId) {
		this.authenticationId = authenticationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	public Integer getHostPort() {
		return hostPort;
	}

	public void setHostPort(Integer hostPort) {
		this.hostPort = hostPort;
	}

	public String getNotificationTypeDesc() {
		return notificationTypeDesc;
	}

	public void setNotificationTypeDesc(String notificationTypeDesc) {
		this.notificationTypeDesc = notificationTypeDesc;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
