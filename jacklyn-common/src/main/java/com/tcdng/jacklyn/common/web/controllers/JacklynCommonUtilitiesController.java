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
package com.tcdng.jacklyn.common.web.controllers;

import com.tcdng.jacklyn.common.constants.CommonModuleAuditConstants;
import com.tcdng.jacklyn.common.constants.JacklynRequestAttributeConstants;
import com.tcdng.jacklyn.common.constants.JacklynSessionAttributeConstants;
import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.AbstractCommonUtilitiesPageController;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.ui.data.SearchBox;
import com.tcdng.unify.web.ui.panel.SearchBoxPanel;

/**
 * Common utilities page controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/application/commonutilities")
@UplBinding("web/common/upl/jacklyncommonutilities.upl")
@ResultMappings({
        @ResultMapping(
                name = "showapplicationreportoptions", response = { "!showpopupresponse popup:$s{reportRunnerPopup}" }),
        @ResultMapping(name = "showapplicationsearch", response = { "!showpopupresponse popup:$s{searchBoxPopup}" }),
        @ResultMapping(
                name = "searchdone",
                response = { "!hidepopupresponse", "!postresponse pathBinding:$s{searchSelectPath}" }),
        @ResultMapping(
                name = "viewreport",
                response = { "!refreshpanelresponse panels:$l{reportRunnerPopup}", "!commonreportresponse" }) })
public class JacklynCommonUtilitiesController extends AbstractCommonUtilitiesPageController<JacklynCommonUtilitiesPageBean> {

    public JacklynCommonUtilitiesController() {
        super(JacklynCommonUtilitiesPageBean.class);
    }

    @Action
    public String generateReport() throws UnifyException {
        ReportOptions reportOptions =
                (ReportOptions) getSessionAttribute(JacklynSessionAttributeConstants.REPORTOPTIONS);
        setRequestAttribute(JacklynRequestAttributeConstants.REPORTOPTIONS, reportOptions);
        getRequestContextUtil().logUserEvent(CommonModuleAuditConstants.GENERATE_REPORT, reportOptions.getTitle());
        return "viewreport";
    }

    @Action
    public String closeReport() throws UnifyException {
        removeSessionAttribute(JacklynSessionAttributeConstants.REPORTOPTIONS);
        return hidePopup();
    }

    @Action
    public String searchBoxSelect() throws UnifyException {
        getSearchBoxPanel().setResultBeanProperties();
        SearchBox searchBoxInfo = (SearchBox) removeSessionAttribute(JacklynSessionAttributeConstants.SEARCHBOX);
        JacklynCommonUtilitiesPageBean jacklynCommonUtilitiesPageBean = getPageBean();
        jacklynCommonUtilitiesPageBean.setSearchSelectPath(searchBoxInfo.getResultPath());
        return "searchdone";
    }

    @Action
    public String searchBoxCancel() throws UnifyException {
        removeSessionAttribute(JacklynSessionAttributeConstants.SEARCHBOX);
        return hidePopup();
    }

    private SearchBoxPanel getSearchBoxPanel() throws UnifyException {
        return getPageWidgetByShortName(SearchBoxPanel.class, "searchBoxPopup");
    }
}
