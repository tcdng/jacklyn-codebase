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
package com.tcdng.jacklyn.file.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.annotation.Format;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.ColumnPositionConstants;
import com.tcdng.jacklyn.file.constants.FileModuleNameConstants;
import com.tcdng.jacklyn.shared.file.FileInboxReadStatus;
import com.tcdng.jacklyn.shared.file.FileInboxStatus;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.constant.HAlignType;

/**
 * Entity for storing file inbox item information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = FileModuleNameConstants.FILE_MODULE, title = "File Inbox", reportable = true, auditable = true)
@Table("JKFILEINBOX")
public class FileInbox extends AbstractFileTransferBox {

    @Format(halign = HAlignType.RIGHT)
    @Column
    private int downloadAttempts;

    @Column(type = ColumnType.TIMESTAMP_UTC, nullable = true)
    private Date downloadedOn;

    @Format(description = "$m{file.fileinbox.readstatus}", halign = HAlignType.CENTER)
    @Column
    private FileInboxReadStatus readStatus;

    @Column(name = "REC_ST", position = ColumnPositionConstants.BASE_COLUMN_POSITION)
    private FileInboxStatus status;

    public int getDownloadAttempts() {
        return downloadAttempts;
    }

    public void setDownloadAttempts(int downloadAttempts) {
        this.downloadAttempts = downloadAttempts;
    }

    public Date getDownloadedOn() {
        return downloadedOn;
    }

    public void setDownloadedOn(Date downloadedOn) {
        this.downloadedOn = downloadedOn;
    }

    public FileInboxStatus getStatus() {
        return status;
    }

    public void setStatus(FileInboxStatus status) {
        this.status = status;
    }

    public void setReadStatus(FileInboxReadStatus readStatus) {
        this.readStatus = readStatus;
    }

    public FileInboxReadStatus getReadStatus() {
        return readStatus;
    }
}
