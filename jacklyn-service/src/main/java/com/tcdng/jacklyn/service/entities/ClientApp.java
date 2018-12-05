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
package com.tcdng.jacklyn.service.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.service.constants.ServiceModuleNameConstants;
import com.tcdng.jacklyn.shared.service.ClientAppType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Client application.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = ServiceModuleNameConstants.SERVICE_MODULE, title = "Client Application",
		reportable = true, auditable = true)
@Table(name = "CLIENTAPP",
		uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class ClientApp extends BaseVersionedStatusEntity {

	@ForeignKey(name = "CLIENTAPP_TY")
	private ClientAppType type;

	@Column(name = "CLIENTAPP_NM", length = 32)
	private String name;

	@Column(name = "CLIENTAPP_DESC", length = 48)
	private String description;

	@ListOnly(key = "type", property = "description")
	private String typeDesc;

	@Override
	public String getDescription() {
		return description;
	}

	public ClientAppType getType() {
		return type;
	}

	public void setType(ClientAppType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

}
