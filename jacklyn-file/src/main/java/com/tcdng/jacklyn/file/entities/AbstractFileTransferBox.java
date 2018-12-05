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
package com.tcdng.jacklyn.file.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.annotation.Format;
import com.tcdng.jacklyn.common.entities.BaseTimestampedEntity;
import com.tcdng.jacklyn.common.entities.ColumnPositionConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Base file transfer box entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractFileTransferBox extends BaseTimestampedEntity {

	@ForeignKey(FileTransferConfig.class)
	private Long fileTransferConfigId;

	@Column(length = 96)
	private String filename;

	@Format(formatter = "!filesizeformat", halign = HAlignType.RIGHT)
	@Column
	private long fileLength;

	@Format(formatter = "!datetimeformat")
	@Column(type = ColumnType.TIMESTAMP, position = ColumnPositionConstants.BASE_COLUMN_POSITION)
	private Date createDt;

	@ListOnly(key = "fileTransferConfigId", property = "name")
	private String fileTransferConfigName;

	@Format(halign = HAlignType.RIGHT)
	@ListOnly(key = "fileTransferConfigId", property = "maxTransferAttempts")
	private Integer maxTransferAttempts;

	@Override
	public String getDescription() {
		return StringUtils.concatenate(filename, "(", fileLength, " bytes)");
	}

	public Long getFileTransferConfigId() {
		return fileTransferConfigId;
	}

	public void setFileTransferConfigId(Long fileTransferConfigId) {
		this.fileTransferConfigId = fileTransferConfigId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getFileTransferConfigName() {
		return fileTransferConfigName;
	}

	public void setFileTransferConfigName(String fileTransferConfigName) {
		this.fileTransferConfigName = fileTransferConfigName;
	}

	public Integer getMaxTransferAttempts() {
		return maxTransferAttempts;
	}

	public void setMaxTransferAttempts(Integer maxTransferAttempts) {
		this.maxTransferAttempts = maxTransferAttempts;
	}
}
