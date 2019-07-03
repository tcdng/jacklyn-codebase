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
package com.tcdng.jacklyn.file.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.annotation.Format;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.ColumnPositionConstants;
import com.tcdng.jacklyn.file.constants.FileModuleNameConstants;
import com.tcdng.jacklyn.shared.file.FileOutboxStatus;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.constant.HAlignType;

/**
 * Entity for storing file outbox item information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = FileModuleNameConstants.FILE_MODULE, title = "File Outbox", reportable = true, auditable = true)
@Table("JKFILEOUTBOX")
public class FileOutbox extends AbstractFileTransferBox {

    @Format(halign = HAlignType.RIGHT)
    @Column
    private int uploadAttempts;

    @Column(type = ColumnType.TIMESTAMP_UTC, nullable = true)
    private Date uploadedOn;

    @Column(name = "REC_ST", position = ColumnPositionConstants.BASE_COLUMN_POSITION)
    private FileOutboxStatus status;

    public int getUploadAttempts() {
        return uploadAttempts;
    }

    public void setUploadAttempts(int uploadAttempts) {
        this.uploadAttempts = uploadAttempts;
    }

    public Date getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(Date uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public FileOutboxStatus getStatus() {
        return status;
    }

    public void setStatus(FileOutboxStatus status) {
        this.status = status;
    }
}
