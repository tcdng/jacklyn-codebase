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

import com.tcdng.jacklyn.common.entities.BaseStatusEntityQuery;

/**
 * Query class for workflow form definitions.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class WfFormQuery extends BaseStatusEntityQuery<WfForm> {

	public WfFormQuery() {
		super(WfForm.class);
	}

	public WfFormQuery wfCategoryId(Long wfCategoryId) {
		return (WfFormQuery) equals("wfCategoryId", wfCategoryId);
	}

	public WfFormQuery wfCategoryName(String wfCategoryName) {
		return (WfFormQuery) equals("wfCategoryName", wfCategoryName);
	}

	public WfFormQuery name(String name) {
		return (WfFormQuery) equals("name", name);
	}

	public WfFormQuery nameLike(String name) {
		return (WfFormQuery) like("name", name);
	}

	public WfFormQuery descriptionLike(String description) {
		return (WfFormQuery) like("description", description);
	}
}
