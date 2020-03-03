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

package com.tcdng.jacklyn.notification.data;

import com.tcdng.jacklyn.shared.notification.NotificationType;
import com.tcdng.unify.core.constant.NetworkSecurityType;

/**
 * Notification channel definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class NotificationChannelDef {

    private Long notificationChannelId;

    private String notificationChannelName;

    private NotificationType notificationType;

    private String hostAddress;

    private Integer hostPort;

    private NetworkSecurityType securityType;

    private String username;

    private String password;

    private long versionNo;

    private boolean serverConfigured;

    public NotificationChannelDef(Long notificationChannelId, String notificationChannelName,
            NotificationType notificationType, String hostAddress, Integer hostPort, NetworkSecurityType securityType,
            String username, String password, long versionNo) {
        this.notificationChannelId = notificationChannelId;
        this.notificationChannelName = notificationChannelName;
        this.notificationType = notificationType;
        this.hostAddress = hostAddress;
        this.hostPort = hostPort;
        this.securityType = securityType;
        this.username = username;
        this.password = password;
        this.versionNo = versionNo;
        this.serverConfigured = false;
    }

    public Long getNotificationChannelId() {
        return notificationChannelId;
    }

    public String getNotificationChannelName() {
        return notificationChannelName;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public Integer getHostPort() {
        return hostPort;
    }

    public NetworkSecurityType getSecurityType() {
        return securityType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getVersionNo() {
        return versionNo;
    }

    public boolean isServerConfigured() {
        return serverConfigured;
    }

    public void setServerConfigured() {
        serverConfigured = true;
    }
}
