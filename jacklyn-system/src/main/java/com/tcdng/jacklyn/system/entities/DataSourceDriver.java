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
package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntity;
import com.tcdng.jacklyn.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Datasource driver record.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Managed(module = SystemModuleNameConstants.SYSTEM_MODULE, title = "DataSource Driver", reportable = true, auditable = true)
@Table(name = "JKDATASOURCEDRIVER", uniqueConstraints = { @UniqueConstraint({ "name" }),
		@UniqueConstraint({ "description" }) })
public class DataSourceDriver extends BaseVersionedStatusEntity {

	@Column(name = "DATASOURCEDRIVER_NM")
	private String name;

	@Column(name = "DATASOURCEDRIVER_DESC", length = 48)
	private String description;

	@Column
	private String dialect;

	@Column(length = 128)
	private String driverType;

    public DataSourceDriver() {

    }

    public DataSourceDriver(String name, String description, String dialect, String driverType) {
        this.name = name;
        this.description = description;
        this.dialect = dialect;
        this.driverType = driverType;
    }

    @Override
	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getDriverType() {
		return driverType;
	}

	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
