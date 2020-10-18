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
package com.tcdng.jacklyn.report.web.controllers;

import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.jacklyn.report.entities.ReportConfiguration;
import com.tcdng.jacklyn.report.web.beans.ReportListingPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.data.LinkGridInfo;

/**
 * Page controller for report listing.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/report/reportlisting")
@UplBinding("web/report/upl/reportlisting.upl")
public class ReportListingController extends AbstractReportPageController<ReportListingPageBean> {

    @Configurable("/resource/jacklynreport")
    private String reportResourcePath;

    public ReportListingController() {
        super(ReportListingPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String prepareGenerateReport() throws UnifyException {
        String reportConfigName = getPageRequestContextUtil().getRequestTargetValue(String.class);
        ReportOptions reportOptions = getReportService().getReportOptionsForConfiguration(reportConfigName);
        reportOptions.setReportResourcePath(reportResourcePath);
        reportOptions.setUserInputOnly(true);
        return showReportOptionsBox(reportOptions);
    }

    @Override
    protected void onInitPage() throws UnifyException {
        setPageValidationEnabled(true);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        // Load link grid information
        LinkGridInfo.Builder lb = LinkGridInfo.newBuilder();
        String roleCode = null;
        if (!getUserToken().isReservedUser() && !isAppAdminView()) {
            roleCode = getUserToken().getRoleCode();
        }

        String grpCode = null;
        for (ReportConfiguration rcd : getReportService().getRoleReportListing(roleCode)) {
            if (!rcd.getReportGroupCode().equals(grpCode)) {
                grpCode = rcd.getReportGroupCode();
                lb.addCategory(grpCode, rcd.getReportGroupDesc(), "/report/reportlisting/prepareGenerateReport");
            }

            String name = rcd.getName();
            lb.addLink(grpCode, name, rcd.getDescription());
        }

        ReportListingPageBean pageBean = getPageBean();
        LinkGridInfo linkGridInfo = lb.build();
        pageBean.setLinkGridInfo(linkGridInfo);
    }

}
