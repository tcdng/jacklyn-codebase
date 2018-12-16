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

import com.tcdng.jacklyn.notification.constants.NotificationModuleNameConstants;
import com.tcdng.jacklyn.notification.data.MessagingChannelDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.FileAttachment;

/**
 * System messaging channel.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(NotificationModuleNameConstants.SYSTEMMESSAGINGCHANNEL)
public class SystemMessagingChannel extends AbstractMessagingChannel {

    @Override
    public boolean sendMessage(MessagingChannelDef notificationChannelDef, String subject, String senderContact,
            List<String> recipientContactList, String messageBody, boolean isHtml,
            List<FileAttachment> fileAttachmentList) throws UnifyException {
        // TODO Auto-generated method stub
        return true;
    }

}
