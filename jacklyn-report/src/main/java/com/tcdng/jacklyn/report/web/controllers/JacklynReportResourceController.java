/*
 * Copyright 2018-2019 The Code Department.
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
package com.tcdng.jacklyn.report.web.controllers;

import java.io.OutputStream;

import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Backing resource controller for retrieving a generated report.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/resource/jacklynreport")
public class JacklynReportResourceController extends AbstractReportResourceController {

    public JacklynReportResourceController() {
        super(true);
    }

    @Override
    public void prepareExecution() throws UnifyException {
        setContentDisposition(getResourceName());
    }

    @Override
    public void execute(OutputStream outputStream) throws UnifyException {
        ReportOptions reportOptions = (ReportOptions) removeSessionAttribute(getResourceName());
        getReportService().populateExtraConfigurationReportOptions(reportOptions);
        getReportService().generateDynamicReport(reportOptions, outputStream);
    }
}
