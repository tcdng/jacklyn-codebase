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
package com.tcdng.jacklyn.notification.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.notification.constants.NotificationModuleNameConstants;
import com.tcdng.jacklyn.shared.notification.MessageType;
import com.tcdng.jacklyn.system.entities.Module;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Message template entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = NotificationModuleNameConstants.NOTIFICATION_MODULE, title = "Notification Template",
        reportable = true, auditable = true)
@Table(name = "JKNOTIFICATIONTEMPLATE", uniqueConstraints = { @UniqueConstraint({ "moduleId", "name" }),
        @UniqueConstraint({ "moduleId", "description" }) })
public class NotificationTemplate extends BaseVersionedStatusEntity {

    @ForeignKey(Module.class)
    private Long moduleId;

    @ForeignKey
    private MessageType messageType;
    
    @Column(name = "TEMPLATE_NM", length = 96)
    private String name;

    @Column(name = "TEMPLATE_DESC", length = 64)
    private String description;

    @Column(length = 64)
    private String subject;

    @Column(length = 2048)
    private String template;

    @Column(length = 64, nullable = true)
    private String actionLink;

    @Column(name = "HTML_FG")
    private Boolean htmlFlag;

    @Column(length = 32, nullable = true)
    private String attachmentGenerator;

    @Column(name="TARGET_LINK", length = 64, nullable = true)
    private String link;

    @ListOnly(name = "MODULE_NM", key = "moduleId", property = "name")
    private String moduleName;

    @ListOnly(name = "MODULE_DESC", key = "moduleId", property = "description")
    private String moduleDescription;

    @ListOnly(key = "messageType", property = "description")
    private String messageTypeDesc;
    
    @Override
    public String getDescription() {
        return this.description;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public String getMessageTypeDesc() {
        return messageTypeDesc;
    }

    public void setMessageTypeDesc(String messageTypeDesc) {
        this.messageTypeDesc = messageTypeDesc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHtmlFlag() {
        return htmlFlag;
    }

    public void setHtmlFlag(Boolean htmlFlag) {
        this.htmlFlag = htmlFlag;
    }

    public String getAttachmentGenerator() {
        return attachmentGenerator;
    }

    public void setAttachmentGenerator(String attachmentGenerator) {
        this.attachmentGenerator = attachmentGenerator;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getActionLink() {
        return actionLink;
    }

    public void setActionLink(String actionLink) {
        this.actionLink = actionLink;
    }
}
