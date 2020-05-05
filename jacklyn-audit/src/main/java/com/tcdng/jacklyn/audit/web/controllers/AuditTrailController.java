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
package com.tcdng.jacklyn.audit.web.controllers;

import java.util.List;

import com.tcdng.jacklyn.audit.entities.AuditTrail;
import com.tcdng.jacklyn.audit.entities.AuditTrailQuery;
import com.tcdng.jacklyn.audit.web.beans.AuditTrailPageBean;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
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
public class AuditTrailController extends AbstractAuditFormController<AuditTrailPageBean, AuditTrail> {

    public AuditTrailController() {
        super(AuditTrailPageBean.class, AuditTrail.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.VIEW | ManageRecordModifier.REPORTABLE);
    }

    @Override
    protected List<AuditTrail> find() throws UnifyException {
        AuditTrailPageBean pageBean = getPageBean();
        AuditTrailQuery query = new AuditTrailQuery();
        if (QueryUtils.isValidStringCriteria(pageBean.getSearchUserLoginId())) {
            query.userLoginId(pageBean.getSearchUserLoginId());
        }
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchModuleId())) {
            query.moduleId(pageBean.getSearchModuleId());
        }

        if (pageBean.getSearchEventType() != null) {
            query.eventType(pageBean.getSearchEventType());
        }
        query.createdOn(pageBean.getSearchCreateDt());
        return getAuditService().findAuditTrail(query);
    }

    @Override
    protected AuditTrail find(Long id) throws UnifyException {
        return getAuditService().findAuditTrail(id);
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
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        AuditTrailPageBean pageBean = getPageBean();
        if (pageBean.getSearchCreateDt() == null) {
            pageBean.setSearchCreateDt(getAuditService().getToday());
        }
    }
}
