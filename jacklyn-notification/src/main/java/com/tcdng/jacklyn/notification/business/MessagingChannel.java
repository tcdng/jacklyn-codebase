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
package com.tcdng.jacklyn.notification.business;

import java.util.List;

import com.tcdng.jacklyn.notification.data.NotificationChannelDef;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.FileAttachment;

/**
 * Messaging channel used for communicating notifications.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface MessagingChannel extends UnifyComponent {

    /**
     * Sends a message.
     * 
     * @param messagingChannelDef
     *            notification channel definition
     * @param subject
     *            the message subject
     * @param senderContact
     *            the sender contact
     * @param recipientContactList
     *            the recipients contact list
     * @param messageBody
     *            the message body
     * @param link
     *            optional reference link
     * @param reference
     *            optional reference
     * @param isHtml
     *            indicates if message body is HTML
     * @param fileAttachmentList
     *            attachment list
     * @return a true value if successfully sent
     * @throws UnifyException
     *             if an error occurs
     */
    boolean sendMessage(NotificationChannelDef messagingChannelDef, String subject, String senderContact,
            List<String> recipientContactList, String messageBody, String link, String reference, boolean isHtml,
            List<FileAttachment> fileAttachmentList) throws UnifyException;
}
