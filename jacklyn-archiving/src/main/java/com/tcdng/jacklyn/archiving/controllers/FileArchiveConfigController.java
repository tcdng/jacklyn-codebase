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
package com.tcdng.jacklyn.archiving.controllers;

import java.util.List;

import com.tcdng.jacklyn.archiving.entities.FileArchiveConfig;
import com.tcdng.jacklyn.archiving.entities.FileArchiveConfigQuery;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Controller for managing file archive configuration record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/archiving/filearchiveconfig")
@UplBinding("web/archiving/upl/managefilearchiveconfig.upl")
public class FileArchiveConfigController
		extends AbstractArchivingRecordController<FileArchiveConfig> {

	private String searchDescription;

	private RecordStatus searchStatus;

	public FileArchiveConfigController() {
		super(FileArchiveConfig.class, "archiving.filearchiveconfig.hint",
				ManageRecordModifier.SECURE | ManageRecordModifier.CRUD
						| ManageRecordModifier.CLIPBOARD | ManageRecordModifier.COPY_TO_ADD
						| ManageRecordModifier.REPORTABLE);
	}

	public String getSearchDescription() {
		return searchDescription;
	}

	public void setSearchDescription(String searchDescription) {
		this.searchDescription = searchDescription;
	}

	public RecordStatus getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(RecordStatus searchStatus) {
		this.searchStatus = searchStatus;
	}

	@Override
	protected List<FileArchiveConfig> find() throws UnifyException {
		FileArchiveConfigQuery query = new FileArchiveConfigQuery();
		if (QueryUtils.isValidStringCriteria(searchDescription)) {
			query.descriptionLike(searchDescription);
		}

		if (this.getSearchStatus() != null) {
			query.status(this.getSearchStatus());
		}
		query.ignoreEmptyCriteria(true);
		return this.getArchivingModule().findFileArchiveConfigs(query);
	}

	@Override
	protected FileArchiveConfig find(Long id) throws UnifyException {
		return this.getArchivingModule().findFileArchiveConfig(id);
	}

	@Override
	protected FileArchiveConfig prepareCreate() throws UnifyException {
		return new FileArchiveConfig();
	}

	@Override
	protected Object create(FileArchiveConfig fileArchiveConfigData) throws UnifyException {
		return this.getArchivingModule().createFileArchiveConfig(fileArchiveConfigData);
	}

	@Override
	protected int update(FileArchiveConfig fileArchiveConfigData) throws UnifyException {
		return this.getArchivingModule().updateFileArchiveConfig(fileArchiveConfigData);
	}

	@Override
	protected int delete(FileArchiveConfig fileArchiveConfigData) throws UnifyException {
		return this.getArchivingModule().deleteFileArchiveConfig(fileArchiveConfigData.getId());
	}
}
