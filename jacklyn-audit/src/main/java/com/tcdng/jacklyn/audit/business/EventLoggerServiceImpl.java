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
package com.tcdng.jacklyn.audit.business;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.jacklyn.audit.constants.AuditModuleErrorConstants;
import com.tcdng.jacklyn.audit.entities.AuditDefinition;
import com.tcdng.jacklyn.audit.entities.AuditDefinitionQuery;
import com.tcdng.jacklyn.audit.entities.AuditDetail;
import com.tcdng.jacklyn.audit.entities.AuditField;
import com.tcdng.jacklyn.audit.entities.AuditFieldQuery;
import com.tcdng.jacklyn.audit.entities.AuditTrail;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.system.constants.SystemReservedUserConstants;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.AbstractBusinessService;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Event logger service implementation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(ApplicationComponents.APPLICATION_EVENTSLOGGER)
public class EventLoggerServiceImpl extends AbstractBusinessService implements EventLoggerService {

    @Override
    public boolean logUserEvent(String eventName, List<String> details) throws UnifyException {
        return logUserEvent(eventName, details.toArray(new String[details.size()]));
    }

    @Override
    public boolean logUserEvent(String eventName, String... details) throws UnifyException {
        AuditDefinition auditDefinition = db().find(new AuditDefinitionQuery().name(eventName).installed(Boolean.TRUE));
        if (auditDefinition == null) {
            throw new UnifyException(AuditModuleErrorConstants.AUDITTYPE_IS_UNKNOWN, eventName);
        }

        if (RecordStatus.ACTIVE.equals(auditDefinition.getStatus())) {
            createAuditTrail(auditDefinition.getId(), details, null);
            return true;
        }

        return false;
    }

    @Override
    public boolean logUserEvent(EventType eventType, Class<? extends Entity> entityClass) throws UnifyException {
        AuditDefinition auditDefinitionData =
                db().find(new AuditDefinitionQuery().recordName(entityClass.getName()).eventType(eventType)
                        .installed(Boolean.TRUE).status(RecordStatus.ACTIVE));
        if (auditDefinitionData != null) {
            createAuditTrail(auditDefinitionData.getId(), null, null);
            return true;
        }
        return false;
    }

    @Override
    public boolean logUserEvent(EventType eventType, Entity record, boolean isNewRecord) throws UnifyException {
        AuditDefinition auditDefinitionData =
                db().find(new AuditDefinitionQuery().recordName(record.getClass().getName()).eventType(eventType)
                        .installed(Boolean.TRUE).status(RecordStatus.ACTIVE));
        if (auditDefinitionData != null) {
            Long auditTypeId = auditDefinitionData.getAuditTypeId();
            String[] narration = null;
            if (isNewRecord) {
                List<AuditField> auditFieldList =
                        db().findAll(new AuditFieldQuery().auditTypeId(auditTypeId).installed(Boolean.TRUE));
                narration = new String[auditFieldList.size()];
                for (int i = 0; i < narration.length; i++) {
                    AuditField auditFieldData = auditFieldList.get(i);
                    String name = auditFieldData.getFieldName();
                    String auditValue =
                            convert(String.class, ReflectUtils.getBeanProperty(record, name),
                                    auditFieldData.getFormatter());
                    narration[i] = getApplicationMessage("eventloggerservice.narration.message.new", name, auditValue);
                }
            }
            createAuditTrail(auditDefinitionData.getId(), narration, (Long) record.getId());
            return true;
        }
        return false;
    }

    @Override
    public <T extends Entity> boolean logUserEvent(EventType eventType, T oldRecord, T newRecord)
            throws UnifyException {
        if (newRecord != null && !oldRecord.getClass().equals(newRecord.getClass())) {
            throw new UnifyException(AuditModuleErrorConstants.CANNOT_AUDIT_DIFFERENT_RECORD_TYPES,
                    oldRecord.getClass(), newRecord.getClass());
        }

        AuditDefinition auditDefinitionData =
                db().find(new AuditDefinitionQuery().recordName(oldRecord.getClass().getName()).eventType(eventType)
                        .installed(Boolean.TRUE).status(RecordStatus.ACTIVE));
        if (auditDefinitionData != null) {
            Long auditTypeId = auditDefinitionData.getAuditTypeId();
            List<String> narrationList = new ArrayList<String>();
            List<AuditField> list =
                    db().findAll(new AuditFieldQuery().auditTypeId(auditTypeId).installed(Boolean.TRUE));
            for (AuditField auditFieldData : list) {
                String name = auditFieldData.getFieldName();
                Object oldValue = ReflectUtils.getBeanProperty(oldRecord, auditFieldData.getFieldName());
                Object newValue = ReflectUtils.getBeanProperty(newRecord, auditFieldData.getFieldName());
                if ((oldValue != null && !oldValue.equals(newValue))
                        || (newValue != null && !newValue.equals(oldValue))) {
                    String oldAuditValue = convert(String.class, oldValue, auditFieldData.getFormatter());
                    String newAuditValue = convert(String.class, newValue, auditFieldData.getFormatter());
                    narrationList.add(getApplicationMessage("eventloggerservice.narration.message.difference", name,
                            oldAuditValue, newAuditValue));
                }
            }

            createAuditTrail(auditDefinitionData.getId(), narrationList.toArray(new String[narrationList.size()]),
                    (Long) oldRecord.getId());

            return true;
        }
        return false;
    }

    private Long createAuditTrail(Long auditDefinitionId, String[] details, Long recordId) throws UnifyException {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setAuditDefinitionId(auditDefinitionId);
        auditTrail.setRecordId(recordId);
        UserToken userToken = getUserToken();
        if (userToken != null) {
            auditTrail.setUserLoginId(userToken.getUserLoginId());
            auditTrail.setIpAddress(userToken.getIpAddress());
            auditTrail.setRemoteEvent(userToken.isRemote());
        } else {
            auditTrail.setUserLoginId(SystemReservedUserConstants.ANONYMOUS_LOGINID);
            auditTrail.setIpAddress(getSessionContext().getRemoteAddress());
            auditTrail.setRemoteEvent(null);
        }
        
        if (details != null && details.length > 0) {
            List<AuditDetail> auditDetailList = new ArrayList<AuditDetail>(details.length);
            for(String detail:details) {
                auditDetailList.add(new AuditDetail(detail));
            }
            
            auditTrail.setAuditDetailList(auditDetailList);
        }
        
        return (Long) db().create(auditTrail);
    }
}
