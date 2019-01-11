/*
 * Copyright 2018-2019 The Code Department.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License "); you may not
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
package com.tcdng.jacklyn.common.business;

import com.tcdng.jacklyn.common.constants.JacklynApplicationAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.report.AbstractRecordReportProcessor;
import com.tcdng.unify.core.report.ReportColumn;

/**
 * Base class for a record report processor.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class BaseRecordReportProcessor extends AbstractRecordReportProcessor {

    public BaseRecordReportProcessor(Class<? extends Query<? extends Entity>> recordQueryClass) {
        super(recordQueryClass);
    }

    @Override
    protected ReportColumn[] getReportColumns(String reportName) throws UnifyException {
        return getCommonReportProvider().findReportableColumns(reportName);
    }

    protected ReportProvider getCommonReportProvider() throws UnifyException {
        return (ReportProvider) getApplicationAttribute(JacklynApplicationAttributeConstants.COMMON_REPORT_PROVIDER);
    }
}
