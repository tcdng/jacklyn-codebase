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

package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.entities.BaseInstallEntityQuery;
import com.tcdng.jacklyn.shared.system.SystemAssetType;

/**
 * System asset query.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class SystemAssetQuery extends BaseInstallEntityQuery<SystemAsset> {

	public SystemAssetQuery() {
		super(SystemAsset.class);
	}

	public SystemAssetQuery moduleId(Long moduleId) {
		return (SystemAssetQuery) equals("moduleId", moduleId);
	}

	public SystemAssetQuery moduleName(String moduleName) {
		return (SystemAssetQuery) equals("moduleName", moduleName);
	}

	public SystemAssetQuery name(String name) {
		return (SystemAssetQuery) equals("name", name);
	}

	public SystemAssetQuery nameLike(String name) {
		return (SystemAssetQuery) like("name", name);
	}

	public SystemAssetQuery descriptionLike(String description) {
		return (SystemAssetQuery) like("description", description);
	}

	public SystemAssetQuery type(SystemAssetType type) {
		return (SystemAssetQuery) equals("type", type);
	}
}
