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

package com.tcdng.jacklyn.audit.controllers;

import java.util.Date;

import com.tcdng.jacklyn.audit.constants.AuditModuleAuditConstants;
import com.tcdng.jacklyn.audit.data.InspectUserInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.logging.EventType;
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
public class InspectUserAuditController extends AbstractAuditController {

    private String searchUserLoginId;

    private Date searchCreateDt;

    private Long searchModuleId;

    private EventType searchEventType;

    private InspectUserInfo inspectUserInfo;

    public InspectUserAuditController() {
        super(true, false);
    }

    public String getModeStyle() {
        return EventType.VIEW.colorMode();
    }

    public String getSearchUserLoginId() {
        return searchUserLoginId;
    }

    public void setSearchUserLoginId(String searchUserLoginId) {
        this.searchUserLoginId = searchUserLoginId;
    }

    public Date getSearchCreateDt() {
        return searchCreateDt;
    }

    public void setSearchCreateDt(Date searchCreateDt) {
        this.searchCreateDt = searchCreateDt;
    }

    public Long getSearchModuleId() {
        return searchModuleId;
    }

    public void setSearchModuleId(Long searchModuleId) {
        this.searchModuleId = searchModuleId;
    }

    public EventType getSearchEventType() {
        return searchEventType;
    }

    public void setSearchEventType(EventType searchEventType) {
        this.searchEventType = searchEventType;
    }

    public InspectUserInfo getInspectUserInfo() {
        return inspectUserInfo;
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        if (searchCreateDt == null) {
            searchCreateDt = getAuditService().getToday();
        }

        performFetchInspectUserInfo();
    }

    @Override
    protected String getDocViewPanelName() {
        return "manageInspectUserPanel";
    }

    @Action
    public String fetchInspectUserInfo() throws UnifyException {
        logUserEvent(AuditModuleAuditConstants.INSPECTUSER_SEARCH);
        performFetchInspectUserInfo();
        return "refreshmain";
    }

    private void performFetchInspectUserInfo() throws UnifyException {
        inspectUserInfo =
                getAuditService().fetchInspectUserInfo(searchUserLoginId, searchCreateDt, searchModuleId,
                        searchEventType);
        setVisible("userDetailsPanel", inspectUserInfo.isUser());
    }
}
