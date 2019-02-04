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
package com.tcdng.jacklyn.workflow.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.jacklyn.shared.notification.MessageType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Workflow message entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "WFMESSAGE", uniqueConstraints = { @UniqueConstraint({ "wfCategoryId", "name" }),
        @UniqueConstraint({ "wfCategoryId", "description" }) })
public class WfMessage extends BaseEntity {

    @ForeignKey(WfCategory.class)
    private Long wfCategoryId;

    @ForeignKey
    private MessageType messageType;
    
    @Column(name = "MESSAGE_NM", length = 64)
    private String name;

    @Column(name = "MESSAGE_DESC", length = 64)
    private String description;

    @Column(name = "DOCUMENT_NM", length = 32)
    private String wfDocName;

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

    @ListOnly(key = "wfCategoryId", property = "name")
    private String wfCategoryName;

    @ListOnly(key = "wfCategoryId", property = "description")
    private String wfCategoryDesc;

    @ListOnly(key = "wfCategoryId", property = "version")
    private String wfCategoryVersion;

    @ListOnly(key = "wfCategoryId", property = "status")
    private RecordStatus wfCategoryStatus;

    @ListOnly(key = "wfCategoryId", property = "statusDesc")
    private String wfCategoryStatusDesc;

    @ListOnly(key = "wfCategoryId", property = "updateDt")
    private Date wfCategoryUpdateDt;

    @ListOnly(key = "messageType", property = "description")
    private String messageTypeDesc;

    @Override
    public String getDescription() {
        return this.description;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWfDocName() {
        return wfDocName;
    }

    public void setWfDocName(String wfDocName) {
        this.wfDocName = wfDocName;
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

    public Long getWfCategoryId() {
        return wfCategoryId;
    }

    public void setWfCategoryId(Long wfCategoryId) {
        this.wfCategoryId = wfCategoryId;
    }

    public String getWfCategoryName() {
        return wfCategoryName;
    }

    public void setWfCategoryName(String wfCategoryName) {
        this.wfCategoryName = wfCategoryName;
    }

    public RecordStatus getWfCategoryStatus() {
        return wfCategoryStatus;
    }

    public void setWfCategoryStatus(RecordStatus wfCategoryStatus) {
        this.wfCategoryStatus = wfCategoryStatus;
    }

    public String getWfCategoryVersion() {
        return wfCategoryVersion;
    }

    public void setWfCategoryVersion(String wfCategoryVersion) {
        this.wfCategoryVersion = wfCategoryVersion;
    }

    public String getWfCategoryDesc() {
        return wfCategoryDesc;
    }

    public void setWfCategoryDesc(String wfCategoryDesc) {
        this.wfCategoryDesc = wfCategoryDesc;
    }

    public String getWfCategoryStatusDesc() {
        return wfCategoryStatusDesc;
    }

    public void setWfCategoryStatusDesc(String wfCategoryStatusDesc) {
        this.wfCategoryStatusDesc = wfCategoryStatusDesc;
    }

    public Date getWfCategoryUpdateDt() {
        return wfCategoryUpdateDt;
    }

    public void setWfCategoryUpdateDt(Date wfCategoryUpdateDt) {
        this.wfCategoryUpdateDt = wfCategoryUpdateDt;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessageTypeDesc() {
        return messageTypeDesc;
    }

    public void setMessageTypeDesc(String messageTypeDesc) {
        this.messageTypeDesc = messageTypeDesc;
    }

    public String getActionLink() {
        return actionLink;
    }

    public void setActionLink(String actionLink) {
        this.actionLink = actionLink;
    }
}
