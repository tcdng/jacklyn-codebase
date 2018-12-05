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

import java.util.Collection;

import com.tcdng.jacklyn.common.entities.BaseTimestampedEntityQuery;

/**
 * Base query class for file transfer box.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractFileBoxDataQuery<T extends AbstractFileTransferBox>
		extends BaseTimestampedEntityQuery<T> {

	public AbstractFileBoxDataQuery(Class<T> entityClass) {
		super(entityClass);
	}

	public AbstractFileBoxDataQuery<T> fileTransferConfigId(Long fileTransferConfigId) {
		return (AbstractFileBoxDataQuery<T>) equals("fileTransferConfigId", fileTransferConfigId);
	}

	public AbstractFileBoxDataQuery<T> fileTransferConfigName(String fileTransferConfigName) {
		return (AbstractFileBoxDataQuery<T>) equals("fileTransferConfigName",
				fileTransferConfigName);
	}

	public AbstractFileBoxDataQuery<T> filenameLikeBeginWith(String filename) {
		return (AbstractFileBoxDataQuery<T>) likeBegin("filename", filename);
	}

	public AbstractFileBoxDataQuery<T> filenameLikeEndWith(String filename) {
		return (AbstractFileBoxDataQuery<T>) likeEnd("filename", filename);
	}

	public AbstractFileBoxDataQuery<T> setFilenameIn(Collection<String> filenames) {
		return (AbstractFileBoxDataQuery<T>) amongst("filename", filenames);
	}
}
