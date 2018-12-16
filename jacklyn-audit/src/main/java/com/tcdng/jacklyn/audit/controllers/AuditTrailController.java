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
package com.tcdng.jacklyn.audit.controllers;

import java.util.Date;
import java.util.List;

import com.tcdng.jacklyn.audit.entities.AuditDetail;
import com.tcdng.jacklyn.audit.entities.AuditTrail;
import com.tcdng.jacklyn.audit.entities.AuditTrailQuery;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;

/**
 * Controller for managing audit trail record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/audit/audittrail")
@UplBinding("web/audit/upl/manageaudittrail.upl")
@ResultMappings({
        @ResultMapping(name = "showusersearch", response = { "!showpopupresponse popup:$s{userSearchBoxPopup}" }) })
public class AuditTrailController extends AbstractAuditRecordController<AuditTrail> {

    private Date searchCreateDt;

    private String searchUserLoginId;

    private Long searchModuleId;

    private EventType searchEventType;

    private List<AuditDetail> auditDetailList;

    public AuditTrailController() {
        super(AuditTrail.class, "audit.audittrail.hint",
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    public Date getSearchCreateDt() {
        return searchCreateDt;
    }

    public void setSearchCreateDt(Date searchCreateDt) {
        this.searchCreateDt = searchCreateDt;
    }

    public String getSearchUserLoginId() {
        return searchUserLoginId;
    }

    public void setSearchUserLoginId(String searchUserLoginId) {
        this.searchUserLoginId = searchUserLoginId;
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

    public List<AuditDetail> getAuditDetailList() {
        return auditDetailList;
    }

    @Override
    protected List<AuditTrail> find() throws UnifyException {
        AuditTrailQuery query = new AuditTrailQuery();
        if (QueryUtils.isValidStringCriteria(searchUserLoginId)) {
            query.userLoginId(searchUserLoginId);
        }
        if (QueryUtils.isValidLongCriteria(searchModuleId)) {
            query.moduleId(searchModuleId);
        }

        if (searchEventType != null) {
            query.eventType(searchEventType);
        }
        query.createdOn(searchCreateDt);
        return getAuditModule().findAuditTrail(query);
    }

    @Override
    protected AuditTrail find(Long id) throws UnifyException {
        return getAuditModule().findAuditTrail(id);
    }

    @Override
    protected AuditTrail prepareCreate() throws UnifyException {
        return null;
    }

    @Override
    protected Object create(AuditTrail auditTrailData) throws UnifyException {
        return null;
    }

    @Override
    protected int update(AuditTrail auditTrailData) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(AuditTrail auditTrailData) throws UnifyException {
        return 0;
    }

    @Override
    protected void onPrepareView(AuditTrail auditTrailData, boolean paste) throws UnifyException {
        auditDetailList = getAuditModule().findAuditDetails(auditTrailData.getId());
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        if (searchCreateDt == null) {
            searchCreateDt = CalendarUtils.getCurrentMidnightDate();
        }
    }

    @Override
    protected void onLoseView(AuditTrail auditTrailData) throws UnifyException {
        auditDetailList = null;
    }
}
