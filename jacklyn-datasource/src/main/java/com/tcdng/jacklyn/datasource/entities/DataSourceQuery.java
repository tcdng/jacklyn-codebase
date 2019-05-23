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
package com.tcdng.jacklyn.datasource.entities;

import com.tcdng.jacklyn.common.entities.BaseVersionedStatusEntityQuery;

/**
 * Datasource query.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class DataSourceQuery extends BaseVersionedStatusEntityQuery<DataSource> {

	public DataSourceQuery() {
		super(DataSource.class);
	}

	public DataSourceQuery dataSourceDriverId(Long dataSourceDriverId) {
		return (DataSourceQuery) equals("dataSourceDriverId", dataSourceDriverId);
	}

	public DataSourceQuery name(String name) {
		return (DataSourceQuery) equals("name", name);
	}

	public DataSourceQuery descriptionLike(String description) {
		return (DataSourceQuery) like("description", description);
	}

}
