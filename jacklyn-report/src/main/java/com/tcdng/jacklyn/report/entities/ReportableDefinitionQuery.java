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
package com.tcdng.jacklyn.report.entities;

import java.util.Collection;

import com.tcdng.jacklyn.common.entities.BaseInstallEntityQuery;

/**
 * Query class for reportable definition records.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ReportableDefinitionQuery extends BaseInstallEntityQuery<ReportableDefinition> {

	public ReportableDefinitionQuery() {
		super(ReportableDefinition.class);
	}

	public ReportableDefinitionQuery name(String name) {
		return (ReportableDefinitionQuery) equals("name", name);
	}

	public ReportableDefinitionQuery recordName(String recordName) {
		return (ReportableDefinitionQuery) equals("recordName", recordName);
	}

	public ReportableDefinitionQuery moduleId(Long moduleId) {
		return (ReportableDefinitionQuery) equals("moduleId", moduleId);
	}

	public ReportableDefinitionQuery moduleName(String moduleName) {
		return (ReportableDefinitionQuery) equals("moduleName", moduleName);
	}

	public ReportableDefinitionQuery moduleActivityIdIn(Collection<Long> moduleActivityId) {
		return (ReportableDefinitionQuery) amongst("moduleActivityId", moduleActivityId);
	}

	public ReportableDefinitionQuery dynamic(boolean dynamic) {
		return (ReportableDefinitionQuery) equals("dynamic", dynamic);
	}
}
