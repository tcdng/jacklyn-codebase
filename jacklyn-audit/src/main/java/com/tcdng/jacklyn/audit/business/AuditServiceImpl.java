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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.tcdng.jacklyn.audit.constants.AuditModuleNameConstants;
import com.tcdng.jacklyn.audit.data.InspectUserAuditItem;
import com.tcdng.jacklyn.audit.data.InspectUserInfo;
import com.tcdng.jacklyn.audit.entities.AuditDefinition;
import com.tcdng.jacklyn.audit.entities.AuditDefinitionQuery;
import com.tcdng.jacklyn.audit.entities.AuditDetail;
import com.tcdng.jacklyn.audit.entities.AuditDetailQuery;
import com.tcdng.jacklyn.audit.entities.AuditField;
import com.tcdng.jacklyn.audit.entities.AuditFieldQuery;
import com.tcdng.jacklyn.audit.entities.AuditTrail;
import com.tcdng.jacklyn.audit.entities.AuditTrailQuery;
import com.tcdng.jacklyn.audit.entities.AuditType;
import com.tcdng.jacklyn.audit.entities.AuditTypeQuery;
import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.utils.JacklynUtils;
import com.tcdng.jacklyn.security.business.SecurityService;
import com.tcdng.jacklyn.security.entities.User;
import com.tcdng.jacklyn.shared.xml.config.module.AuditConfig;
import com.tcdng.jacklyn.shared.xml.config.module.FieldConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ManagedConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of audit business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(AuditModuleNameConstants.AUDITSERVICE)
public class AuditServiceImpl extends AbstractJacklynBusinessService implements AuditService {

    @Configurable
    private SystemService systemService;

    @Configurable
    private SecurityService securityService;

