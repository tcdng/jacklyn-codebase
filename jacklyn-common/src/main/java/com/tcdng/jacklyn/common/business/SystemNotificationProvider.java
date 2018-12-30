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

package com.tcdng.jacklyn.common.business;

import java.util.List;

import com.tcdng.jacklyn.common.data.SystemNotification;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * System notification provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface SystemNotificationProvider extends UnifyComponent {

    /**
     * Finds unread system notifications for a particular user.
     * 
     * @param userId
     *            the user ID
     * @return a list of unread notifications
     * @throws UnifyException
     *             if an error occurs
     */
    List<? extends SystemNotification> findUserSystemNotifications(String userId) throws UnifyException;

    /**
     * Counts unread system notifications for a particular user.
     * 
     * @param userId
     *            the user ID
     * @param userId
     * @return the number of unread items
     * @throws UnifyException
     *             if an error occurs
     */
    int countUserSystemNotifications(String userId) throws UnifyException;

    /**
     * Dismisses all user notifications.
     * 
     * @param userId
     *            the user ID
     * @return the number of items dismissed
     * @throws UnifyException
     *             if an error occurs
     */
    int dismissUserSystemNotifications(String userId) throws UnifyException;

    /**
     * Dismisses a user system notification.
     * 
     * @param systemNotification
     *            the notification to dismiss
     * @return the number of items dismissed
     * @throws UnifyException
     *             if an error occurs
     */
    int dismissUserSystemNotification(SystemNotification systemNotification) throws UnifyException;
}
