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

import java.util.Date;
import java.util.List;

import com.tcdng.jacklyn.audit.data.InspectUserInfo;
import com.tcdng.jacklyn.audit.entities.AuditDefinition;
import com.tcdng.jacklyn.audit.entities.AuditDefinitionQuery;
import com.tcdng.jacklyn.audit.entities.AuditDetail;
import com.tcdng.jacklyn.audit.entities.AuditTrail;
import com.tcdng.jacklyn.audit.entities.AuditTrailQuery;
import com.tcdng.jacklyn.common.business.JacklynBusinessService;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.logging.EventType;

/**
 * Audit business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface AuditService extends JacklynBusinessService {

    /**
     * Finds audit types by query.
     * 
     * @param query
     *            the audit type query
     * @return a list of audit type data
     * @throws UnifyException
     *             if an error occurs
     */
    List<AuditDefinition> findAuditTypes(AuditDefinitionQuery query) throws UnifyException;

    /**
     * Finds an audit type record by query.
     * 
     * @param query
     *            the audit type query
     * @return the audit trail record
     * @throws UnifyException
     *             if an error occurs
     */
    AuditDefinition findAuditType(AuditDefinitionQuery query) throws UnifyException;

    /**
     * Finds an audit type record by ID.
     * 
     * @param id
     *            the audit type ID
     * @return the audit trail record
     * @throws UnifyException
     *             if an error occurs
     */
    AuditDefinition findAuditType(Long id) throws UnifyException;

    /**
     * Sets the status of audit types determined by query.
     * 
     * @param query
     *            the query
     * @param status
     *            the status to set audit types to
     * @return the number of records updated
     * @throws UnifyException
     *             if an error occurs
     */
    int setAuditTypeStatus(AuditDefinitionQuery query, RecordStatus status) throws UnifyException;

    /**
     * Finds audit trail by query.
     * 
     * @param query
     *            the audit trail query
     * @return a list of audit trail data
     * @throws UnifyException
     *             if an error occurs
     */
    List<AuditTrail> findAuditTrail(AuditTrailQuery query) throws UnifyException;

    /**
     * Finds an audit trail record.
     * 
     * @param id
     *            the audit trail ID
     * @return the audit trail record
     * @throws UnifyException
     *             if an error occurs
     */
    AuditTrail findAuditTrail(Long id) throws UnifyException;

    /**
     * Finds audit detail list by audit trail ID.
     * 
     * @param auditTrailId
     *            the audit trail ID
     * @return a list of audit trail detail data
     * @throws UnifyException
     *             if an error occurs
     */
    List<AuditDetail> findAuditDetails(Long auditTrailId) throws UnifyException;

    /**
     * Fetches inspect user information using supplied parameters.
     * 
     * @param userLoginId
     *            the user login ID
     * @param createDt
     *            the audit create date
     * @param moduleId
     *            optional module ID
     * @param eventType
     *            optional event type
     * @return the inspect user information
     * @throws UnifyException
     *             if an error occurs
     */
    InspectUserInfo fetchInspectUserInfo(String userLoginId, Date createDt, Long moduleId, EventType eventType)
            throws UnifyException;
}
