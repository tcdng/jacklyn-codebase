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
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.Tooling;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * DataSource entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Tooling(name = "dataSource", description = "DataSource")
@Managed(module = SystemModuleNameConstants.SYSTEM_MODULE, title = "DataSource", reportable = true, auditable = true)
@Table(name = "JKDATASOURCE", uniqueConstraints = { @UniqueConstraint({ "name" }),
		@UniqueConstraint({ "description" }) })
public class DataSource extends BaseVersionedStatusEntity {

	@ForeignKey(value = DataSourceDriver.class, name = "DATASOURCEDRIVER_ID")
	private Long dataSourceDriverId;

	@Column(name = "DATASOURCE_NM")
	private String name;

	@Column(name = "DATASOURCE_DESC", length = 48)
	private String description;

	@Column(length = 96)
	private String connectionUrl;

	@Column
	private Integer maxConnections;

	@Column(name = "USER_NAME", nullable = true)
	private String userName;

	@Column(name = "USER_PASSWORD", length = 128, transformer = "twoway-stringcryptograph", nullable = true)
	private String password;

	@ListOnly(key = "dataSourceDriverId", property = "dialect")
	private String dialect;

	@ListOnly(key = "dataSourceDriverId", property = "driverType")
	private String driverType;

	@ListOnly(key = "dataSourceDriverId", property = "description")
	private String dataSourceDriverDesc;

	@Override
	public String getDescription() {
		return this.description;
	}

	public Long getDataSourceDriverId() {
		return dataSourceDriverId;
	}

	public void setDataSourceDriverId(Long dataSourceDriverId) {
		this.dataSourceDriverId = dataSourceDriverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

    public Integer getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getDataSourceDriverDesc() {
		return dataSourceDriverDesc;
	}

	public void setDataSourceDriverDesc(String dataSourceDriverDesc) {
		this.dataSourceDriverDesc = dataSourceDriverDesc;
	}

}
