/*
 * Copyright 2018-2019 The Code Department.
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

import java.util.List;

import com.tcdng.jacklyn.shared.notification.MessageType;
import com.tcdng.unify.core.util.StringUtils.StringToken;

/**
 * Notification template definition.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class NotificationTemplateDef {

    private Long notificationTemplateId;

    private String subject;

    private String actionLink;

    private List<StringToken> tokenList;

    private MessageType messageType;
    
    private boolean html;

    private long versionNo;

    public NotificationTemplateDef(Long notificationTemplateId, String subject, String actionLink,
            List<StringToken> tokenList, MessageType messageType, boolean html, long versionNo) {
        this.notificationTemplateId = notificationTemplateId;
        this.subject = subject;
        this.actionLink = actionLink;
        this.tokenList = tokenList;
        this.messageType = messageType;
        this.html = html;
        this.versionNo = versionNo;
    }

    public Long getNotificationTemplateId() {
        return notificationTemplateId;
    }

    public String getSubject() {
        return subject;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getActionLink() {
        return actionLink;
    }

    public List<StringToken> getTokenList() {
        return tokenList;
    }

    public boolean isHtml() {
        return html;
    }

    public long getVersionNo() {
        return versionNo;
    }
}
