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
package com.tcdng.jacklyn.security.entities;

import com.tcdng.jacklyn.common.entities.BaseInstallEntityQuery;

/**
 * Privilege query.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class PrivilegeQuery extends BaseInstallEntityQuery<Privilege> {

	public PrivilegeQuery() {
		super(Privilege.class);
	}

	public PrivilegeQuery name(String name) {
		return (PrivilegeQuery) equals("name", name);
	}

	public PrivilegeQuery privilegeGroupId(Long privilegeGroupId) {
		return (PrivilegeQuery) equals("privilegeGroupId", privilegeGroupId);
	}

	public PrivilegeQuery moduleId(Long moduleId) {
		return (PrivilegeQuery) equals("moduleId", moduleId);
	}

	public PrivilegeQuery moduleName(String moduleName) {
		return (PrivilegeQuery) equals("moduleName", moduleName);
	}

	public PrivilegeQuery privilegeCategoryId(Long privilegeCategoryId) {
		return (PrivilegeQuery) equals("privilegeCategoryId", privilegeCategoryId);
	}

	public PrivilegeQuery categoryName(String categoryName) {
		return (PrivilegeQuery) equals("categoryName", categoryName);
	}
}
