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

import java.util.List;

import com.tcdng.jacklyn.audit.constants.AuditModuleAuditConstants;
import com.tcdng.jacklyn.audit.entities.AuditDefinition;
import com.tcdng.jacklyn.audit.entities.AuditDefinitionQuery;
import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.common.controllers.ManageRecordModifier;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.logging.EventType;
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
public class AuditSettingController extends AbstractAuditRecordController<AuditDefinition> {

	private Long searchModuleId;

	private EventType searchEventType;

	private RecordStatus searchStatus;

	public AuditSettingController() {
		super(AuditDefinition.class, "audit.auditsettings.hint",
				ManageRecordModifier.SECURE | ManageRecordModifier.REPORTABLE);
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

	public RecordStatus getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(RecordStatus searchStatus) {
		this.searchStatus = searchStatus;
	}

	@Action
	public String activateAuditTypes() throws UnifyException {
		List<Long> auditTypeIds = this.getSelectedIds();
		if (!auditTypeIds.isEmpty()) {
			this.getAuditModule().setAuditTypeStatus(
					(AuditDefinitionQuery) new AuditDefinitionQuery().idIn(auditTypeIds),
					RecordStatus.ACTIVE);
			this.logUserEvent(AuditModuleAuditConstants.ACTIVATE_AUDITTYPE,
					this.getSelectedDescription());
			this.hintUser("hint.records.activated");
		}
		return this.findRecords();
	}

	@Action
	public String deactivateAuditTypes() throws UnifyException {
		List<Long> auditTypeIds = this.getSelectedIds();
		if (!auditTypeIds.isEmpty()) {
			this.getAuditModule().setAuditTypeStatus(
					(AuditDefinitionQuery) new AuditDefinitionQuery().idIn(auditTypeIds),
					RecordStatus.INACTIVE);
			this.logUserEvent(AuditModuleAuditConstants.DEACTIVATE_AUDITTYPE,
					this.getSelectedDescription());
			this.hintUser("hint.records.deactivated");
		}
		return this.findRecords();
	}

	@Override
	protected List<AuditDefinition> find() throws UnifyException {
		AuditDefinitionQuery query = new AuditDefinitionQuery();
		if (QueryUtils.isValidLongCriteria(this.searchModuleId)) {
			query.moduleId(this.searchModuleId);
		}
		if (this.getSearchEventType() != null) {
			query.eventType(this.getSearchEventType());
		}
		if (this.getSearchStatus() != null) {
			query.status(this.getSearchStatus());
		}
		query.ignoreEmptyCriteria(true);
		return this.getAuditModule().findAuditTypes(query);
	}

	@Override
	protected AuditDefinition find(Long id) throws UnifyException {
		return this.getAuditModule().findAuditType(id);
	}

	@Override
	protected AuditDefinition prepareCreate() throws UnifyException {
		return null;
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
