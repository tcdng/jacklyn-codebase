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
package com.tcdng.jacklyn.system.controllers;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordController;
import com.tcdng.jacklyn.system.business.SystemModule;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.Entity;

/**
 * Abstract base system module record management page controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractSystemRecordController<T extends Entity>
		extends ManageRecordController<T, Long> {

	@Configurable(SystemModuleNameConstants.SYSTEMBUSINESSMODULE)
	private SystemModule systemModule;

	private RecordStatus searchStatus;

	public AbstractSystemRecordController(Class<T> entityClass, String hintKey, int modifier) {
		super(entityClass, hintKey, modifier);
	}

	public RecordStatus getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(RecordStatus searchStatus) {
		this.searchStatus = searchStatus;
	}

	protected SystemModule getSystemModule() throws UnifyException {
		return systemModule;
	}
}
