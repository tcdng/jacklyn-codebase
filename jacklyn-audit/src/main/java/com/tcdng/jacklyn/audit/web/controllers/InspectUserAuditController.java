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

package com.tcdng.jacklyn.audit.web.controllers;

import com.tcdng.jacklyn.audit.constants.AuditModuleAuditConstants;
import com.tcdng.jacklyn.audit.data.InspectUserInfo;
import com.tcdng.jacklyn.audit.web.beans.InspectUserAuditPageBean;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * Controller for inspect user page.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/audit/inspectuser")
@UplBinding("web/audit/upl/inspectuser.upl")
@ResultMappings({ @ResultMapping(name = "refreshmain", response = { "!refreshpanelresponse panels:$l{mainPanel}" }) })
public class InspectUserAuditController extends AbstractAuditPageController<InspectUserAuditPageBean> {

    public InspectUserAuditController() {
        super(InspectUserAuditPageBean.class, true, false, false);
    }

    @Action
    public String fetchInspectUserInfo() throws UnifyException {
        logUserEvent(AuditModuleAuditConstants.INSPECTUSER_SEARCH);
        performFetchInspectUserInfo();
        return "refreshmain";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        InspectUserAuditPageBean pageBean = getPageBean();
        if (pageBean.getSearchCreateDt() == null) {
            pageBean.setSearchCreateDt(getAuditService().getToday());
        }

        performFetchInspectUserInfo();
    }

    private void performFetchInspectUserInfo() throws UnifyException {
        InspectUserAuditPageBean pageBean = getPageBean();
        InspectUserInfo inspectUserInfo =
                getAuditService().fetchInspectUserInfo(pageBean.getSearchUserLoginId(), pageBean.getSearchCreateDt(),
                        pageBean.getSearchModuleId(), pageBean.getSearchEventType());
        pageBean.setInspectUserInfo(inspectUserInfo);
        setPageWidgetVisible("userDetailsPanel", inspectUserInfo.isUser());
    }
}
