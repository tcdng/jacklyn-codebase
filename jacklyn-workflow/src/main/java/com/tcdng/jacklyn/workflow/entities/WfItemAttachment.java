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
package com.tcdng.jacklyn.workflow.entities;

import com.tcdng.jacklyn.common.entities.BaseTimestampedEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Represents a workflow item attachment.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "WFITEMATTACHMENT", uniqueConstraints = { @UniqueConstraint({ "wfItemId", "name" }) })
public class WfItemAttachment extends BaseTimestampedEntity {

    @ForeignKey(WfItem.class)
    private Long wfItemId;

    @Column(name = "ATTACHMENTDEF_NM", length = 32)
    private String name;

    @Column(name = "FILE_NM", length = 64)
    private String fileName;

    @Column(name = "ATTACHMENT_DATA")
    private byte[] data;

    @Override
    public String getDescription() {
        return this.name;
    }

    public Long getWfItemId() {
        return wfItemId;
    }

    public void setWfItemId(Long wfItemId) {
        this.wfItemId = wfItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