    @Override
    public List<AuditDefinition> findAuditTypes(AuditDefinitionQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public AuditDefinition findAuditType(Long id) throws UnifyException {
        return db().list(AuditDefinition.class, id);
    }

    @Override
    public AuditDefinition findAuditType(AuditDefinitionQuery query) throws UnifyException {
        return db().list(query);
    }

    @Override
    public int setAuditTypeStatus(AuditDefinitionQuery query, RecordStatus status) throws UnifyException {
        return db().updateAll(query, new Update().add("status", status));
    }

    @Override
    public List<AuditTrail> findAuditTrail(AuditTrailQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public AuditTrail findAuditTrail(Long id) throws UnifyException {
        return db().list(AuditTrail.class, id);
    }

    @Override
    public List<AuditDetail> findAuditDetails(Long auditTrailId) throws UnifyException {
        return db().listAll(new AuditDetailQuery().auditTrailId(auditTrailId).orderById());
    }

    @Override
    public InspectUserInfo fetchInspectUserInfo(String userLoginId, Date createDt, Long moduleId, EventType eventType)
            throws UnifyException {
        if (StringUtils.isBlank(userLoginId)) {
            return new InspectUserInfo();
        }

        User user = securityService.findUser(userLoginId);
        byte[] photo = securityService.findUserPhotograph(userLoginId);

        AuditTrailQuery query = new AuditTrailQuery();
        query.userLoginId(userLoginId);

        if (QueryUtils.isValidLongCriteria(moduleId)) {
            query.moduleId(moduleId);
        }

        if (eventType != null) {
            query.eventType(eventType);
        }
        query.createdOn(createDt);
        query.orderById();

        List<InspectUserAuditItem> auditItems = Collections.emptyList();
        List<AuditTrail> auditTrailList = db().listAll(query);
        if (!auditTrailList.isEmpty()) {
            auditItems = new ArrayList<InspectUserAuditItem>();
            for (AuditTrail auditTrail : auditTrailList) {
                List<String> details = Collections.emptyList();
                List<AuditDetail> auditDetailList =
                        db().findAll(new AuditDetailQuery().auditTrailId(auditTrail.getId()));
                if (!auditDetailList.isEmpty()) {
                    details = new ArrayList<String>();
                    for (AuditDetail auditDetailData : auditDetailList) {
                        details.add(auditDetailData.getDetail());
                    }
                    details = Collections.unmodifiableList(details);
                }

                auditItems.add(new InspectUserAuditItem(auditTrail.getAuditDesc(), auditTrail.getModuleDesc(),
                        auditTrail.getIpAddress(), auditTrail.getCreateDt(), auditTrail.getEventType(),
                        auditTrail.getActionDesc(), details));
            }
            auditItems = Collections.unmodifiableList(auditItems);
        }

        return new InspectUserInfo(user.getFullName(), user.getEmail(), photo, auditItems);
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
        // Uninstall old records
        db().updateAll(new AuditDefinitionQuery().installed(Boolean.TRUE),
                new Update().add("installed", Boolean.FALSE));

        // Install new and update old
        AuditDefinition auditDefinition = new AuditDefinition();

        for (ModuleConfig moduleConfig : moduleConfigList) {
            Long moduleId = systemService.getModuleId(moduleConfig.getName());

            if (moduleConfig.getAudits() != null && !DataUtils.isBlank(moduleConfig.getAudits().getAuditList())) {
                logDebug("Installing audit definitions for module [{0}]...",
                        resolveApplicationMessage(moduleConfig.getDescription()));
                AuditDefinitionQuery adQuery = new AuditDefinitionQuery();
                for (AuditConfig auditConfig : moduleConfig.getAudits().getAuditList()) {
                    adQuery.clear();
                    Long auditTypeId = null;
                    if (auditConfig.isType()) {
                        auditTypeId =
                                installAuditType(
                                        JacklynUtils.getManagedConfig(moduleConfig, auditConfig.getAuditable()),
                                        auditConfig);
                    }

                    AuditDefinition oldAuditDefinition = db().find(adQuery.name(auditConfig.getName()));
                    String description = resolveApplicationMessage(auditConfig.getDescription());
                    if (oldAuditDefinition == null) {
                        auditDefinition = new AuditDefinition();
                        auditDefinition.setModuleId(moduleId);
                        auditDefinition.setAuditTypeId(auditTypeId);
                        auditDefinition.setName(auditConfig.getName());
                        auditDefinition.setDescription(description);
                        auditDefinition.setEventType(auditConfig.getAction());
                        auditDefinition.setInstalled(Boolean.TRUE);
                        if (auditConfig.isActive()) {
                            auditDefinition.setStatus(RecordStatus.ACTIVE);
                        } else {
                            auditDefinition.setStatus(RecordStatus.INACTIVE);
                        }
                        db().create(auditDefinition);
                    } else {
                        oldAuditDefinition.setModuleId(moduleId);
                        oldAuditDefinition.setAuditTypeId(auditTypeId);
                        oldAuditDefinition.setDescription(description);
                        oldAuditDefinition.setEventType(auditConfig.getAction());
                        oldAuditDefinition.setInstalled(Boolean.TRUE);
                        db().updateByIdVersion(oldAuditDefinition);
                    }
                }
            }
        }
    }

    private Long installAuditType(ManagedConfig managedConfig, AuditConfig auditConfig) throws UnifyException {
        Long auditTypeId = null;
        String type = auditConfig.getAuditable();
        if (type != null) {
            AuditType auditType = db().find(new AuditTypeQuery().recordName(type));
            if (auditType == null) {
                auditType = new AuditType();
                auditType.setRecordName(type);
                auditTypeId = (Long) db().create(auditType);
            } else {
                db().updateByIdVersion(auditType);
                auditTypeId = auditType.getId();
                db().deleteAll(new AuditFieldQuery().auditTypeId(auditTypeId));
            }

            AuditField auditField = new AuditField();
            auditField.setAuditTypeId(auditTypeId);
            for (FieldConfig adf : managedConfig.getFieldList()) {
                if (adf.isAuditable()) {
                    auditField.setFieldDescription(adf.getDescription());
                    auditField.setFormatter(adf.getFormatter());
                    auditField.setList(adf.getList());
                    auditField.setMask(adf.isMask());
                    auditField.setFieldName(adf.getName());
                    auditField.setInstalled(Boolean.TRUE);
                    auditField.setStatus(RecordStatus.ACTIVE);
                    db().create(auditField);
                }
            }
        }
        return auditTypeId;
    }
}
