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

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.constant.FileAttachmentType;

/**
 * Workflow document attachment definition entity.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Table(name = "WFDOCATTACHMENT", uniqueConstraints = { @UniqueConstraint({ "wfDocId", "name" }),
        @UniqueConstraint({ "wfDocId", "description" }) })
public class WfDocAttachment extends BaseEntity {

    @ForeignKey(WfDoc.class)
    private Long wfDocId;

    @ForeignKey
    private FileAttachmentType attachmentType;

    @Column(name = "ATTACHMENT_NM", length = 32)
    private String name;

    @Column(name = "ATTACHMENT_DESC", length = 64)
    private String description;

    @Column(name = "ATTACHMENT_LABEL", length = 64, nullable = true)
    private String label;

    @ListOnly(key = "wfDocId", property = "name")
    private String wfDocName;

    @ListOnly(key = "wfDocId", property = "description")
    private String wfDocDesc;

    @ListOnly(key = "attachmentType", property = "description")
    private String typeDesc;

    @Override
    public String getDescription() {
        return this.description;
    }

    public Long getWfDocId() {
        return wfDocId;
    }

    public void setWfDocId(Long wfDocId) {
        this.wfDocId = wfDocId;
    }

    public FileAttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(FileAttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWfDocName() {
        return wfDocName;
    }

    public void setWfDocName(String wfDocName) {
        this.wfDocName = wfDocName;
    }

    public String getWfDocDesc() {
        return wfDocDesc;
    }

    public void setWfDocDesc(String wfDocDesc) {
        this.wfDocDesc = wfDocDesc;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
