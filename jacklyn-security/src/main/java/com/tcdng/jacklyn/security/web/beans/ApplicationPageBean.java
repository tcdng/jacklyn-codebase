/*
 * Copyright 2018-2020 The Code Department
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

package com.tcdng.jacklyn.security.web.beans;

import java.util.List;

import com.tcdng.jacklyn.common.business.SystemNotificationProvider;
import com.tcdng.jacklyn.common.data.SystemNotification;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.resource.ImageGenerator;

/**
 * Application page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ApplicationPageBean extends AbstractApplicationForwarderPageBean {

    private List<? extends SystemNotification> userNotifications;

    private String notificationResolutionPath;

    private SystemNotificationProvider systemNotificationProvider;

    private ImageGenerator userPhotoGenerator;

    private String userLoginId;
    
    public ApplicationPageBean() {
        super(null);
    }

    public List<? extends SystemNotification> getUserNotifications() {
        return userNotifications;
    }

    public void setUserNotifications(List<? extends SystemNotification> userNotifications) {
        this.userNotifications = userNotifications;
    }

    public String getNotificationResolutionPath() {
        return notificationResolutionPath;
    }

    public void setNotificationResolutionPath(String notificationResolutionPath) {
        this.notificationResolutionPath = notificationResolutionPath;
    }

    public SystemNotificationProvider getSystemNotificationProvider() {
        return systemNotificationProvider;
    }

    public void setSystemNotificationProvider(SystemNotificationProvider systemNotificationProvider) {
        this.systemNotificationProvider = systemNotificationProvider;
    }

    public ImageGenerator getUserPhotoGenerator() {
        return userPhotoGenerator;
    }

    public void setUserPhotoGenerator(ImageGenerator userPhotoGenerator) {
        this.userPhotoGenerator = userPhotoGenerator;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }
    public int getAlertCount() throws UnifyException {
        return systemNotificationProvider.countUserSystemNotifications(userLoginId);
    }

}
