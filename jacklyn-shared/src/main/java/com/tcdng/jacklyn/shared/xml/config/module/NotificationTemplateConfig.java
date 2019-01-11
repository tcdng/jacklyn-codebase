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
package com.tcdng.jacklyn.shared.xml.config.module;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.notification.MessageType;
import com.tcdng.jacklyn.shared.xml.adapter.MessageTypeXmlAdapter;
import com.tcdng.jacklyn.shared.xml.config.BaseConfig;

/**
 * Notification template configurations.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class NotificationTemplateConfig extends BaseConfig {

    private String subject;

    private String template;

    private MessageType messageType;

    private String actionLink;
    
    private boolean html;

    public NotificationTemplateConfig() {
        messageType = MessageType.INFORMATION;
    }
    
    public String getSubject() {
        return subject;
    }

    @XmlAttribute(required = true)
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplate() {
        return template;
    }

    @XmlAttribute(required = true)
    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean isHtml() {
        return html;
    }

    @XmlAttribute(required = true)
    public void setHtml(boolean html) {
        this.html = html;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @XmlJavaTypeAdapter(MessageTypeXmlAdapter.class)
    @XmlAttribute(name="message-type")
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getActionLink() {
        return actionLink;
    }

    @XmlAttribute(name="action-link")
    public void setActionLink(String actionLink) {
        this.actionLink = actionLink;
    }
}
