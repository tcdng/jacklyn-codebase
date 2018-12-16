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

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.entities.BaseEntityQuery;

/**
 * Query class for workflow document definitions.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfDocQuery extends BaseEntityQuery<WfDoc> {

	public WfDocQuery() {
		super(WfDoc.class);
	}

	public WfDocQuery wfCategoryId(Long wfCategoryId) {
		return (WfDocQuery) equals("wfCategoryId", wfCategoryId);
	}

	public WfDocQuery wfCategoryName(String wfCategoryName) {
		return (WfDocQuery) equals("wfCategoryName", wfCategoryName);
	}

	public WfDocQuery wfCategoryStatus(RecordStatus wfCategoryStatus) {
		return (WfDocQuery) equals("wfCategoryStatus", wfCategoryStatus);
	}

	public WfDocQuery wfCategoryVersion(String wfCategoryVersion) {
		return (WfDocQuery) equals("wfCategoryVersion", wfCategoryVersion);
	}

	public WfDocQuery name(String name) {
		return (WfDocQuery) equals("name", name);
	}

	public WfDocQuery nameLike(String name) {
		return (WfDocQuery) like("name", name);
	}

	public WfDocQuery descriptionLike(String description) {
		return (WfDocQuery) like("description", description);
	}
}
