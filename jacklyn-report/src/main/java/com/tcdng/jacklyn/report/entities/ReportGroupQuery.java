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
package com.tcdng.jacklyn.report.entities;

import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntityQuery;

/**
 * Report group query.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ReportGroupQuery extends BaseVersionedStatusEntityQuery<ReportGroup> {

	public ReportGroupQuery() {
		super(ReportGroup.class);
	}

	public ReportGroupQuery moduleId(Long moduleId) {
		return (ReportGroupQuery) addEquals("moduleId", moduleId);
	}

	public ReportGroupQuery name(String name) {
		return (ReportGroupQuery) addEquals("name", name);
	}

	public ReportGroupQuery descriptionLike(String description) {
		return (ReportGroupQuery) addLike("description", description);
	}
}
