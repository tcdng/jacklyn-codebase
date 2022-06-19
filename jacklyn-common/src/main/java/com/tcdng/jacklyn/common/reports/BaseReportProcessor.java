/*
 * Copyright 2018-2020 The Code Department
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

package com.tcdng.jacklyn.common.reports;

import java.util.List;

import com.tcdng.jacklyn.common.web.UserSessionViewAccessProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.report.AbstractReportProcessor;
import com.tcdng.unify.core.report.ReportParameters;

/**
 * Convenient abstract base class for report processors.
 * 
 * @author The Code Department
 * @since 1.0.0
 */
public abstract class BaseReportProcessor extends AbstractReportProcessor {

    @Configurable
    private UserSessionViewAccessProvider userSessionViewAccessProvider;

    protected List<Long> getUserSessionBranchIds() throws UnifyException {
        return userSessionViewAccessProvider.findUserSessionBranchIds();
    }

    protected List<Long> getUserSessionDepartmentIds() throws UnifyException {
        return userSessionViewAccessProvider.findUserSessionDepartmentIds();
    }

    protected void addDateParamHeader(ReportParameters reportParameters, String paramName, String description)
            throws UnifyException {
        setReportHeaderParameter(reportParameters, "header_" + paramName, description,
                "!fixeddatetimeformat pattern:$s{dd/MM/yyyy}", reportParameters.getParameterValue(paramName));
    }

    protected void addEnumParamHeader(ReportParameters reportParameters, String paramName, String description,
            String enumStaticList) throws UnifyException {
        if (reportParameters.isParameter(paramName)) {
            String itemTypeDesc = "";
            if (reportParameters.isParameterNotNull(paramName)) {
                itemTypeDesc =
                        getListItemByKey(LocaleType.SESSION, enumStaticList,
                                (String) reportParameters.getParameterValue(paramName)).getListDescription();
            }
            setReportHeaderParameter(reportParameters, "header_" + paramName, description, itemTypeDesc);
        }
    }

    protected void addLiteralParamHeader(ReportParameters reportParameters, String paramName, String description)
            throws UnifyException {
        if (reportParameters.isParameter(paramName)) {
            String itemTypeDesc = "";
            if (reportParameters.isParameterNotNull(paramName)) {
                itemTypeDesc = String.valueOf(reportParameters.getParameterValue(paramName));
            }
            setReportHeaderParameter(reportParameters, "header_" + paramName, description, itemTypeDesc);
        }
    }

    protected void addLiteralParamHeader(ReportParameters reportParameters, String paramName, String description,
            String text) throws UnifyException {
        setReportHeaderParameter(reportParameters, "header_" + paramName, description, text);
    }

}
