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
package com.tcdng.jacklyn.common.entities;

import java.util.Date;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Entity;

/**
 * Entity policy class for entity with status.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("statusentity-policy")
public class BaseStatusEntityPolicy extends BaseEntityPolicy {

	public BaseStatusEntityPolicy() {

	}

	public BaseStatusEntityPolicy(boolean setNow) {
		super(setNow);
	}

	@Override
	public Object preCreate(Entity record, Date now) throws UnifyException {
		if (((BaseStatusEntity) record).getStatus() == null) {
			((BaseStatusEntity) record).setStatus(RecordStatus.ACTIVE);
		}

		return super.preCreate(record, now);
	}
}
