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
package com.tcdng.jacklyn.notification.business;

import java.util.List;

import com.tcdng.jacklyn.notification.constants.NotificationModuleNameConstants;
import com.tcdng.jacklyn.notification.data.NotificationChannelDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.FileAttachment;
import com.tcdng.unify.core.notification.Email;
import com.tcdng.unify.core.notification.EmailRecipient.TYPE;
import com.tcdng.unify.core.notification.EmailServer;
import com.tcdng.unify.core.notification.EmailServerConfig;

/**
 * Email messaging channel.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(NotificationModuleNameConstants.EMAILMESSAGINGCHANNEL)
public class EmailMessagingChannel extends AbstractMessagingChannel {

    @Configurable
    private EmailServer emailServer;

    @Override
    public boolean sendMessage(NotificationChannelDef notificationChannelDef, String subject, String senderContact,
            List<String> recipientContactList, String messageBody, boolean isHtml,
            List<FileAttachment> fileAttachmentList) throws UnifyException {
        String configurationCode = notificationChannelDef.getNotificationChannelName();
        if (!notificationChannelDef.isServerConfigured() || !emailServer.isConfigured(configurationCode)) {
            EmailServerConfig emailServerConfig =
                    EmailServerConfig.newBuilder().hostAddress(notificationChannelDef.getHostAddress())
                            .hostPort(notificationChannelDef.getHostPort())
                            .useSecurityType(notificationChannelDef.getSecurityType())
                            .username(notificationChannelDef.getUsername())
                            .password(notificationChannelDef.getPassword()).build();
            emailServer.configure(configurationCode, emailServerConfig);
            notificationChannelDef.setServerConfigured();
        }

        Email email =
                Email.newBuilder().fromSender(senderContact).toRecipients(TYPE.TO, recipientContactList)
                        .withSubject(subject).withAttachments(fileAttachmentList).containingMessage(messageBody)
                        .asHTML(isHtml).build();
        emailServer.sendEmail(configurationCode, email);
        return email.isSent();
    }
}
