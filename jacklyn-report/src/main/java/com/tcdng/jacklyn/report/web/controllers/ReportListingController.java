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

import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.jacklyn.report.entities.ReportConfiguration;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.data.LinkGridInfo;

/**
 * Page controller for report listing.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/report/reportlisting")
@UplBinding("web/report/upl/reportlisting.upl")
public class ReportListingController extends AbstractReportPageController {

	@Configurable("/resource/jacklynreport")
	private String reportResourcePath;

	private LinkGridInfo linkGridInfo;

	public ReportListingController() {
		super(true, false);
	}

	@Action
	public String prepareGenerateReport() throws UnifyException {
		String reportConfigName = getRequestContextUtil().getRequestTargetValue(String.class);
		ReportOptions reportOptions = getReportService().getReportOptionsForConfiguration(reportConfigName);
		reportOptions.setReportResourcePath(reportResourcePath);
		return showReportOptionsBox(reportOptions);
	}

	public LinkGridInfo getLinkGridInfo() {
		return linkGridInfo;
	}

	public void setLinkGridInfo(LinkGridInfo linkGridInfo) {
		this.linkGridInfo = linkGridInfo;
	}

    @Override
    protected String getDocViewPanelName() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	protected void onSetPage() throws UnifyException {
		super.onSetPage();
		setPageValidationEnabled(true);
	}

	@Override
	protected void onClosePage() throws UnifyException {
		linkGridInfo = null;
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

		linkGridInfo = lb.build();
	}

}
