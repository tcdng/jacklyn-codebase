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
package com.tcdng.jacklyn.common.controllers;

import java.io.OutputStream;

import com.tcdng.jacklyn.common.business.ReportProvider;
import com.tcdng.jacklyn.common.constants.JacklynApplicationAttributeConstants;
import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.web.AbstractResourceController;

/**
 * Common resource controller for generating a report.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/common/resource/report")
public class CommonReportResourceController extends AbstractResourceController {

    public CommonReportResourceController() {
        super(true);
    }

    @Override
    public void prepareExecution() throws UnifyException {
        setContentDisposition(getResourceName());
    }

    @Override
    public void execute(OutputStream outputStream) throws UnifyException {
        ReportOptions reportOptions = (ReportOptions) removeSessionAttribute(getResourceName());
        getCommonReportProvider().generateDynamicReport(reportOptions, outputStream);
    }

    private ReportProvider getCommonReportProvider() throws UnifyException {
        return (ReportProvider) getApplicationAttribute(JacklynApplicationAttributeConstants.COMMON_REPORT_PROVIDER);
    }
}
