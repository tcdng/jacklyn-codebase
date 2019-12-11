/*
 * Copyright 2018-2019 The Code Department
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

package com.tcdng.jacklyn.audit.web.beans;

import java.util.Date;

import com.tcdng.jacklyn.audit.data.InspectUserInfo;
import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.unify.core.logging.EventType;

/**
 * Inspect user audit page bean.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class InspectUserAuditPageBean extends BasePageBean {

    private String searchUserLoginId;

    private Date searchCreateDt;

    private Long searchModuleId;

    private EventType searchEventType;

    private InspectUserInfo inspectUserInfo;

    public InspectUserAuditPageBean() {
        super("manageInspectUserPanel");
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

    public void setInspectUserInfo(InspectUserInfo inspectUserInfo) {
        this.inspectUserInfo = inspectUserInfo;
    }

    public String getModeStyle() {
        return EventType.VIEW.colorMode();
    }

}
