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
package com.tcdng.jacklyn.system.business;

import java.util.Date;

import com.tcdng.jacklyn.common.business.BaseRecordReportProcessor;
import com.tcdng.jacklyn.system.entities.ScheduledTaskHistQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.report.ReportParameters;
import com.tcdng.unify.core.task.TaskStatus;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Processor for scheduled task history report.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("scheduledtaskhist-reportprocessor")
public class ScheduledTaskHistReportProcessor extends BaseRecordReportProcessor {

	public ScheduledTaskHistReportProcessor() {
		super(ScheduledTaskHistQuery.class);
	}

	@Override
	protected void populate(Query<? extends Entity> query, ReportParameters reportParameters)
			throws UnifyException {
		Long scheduledTaskId = (Long) reportParameters.getParameter("scheduledTaskId");
		if (QueryUtils.isValidLongCriteria(scheduledTaskId)) {
			((ScheduledTaskHistQuery) query).scheduledTaskId(scheduledTaskId);
		}

		TaskStatus status = (TaskStatus) reportParameters.getParameter("taskStatus");
		if (status != null) {
			((ScheduledTaskHistQuery) query).taskStatus(status);
		}

		((ScheduledTaskHistQuery) query)
				.createdOn((Date) reportParameters.getParameter("executionDt"));
	}
}
