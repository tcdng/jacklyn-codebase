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
package com.tcdng.jacklyn.workflow.entities;

import com.tcdng.jacklyn.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.constant.RequirementType;

/**
 * Entity that represents workflow attachment check.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "WFATTACHMENTCHECK",
		uniqueConstraints = { @UniqueConstraint({ "wfUserActionId", "wfDocAttachmentName" }) })
public class WfAttachmentCheck extends BaseEntity {

	@ForeignKey(WfUserAction.class)
	private Long wfUserActionId;

	@ForeignKey
	private RequirementType requirementType;

	@Column(name = "ATTACHMENT_NM")
	private String wfDocAttachmentName;

	@ListOnly(key = "requirementType", property = "description")
	private String typeDesc;

	@Override
	public String getDescription() {
		return this.wfDocAttachmentName;
	}

	public Long getWfUserActionId() {
		return wfUserActionId;
	}

	public void setWfUserActionId(Long wfUserActionId) {
		this.wfUserActionId = wfUserActionId;
	}

	public RequirementType getRequirementType() {
		return requirementType;
	}

	public void setRequirementType(RequirementType requirementType) {
		this.requirementType = requirementType;
	}

	public String getWfDocAttachmentName() {
		return wfDocAttachmentName;
	}

	public void setWfDocAttachmentName(String wfDocAttachmentName) {
		this.wfDocAttachmentName = wfDocAttachmentName;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

}
