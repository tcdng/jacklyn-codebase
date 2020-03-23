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

import com.tcdng.jacklyn.audit.constants.AuditModuleAuditConstants;
import com.tcdng.jacklyn.audit.entities.AuditDefinition;
import com.tcdng.jacklyn.audit.entities.AuditDefinitionQuery;
import com.tcdng.jacklyn.audit.web.beans.AuditSettingPageBean;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.web.controllers.ManageRecordModifier;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;

/**
 * Controller for managing audit type record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/audit/auditsettings")
@UplBinding("web/audit/upl/manageauditsettings.upl")
public class AuditSettingController extends AbstractAuditFormController<AuditSettingPageBean, AuditDefinition> {

    public AuditSettingController() {
        super(AuditSettingPageBean.class, AuditDefinition.class,
                ManageRecordModifier.SECURE | ManageRecordModifier.REPORTABLE);
    }

    @Action
    public String activateAuditTypes() throws UnifyException {
        List<Long> auditTypeIds = getSelectedIds();
        if (!auditTypeIds.isEmpty()) {
            getAuditService().setAuditTypeStatus((AuditDefinitionQuery) new AuditDefinitionQuery().idIn(auditTypeIds),
                    RecordStatus.ACTIVE);
            logUserEvent(AuditModuleAuditConstants.ACTIVATE_AUDITTYPE, getSelectedDescription());
            hintUser("$m{hint.records.activated}");
        }
        return findRecords();
    }

    @Action
    public String deactivateAuditTypes() throws UnifyException {
        List<Long> auditTypeIds = getSelectedIds();
        if (!auditTypeIds.isEmpty()) {
            getAuditService().setAuditTypeStatus((AuditDefinitionQuery) new AuditDefinitionQuery().idIn(auditTypeIds),
                    RecordStatus.INACTIVE);
            logUserEvent(AuditModuleAuditConstants.DEACTIVATE_AUDITTYPE, getSelectedDescription());
            hintUser("$m{hint.records.deactivated}");
        }
        return findRecords();
    }

    @Override
    protected List<AuditDefinition> find() throws UnifyException {
        AuditSettingPageBean pageBean = getPageBean();
        AuditDefinitionQuery query = new AuditDefinitionQuery();
        if (QueryUtils.isValidLongCriteria(pageBean.getSearchModuleId())) {
            query.moduleId(pageBean.getSearchModuleId());
        }

        if (pageBean.getSearchEventType() != null) {
            query.eventType(pageBean.getSearchEventType());
        }

        if (pageBean.getSearchStatus() != null) {
            query.status(pageBean.getSearchStatus());
        }

        query.installed(Boolean.TRUE);
        query.ignoreEmptyCriteria(true);
        return getAuditService().findAuditTypes(query);
    }

    @Override
    protected AuditDefinition find(Long id) throws UnifyException {
        return getAuditService().findAuditType(id);
    }

    @Override
    protected Object create(AuditDefinition auditDefinitionData) throws UnifyException {
        return null;
    }

    @Override
    protected int update(AuditDefinition auditDefinitionData) throws UnifyException {
        return 0;
    }

    @Override
    protected int delete(AuditDefinition auditDefinitionData) throws UnifyException {
        return 0;
    }
}
