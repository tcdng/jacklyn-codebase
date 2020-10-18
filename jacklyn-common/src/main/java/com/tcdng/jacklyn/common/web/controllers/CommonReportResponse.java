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
package com.tcdng.jacklyn.common.web.controllers;

import com.tcdng.jacklyn.common.constants.JacklynRequestAttributeConstants;
import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.constant.MimeType;
import com.tcdng.unify.core.report.ReportFormat;
import com.tcdng.unify.web.ui.AbstractOpenWindowPageControllerResponse;

/**
 * Used for preparing the generation of a report and the presentation of the
 * report in a window.
 * <p>
 * Expects the report options be stored in the request scope using the key
 * {@link JacklynRequestAttributeConstants#REPORTOPTIONS}.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("commonreportresponse")
public class CommonReportResponse extends AbstractOpenWindowPageControllerResponse {

    @Override
    protected WindowResourceInfo prepareWindowResource() throws UnifyException {
        ReportOptions reportOptions =
                (ReportOptions) getRequestAttribute(JacklynRequestAttributeConstants.REPORTOPTIONS);
        String reportFormat = reportOptions.getReportFormat();
        String resourceName =
                getTimestampedResourceName(reportOptions.getTitle())
                        + ReportFormat.fromName(reportOptions.getReportFormat()).fileExt();
        boolean download = reportOptions.isDownload();

        MimeType mimeType = MimeType.APPLICATION_OCTETSTREAM;
        if (!download) {
            mimeType = ReportFormat.fromName(reportFormat).mimeType();
        }

        return new WindowResourceInfo(reportOptions, reportOptions.getReportResourcePath(), resourceName,
                mimeType.template(), download);
    }
}
